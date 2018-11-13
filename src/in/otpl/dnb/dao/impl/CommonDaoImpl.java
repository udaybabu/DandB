package in.otpl.dnb.dao.impl;

import in.otpl.dnb.dao.CommonDao;
import in.otpl.dnb.user.UserDao;
import in.otpl.dnb.user.UserDto;
import in.otpl.dnb.user.UserLogic;
import in.otpl.dnb.util.ConfigManager;
import in.otpl.dnb.util.EmailUtil;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDaoImpl implements CommonDao {

	private static final Logger LOG = Logger.getLogger(CommonDaoImpl.class);
	
	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ConfigManager configManager;
	@Autowired
	private UserLogic userLogic;
	@Autowired
	private EmailUtil emailUtil;

	@Override
	public JSONObject getDNBMediaDetails(Connection conn, String uKey,long masterId, long dnbMasterId, String docId, String mediaId) {
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		JSONObject dnbMediaObject = new JSONObject();
		JSONArray mediaArray = new JSONArray();
		try {
			String sql = "select media_link, action_id, doc_id, seq_id, media_path from dnb_workflow_media where ";
			if (mediaId != null && !mediaId.equals(""))
				sql += " id = '" + mediaId + "'";
			else
				sql += " unique_key = '" + uKey + "' and dnb_master_id = "+ dnbMasterId + " and doc_id in ('"+docId+"') and is_deleted = 0 ";
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				JSONObject mobj = new JSONObject();
				mobj.put("docId", rst.getString("doc_id"));
				mobj.put("mLink", rst.getString("media_link"));
				mobj.put("seqId", rst.getString("seq_id"));
				mobj.put("actionId", rst.getString("action_id"));
				mobj.put("mPath", rst.getString("media_path"));
				mediaArray.add(mobj);
			}
			dnbMediaObject.put("mediaArray", mediaArray);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		return dnbMediaObject;
	}
	@Override
	public boolean insertSVRData(Connection conn, long asId, String enquiryId, String uKey, int userId, JSONObject svrObject, int customerId, long masterId, long dnbMasterId) {
		PreparedStatement pstmt = null;
		String amntOrComments = "", personSig = "", corespondentSig = "", mediaId = "";
		JSONObject dnbMediaObj = null;
		int row = 0;
		String docIds = "2";
		try {
			dnbMediaObj = getDNBMediaDetails(conn, uKey, masterId, dnbMasterId,docIds, mediaId);
			if (dnbMediaObj != null && dnbMediaObj.size() > 0) {
				JSONArray mediaArray = JSONArray.fromObject(dnbMediaObj.getString("mediaArray"));
				if (mediaArray != null && mediaArray.size() > 0) {
					for (int k = 0; k < mediaArray.size(); k++) {
						JSONObject mObj = mediaArray.getJSONObject(k);
						String docId = mObj.getString("docId");
						String seqId = mObj.getString("seqId");
						String mediaLink = mObj.getString("mLink");
						if (docId.equals("2") && seqId.equals("1"))
							personSig = mediaLink;
						else if (docId.equals("2") && seqId.equals("2"))
							corespondentSig = mediaLink;
					}
				}
			}
			String sql = "insert into dnb_svr_details (customer_id,assignment_id,enquiry_id,unique_key,user_id,created_time,modified_time,"
					+ "svr_1,svr_2,svr_3,svr_4,svr_5,svr_6,svr_7,svr_8,svr_9,svr_10,"
					+ "svr_11,svr_12,svr_13,svr_14,svr_15,svr_16,svr_17,svr_18,svr_19,svr_20,"
					+ "svr_21,svr_22,svr_23,svr_24,svr_25,svr_26,svr_27,svr_28,svr_29,svr_30,"
					+ "svr_31,svr_32,svr_33,svr_34,svr_35,svr_36,svr_37,svr_38,svr_39,master_id,dnb_master_id,"
					+ "svr_40,amnt_or_comments,p_name,p_desg,p_sig,c_name,c_desg,c_sig) "
					+ "values (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, "
					+ "?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?) ";
			pstmt = conn.prepareStatement(sql);
			row = 0;
			int index = 1;
			pstmt.setLong(index++, customerId);
			pstmt.setLong(index++, asId);
			pstmt.setString(index++, enquiryId);
			pstmt.setString(index++, uKey);
			pstmt.setInt(index++, userId);
			pstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setString(index++,(svrObject.containsKey("svr-name-1")) ? svrObject.getString("svr-name-1") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-2")) ? svrObject.getString("svr-name-2") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-3")) ? svrObject.getString("svr-name-3") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-4")) ? svrObject.getString("svr-name-4") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-5")) ? svrObject.getString("svr-name-5") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-6")) ? svrObject.getString("svr-name-6") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-7")) ? svrObject.getString("svr-name-7") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-8")) ? svrObject.getString("svr-name-8") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-9")) ? svrObject.getString("svr-name-9") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-10")) ? svrObject.getString("svr-name-10") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-11")) ? svrObject.getString("svr-name-11") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-12")) ? svrObject.getString("svr-name-12") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-13")) ? svrObject.getString("svr-name-13") : ""); 
			pstmt.setString(index++,(svrObject.containsKey("svr-name-14")) ? svrObject.getString("svr-name-14") : ""); 
			pstmt.setString(index++,(svrObject.containsKey("svr-name-15")) ? svrObject.getString("svr-name-15") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-16")) ? svrObject.getString("svr-name-16") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-17")) ? svrObject.getString("svr-name-17") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-18")) ? svrObject.getString("svr-name-18") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-19")) ? svrObject.getString("svr-name-19") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-20")) ? svrObject.getString("svr-name-20") : "");
			String val = (!svrObject.getString("svr-name-21").equalsIgnoreCase("null") ? svrObject.getString("svr-name-21") : "");
			pstmt.setString(index++, val);
			pstmt.setString(index++,(svrObject.containsKey("svr-name-22")) ? svrObject.getString("svr-name-22") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-23")) ? svrObject.getString("svr-name-23") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-24")) ? svrObject.getString("svr-name-24") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-25")) ? svrObject.getString("svr-name-25") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-26")) ? svrObject.getString("svr-name-26") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-27")) ? svrObject.getString("svr-name-27") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-28")) ? svrObject.getString("svr-name-28") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-29")) ? svrObject.getString("svr-name-29") : ""); 
			pstmt.setString(index++,(svrObject.containsKey("svr-name-30")) ? svrObject.getString("svr-name-30") : ""); 
			pstmt.setString(index++,(svrObject.containsKey("svr-name-31")) ? svrObject.getString("svr-name-31") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-32")) ? svrObject.getString("svr-name-32") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-33")) ? svrObject.getString("svr-name-33") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-34")) ? svrObject.getString("svr-name-34") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-35")) ? svrObject.getString("svr-name-35") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-36")) ? svrObject.getString("svr-name-36") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-37")) ? svrObject.getString("svr-name-37") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-38")) ? svrObject.getString("svr-name-38") : "");
			pstmt.setString(index++,(svrObject.containsKey("svr-name-39")) ? svrObject.getString("svr-name-39") : "");
			pstmt.setLong(index++, masterId);
			pstmt.setLong(index++, dnbMasterId);
			pstmt.setString(index++,(svrObject.containsKey("svr-name-40")) ? svrObject.getString("svr-name-40") : "");
			if (svrObject.containsKey("svr-name-40")) {
				if (svrObject.getString("svr-name-40").equalsIgnoreCase("yes")) {
					amntOrComments = (svrObject.containsKey("svr-name-321")) ? svrObject.getString("svr-name-321") : "";
				} else if (svrObject.getString("svr-name-40").equalsIgnoreCase("no")) {
					amntOrComments = (svrObject.containsKey("svr-name-323")) ? svrObject.getString("svr-name-323") : "";
				}
			}
			pstmt.setString(index++, amntOrComments);
			pstmt.setString(index++,(svrObject.containsKey("p-name")) ? svrObject.getString("p-name") : "");
			pstmt.setString(index++,(svrObject.containsKey("p-desg")) ? svrObject.getString("p-desg") : "");
			pstmt.setString(index++, personSig);
			pstmt.setString(index++,(svrObject.containsKey("c-name")) ? svrObject.getString("c-name") : "");
			pstmt.setString(index++,(svrObject.containsKey("c-desg")) ? svrObject.getString("c-desg") : "");
			pstmt.setString(index++, corespondentSig);
			row = pstmt.executeUpdate();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(pstmt);
		}
		if (row > 0)
			return true;
		else
			return false;
	}
	@Override
	public boolean insertCCAMData(Connection conn, long asId, String enquiryId, String uKey, int userId, JSONObject ccamObject, int customerId, long masterId, long dnbMasterId) {
		PreparedStatement gpstmt = null, bpstmt = null, epstmt = null, ppstmt = null, spstmt = null, cpstmt = null, sespstmt = null, taxpstmt = null, bipstmt = null, orgpstmt = null, finpstmt = null, supppstmt = null;
		int index = 1, rowCount = 0;
		String telHO1 = "", telHO2 = "", faxHO = "", telRO = "", faxRO = "";
		String tel1 = "", tel2 = "", tel3 = "", tel4 = "", tel5 = "", tel6 = "", tel7 = "", tel8 = "", tel9 = "", tel10 = "", tel11 = "", tel12 = "", tel13 = "", tel14 = "";
		try {
			tel1 = (ccamObject.containsKey("telephone1")) ? ccamObject.getString("telephone1") : "";
			tel2 = (ccamObject.containsKey("telephone2")) ? ccamObject.getString("telephone2") : "";
			tel3 = (ccamObject.containsKey("telephone3")) ? ccamObject.getString("telephone3") : "";
			tel4 = (ccamObject.containsKey("telephone4")) ? ccamObject.getString("telephone4") : "";
			tel5 = (ccamObject.containsKey("telephone5")) ? ccamObject.getString("telephone5") : "";
			tel6 = (ccamObject.containsKey("telephone6")) ? ccamObject.getString("telephone6") : "";
			tel7 = (ccamObject.containsKey("telephone7")) ? ccamObject.getString("telephone7") : "";
			tel8 = (ccamObject.containsKey("telephone8")) ? ccamObject.getString("telephone8") : "";
			tel9 = (ccamObject.containsKey("telephone9")) ? ccamObject.getString("telephone9") : "";
			tel10 = (ccamObject.containsKey("telephone10")) ? ccamObject.getString("telephone10") : "";
			tel11 = (ccamObject.containsKey("telephone11")) ? ccamObject.getString("telephone11") : "";
			tel12 = (ccamObject.containsKey("telephone12")) ? ccamObject.getString("telephone12") : "";
			tel13 = (ccamObject.containsKey("telephone13")) ? ccamObject.getString("telephone13") : "";
			tel14 = (ccamObject.containsKey("telephone14")) ? ccamObject.getString("telephone14") : "";
			telHO1 = tel1 + "-" + tel2 + "-" + tel3;
			telHO2 = tel4 + "-" + tel5 + "-" + tel6;
			faxHO = tel7 + "-" + tel8 + "-" + tel9;
			telRO = 91 + "-" + tel11 + "-" + tel12;
			faxHO = 91 + "-" + tel13 + "-" + tel14;
			String generalSQL = "insert into dnb_ccam_general_data (customer_id,assignment_id,enquiry_id,unique_key,user_id,created_time,modified_time,"
					+ "entity_name,tradstyle,add_ho,tel_ho1,tel_ho2,fax_ho,add_ro,tel_ro,fax_ro,email,website,ce_fname,ce_mname,ce_lname,title,master_id,dnb_master_id) "
					+ "values (?,?,?,?,?,?,?,?,?,? , ?,?,?,?,?,?,?,?,? , ?,?,?,?,?)";
			gpstmt = conn.prepareStatement(generalSQL);
			gpstmt.setLong(index++, customerId);
			gpstmt.setLong(index++, asId);
			gpstmt.setString(index++, enquiryId);
			gpstmt.setString(index++, uKey);
			gpstmt.setInt(index++, userId);
			gpstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			gpstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			gpstmt.setString(index++,(ccamObject.containsKey("giname")) ? ccamObject.getString("giname") : "");
			gpstmt.setString(index++,(ccamObject.containsKey("gitradestyle")) ? ccamObject.getString("gitradestyle") : "");
			gpstmt.setString(index++,(ccamObject.containsKey("giaddress")) ? ccamObject.getString("giaddress") : "");
			gpstmt.setString(index++, telHO1);
			gpstmt.setString(index++, telHO2);
			gpstmt.setString(index++, faxHO);
			gpstmt.setString(index++,(ccamObject.containsKey("gireof")) ? ccamObject.getString("gireof") : "");
			gpstmt.setString(index++, telRO);
			gpstmt.setString(index++, faxRO);
			gpstmt.setString(index++,(ccamObject.containsKey("gimail")) ? ccamObject.getString("gimail") : "");
			gpstmt.setString(index++,(ccamObject.containsKey("giws")) ? ccamObject.getString("giws") : "");
			gpstmt.setString(index++,(ccamObject.containsKey("gice")) ? ccamObject.getString("gice") : "");
			gpstmt.setString(index++,(ccamObject.containsKey("gice1")) ? ccamObject.getString("gice1") : "");
			gpstmt.setString(index++,(ccamObject.containsKey("gice2")) ? ccamObject.getString("gice2") : "");
			gpstmt.setString(index++,(ccamObject.containsKey("title")) ? ccamObject.getString("title") : "");
			gpstmt.setLong(index++, masterId);
			gpstmt.setLong(index++, dnbMasterId);
			rowCount += gpstmt.executeUpdate();
			index = 1;
			String buisnessSQL = "insert into dnb_ccam_buisness_data (customer_id,assignment_id,enquiry_id,unique_key,user_id,created_time,modified_time,"
					+ "lob,lob_if_others,lob_of,lob1,lob1_of,lob1_others,master_id,dnb_master_id) "
					+ "values (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?)";
			bpstmt = conn.prepareStatement(buisnessSQL);
			bpstmt.setInt(index++, customerId);
			bpstmt.setLong(index++, asId);
			bpstmt.setString(index++, enquiryId);
			bpstmt.setString(index++, uKey);
			bpstmt.setInt(index++, userId);
			bpstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			bpstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			bpstmt.setString(index++,(ccamObject.containsKey("lob")) ? ccamObject.getString("lob") : "");
			bpstmt.setString(index++,(ccamObject.containsKey("ba-of21")) ? ccamObject.getString("ba-of21") : "");
			bpstmt.setString(index++,(ccamObject.containsKey("ba-of")) ? ccamObject.getString("baof1") : "");
			bpstmt.setString(index++,(ccamObject.containsKey("lob-and")) ? ccamObject.getString("lob-and") : "");
			bpstmt.setString(index++,(ccamObject.containsKey("ba-of2")) ? ccamObject.getString("baof2") : "");
			bpstmt.setString(index++,(ccamObject.containsKey("ba-other-name3")) ? ccamObject.getString("ba-other-name3") : "");
			bpstmt.setLong(index++, masterId);
			bpstmt.setLong(index++, dnbMasterId);
			rowCount += bpstmt.executeUpdate();
			index = 1;
			String employeeSQL = "insert into dnb_ccam_emp_data (customer_id,assignment_id,enquiry_id,unique_key,user_id,created_time,modified_time,"
					+ "pw_cy,pw_cy_ac,pw_py,ft_cy,ft_cy_ac,ft_py,pt_cy,pt_cy_ac,pt_py,se_cy,se_cy_ac,se_py,total_cy,total_cy_ac,"
					+ "total_py,is_staff_shared,master_id,dnb_master_id)"
					+ "values (?,?,?,?,?,?,?,?,?,? , ?,?,?,?,?,?,?,?,?,? , ?,?,?,?,?)";
			epstmt = conn.prepareStatement(employeeSQL);
			epstmt.setInt(index++, customerId);
			epstmt.setLong(index++, asId);
			epstmt.setString(index++, enquiryId);
			epstmt.setString(index++, uKey);
			epstmt.setInt(index++, userId);
			epstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			epstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			epstmt.setString(index++,(ccamObject.containsKey("ne-curr-year")) ? ccamObject.getString("ne-curr-year") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-curr-year1")) ? ccamObject.getString("ne-curr-year1") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-prev-year")) ? ccamObject.getString("ne-prev-year") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-ft-1")) ? ccamObject.getString("ne-ft-1") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-ft-2")) ? ccamObject.getString("ne-ft-2") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-ft-3")) ? ccamObject.getString("ne-ft-3") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-pt-1")) ? ccamObject.getString("ne-pt-1") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-pt-2")) ? ccamObject.getString("ne-pt-2") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-pt-3")) ? ccamObject.getString("ne-pt-3") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-se-1")) ? ccamObject.getString("ne-se-1") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-se-2")) ? ccamObject.getString("ne-se-2") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-se-3")) ? ccamObject.getString("ne-se-3") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-tt-1")) ? ccamObject.getString("ne-tt-1") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-tt-2")) ? ccamObject.getString("ne-tt-2") : "");
			epstmt.setString(index++,(ccamObject.containsKey("ne-tt-3")) ? ccamObject.getString("ne-tt-3") : "");
			epstmt.setString(index++,(ccamObject.containsKey("staff-shared")) ? ccamObject.getString("staff-shared") : "");
			epstmt.setLong(index++, masterId);
			epstmt.setLong(index++, dnbMasterId);
			rowCount += epstmt.executeUpdate();

			index = 1;
			String purchaseSQL = "insert into dnb_ccam_purchase_data (customer_id,assignment_id,enquiry_id,unique_key,user_id,created_time,modified_time,"
					+ "pu_local,international,from_india,japan,taiwan,europe,germany,uk,fareast,italy,australia,other1,middle_east,china,other2,"
					+ "usa,skorea,other3,"
					+ "bt_local,bt_local_days,bt_overseas,bt_overseas_days,bt_others,master_id,dnb_master_id) "
					+ "values (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,? , ?,?)";
			ppstmt = conn.prepareStatement(purchaseSQL);
			ppstmt.setInt(index++, customerId);
			ppstmt.setLong(index++, asId);
			ppstmt.setString(index++, enquiryId);
			ppstmt.setString(index++, uKey);
			ppstmt.setInt(index++, userId);
			ppstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			ppstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			ppstmt.setString(index++,(ccamObject.containsKey("local-international")) ? ccamObject.getString("local-international") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("local-international1")) ? ccamObject.getString("local-international1") : ""); // from
			ppstmt.setString(index++,(ccamObject.containsKey("india")) ? ccamObject.getString("india") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("japan")) ? ccamObject.getString("japan") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("taiwan")) ? ccamObject.getString("taiwan") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("europe")) ? ccamObject.getString("europe") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("germany")) ? ccamObject.getString("germany") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("uk")) ? ccamObject.getString("uk"): "");
			ppstmt.setString(index++,(ccamObject.containsKey("far-east")) ? ccamObject.getString("far-east") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("italy")) ? ccamObject.getString("italy") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("australia")) ? ccamObject.getString("australia") : "");
			ppstmt.setString(index++,"");
			ppstmt.setString(index++,(ccamObject.containsKey("mid-east")) ? ccamObject.getString("mid-east") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("china")) ? ccamObject.getString("china") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("country2")) ? ccamObject.getString("country2") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("usa")) ? ccamObject.getString("usa") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("skorea")) ? ccamObject.getString("skorea") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("country3")) ? ccamObject.getString("country3") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("local-1")) ? ccamObject.getString("local-1") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("local1")) ? ccamObject.getString("local1") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("overseas-1")) ? ccamObject.getString("overseas-1") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("overseas1")) ? ccamObject.getString("overseas1") : "");
			ppstmt.setString(index++,(ccamObject.containsKey("bt-others")) ? ccamObject.getString("bt-others") : "");
			ppstmt.setLong(index++, masterId);
			ppstmt.setLong(index++, dnbMasterId);
			rowCount += ppstmt.executeUpdate();
			index = 1;
			String salesSQL = "insert into dnb_ccam_sales_data (customer_id,assignment_id,enquiry_id,unique_key,user_id,created_time,modified_time,"
					+ "ts_local,ts_international,india,japan,taiwan,europe,germany,uk,fareast,italy,australia,other1,"
					+ "middle_east,china,other2,usa,skorea,other3,sl_local,sl_local_days,sl_overseas,"
					+ "sl_overseas_days,sl_others,master_id,dnb_master_id)"
					+ "values (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?,?)";
			spstmt = conn.prepareStatement(salesSQL);
			spstmt.setInt(index++, customerId);
			spstmt.setLong(index++, asId);
			spstmt.setString(index++, enquiryId);
			spstmt.setString(index++, uKey);
			spstmt.setInt(index++, userId);
			spstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			spstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt1")) ? ccamObject.getString("sa-txt1") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt2")) ? ccamObject.getString("sa-txt2") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt3")) ? ccamObject.getString("sa-txt3") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt4")) ? ccamObject.getString("sa-txt4") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt5")) ? ccamObject.getString("sa-txt5") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt6")) ? ccamObject.getString("sa-txt6") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-germany")) ? ccamObject.getString("sa-germany") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt7")) ? ccamObject.getString("sa-txt7") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt8")) ? ccamObject.getString("sa-txt8") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt9")) ? ccamObject.getString("sa-txt9") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt10")) ? ccamObject.getString("sa-txt155") : "");
			spstmt.setString(index++,"");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt12")) ? ccamObject.getString("sa-txt12") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt13")) ? ccamObject.getString("sa-txt13") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt14")) ? ccamObject.getString("sa-txt14") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt15")) ? ccamObject.getString("sa-txt15") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt16")) ? ccamObject.getString("sa-txt16") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt17")) ? ccamObject.getString("sa-txt17") : "");
			spstmt.setString(index++,(ccamObject.containsKey("local-2")) ? ccamObject.getString("local-2") : "");
			spstmt.setString(index++,(ccamObject.containsKey("local2")) ? ccamObject.getString("local2") : "");
			spstmt.setString(index++,(ccamObject.containsKey("overseas-2")) ? ccamObject.getString("overseas-2") : "");
			spstmt.setString(index++,(ccamObject.containsKey("overseas2")) ? ccamObject.getString("overseas2") : "");
			spstmt.setString(index++,(ccamObject.containsKey("sa-txt20")) ? ccamObject.getString("sa-txt20") : "");
			spstmt.setLong(index++, masterId);
			spstmt.setLong(index++, dnbMasterId);
			rowCount += spstmt.executeUpdate();
			index = 1;
			String custDetailsSQL = "insert into dnb_customer_data (customer_id,assignment_id,enquiry_id,unique_key,user_id,created_time,modified_time,"
					+ "type_of_customer,nos_of_customer,cust_ranges_frm,cust_ranges_to,buisnes_relationship,"
					+ "principal_names,which_project,"
					+ "customer_name,person_name,tel_no,total_sales,"
					+"master_id,dnb_master_id) "
					+ "values (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?)";
			cpstmt = conn.prepareStatement(custDetailsSQL);
			cpstmt.setInt(index++, customerId);
			cpstmt.setLong(index++, asId);
			cpstmt.setString(index++, enquiryId);
			cpstmt.setString(index++, uKey);
			cpstmt.setInt(index++, userId);
			cpstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			cpstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			cpstmt.setString(index++,(ccamObject.containsKey("toc")) ? ccamObject.getString("toc") : "");
			cpstmt.setString(index++,(ccamObject.containsKey("cd-txt1")) ? ccamObject.getString("cd-txt1") : "");
			cpstmt.setString(index++,(ccamObject.containsKey("cd-txt2")) ? ccamObject.getString("cd-txt2") : "");
			cpstmt.setString(index++,(ccamObject.containsKey("cd-to")) ? ccamObject.getString("cd-to") : "");
			cpstmt.setString(index++,(ccamObject.containsKey("br-agencies")) ? ccamObject.getString("br-agencies") : "");
			cpstmt.setString(index++,(ccamObject.containsKey("cd-txt7")) ? ccamObject.getString("cd-txt7") : "");
			cpstmt.setString(index++,(ccamObject.containsKey("cd-txt8")) ? ccamObject.getString("cd-txt8") : "");
			cpstmt.setString(index++,(ccamObject.containsKey("cd-txt3")) ? ccamObject.getString("cd-txt3") : "");
			cpstmt.setString(index++,(ccamObject.containsKey("cd-txt4")) ? ccamObject.getString("cd-txt4") : "");
			cpstmt.setString(index++,(ccamObject.containsKey("cd-txt5")) ? ccamObject.getString("cd-txt5") : "");
			cpstmt.setString(index++,(ccamObject.containsKey("cd-txt6")) ? ccamObject.getString("cd-txt6") : "");
			cpstmt.setLong(index++, masterId);
			cpstmt.setLong(index++, dnbMasterId);
			rowCount += cpstmt.executeUpdate();
			index = 1;
			String seasonalSQL = "insert into dnb_seasonal_data (customer_id,assignment_id,enquiry_id,unique_key,user_id,created_time,modified_time,"
					+ "whether_seasonal,peak_season,peak_period_from,peak_period_to,"
					+ "type_of_good,ui_measurement,capacity,actual_production,no_of_sheet,"
					+ "period,whether_main_orsub,whether_reg_contractor,reg_body,category,financial_grade,"
					+ "last_six_pn,last_six_pl,last_six_comp_date,current_pn,current_pl,current_comp_date,"
					+ "master_id,dnb_master_id)" + "values (?,";
			for (int j = 0; j < 28; j++)
				seasonalSQL += "?,";
			seasonalSQL += "?)";
			sespstmt = conn.prepareStatement(seasonalSQL);
			sespstmt.setInt(index++, customerId);
			sespstmt.setLong(index++, asId);
			sespstmt.setString(index++, enquiryId);
			sespstmt.setString(index++, uKey);
			sespstmt.setInt(index++, userId);
			sespstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			sespstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad1")) ? ccamObject.getString("cd-rad1") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad2")) ? ccamObject.getString("cd-rad2") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad3")) ? ccamObject.getString("cd-rad3") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad4")) ? ccamObject.getString("cd-rad4") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad5")) ? ccamObject.getString("cd-rad5") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad6")) ? ccamObject.getString("cd-rad6") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad7")) ? ccamObject.getString("cd-rad7") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad8")) ? ccamObject.getString("cd-rad8") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad9")) ? ccamObject.getString("cd-rad9") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad10")) ? ccamObject.getString("cd-rad10") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("contractor")) ? ccamObject.getString("contractor") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("Registered-Contractor")) ? ccamObject.getString("Registered-Contractor") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad11")) ? ccamObject.getString("cd-rad11") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad12")) ? ccamObject.getString("cd-rad12") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad13")) ? ccamObject.getString("cd-rad13") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad14")) ? ccamObject.getString("cd-rad14") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad15")) ? ccamObject.getString("cd-rad15") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad16")) ? ccamObject.getString("cd-rad16") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad17")) ? ccamObject.getString("cd-rad17") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad18")) ? ccamObject.getString("cd-rad18") : "");
			sespstmt.setString(index++,(ccamObject.containsKey("cd-rad19")) ? ccamObject.getString("cd-rad19") : "");
			sespstmt.setLong(index++, masterId);
			sespstmt.setLong(index++, dnbMasterId);
			rowCount += sespstmt.executeUpdate();
			index = 1;
			String taxSQL = "insert into dnb_ccam_tax_data (customer_id,assignment_id,enquiry_id,unique_key,user_id,created_time,modified_time,"
					+ "tax_incentive,tax_other,iso_no,iso_start_date,iso_expiry_date,year_value1,year_value2,year_value3,"
					+ "fob_value1,fob_value2,fob_value3,cif_value1,cif_value2,cif_value3,"
					+ "insurance_cmp_name,insurance_type,insurance_items,insurance_in_lakhs,insurance_valid_frm,insurance_valid_upto,"
					+ "ro_address,ro_rol,ro_area,ho_address,ho_rol,ho_area,"
					+ "brances_address,brances_rol,brances_area,divisions_address,divisions_rol,divisions_area,"
					+ "annexures,bank_details,rs_in_bank,"
					+ "bank_name,bank_type,bank_amnt_sactioned,bank_amnt_disbursed,bank_outstanding,"
					+ "principals_prefix,principals_fname,principals_mname,principals_lname,principals_title,master_id,dnb_master_id) "
					+ "values (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,? , ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			taxpstmt = conn.prepareStatement(taxSQL);
			taxpstmt.setInt(index++, customerId);
			taxpstmt.setLong(index++, asId);
			taxpstmt.setString(index++, enquiryId);
			taxpstmt.setString(index++, uKey);
			taxpstmt.setInt(index++, userId);
			taxpstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			taxpstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			taxpstmt.setString(index++,(ccamObject.containsKey("tax-incentive")) ? ccamObject.getString("tax-incentive") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt1")) ? ccamObject.getString("ti-txt1") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt2")) ? ccamObject.getString("ti-txt2") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-dat1")) ? ccamObject.getString("ti-dat1") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-dat2")) ? ccamObject.getString("ti-dat2") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt01")) ? ccamObject.getString("ti-txt01") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt02")) ? ccamObject.getString("ti-txt02") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt03")) ? ccamObject.getString("ti-txt03") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt3")) ? ccamObject.getString("ti-txt3") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt4")) ? ccamObject.getString("ti-txt4") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt5")) ? ccamObject.getString("ti-txt5") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt6")) ? ccamObject.getString("ti-txt6") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt7")) ? ccamObject.getString("ti-txt7") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt8")) ? ccamObject.getString("ti-txt8") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt9")) ? ccamObject.getString("ti-txt9") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt10")) ? ccamObject.getString("ti-txt10") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt11")) ? ccamObject.getString("ti-txt11") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt12")) ? ccamObject.getString("ti-txt12") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt13")) ? ccamObject.getString("ti-txt13") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt14")) ? ccamObject.getString("ti-txt14") : "");

			String roa1 = "",roa2="",roa3="",roa4="",rol1="",rol2="",rol3="",rol4="",roAdd1="",roAdd2="",roAdd3="",roAdd4="";
			roa1 = (ccamObject.containsKey("ti-txt15")) ? ccamObject.getString("ti-txt15") : "NA";
			roa2 = (ccamObject.containsKey("ti-txt18")) ? ccamObject.getString("ti-txt18") : "NA";
			roa3 = (ccamObject.containsKey("ti-txt21")) ? ccamObject.getString("ti-txt21") : "NA";
			roa4 = (ccamObject.containsKey("ti-txt24")) ? ccamObject.getString("ti-txt24") : "NA";
			rol1 = (ccamObject.containsKey("ti-txt16")) ? ccamObject.getString("ti-txt16") : "NA";
			rol2 = (ccamObject.containsKey("ti-txt19")) ? ccamObject.getString("ti-txt19") : "NA";
			rol3 = (ccamObject.containsKey("ti-txt22")) ? ccamObject.getString("ti-txt22") : "NA";
			rol4 = (ccamObject.containsKey("ti-txt25")) ? ccamObject.getString("ti-txt25") : "NA";
			roAdd1 = (ccamObject.containsKey("ti-txt17")) ? ccamObject.getString("ti-txt17") : "NA";
			roAdd2 = (ccamObject.containsKey("ti-txt20")) ? ccamObject.getString("ti-txt20") : "NA";
			roAdd3 = (ccamObject.containsKey("ti-txt23")) ? ccamObject.getString("ti-txt23") : "NA";
			roAdd4 = (ccamObject.containsKey("ti-txt26")) ? ccamObject.getString("ti-txt26") : "NA";

			taxpstmt.setString(index++, roa1+"~~~"+roa2+"~~~"+roa3+"~~~"+roa4); 
			taxpstmt.setString(index++, rol1+"~~~"+rol2+"~~~"+rol3+"~~~"+rol4); 
			taxpstmt.setString(index++, roAdd1+"~~~"+roAdd2+"~~~"+roAdd3+"~~~"+roAdd4); 

			String hoa1 = "",hoa2="",hoa3="",hoa4="",hol1="",hol2="",hol3="",hol4="",hoAdd1="",hoAdd2="",hoAdd3="",hoAdd4="";
			hoa1 = (ccamObject.containsKey("ti-txt27")) ? ccamObject.getString("ti-txt27") : "NA";
			hoa2 = (ccamObject.containsKey("ti-txt30")) ? ccamObject.getString("ti-txt30") : "NA";
			hoa3 = (ccamObject.containsKey("ti-txt33")) ? ccamObject.getString("ti-txt33") : "NA";
			hoa4 = (ccamObject.containsKey("ti-txt36")) ? ccamObject.getString("ti-txt36") : "NA";
			hol1 = (ccamObject.containsKey("ti-txt28")) ? ccamObject.getString("ti-txt28") : "NA";
			hol2 = (ccamObject.containsKey("ti-txt31")) ? ccamObject.getString("ti-txt31") : "NA";
			hol3 = (ccamObject.containsKey("ti-txt34")) ? ccamObject.getString("ti-txt34") : "NA";
			hol4 = (ccamObject.containsKey("ti-txt37")) ? ccamObject.getString("ti-txt37") : "NA";
			hoAdd1 = (ccamObject.containsKey("ti-txt29")) ? ccamObject.getString("ti-txt29") : "NA";
			hoAdd2 = (ccamObject.containsKey("ti-txt32")) ? ccamObject.getString("ti-txt32") : "NA";
			hoAdd3 = (ccamObject.containsKey("ti-txt35")) ? ccamObject.getString("ti-txt35") : "NA";
			hoAdd4 = (ccamObject.containsKey("hd-txt38")) ? ccamObject.getString("hd-txt38") : "NA";

			taxpstmt.setString(index++, hoa1+"~~~"+hoa2+"~~~"+hoa3+"~~~"+hoa4); 
			taxpstmt.setString(index++, hol1+"~~~"+hol2+"~~~"+hol3+"~~~"+hol4); 
			taxpstmt.setString(index++, hoAdd1+"~~~"+hoAdd2+"~~~"+hoAdd3+"~~~"+hoAdd4); 

			String boa1 = "",boa2="",boa3="",boa4="",bol1="",bol2="",bol3="",bol4="",boAdd1="",boAdd2="",boAdd3="",boAdd4="";
			boa1 = (ccamObject.containsKey("ti-txt38")) ? ccamObject.getString("ti-txt38") : "NA";
			boa2 = (ccamObject.containsKey("ti-txt41")) ? ccamObject.getString("ti-txt41") : "NA";
			boa3 = (ccamObject.containsKey("ti-txt44")) ? ccamObject.getString("ti-txt44") : "NA";
			boa4 = (ccamObject.containsKey("ti-txt47")) ? ccamObject.getString("ti-txt47") : "NA";
			bol1 = (ccamObject.containsKey("ti-txt39")) ? ccamObject.getString("ti-txt39") : "NA";
			bol2 = (ccamObject.containsKey("ti-txt42")) ? ccamObject.getString("ti-txt42") : "NA";
			bol3 = (ccamObject.containsKey("ti-txt45")) ? ccamObject.getString("ti-txt45") : "NA";
			bol4 = (ccamObject.containsKey("ti-txt48")) ? ccamObject.getString("ti-txt48") : "NA";
			boAdd1 = (ccamObject.containsKey("ti-txt40")) ? ccamObject.getString("ti-txt40") : "NA";
			boAdd2 = (ccamObject.containsKey("ti-txt43")) ? ccamObject.getString("ti-txt43") : "NA";
			boAdd3 = (ccamObject.containsKey("ti-txt46")) ? ccamObject.getString("ti-txt46") : "NA";
			boAdd4 = (ccamObject.containsKey("hd-txt49")) ? ccamObject.getString("hd-txt49") : "NA";

			taxpstmt.setString(index++, boa1+"~~~"+boa2+"~~~"+boa3+"~~~"+boa4); 
			taxpstmt.setString(index++, bol1+"~~~"+bol2+"~~~"+bol3+"~~~"+bol4); 
			taxpstmt.setString(index++, boAdd1+"~~~"+boAdd2+"~~~"+boAdd3+"~~~"+boAdd4); 

			String doa1 = "",doa2="",doa3="",doa4="",dol1="",dol2="",dol3="",dol4="",doAdd1="",doAdd2="",doAdd3="",doAdd4="";
			doa1 = (ccamObject.containsKey("ti-txt50")) ? ccamObject.getString("ti-txt50") : "NA";
			doa2 = (ccamObject.containsKey("ti-txt53")) ? ccamObject.getString("ti-txt53") : "NA";
			doa3 = (ccamObject.containsKey("ti-txt56")) ? ccamObject.getString("ti-txt56") : "NA";
			doa4 = (ccamObject.containsKey("ti-txt59")) ? ccamObject.getString("ti-txt59") : "NA";
			dol1 = (ccamObject.containsKey("ti-txt51")) ? ccamObject.getString("ti-txt51") : "NA";
			dol2 = (ccamObject.containsKey("ti-txt54")) ? ccamObject.getString("ti-txt54") : "NA";
			dol3 = (ccamObject.containsKey("ti-txt57")) ? ccamObject.getString("ti-txt57") : "NA";
			dol4 = (ccamObject.containsKey("ti-txt60")) ? ccamObject.getString("ti-txt60") : "NA";
			doAdd1 = (ccamObject.containsKey("ti-txt52")) ? ccamObject.getString("ti-txt52") : "NA";
			doAdd2 = (ccamObject.containsKey("ti-txt55")) ? ccamObject.getString("ti-txt55") : "NA";
			doAdd3 = (ccamObject.containsKey("ti-txt58")) ? ccamObject.getString("ti-txt58") : "NA";
			doAdd4 = (ccamObject.containsKey("hd-txt61")) ? ccamObject.getString("hd-txt61") : "NA";

			taxpstmt.setString(index++, doa1+"~~~"+doa2+"~~~"+doa3+"~~~"+doa4); 
			taxpstmt.setString(index++, dol1+"~~~"+dol2+"~~~"+dol3+"~~~"+dol4); 
			taxpstmt.setString(index++, doAdd1+"~~~"+doAdd2+"~~~"+doAdd3+"~~~"+doAdd4); 
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt62")) ? ccamObject.getString("ti-txt62") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt63")) ? ccamObject.getString("ti-txt63") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt64")) ? ccamObject.getString("ti-txt64") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt65")) ? ccamObject.getString("ti-txt65") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt66")) ? ccamObject.getString("ti-txt66") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt67")) ? ccamObject.getString("ti-txt67") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt68")) ? ccamObject.getString("ti-txt68") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt69")) ? ccamObject.getString("ti-txt69") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("prefix1")) ? ccamObject.getString("prefix1") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt70")) ? ccamObject.getString("ti-txt70") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt701")) ? ccamObject.getString("ti-txt701") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt702")) ? ccamObject.getString("ti-txt702") : "");
			taxpstmt.setString(index++,(ccamObject.containsKey("ti-txt71")) ? ccamObject.getString("ti-txt71") : "");
			taxpstmt.setLong(index++, masterId);
			taxpstmt.setLong(index++, dnbMasterId);
			rowCount += taxpstmt.executeUpdate();
			index = 1;
			String biSQL = "insert into dnb_ccam_bi_data (customer_id,assignment_id,enquiry_id,unique_key,user_id,created_time,modified_time,"
					+ "prefix,fname,mname,lname,designation,address,tel,email,passport_no,dob,edu_details,doj_subject,current_pos_date,is_founder,"
					+ "years,total_exp,is_active,is_retired,company_name,last_position,period,other_directorships,"
					+ "prefix1,fname1,mname1,lname1,designation1,address1,tel1,email1,passport_no1,dob1,edu_details1,doj_subject1,current_pos_date1,is_founder1,"
					+ "years1,total_exp1,is_active1,is_retired1,company_name1,last_position1,period1,other_directorships1,"
					+ "master_id,dnb_master_id) " + "values (?,";
			for (int k = 0; k < 51; k++)
				biSQL += "?,";
			biSQL += "?)";
			bipstmt = conn.prepareStatement(biSQL);
			bipstmt.setInt(index++, customerId);
			bipstmt.setLong(index++, asId);
			bipstmt.setString(index++, enquiryId);
			bipstmt.setString(index++, uKey);
			bipstmt.setInt(index++, userId);
			bipstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			bipstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			bipstmt.setString(index++,(ccamObject.containsKey("bi1")) ? ccamObject.getString("bi1") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi1-f")) ? ccamObject.getString("bi1-f") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi101")) ? ccamObject.getString("bi101") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi100")) ? ccamObject.getString("bi100") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi-designation")) ? ccamObject.getString("bi-designation") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi3")) ? ccamObject.getString("bi3") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi4")) ? ccamObject.getString("bi4") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi5")) ? ccamObject.getString("bi5") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi6")) ? ccamObject.getString("bi6") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi7")) ? ccamObject.getString("bi7") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi8")) ? ccamObject.getString("bi8") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi9")) ? ccamObject.getString("bi9") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi10")) ? ccamObject.getString("bi10") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("founder")) ? ccamObject.getString("founder") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi11")) ? ccamObject.getString("bi11") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi12")) ? ccamObject.getString("bi12") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("d-to-d")) ? ccamObject.getString("d-to-d") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi13")) ? ccamObject.getString("bi13") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi14")) ? ccamObject.getString("bi14") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi15")) ? ccamObject.getString("bi15") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi16")) ? ccamObject.getString("bi16") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi17")) ? ccamObject.getString("bi17") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi12")) ? ccamObject.getString("bi12") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi1-f2")) ? ccamObject.getString("bi1-f2") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi1012")) ? ccamObject.getString("bi1012") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi1002")) ? ccamObject.getString("bi1002") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi-designation2")) ? ccamObject.getString("bi-designation2") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi32")) ? ccamObject.getString("bi32") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi42")) ? ccamObject.getString("bi42") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi52")) ? ccamObject.getString("bi52") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi62")) ? ccamObject.getString("bi62") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi72")) ? ccamObject.getString("bi72") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi82")) ? ccamObject.getString("bi82") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi92")) ? ccamObject.getString("bi92") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi102")) ? ccamObject.getString("bi102") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("founder2")) ? ccamObject.getString("founder2") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi112")) ? ccamObject.getString("bi112") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi122")) ? ccamObject.getString("bi122") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("d-to-d2")) ? ccamObject.getString("d-to-d2") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi132")) ? ccamObject.getString("bi132") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi142")) ? ccamObject.getString("bi142") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi152")) ? ccamObject.getString("bi152") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi162")) ? ccamObject.getString("bi162") : "");
			bipstmt.setString(index++,(ccamObject.containsKey("bi172")) ? ccamObject.getString("bi172") : "");
			bipstmt.setLong(index++, masterId);
			bipstmt.setLong(index++, dnbMasterId);
			rowCount += bipstmt.executeUpdate();
			index = 1;
			String orgSQL = "insert into dnb_ccam_org_data (customer_id,assignment_id,enquiry_id,unique_key,user_id,created_time,modified_time,"
					+ "master_id,dnb_master_id,legal_status,incor_date,incor_reg_no,in_state_of,previous_legals,"
					+ "ls_change_date,previous_comp_name,comp_name_cd,date_of_change,ssi,ssi_reg_no,ac_val,ac_par_val,"
					+ "ac_par_share,ices_val,ices_par_val,ices_par_share,icps_val,icps_par_val,icps_par_share,pces_val," //30
					+ "pces_par_val,pces_par_share,as_on,change_in_capital,capital_details,ld_name,ld_date,"
					+ "proposed_details,prefix,name,nationality,share_ord,share_pref,equity_ord,"
					+ "equity_pref,"
					+ "share_as_on," // 46
					+ "last_agm_date,last_annual_date,last_financial_date,dormant_period,bs_nature,bs_details,bs_details_others,"
					+ "auditor_name,auditor_address,pc_cmpny_name,pc_address,pc_shareholding,imm_cmpny_name,imm_address,imm_shareholding," // 61
					+ "ulti_cmpny_name,ulti_address,ulti_shareholding,sbus_cmpny_name,sbus_address,sbus_shareholding,sbus_others,"
					+ "sbus_others_cmpny_name,sbus_others_address,sbus_others_shareholding,affi_cmpny_name,affi_address," // 73
					+ "affi_shareholding,affi_others,affi_others_cmpny_name,affi_others_address,affi_others_sharholding,"
					+ "gc_cmpny_name,gc_address,gc_shareholding,gc_others,gc_others_cmpny_name,gc_others_address,gc_others_sharholding) " // 85
					+ "values (?,";
			for (int k = 0; k < 83; k++)
				orgSQL += "?,";
			orgSQL += "?)";
			orgpstmt = conn.prepareStatement(orgSQL);
			orgpstmt.setInt(index++, customerId);
			orgpstmt.setLong(index++, asId);
			orgpstmt.setString(index++, enquiryId);
			orgpstmt.setString(index++, uKey);
			orgpstmt.setInt(index++, userId);
			orgpstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			orgpstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			orgpstmt.setLong(index++, masterId);
			orgpstmt.setLong(index++, dnbMasterId);
			orgpstmt.setString(index++,(ccamObject.containsKey("legal")) ? ccamObject.getString("legal") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt1")) ? ccamObject.getString("org-txt1") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt2")) ? ccamObject.getString("org-txt2") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt3")) ? ccamObject.getString("org-txt3") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt4")) ? ccamObject.getString("org-txt4") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("ls")) ? ccamObject.getString("ls"): "");
			orgpstmt.setString(	index++,(ccamObject.containsKey("ls1")) ? ccamObject.getString("ls1") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("changedate")) ? ccamObject.getString("changedate") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt5")) ? ccamObject.getString("org-txt5") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-rad1")) ? ccamObject.getString("org-rad1") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt8")) ? ccamObject.getString("org-txt8") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-ch7")) ? ccamObject.getString("org-ch7") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt9")) ? ccamObject.getString("org-txt9") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt10")) ? ccamObject.getString("org-txt10") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-ch8")) ? ccamObject.getString("org-ch8") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt11")) ? ccamObject.getString("org-txt11") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt12")) ? ccamObject.getString("org-txt12") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-ch9")) ? ccamObject.getString("org-ch9") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt13")) ? ccamObject.getString("org-txt13") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt14")) ? ccamObject.getString("org-txt14") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-ch10")) ? ccamObject.getString("org-ch10") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt15")) ? ccamObject.getString("org-txt15") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt16")) ? ccamObject.getString("org-txt16") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt17")) ? ccamObject.getString("org-txt17") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt18")) ? ccamObject.getString("org-txt18") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt18-ccd")) ? ccamObject.getString("org-txt18-ccd") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("stock-exchange")) ? ccamObject.getString("stock-exchange") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt20")) ? ccamObject.getString("org-txt20") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt21")) ? ccamObject.getString("org-txt21") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("prefix102")) ? ccamObject.getString("prefix102") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt222")) ? ccamObject.getString("org-txt222") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt252")) ? ccamObject.getString("org-txt252") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt262")) ? ccamObject.getString("org-txt262") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt272")) ? ccamObject.getString("org-txt272") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt282")) ? ccamObject.getString("org-txt282") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt292")) ? ccamObject.getString("org-txt292") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt30")) ? ccamObject.getString("org-txt30") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt31")) ? ccamObject.getString("org-txt31") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt32")) ? ccamObject.getString("org-txt32") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt33")) ? ccamObject.getString("org-txt33") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt34")) ? ccamObject.getString("org-txt34") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("restructuring")) ? ccamObject.getString("restructuring") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt35")) ? ccamObject.getString("org-txt35") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt36")) ? ccamObject.getString("org-txt36") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt37")) ? ccamObject.getString("org-txt37") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt370")) ? ccamObject.getString("org-txt370") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt38")) ? ccamObject.getString("org-txt38") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt39")) ? ccamObject.getString("org-txt39") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt40")) ? ccamObject.getString("org-txt40") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt41")) ? ccamObject.getString("org-txt41") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt42")) ? ccamObject.getString("org-txt42") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt43")) ? ccamObject.getString("org-txt43") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt44")) ? ccamObject.getString("org-txt44") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt45")) ? ccamObject.getString("org-txt45") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt46")) ? ccamObject.getString("org-txt46") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt47")) ? ccamObject.getString("org-txt47") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt48")) ? ccamObject.getString("org-txt48") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt49")) ? ccamObject.getString("org-txt49") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt50")) ? ccamObject.getString("org-txt50") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt51")) ? ccamObject.getString("org-txt51") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt52")) ? ccamObject.getString("org-txt52") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt53")) ? ccamObject.getString("org-txt53") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt54")) ? ccamObject.getString("org-txt54") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt55")) ? ccamObject.getString("org-txt55") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt56")) ? ccamObject.getString("org-txt56") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt57")) ? ccamObject.getString("org-txt57") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt58")) ? ccamObject.getString("org-txt58") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt59")) ? ccamObject.getString("org-txt59") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt60")) ? ccamObject.getString("org-txt60") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt61")) ? ccamObject.getString("org-txt61") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt62")) ? ccamObject.getString("org-txt62") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt63")) ? ccamObject.getString("org-txt63") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt64")) ? ccamObject.getString("org-txt64") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt65")) ? ccamObject.getString("org-txt65") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt66")) ? ccamObject.getString("org-txt66") : "");
			orgpstmt.setString(index++,(ccamObject.containsKey("org-txt67")) ? ccamObject.getString("org-txt67") : "");
			rowCount += orgpstmt.executeUpdate();
			index = 1;
			String financialSQL = "insert into dnb_ccam_financial_data (customer_id,assignment_id,enquiry_id,unique_key,user_id,created_time,modified_time,"
					+ "master_id,dnb_master_id,financial_sd1,financial_sd2,financial_sd3,sales1,sales2,sales3,other_income1,other_income2,other_income3,"
					+ "mfg_exp1,mfg_exp2,mfg_exp3,extra_exp1,extra_exp2,extra_exp3,depreciation1,depreciation2,depreciation3,interest1,interest2,interest3,"
					+ "tax1,tax2,tax3,profit_loss1,profit_loss2,profit_loss3,assets1,assets2,assets3,cash_bank1,cash_bank2,cash_bank3,accounts_receivable1,"
					+ "accounts_receivable2,accounts_receivable3,inventory1,inventory2,inventory3,prepayments1,prepayments2,prepayments3,other1,other2,"
					+ "other3,total_assets1,total_assets2,total_assets3,fix_equip1,fix_equip2,fix_equip3,land_build1,land_build2,land_build3,plant_machine1,"
					+ "plant_machine2,plant_machine3,investment1,investment2,investment3,other_11_1,other_11_2,other_11_3,total_fixed_assets1,"
					+ "total_fixed_assets2,total_fixed_assets3,intangibles1,intangibles2,intangibles3,total_assets_abc1,total_assets_abc2,total_assets_abc3,"
					+ "liabilities1,liabilities2,liabilities3,accounts_payable1,accounts_payable2 ,accounts_payable3,st_sl_2_1,st_sl_2_2,st_sl_2_3,st_usl_3_1,"
					+ "st_usl_3_2,st_usl_3_3,tax_provision1,tax_provision2,tax_provision3,other_provision1,other_provision2,other_provision3,other_liabilities1,"
					+ "other_liabilities2,other_liabilities3,total_crnt_liabilities1,total_crnt_liabilities2,total_crnt_liabilities3,lt_sl_7_1,lt_sl_7_2,"
					+ "lt_sl_7_3,lt_usl_8_1,lt_usl_8_2,lt_usl_8_3,other_lt_liabilities1,other_lt_liabilities2,other_lt_liabilities3,issued_capital1,"
					+ "issued_capital2,issued_capital3,retained_earnings1,retained_earnings2,retained_earnings3,reserves1,reserves2,reserves3,"
					+ "totatl_liabilites_euities1 ,totatl_liabilites_euities2 ,totatl_liabilites_euities3 ,networth1,networth2,networth3,accounts_are) "
					+ "values (?,";
			for (int k = 0; k < 128; k++)
				financialSQL += "?,";
			financialSQL += "?)";
			finpstmt = conn.prepareStatement(financialSQL);
			finpstmt.setInt(index++, customerId);
			finpstmt.setLong(index++, asId);
			finpstmt.setString(index++, enquiryId);
			finpstmt.setString(index++, uKey);
			finpstmt.setInt(index++, userId);
			finpstmt.setString(index++,
					PlatformUtil.getRealDateToSQLDateTime(new Date()));
			finpstmt.setString(index++,
					PlatformUtil.getRealDateToSQLDateTime(new Date()));
			finpstmt.setLong(index++, masterId);
			finpstmt.setLong(index++, dnbMasterId);
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt2") ? ccamObject.getString("fi-txt2") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt21") ? ccamObject.getString("fi-txt2") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt22s") ? ccamObject.getString("fi-txt22s") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt4") ? ccamObject.getString("fi-txt2") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt42") ? ccamObject.getString("fi-txt42") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt43s") ? ccamObject.getString("fi-txt43s") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt6") ? ccamObject.getString("fi-txt6") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt62") ? ccamObject.getString("fi-txt62") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt63s") ? ccamObject.getString("fi-txt63s") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt8") ? ccamObject.getString("fi-txt8") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt82") ? ccamObject.getString("fi-txt82") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt83") ? ccamObject.getString("fi-txt83") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt10") ? ccamObject.getString("fi-txt10") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt101") ? ccamObject.getString("fi-txt101") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt102") ? ccamObject.getString("fi-txt102") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt12") ? ccamObject.getString("fi-txt12") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt122") ? ccamObject.getString("fi-txt122") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt121") ? ccamObject.getString("fi-txt121") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt14") ? ccamObject.getString("fi-txt14") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt141") ? ccamObject.getString("fi-txt141") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt142") ? ccamObject.getString("fi-txt142") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt16") ? ccamObject.getString("fi-txt16") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt161") ? ccamObject.getString("fi-txt161") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt162") ? ccamObject.getString("fi-txt162") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt18") ? ccamObject.getString("fi-txt18") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt181") ? ccamObject.getString("fi-txt181") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt182") ? ccamObject.getString("fi-txt182") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt20") ? ccamObject.getString("fi-txt20") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt201") ? ccamObject.getString("fi-txt201") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt202") ? ccamObject.getString("fi-txt202") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt22") ? ccamObject.getString("fi-txt22") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt221") ? ccamObject.getString("fi-txt21") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt222") ? ccamObject.getString("fi-txt22") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt24") ? ccamObject.getString("fi-txt24") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt241") ? ccamObject.getString("fi-txt241") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt242") ? ccamObject.getString("fi-txt242") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt26") ? ccamObject.getString("fi-txt26") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt262") ? ccamObject.getString("fi-txt262") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt263") ? ccamObject.getString("fi-txt263") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt28") ? ccamObject.getString("fi-txt28") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt281") ? ccamObject.getString("fi-txt281") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt282") ? ccamObject.getString("fi-txt282") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt30") ? ccamObject.getString("fi-txt30") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt301") ? ccamObject.getString("fi-txt301") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt302") ? ccamObject.getString("fi-txt302") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt32") ? ccamObject.getString("fi-txt32") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt321") ? ccamObject.getString("fi-txt321") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt322") ? ccamObject.getString("fi-txt32") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt34") ? ccamObject.getString("fi-txt34") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt341") ? ccamObject.getString("fi-txt341") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt342") ? ccamObject.getString("fi-txt342") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt36") ? ccamObject.getString("fi-txt36") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt361") ? ccamObject.getString("fi-txt361") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt362") ? ccamObject.getString("fi-txt362") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt37") ? ccamObject.getString("fi-txt37") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt371") ? ccamObject.getString("fi-txt371") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt372") ? ccamObject.getString("fi-txt372") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt39") ? ccamObject.getString("fi-txt39") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt391") ? ccamObject.getString("fi-txt391") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt392") ? ccamObject.getString("fi-txt392") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt41") ? ccamObject.getString("fi-txt41") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt411") ? ccamObject.getString("fi-txt411") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt412") ? ccamObject.getString("fi-txt412") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt43") ? ccamObject.getString("fi-txt43") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt431") ? ccamObject.getString("fi-txt431") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt432") ? ccamObject.getString("fi-txt432") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt45") ? ccamObject.getString("fi-txt45") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt451") ? ccamObject.getString("fi-txt451") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt452") ? ccamObject.getString("fi-txt452") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt47") ? ccamObject.getString("fi-txt47") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt471s") ? ccamObject.getString("fi-txt471s") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt472s") ? ccamObject.getString("fi-txt472s") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt49") ? ccamObject.getString("fi-txt49") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt491") ? ccamObject.getString("fi-txt491") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt492") ? ccamObject.getString("fi-txt492") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt51") ? ccamObject.getString("fi-txt51") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt511") ? ccamObject.getString("fi-txt511") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt512") ? ccamObject.getString("fi-txt512") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt53") ? ccamObject.getString("fi-txt53") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt531") ? ccamObject.getString("fi-txt531") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt532") ? ccamObject.getString("fi-txt532") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt55") ? ccamObject.getString("fi-txt55") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt551") ? ccamObject.getString("fi-txt551") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt552") ? ccamObject.getString("fi-txt552") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt57") ? ccamObject.getString("fi-txt57") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt571") ? ccamObject.getString("fi-txt571") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt572") ? ccamObject.getString("fi-txt572") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt59") ? ccamObject.getString("fi-txt59") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt591") ? ccamObject.getString("fi-txt591") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt592") ? ccamObject.getString("fi-txt592") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt61") ? ccamObject.getString("fi-txt61") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt611") ? ccamObject.getString("fi-txt611") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt612") ? ccamObject.getString("fi-txt612") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt63s") ? ccamObject.getString("fi-txt63s") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt631") ? ccamObject.getString("fi-txt631") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt632") ? ccamObject.getString("fi-txt632") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt65") ? ccamObject.getString("fi-txt65") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt651") ? ccamObject.getString("fi-txt651") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt652") ? ccamObject.getString("fi-txt652") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt67") ? ccamObject.getString("fi-txt67") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt671") ? ccamObject.getString("fi-txt671") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt672") ? ccamObject.getString("fi-txt672") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt69") ? ccamObject.getString("fi-txt69") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt691") ? ccamObject.getString("fi-txt691") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt692") ? ccamObject.getString("fi-txt692") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt71") ? ccamObject.getString("fi-txt71") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt711") ? ccamObject.getString("fi-txt711") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt712") ? ccamObject.getString("fi-txt712") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt73") ? ccamObject.getString("fi-txt73") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt731") ? ccamObject.getString("fi-txt731") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt732") ? ccamObject.getString("fi-txt732") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt75") ? ccamObject.getString("fi-txt75") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt751") ? ccamObject.getString("fi-txt751") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt752") ? ccamObject.getString("fi-txt752") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt77") ? ccamObject.getString("fi-txt77") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt771") ? ccamObject.getString("fi-txt771") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt772") ? ccamObject.getString("fi-txt772") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt79") ? ccamObject.getString("fi-txt79") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt791") ? ccamObject.getString("fi-txt791") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-txt792") ? ccamObject.getString("fi-txt792") : ""));
			finpstmt.setString(index++,(ccamObject.containsKey("fi-rad1") ? ccamObject.getString("fi-rad1") : ""));
			rowCount += finpstmt.executeUpdate();
			index = 1;
			String supplierSQL = "insert into dnb_ccam_supplier_data (customer_id,assignment_id,enquiry_id,unique_key,user_id,created_time,modified_time,"
					+ "supplier_name,contact_person,tel_no,total_sale,cmpny_history,cmpny_certificates,cmpny_other_info,quest_filled_name,"
					+ "quest_filled_desig,master_id,dnb_master_id) "
					+ "values (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?)";
			supppstmt = conn.prepareStatement(supplierSQL);
			supppstmt.setInt(index++, customerId);
			supppstmt.setLong(index++, asId);
			supppstmt.setString(index++, enquiryId);
			supppstmt.setString(index++, uKey);
			supppstmt.setInt(index++, userId);
			supppstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			supppstmt.setString(index++,PlatformUtil.getRealDateToSQLDateTime(new Date()));
			supppstmt.setString(index++,(ccamObject.containsKey("sr-txt1")) ? ccamObject.getString("sr-txt1") : "");
			supppstmt.setString(index++,(ccamObject.containsKey("sr-txt2")) ? ccamObject.getString("sr-txt2") : "");
			supppstmt.setString(index++,(ccamObject.containsKey("sr-txt3")) ? ccamObject.getString("sr-txt3") : "");
			supppstmt.setString(index++,(ccamObject.containsKey("sr-txt4")) ? ccamObject.getString("sr-txt4") : "");
			supppstmt.setString(index++,(ccamObject.containsKey("sr-txt5")) ? ccamObject.getString("sr-txt5") : "");
			supppstmt.setString(index++,(ccamObject.containsKey("sr-txt6")) ? ccamObject.getString("sr-txt6") : "");
			supppstmt.setString(index++,(ccamObject.containsKey("sr-txt7")) ? ccamObject.getString("sr-txt7") : "");
			supppstmt.setString(index++,(ccamObject.containsKey("sr-txt8")) ? ccamObject.getString("sr-txt8") : "");
			supppstmt.setString(index++,(ccamObject.containsKey("sr-txt9")) ? ccamObject.getString("sr-txt9") : "");
			supppstmt.setLong(index++, masterId);
			supppstmt.setLong(index++, dnbMasterId);
			rowCount += supppstmt.executeUpdate();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(gpstmt);
			resourceManager.close(bpstmt);
			resourceManager.close(epstmt);
			resourceManager.close(ppstmt);
			resourceManager.close(spstmt);
			resourceManager.close(cpstmt);
			resourceManager.close(sespstmt);
			resourceManager.close(taxpstmt);
			resourceManager.close(bipstmt);
			resourceManager.close(orgpstmt);
			resourceManager.close(finpstmt);
			resourceManager.close(supppstmt);
		}
		if (rowCount == 12)
			return true;
		else
			return false;
	}
	@Override
	public boolean insertDocData(Connection conn, long asId,String enquiryId, String uKey, int userId, JSONArray docArray,int customerId, long masterId, long dnbMasterId) {
		PreparedStatement pstmt = null,pstmt1=null;
		try {
			String sql = "insert into dnb_workflow_media (customer_id,user_id,doc_id,as_id,enquiry_id,unique_key,media_type,"
					+ "media_key,media_path,media_link,action_id,comments,next_follow_date,created_time,modified_time,seq_id,dnb_master_id) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			pstmt = conn.prepareStatement(sql);
			String updateSQL = "update dnb_workflow_media set is_deleted = 0 where app_rej != 2 and unique_key = ? and dnb_master_id = ? and doc_id = ?";
			pstmt1 = conn.prepareStatement(updateSQL);
			if(docArray != null) {
				for (int i = 0; i < docArray.size(); i++){
					JSONObject jobj = docArray.getJSONObject(i);
					int actionId = jobj.getInt("actionId");
					int docId = jobj.getInt("docId");
					String comments = jobj.getString("comments");
					String nextFollowUpDate = jobj.getString("nextFollowUpDate");
					if(actionId != 1){ // Insert only those docs for which status selected is Declined , differ or NA selected on mobile
						int index = 1;
						pstmt.setInt(index++, customerId);
						pstmt.setInt(index++, userId);
						pstmt.setInt(index++, docId);
						pstmt.setLong(index++, asId);
						pstmt.setString(index++, enquiryId);
						pstmt.setString(index++, uKey);
						pstmt.setString(index++, "");
						pstmt.setString(index++, "");
						pstmt.setString(index++, "");
						pstmt.setString(index++, "");
						pstmt.setInt(index++, actionId);
						pstmt.setString(index++, comments);
						pstmt.setString(index++, nextFollowUpDate);
						pstmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
						pstmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
						pstmt.setInt(index++, 0);
						pstmt.setLong(index++, dnbMasterId);
						pstmt.addBatch();
					}else{ // Next time after rejection from first time , used for pdf_creation where is_deleted = 0 
						pstmt1.setString(1, uKey);
						pstmt1.setLong(2, dnbMasterId);
						pstmt1.setInt(3, docId);
						pstmt1.addBatch();
					}
				}
			}
			pstmt.executeBatch();
			pstmt1.executeBatch();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			return false;
		} finally {
			resourceManager.close(pstmt1);
			resourceManager.close(pstmt);
		}
		return true;
	}
	@Override
	public boolean checkIfDataAlreadyExist(Connection conn, int id, String desc, String tableName,int docId){
		PreparedStatement pstmt = null,pstmt1 = null;
		ResultSet rst = null;
		boolean isPresent = false;
		String sql = "",usql = "";
		try{
			if(tableName.equalsIgnoreCase("dnb_doc_checklist")){
				sql = "select doc_id from dnb_doc_checklist where doc_id = "+id+" ";
				pstmt = conn.prepareStatement(sql);
				rst = pstmt.executeQuery();
				if(rst.next()){
					usql = "update dnb_doc_checklist set doc_description = '"+desc+"' where doc_id = "+id+" ";
					pstmt1 = conn.prepareStatement(usql);
					pstmt1.executeUpdate();
					isPresent = true;
				}
			}else if(tableName.equalsIgnoreCase("dnb_casetype_master")){
				sql = "select case_id from dnb_casetype_master where case_id = "+id+" ";
				pstmt = conn.prepareStatement(sql);
				rst = pstmt.executeQuery();
				if(rst.next()){
					usql = "update dnb_casetype_master set case_description = '"+desc+"' where case_id = "+id+" ";
					pstmt1 = conn.prepareStatement(usql);
					pstmt1.executeUpdate();
					isPresent = true;
				}
			}else if(tableName.equalsIgnoreCase("dnb_corporate_master")){
				sql = "select corporate_id from dnb_corporate_master where corporate_id = "+id+" ";
				pstmt = conn.prepareStatement(sql);
				rst = pstmt.executeQuery();
				if(rst.next()){
					usql = "update dnb_corporate_master set corporate_name = '"+desc+"' where corporate_id = "+id+" ";
					pstmt1 = conn.prepareStatement(usql);
					pstmt1.executeUpdate();
					isPresent = true;
				}
			}else if(tableName.equalsIgnoreCase("dnb_city_master")){
				sql = "select city_id from dnb_city_master where city_id = "+id+" ";
				pstmt = conn.prepareStatement(sql);
				rst = pstmt.executeQuery();
				if(rst.next()){
					usql = "update dnb_city_master set city_name = '"+desc+"' where city_id = "+id+" ";
					pstmt1 = conn.prepareStatement(usql);
					pstmt1.executeUpdate();
					isPresent = true;
				}
			}else if(tableName.equalsIgnoreCase("dnb_state_master")){
				sql = "select state_id from dnb_state_master where state_id = "+id+" ";
				pstmt = conn.prepareStatement(sql);
				rst = pstmt.executeQuery();
				if(rst.next()){
					usql = "update dnb_state_master set state_name = '"+desc+"' where state_id = "+id+" ";
					pstmt1 = conn.prepareStatement(usql);
					pstmt1.executeUpdate();
					isPresent = true;
				}
			}else if(tableName.equalsIgnoreCase("dnb_doccase_master")){
				sql = "select doc_id,case_id from dnb_doccase_master where doc_id = "+docId+" and case_id = "+id+"";
				pstmt = conn.prepareStatement(sql);
				rst = pstmt.executeQuery();
				if(rst.next()){
					usql = "update dnb_doccase_master set doc_id = "+docId+",case_id = "+id+" where doc_id = "+docId+" and case_id = "+id+" ";
					pstmt1 = conn.prepareStatement(usql);
					pstmt1.executeUpdate();
					isPresent = true;
				}
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(pstmt1);
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		return isPresent;
	}
	@Override
	public int insertEmailDetails(int customerId, String emailTo, String emailCC, String emailBCC, String subject, String body){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int row=0;
		String sql = "insert into email_details (customer_id,email_to,email_cc,email_bcc,email_subject,email_body,created_time,modified_time) values (?,?,?,?,?,?,?,?) ";
		try{
			conn = resourceManager.getConnection();
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			pstmt.setString(2, emailTo);
			pstmt.setString(3, emailCC);
			pstmt.setString(4, emailBCC);
			pstmt.setString(5, subject);
			pstmt.setString(6, body);
			pstmt.setString(7, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setString(8, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			row = pstmt.executeUpdate();
		}catch (Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(pstmt);
			resourceManager.close(conn);
		}

		return row;
	}

	@Override
	public void checkEnquiryCompleted(Connection conn,int customerId) {
		PreparedStatement pstmt = null,pstmt1 = null;
		ResultSet rst = null;
		String emailTo = "";
		try{
			String sql = "select dnbm.enquiry_id,dnbm.pdf_link,dnbm.dnb_master_id,dnbm.user_id,"
					+ "concat(u.first_name,' ',u.last_name) as uName,"
					+"(select lead_id from team where id = u.team_id) lead_id,dnbm.created_time "
					+"from dnb_master_data dnbm "
					+"left join user as u on (dnbm.user_id = u.id) "
					+"where dnbm.pdf_link != '' and dnbm.is_image_linked = 0 and dnbm.customer_id = "+customerId+" ";
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();
			String updateSQL = "update dnb_master_data set is_image_linked = 1 where dnb_master_id = ? ";
			pstmt1 = conn.prepareStatement(updateSQL);
			while(rst.next()){
				String leadName = ""; 
				String pdfLink = rst.getString("pdf_link");
				long dnbMasterId = rst.getLong("dnb_master_id");
				String createdTime = rst.getString("created_time").substring(0, rst.getString("created_time").lastIndexOf(":")-0);
				String leadId = rst.getString("lead_id");
				UserDto user = userLogic.getUserDetails(Integer.parseInt(leadId));
				if(user != null){
					leadName = user.getFirstName()+" "+user.getLastName();
					emailTo = user.getEmailAddress();
				}
				String uName = rst.getString("uName");
				String enquiryNo = rst.getString("enquiry_id");
				String subject = "Notification: Enquiry Completed - <"+enquiryNo+"> by <"+uName+">";
				String body = emailBody(uName,leadName,enquiryNo,pdfLink,createdTime);

				if(!emailTo.equals("") && !body.equals("")){
					int count = insertEmailDetails(customerId, emailTo, "", "", subject, body);
					if(count > 0){
						pstmt1.setLong(1, dnbMasterId);
						pstmt1.addBatch();
					}
				}
			}
			pstmt1.executeBatch();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
	}
	private String emailBody(String userName, String leadName,String enquiryNo,String pdfLink,String createdTime){
		String emailBody = "";
		emailBody += "<!doctype html><html><head><meta charset='utf-8'></head><body>"
				+"<p>Dear "+leadName+",</p><p>Enquiry "+enquiryNo+" has been submitted by associate "+userName+" as on "+createdTime+".</p>"
				+"<p>Please take necessary action on it.</p>"
				+"<table border='0' width='800px;'><td><b>Please use link to view / refer .</b></td><td><a href='"+pdfLink+"'>"+enquiryNo+".pdf</a></td></tr>"
				+"</table><p>Thanks</p></body></html>";
		return emailBody;
	}
	@Override
	public void dnbReassignmentScheduler(Connection conn,int customerId) {
		PreparedStatement pstmt = null , ipstmt = null , uHpstmt = null , uRpstmt = null , uEpstmt = null;
		ResultSet rst = null;
		String dnbMasterIds = "" , rassignIds = "";
		try{
			Date date = new Date();
			if(conn !=null){
				String insertHistoryPoolSQL = "insert into dnb_assignment_pool_history (dnb_master_id,lead_name,date_of_site_visit,created_time," +
						"modified_time) values (?,?,?,?,?) ";
				ipstmt = conn.prepareStatement(insertHistoryPoolSQL);
				String updateHistoryPoolSQL = "update dnb_assignment_pool_history set is_deleted = 1 , modified_time = ? where is_deleted = 0 and find_in_set(dnb_master_id,?) and is_processed !=2 ";
				uHpstmt = conn.prepareStatement(updateHistoryPoolSQL);
				String updateEnquirySQL = "update dnb_enquiry_details set is_deleted = 1 , modified_time = ? where is_deleted = 0 and find_in_set(dnb_master_id,?) and status = 1 ";
				uEpstmt = conn.prepareStatement(updateEnquirySQL);
				String updateReassignmentPoolSQL = "update dnb_reassignment_pool set is_processed = 1, modified_time = ? where find_in_set(id,?) ";
				uRpstmt = conn.prepareStatement(updateReassignmentPoolSQL);
				String SQL = "select id,lead_login_name,date_of_site_visit,dnb_master_id,is_processed " +
						"from dnb_reassignment_pool where is_processed = 0 and customer_id = "+customerId+"";
				pstmt = conn.prepareStatement(SQL);
				rst = pstmt.executeQuery();
				while(rst.next()){
					long rowId = rst.getLong("id");
					String lead_login_name = rst.getString("lead_login_name");
					String date_of_site_visit = rst.getString("date_of_site_visit");
					if(rassignIds != "") rassignIds += ",";
					rassignIds += rowId;
					if(dnbMasterIds != "") dnbMasterIds +=",";
					dnbMasterIds += rst.getLong("dnb_master_id");
					ipstmt.setLong(1, rst.getLong("dnb_master_id"));
					ipstmt.setString(2, lead_login_name);
					ipstmt.setString(3, date_of_site_visit);
					ipstmt.setString(4, PlatformUtil.getRealDateToSQLDateTime(date));
					ipstmt.setString(5, PlatformUtil.getRealDateToSQLDateTime(date));
					ipstmt.addBatch();
				}
				if(!dnbMasterIds.equals("")  && !rassignIds.equals("")){
					uHpstmt.setString(1, PlatformUtil.getRealDateToSQLDateTime(date));
					uHpstmt.setString(2, dnbMasterIds);
					uEpstmt.setString(1, PlatformUtil.getRealDateToSQLDateTime(date));
					uEpstmt.setString(2, dnbMasterIds);
					uRpstmt.setString(1, PlatformUtil.getRealDateToSQLDateTime(date));
					uRpstmt.setString(2, rassignIds);
					uHpstmt.executeUpdate();
					uEpstmt.executeUpdate();
					uRpstmt.executeUpdate();
					ipstmt.executeBatch();
				}
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(ipstmt);
			resourceManager.close(uHpstmt);
			resourceManager.close(uEpstmt);
			resourceManager.close(uRpstmt);
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
	}
	@Override
	public void getInitiatedEnquiryDetails(Connection conn,Connection dnbConn, int customerId) {
		PreparedStatement dnbpstmt = null, dnbpstmt1 = null, pstmt = null, hstInsertPstmt = null;
		ResultSet dnbrst = null;
		try {
			String sql = "insert into dnb_assignment_pool (customer_id,enquiry_id,case_id,"
					+ "entity_company_name,entity_address,contact_person_name,contact_number,customer_reference_number,customer_email_address,"
					+ "corporate_id,dnb_master_id,city_id,state_id,pincode,web_address,workflow_remarks) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			String dnbUpdateSQL = "update dnb_master_table set is_processed = 1, processed_time = ? where dnb_master_id = ? "; // DNB Table
			dnbpstmt1 = dnbConn.prepareStatement(dnbUpdateSQL);
			String enquiryDetails = "select * from dnb_master_table where is_processed = 0"; // DNB Table
			dnbpstmt = dnbConn.prepareStatement(enquiryDetails);
			dnbrst = dnbpstmt.executeQuery();
			String hstInsertSQL = "insert into dnb_assignment_pool_history (lead_name,date_of_site_visit,dnb_master_id,created_time,modified_time) values (?,?,?,?,?) ";
			hstInsertPstmt = conn.prepareStatement(hstInsertSQL);
			String dnbMasterIds = "";
			while(dnbrst.next()){
				int index = 1;
				long dnbMasterId = dnbrst.getLong("dnb_master_id");
				pstmt.setInt(index++, customerId);
				pstmt.setString(index++, dnbrst.getString("enquiry_id"));
				pstmt.setInt(index++, dnbrst.getInt("case_type"));
				pstmt.setString(index++, dnbrst.getString("entity_company_name"));
				pstmt.setString(index++, dnbrst.getString("entity_address"));
				pstmt.setString(index++, dnbrst.getString("contact_person_name"));
				pstmt.setString(index++, dnbrst.getString("contact_number"));
				pstmt.setString(index++, dnbrst.getString("customer_reference_number"));
				pstmt.setString(index++, dnbrst.getString("customer_email_address"));
				pstmt.setInt(index++, dnbrst.getInt("corporate_id"));
				pstmt.setLong(index++, dnbMasterId);
				pstmt.setInt(index++, dnbrst.getInt("City"));
				pstmt.setInt(index++, dnbrst.getInt("State"));
				pstmt.setString(index++, dnbrst.getString("Pincode"));
				pstmt.setString(index++, dnbrst.getString("WebAddress"));
				pstmt.setString(index++, dnbrst.getString("Workflow_Comments"));
				pstmt.addBatch();
				hstInsertPstmt.setString(1,dnbrst.getString("employee_id"));
				hstInsertPstmt.setString(2,dnbrst.getString("date_of_site_visit"));
				hstInsertPstmt.setLong(3,dnbMasterId);
				hstInsertPstmt.setString(4,PlatformUtil.getRealDateToSQLDateTime(new Date()));
				hstInsertPstmt.setString(5,PlatformUtil.getRealDateToSQLDateTime(new Date()));
				hstInsertPstmt.addBatch();
				dnbpstmt1.setString(1,PlatformUtil.getRealDateToSQLDateTime(new Date()));
				dnbpstmt1.setLong(2, dnbMasterId);
				dnbpstmt1.addBatch();
				if (dnbMasterIds != "") dnbMasterIds += ",";
				dnbMasterIds += dnbMasterId;
			}
			if(dnbMasterIds != ""){
				pstmt.executeBatch();
				hstInsertPstmt.executeBatch();
				dnbpstmt1.executeBatch();
			}else{
				LOG.info("Nothing to update as DNB Master ID not found ");
			}
		}catch(Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(dnbrst);
			resourceManager.close(dnbpstmt);
			resourceManager.close(pstmt);
			resourceManager.close(hstInsertPstmt);
			resourceManager.close(dnbpstmt1);
		}
	}
	
	/*@Override
	public void createPDF(Connection conn,int customerId) {
		PreparedStatement pstmt = null, pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null;
		ResultSet rst = null,rst1 = null;
		String baseServerURL = configManager.getBaseServerURL();
		try {
			conn = resourceManager.getConnection();
			String sql = "select dmd.id,dmd.pdf_link,dmd.enquiry_id,dmd.unique_key,dmd.img_count,dmd.customer_id,dmd.dnb_master_id,"
					+ "dmd.assignment_id from dnb_master_data as dmd where dmd.pdf_link = '' and dmd.customer_id = "+customerId+" and dmd.is_data_distributed = 1 ";
			String updateSQL = "update dnb_master_data set pdf_link = ? where id = ? and customer_id = ? ";
			String updateASPSQL = "update dnb_assignment_pool set pdf_link = ?, pdf_cdate= ?, web_link = ? where dnb_master_id = ? ";
			String updateASPHSQL = "update dnb_assignment_pool_history set pdf_link = ? , pdf_cdate = ? where dnb_master_id = ? order by id desc limit 1";
			pstmt = conn.prepareStatement(sql);
			pstmt1 = conn.prepareStatement(updateSQL);
			pstmt2 = conn.prepareStatement(updateASPSQL);
			pstmt3 = conn.prepareStatement(updateASPHSQL);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				int mId = rst.getInt("id");
				String enquiryId = rst.getString("enquiry_id");
				int mainImgCount = rst.getInt("img_count");
				long dnbMasterId = rst.getLong("dnb_master_id");
				int cId = rst.getInt("customer_id");
				long asId = rst.getLong("assignment_id");
				LOG.info("mainImgCount "+mainImgCount+" for dnbMasterId "+dnbMasterId);
				if (mainImgCount == 0){
					String body = doLinktoHTMLParse(baseServerURL+URLConstants.ENQUIRY_REPORT_HTML+mId);
					body = body.replace("&", "&amp;");
					String pdfStorePath = configManager.getMediaPdfFilePath();
					String dateDirectory = mediaDirectoryDateFormat.format(new Date());
					String completePDFFileLocation = pdfStorePath+seperator+ dateDirectory;
					String pdfName = enquiryId+"_" + asId + ".pdf";
					File file = new File(completePDFFileLocation);
					if (!file.exists()) file.mkdirs();
					Document document = new Document();
					PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(file+seperator+pdfName)));
					document.open();
					XMLWorkerHelper.getInstance().parseXHtml(writer, document,new StringReader(body));
					document.close();

					pstmt1.setString(1, baseServerURL+URLConstants.ENQUIRY_REPORT_PDF+dateDirectory+seperator+pdfName);
					pstmt1.setLong(2, mId);
					pstmt1.setInt(3, cId);
					pstmt1.executeUpdate();

					pstmt2.setString(1, pdfName);
					pstmt2.setString(2, dateDirectory);
					pstmt2.setString(3, baseServerURL+URLConstants.ENQUIRY_REPORT_HTML+mId);
					pstmt2.setLong(4, dnbMasterId);
					pstmt2.executeUpdate();

					pstmt3.setString(1, pdfName);
					pstmt3.setString(2, dateDirectory);
					pstmt3.setLong(3, dnbMasterId);
					pstmt3.executeUpdate();
				}else if(mainImgCount > 0){
					boolean check = checkIfAllImgReceived(conn,dnbMasterId,customerId);
					if(check){
						String body = doLinktoHTMLParse(baseServerURL+URLConstants.ENQUIRY_REPORT_HTML+mId);
						body = body.replace("&", "&amp;");
						String pdfStorePath = configManager.getMediaPdfFilePath();
						String dateDirectory = mediaDirectoryDateFormat.format(new Date());
						String completePDFFileLocation = pdfStorePath+seperator+ dateDirectory;
						String pdfName = enquiryId+"_" + asId + ".pdf";
						File file = new File(completePDFFileLocation);
						if (!file.exists()) file.mkdirs();
						Document document = new Document();
						PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(file+seperator+pdfName)));
						document.open();
						XMLWorkerHelper.getInstance().parseXHtml(writer, document,new StringReader(body));
						document.close();
						pstmt1.setString(1, baseServerURL+URLConstants.ENQUIRY_REPORT_PDF+dateDirectory+seperator+pdfName);
						pstmt1.setLong(2, mId);
						pstmt1.setInt(3, cId);
						pstmt1.executeUpdate();

						pstmt2.setString(1, pdfName);
						pstmt2.setString(2, dateDirectory);
						pstmt2.setString(3, baseServerURL+URLConstants.ENQUIRY_REPORT_HTML+mId);
						pstmt2.setLong(4, dnbMasterId);
						pstmt2.executeUpdate();

						pstmt3.setString(1, pdfName);
						pstmt3.setString(2, dateDirectory);
						pstmt3.setLong(3, dnbMasterId);
						pstmt3.executeUpdate();
					}else if(!check && mainImgCount > 0){ // For rejection case , if mobile has not send any images next time
						String SQL = "select count(*) as imgCount from dnb_workflow_media where action_id = 1 and is_deleted = 0 and media_link !='' and dnb_master_id = "+dnbMasterId+"";
						pstmt4 = conn.prepareStatement(SQL);
						rst1 = pstmt4.executeQuery();
						if(rst1.next()){
							int mediaImageCount = rst1.getInt("imgCount");
							if(mediaImageCount == mainImgCount){
								String body = doLinktoHTMLParse(baseServerURL+URLConstants.ENQUIRY_REPORT_HTML+mId);
								body = body.replace("&", "&amp;");
								String pdfStorePath = configManager.getMediaPdfFilePath();
								String dateDirectory = mediaDirectoryDateFormat.format(new Date());
								String completePDFFileLocation = pdfStorePath+seperator+ dateDirectory;
								String pdfName = enquiryId+"_" + asId + ".pdf";
								File file = new File(completePDFFileLocation);
								if (!file.exists()) file.mkdirs();
								Document document = new Document();
								PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(file+seperator+pdfName)));
								document.open();
							    XMLWorkerHelper.getInstance().parseXHtml(writer, document,new StringReader(body));
								document.close();
								pstmt1.setString(1, baseServerURL+URLConstants.ENQUIRY_REPORT_PDF+dateDirectory+seperator+pdfName);
								pstmt1.setLong(2, mId);
								pstmt1.setInt(3, cId);
								pstmt1.executeUpdate();

								pstmt2.setString(1, pdfName);
								pstmt2.setString(2, dateDirectory);
								pstmt2.setString(3, baseServerURL+URLConstants.ENQUIRY_REPORT_HTML+mId);
								pstmt2.setLong(4, dnbMasterId);
								pstmt2.executeUpdate();

								pstmt3.setString(1, pdfName);
								pstmt3.setString(2, dateDirectory);
								pstmt3.setLong(3, dnbMasterId);
								pstmt3.executeUpdate();
							}else{
								LOG.info("Count Mismatch "+dnbMasterId);
							}
						}
					}else{
						LOG.info("All the document not received against dnbMasterId "+dnbMasterId);
					}
				}
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally{
			resourceManager.close(rst1);
			resourceManager.close(rst);
			resourceManager.close(pstmt4);
			resourceManager.close(pstmt3);
			resourceManager.close(pstmt2);
			resourceManager.close(pstmt1);
			resourceManager.close(pstmt);
			resourceManager.close(conn);
		}
	}*/
	
	}