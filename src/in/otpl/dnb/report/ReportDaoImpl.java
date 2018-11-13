package in.otpl.dnb.report;

import in.otpl.dnb.user.TeamLogic;
import in.otpl.dnb.user.UserDto;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDaoImpl implements ReportDao {

private static final Logger LOG = Logger.getLogger(ReportDaoImpl.class);
	
	@Autowired
	private ResourceManager resourceManager;
    @Autowired
    private TeamLogic teamLogic;
   
	@Override
	public Map<String, Object> getUserTrackingReport(UserTrackingForm form, int customerId,Connection conn){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String select = "SELECT t.client_time, concat(u.first_name,'  ',u.last_name) as fullname,"
				+ "t.event_name, t.lattitude, t.longitude, t.speed, t.is_cellsite, t.cellsite_loc, t.db_time, t.accuracy, t.status";
			
			String  from = " FROM user u, tracking t ";
			String where = " where u.customer_id = ? and t.user_id = u.id and t.user_id = ? and t.client_time between ? and ? ";
			String orderBy =" order by t.client_time desc";
			String limit = "";
			if(form.getRowcount() > 0){
				limit = " LIMIT ? , ?";
			}
			String sql = select + from + where + orderBy + limit;
			stmt = conn.prepareStatement(sql);
			int index = 1;
			stmt.setInt(index++, customerId);
			stmt.setLong(index++, form.getUserId());
			stmt.setString(index++, form.getDateFrom());
			stmt.setString(index++, form.getDateTo());
			if(form.getRowcount() > 0){
				stmt.setInt(index++, form.getOffset());
				stmt.setInt(index++, form.getRowcount());
			}
			form.setCustomerId(customerId);
			rs = stmt.executeQuery();
			List<UserTrackingDto> data = fetchMultiResults(rs,form);
			String countQuery = "SELECT count(t.id) " + "\n" + from + "\n" + where;
			stmt = conn.prepareStatement(countQuery);
			index = 1;
			stmt.setInt(index++, customerId);
			stmt.setLong(index++, form.getUserId());
			stmt.setString(index++, form.getDateFrom());
			stmt.setString(index++, form.getDateTo());
			rs = stmt.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			UserTrackingDto[] ret = new UserTrackingDto[data.size()];
			ret = data.toArray(ret);
			map.put("data", ret);
			map.put("totalRows", count);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return map;
	}
	
	private List<UserTrackingDto> fetchMultiResults(ResultSet rs,UserTrackingForm form) throws SQLException {
		List<UserTrackingDto> resultList = new ArrayList<UserTrackingDto>();
		while (rs.next()) {
			int cellSite = rs.getInt("is_cellsite");
			UserTrackingDto userTrack = new UserTrackingDto();
			userTrack.setClientTime(PlatformUtil.getDateTimeInddMMMYYYY(rs.getString(1)));
			userTrack.setFullName(rs.getString(2));
			
			String eventName = "", bccStatus = "";
			if(rs.getString("event_name").equalsIgnoreCase("TR")){
				eventName = "Tracking Record";
			}else if(rs.getString("event_name").equalsIgnoreCase("LI")){
				eventName = "Application Login (MOB)";
			}else if(rs.getString("event_name").equalsIgnoreCase("LI-WEB")){
				eventName = "Application Login (WEB)";
			}else if(rs.getString("event_name").equalsIgnoreCase("EX")){
				eventName = "Application Exit (MOB)";
			}else if(rs.getString("event_name").equalsIgnoreCase("EX-WEB")){
				eventName = "Application Exit (WEB)";
			}else if(rs.getString("event_name").equalsIgnoreCase("LB")){
				eventName = "Low Battery";
				bccStatus = rs.getString("status");
				if(bccStatus.trim() != "")	bccStatus += "%";
			}else if(rs.getString("event_name").equalsIgnoreCase("CC")){
				eventName = "GPS / Location Services";
				if(!rs.getString("status").equals("")){
					int status = 0;
					try{
						status = rs.getInt("status");
					}catch (Exception e) {
						LOG.error(ErrorLogHandler.getStackTraceAsString(e));
					}
					if(status == 1) bccStatus = "Enabled"; else bccStatus = "Disabled";
				}
			}else if(rs.getString("event_name").equalsIgnoreCase("PE")){
				eventName = "Application Partial Exit";
			}else{
				eventName = rs.getString("event_name");
			}
			userTrack.setEventName(eventName);
			userTrack.setBccStatus(bccStatus);
			userTrack.setCellSite(cellSite);
			if(cellSite == 1) {
				userTrack.setCellSiteLocation("Yes");
			}else{
				userTrack.setCellSiteLocation("No");
			}
			userTrack.setLatitude(rs.getString("lattitude"));
			userTrack.setLongitude(rs.getString("longitude"));
			String speed = rs.getString("speed");
			String filterSpeed = speed;
			if(speed != "" && speed != "NA" && speed != "0"){
				int index = speed.indexOf(".");
				if(index > -1){
				filterSpeed = speed.substring(0,index+2);
				}
			}else{
				filterSpeed = "0.0";
			}
			userTrack.setSpeed(filterSpeed);
			userTrack.setDbTime(rs.getString("db_time"));
			userTrack.setAccuracy(rs.getString("accuracy"));
			resultList.add(userTrack);
		}
		return resultList;
	}
	
	@Override
	public Map<String, Object> getLoginDetailList(LoginDetailsForm loginDetailsForm, int customerId, Connection conn, UserDto user) {
		PreparedStatement stmt = null,stmt2=null,stmt3=null,stmt4=null,stmt5=null;
		ResultSet rs = null,rs2=null,rs3=null,rs4=null,rs5=null;
		int count=0;
		int notloggedcount=0;
		String loginFrom="";
		int htmlcount=0;
		int mobilecount=0;
        int trackList[] =new int[1000];
        long noDays =0;
        Map<String,Object> datamap=new HashMap<String,Object>();
		try {
			String where="";
			String orderBy="";
			String select = "SELECT SQL_CALC_FOUND_ROWS distinct a.user_id as user_id, max(a.modification_time) as modification_time, "
					+"a.session_key as session_key, a.login_from as login_from, concat(u.first_name,'  ',u.last_name) as fullname, "
					+ "u.team_id as team_id, t.name as team_name";
			String  from = " from app_session a join user u on a.user_id=u.id left join team t on u.team_id=t.id ";
			if(user.getUserType().equalsIgnoreCase("Lead")){
                String defaultTeamIds ="";  
	            defaultTeamIds=teamLogic.teamIdsByLeadId(customerId, loginDetailsForm.getUserId());
				where = " where u.customer_id ="+customerId+" "
						+ " and a.user_id in (select id from user where team_id in ("+defaultTeamIds+") )  "
						+ " and u.status=1 and a.session_key!='null'"
						+ " and a.login_from!=''";
			}else{
				where = " where u.customer_id ="+customerId+" and u.user_type_id=3 and u.team_id != 0 and u.status=1 and a.session_key!='null' and a.login_from!=''";
			}
			orderBy =" group by a.id order by modification_time desc ";
			String sql = select + from + where + orderBy;
			if(loginDetailsForm.getRowcount() > 0){
				sql+=" LIMIT "+loginDetailsForm.getOffset()+","+loginDetailsForm.getRowcount();
			}
			stmt = conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			LoginDetailsDto loginDetails = null;
		    List<LoginDetailsDto>  data= new ArrayList<LoginDetailsDto>();
		    List<LoginDetailsDto> mobiledata=new ArrayList<LoginDetailsDto>();
		    List<LoginDetailsDto> htmldata=new ArrayList<LoginDetailsDto>();
		    		while(rs.next()){
		    	 				loginDetails = new LoginDetailsDto();
	    				        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
							    Date currentDate = dateFormat.parse(PlatformUtil.getDateMonthYear());
							    loginFrom=rs.getString("login_from");
							    trackList[count]=rs.getInt("user_id");
		    				    loginDetails.setUserName(rs.getString(5));
		    				    loginDetails.setTeamName(rs.getString("team_name"));
		    				    loginDetails.setModificationTime(PlatformUtil.getTimeSimpleDateTime(rs.getString("modification_time")));
								Date modifyDate=dateFormat.parse(PlatformUtil.getDateMonthYear(rs.getTimestamp("modification_time")));
								long diff = currentDate.getTime() - modifyDate.getTime();
								noDays = diff / (1000 * 60 * 60 * 24) ;
		    				    loginDetails.setNoDays(noDays);
		    				    if(data!=null || loginDetails!=null){
		    				    data.add(loginDetails);
		    				    }
		    			    	if(loginFrom.equals("web")){
		    			    		htmldata.add(loginDetails);
		    			    	}else if(loginFrom.equals("mob")){
		    			    		mobiledata.add(loginDetails);
		    			    	}
		    }
		   List<LoginDetailsDto>  notLogged= new ArrayList<LoginDetailsDto>();
		   String sql2="select u.id,concat(u.first_name,'  ',u.last_name) as fullname,t.name,u.team_id from user u left join team t on u.team_id=t.id where u.customer_id="+customerId+" and u.status=1 and u.user_type_id=3 and u.id not in(select" +
	    			" user_id from app_session where session_key!='null' and customer_id="+customerId+" and login_from!='')";
		    if(user.getUserType().equalsIgnoreCase("Lead")){
                    String defaultTeamIds ="";  
		            defaultTeamIds=teamLogic.teamIdsByLeadId(customerId, loginDetailsForm.getUserId());
					sql2 = sql2 + " AND  u.id in (select id from user where team_id in ("+defaultTeamIds+"))";
		    }
		    if(loginDetailsForm.getRowcount() > 0){
				sql2+=" LIMIT "+loginDetailsForm.getOffset()+","+loginDetailsForm.getRowcount();
			}
		    stmt2=conn.prepareStatement(sql2);
			rs2=stmt2.executeQuery();
			while(rs2.next()){
						loginDetails=new LoginDetailsDto();
						loginDetails.setUserName(rs2.getString("fullname"));
						loginDetails.setTeamName(rs2.getString("t.name"));
			            notLogged.add(loginDetails); 
			}
			String sql3="select count(*) as count from user where customer_id="+customerId+" and status=1 and user_type_id=3 and id not in(select" +
					" user_id from app_session where session_key!='null' and customer_id="+customerId+" and login_from!='')";
			 if(user.getUserType().equalsIgnoreCase("Lead")){
			                    String defaultTeamIds ="";  
					            defaultTeamIds=teamLogic.teamIdsByLeadId(customerId, loginDetailsForm.getUserId());
								sql3 = sql3 + " AND id in (select id from user where team_id in ("+defaultTeamIds+")) ";
			 }
			 stmt3=conn.prepareStatement(sql3);
			 rs3=stmt3.executeQuery();
			 rs3.next();
			 notloggedcount = rs3.getInt("count");
			 stmt4 = conn.prepareStatement(sql);
			 rs4 =stmt4.executeQuery();
				while(rs4.next()){
	   				 loginDetails = new LoginDetailsDto();
	   			     loginFrom=rs4.getString("login_from"); 
	   			    	if(loginFrom.equals("web")){
	   			    		htmlcount++;
	   			    	}
	   			    	if(loginFrom.equals("mob")){
	   			    		mobilecount++;
	   			    	}
					}
			
				stmt5 = conn.prepareStatement("SELECT FOUND_ROWS()");
				rs5 =stmt5.executeQuery();
				if(rs5.next()){
					count = rs5.getInt(1);
				}
				if(loginDetailsForm.getTypeData() == 1){
				    datamap.put("data", data);
					datamap.put("totalRows", count);
				}
				else if(loginDetailsForm.getTypeData() == 2){
			       datamap.put("data", notLogged);
			       datamap.put("totalRows", notloggedcount);
				}
				else if(loginDetailsForm.getTypeData() == 3){
			       datamap.put("data", htmldata);
			       datamap.put("totalRows", count);
				}else if(loginDetailsForm.getTypeData() == 4){
				   datamap.put("data", mobiledata);
				   datamap.put("totalRows", count);
				}
		    } catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		    } finally {
		    resourceManager.close(rs5);
			resourceManager.close(stmt5);
			resourceManager.close(rs4);
			resourceManager.close(stmt4);
			resourceManager.close(rs3);
			resourceManager.close(stmt3);
			resourceManager.close(rs2);
			resourceManager.close(stmt2);
			resourceManager.close(rs);
			resourceManager.close(stmt);
			
		   }
		   return datamap;
	}
	
}
