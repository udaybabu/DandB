package in.otpl.dnb.user;

import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.SessionConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TeamDaoImpl implements TeamDao {

	private static final Logger LOG = Logger.getLogger(TeamDaoImpl.class);

	@Autowired
	private ResourceManager resourceManager;

	private static final String SQLSELECT = "SELECT id, customer_id, name, lead_id, creation_time, modification_time , status FROM "
			+ getTableName() + "";

	private static final String SQLINSERT = "INSERT INTO " + getTableName()
			+ " ( id, customer_id, name, lead_id, creation_time, modification_time) VALUES ( ?, ?, ?, ?, ?, ?)";

	private static final String SQLUPDATE = "UPDATE " + getTableName()
			+ " SET name = ?, lead_id = ?, modification_time = ? " + "WHERE id = ?";

	private static final String SQLCOUNT = "SELECT count(*) FROM " + getTableName();
	
	private static final String SQLUPDATESTATUS = "UPDATE " + getTableName() + " SET status = ?, modification_time = ? WHERE id = ?";


	private static String getTableName() {
		return "team";
	}

	@Override
	public long insert(TeamDto dto, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(SQLINSERT, Statement.RETURN_GENERATED_KEYS);
			int index = 1;
			stmt.setLong(index++, dto.getId());
			stmt.setInt(index++, dto.getCustomerId());
			stmt.setString(index++, dto.getName().trim());
			stmt.setLong(index++, dto.getLeadId());
			stmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			stmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setId(rs.getLong(1));
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return dto.getId();
	}

	@Override
	public void update(TeamDto dto, Connection conn) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(SQLUPDATE);
			int index = 1;
			stmt.setString(index++, dto.getName());
			stmt.setLong(index++, dto.getLeadId());
			stmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			stmt.setLong(index++, dto.getId());
			stmt.executeUpdate();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(stmt);
		}
	}

	@Override
	public TeamDto findByPrimaryKey(long id, Connection conn) {
		TeamDto[] ret = findByDynamicSelect(SQLSELECT + " WHERE id = ? ORDER BY name ASC",
				new Object[] { new Long(id) }, conn);
		return ret.length == 0 ? null : ret[0];
	}

	@Override
	public TeamDto[] findWhereIdEquals(long id, int customerId, Connection conn) {
		return findByDynamicSelect(SQLSELECT + " WHERE id = ? ORDER BY name ASC", new Object[] { new Long(id) }, conn);
	}

	@Override
	public TeamDto[] findWhereCustomerIdEquals(int customerId, Connection conn) {
		return findByDynamicSelect(SQLSELECT + " WHERE customer_id = ? ORDER BY name ASC",
				new Object[] { new Integer(customerId) }, conn);
	}

	@Override
	public TeamDto[] findWhereCustomerId(int customerId, Connection conn) {
		return findByDynamicSelect(SQLSELECT + " WHERE customer_id = ? and status = 1 ORDER BY name ASC",
				new Object[] { new Integer(customerId) }, conn);
	}

	@Override
	public TeamDto[] findWhereCustomerIdTeamIdsIn(int customerId, String teamIds, Connection conn) {
		return findByDynamicSelect(SQLSELECT + " WHERE customer_id = ? and id in (" + teamIds + ") ORDER BY name ASC",
				new Object[] { new Integer(customerId) }, conn);
	}

	@Override
	public TeamDto[] findWhereNameEquals(String name, int customerId, Connection conn) {
		return findByDynamicSelect(SQLSELECT + " WHERE name = ? and customer_id = ? ORDER BY name ASC",
				new Object[] { name, customerId }, conn);
	}

	@Override
	public TeamDto[] findWhereLeadIdEquals(long leadId, Connection conn) {
		return findByDynamicSelect(SQLSELECT + " where lead_id = ? ORDER BY name ASC",
				new Object[] { new Long(leadId) }, conn);
	}

	@Override
	public TeamDto[] findByDynamicWhere(String sql, Object[] sqlParams, Connection conn) {
		final String query = SQLSELECT + " WHERE " + sql;
		return findByDynamicSelect(query, sqlParams, conn);
	}

	@Override
	public String getTeamIdByLeadId(int customerId, long leadId, Connection conn) {
		String teamIds = "";
		try {
			TeamDto[] teams = findByDynamicSelect(
					SQLSELECT + " WHERE customer_id = ? and lead_id = ? ORDER BY name ASC ",
					new Object[] { customerId, new Long(leadId) }, conn);
			if (teams != null) {
				for (TeamDto teamDto : teams) {
					if (teamIds != "") {
						teamIds += ",";
					}
					teamIds += teamDto.getId();
				}
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return teamIds;
	}

	@Override
	public long getLeadIdByTeamId(int customerId, String teamId, Connection conn) {
		long leadId = 0;
		try {
			TeamDto[] teams = findByDynamicSelect(
					SQLSELECT + " WHERE customer_id = ? and id in (" + teamId + ") ORDER BY name ASC",
					new Object[] { customerId }, conn);
			if (teams != null)
				leadId = teams[0].getLeadId();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return leadId;
	}

	private TeamDto[] fetchMultiResults(ResultSet rs) throws SQLException {
		List<TeamDto> resultList = new ArrayList<TeamDto>();
		while (rs.next()) {
			TeamDto dto = new TeamDto();
			populateDto(dto, rs);
			resultList.add(dto);
		}

		TeamDto[] ret = new TeamDto[resultList.size()];
		resultList.toArray(ret);
		return ret;
	}

	private void populateDto(TeamDto dto, ResultSet rs) throws SQLException {
		dto.setId(rs.getLong("id"));
		dto.setCustomerId(rs.getInt("customer_id"));
		dto.setName(rs.getString("name"));
		dto.setLeadId(rs.getInt("lead_id"));
		dto.setCreationTime(rs.getString("creation_time"));
		dto.setModificationTime(rs.getString("modification_time"));
	}

	@Override
	public TeamDto[] findByDynamicSelect(String sql, Object[] sqlParams, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		TeamDto[] teams = null;
		try {
			stmt = conn.prepareStatement(sql);
			for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
				stmt.setObject(i + 1, sqlParams[i]);
			}
			rs = stmt.executeQuery();
			teams = fetchMultiResults(rs);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		if (teams == null)
			teams = new TeamDto[0];
		return teams;
	}

	@Override
	public int getCount(String sql, Object[] sqlParams, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			final String query = SQLCOUNT + " WHERE " + sql;
			stmt = conn.prepareStatement(query);
			for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
				stmt.setObject(i + 1, sqlParams[i]);
			}
			rs = stmt.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return count;
	}

	@Override
	public Map<String, Object> teamList(int customerId, TeamForm form, Connection conn) {
		Map<String, Object> map = new HashMap<String, Object>();
		PreparedStatement stmt = null, stmt2 = null;
		ResultSet rs = null, rs2 = null;
		int count = 0;
		try {
			String select = "SELECT SQL_CALC_FOUND_ROWS t.id, t.name, t.lead_id, t.lead_name , t.members, t.customer_id, t.status    "
					+ "from (SELECT t.id, t.name, t.lead_id, concat(l.first_name, ' ', l.last_name) lead_name, "
					+ "group_concat(concat(u.first_name,' ',u.last_name) separator ', ') members, t.customer_id, t.status  "
					+ "FROM team t " + "INNER JOIN user l ON t.lead_id = l.id and t.customer_id = l.customer_id "
					+ "LEFT JOIN user u ON t.id = u.team_id and t.customer_id = u.customer_id "
					+ "WHERE t.customer_id = ?  " + "GROUP BY t.id) t ";
			String where = "";
			String orderBy = " ORDER BY t.name , members ASC ";
			List<Object> list = new ArrayList<Object>();
			list.add(customerId);

			if (PlatformUtil.isNotEmpty(form.getName())) {
				if (where != "")
					where += " and ";
				where += " t.name like ? ";
				list.add("%" + form.getName().trim() + "%");
			}

			if (PlatformUtil.isNotEmpty(form.getLeadName())) {
				String leadName = form.getLeadName().trim();
				if (where != "")
					where += " and ";
				where += " t.lead_name like ? ";
				list.add("%" + leadName + "%");
			}
		
			if (form.getStatus() != SessionConstants.STATUS_ALL) {
				if(where != "")
					where += " and ";
				where += " t.status = ? ";
				list.add(form.getStatus());
			}
		
			if (PlatformUtil.isNotEmpty(form.getMemberName())) {
				String memberName = form.getMemberName().trim();
				if (where != "")
					where += " and ";
				where += " t.members like ? ";
				list.add("%" + memberName + "%");
			}

			if (where != "")
				where = " where " + where;

			String sql = "";
			sql = select + "\n" + where + "\n" + orderBy;
			if (form.getRowcount() > 0) {
				sql += " LIMIT ?, ? ";
				list.add(form.getOffset());
				list.add(form.getRowcount());
			}

			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				stmt.setObject(i + 1, list.get(i));
			}
			rs = stmt.executeQuery();
			List<TeamDto> teamlist = new ArrayList<TeamDto>();
			while (rs.next()) {
				TeamDto dto = new TeamDto();
				dto.setId(rs.getInt("id"));
				dto.setName(rs.getString("name"));
				dto.setLeadId(rs.getInt("lead_id"));
				dto.setLeadName(rs.getString("lead_name"));
				dto.setMembers(rs.getString("members"));
				dto.setCustomerId(rs.getInt("customer_id"));
		    	dto.setStatus(rs.getInt("status"));
				teamlist.add(dto);
						
			}
			stmt2 = conn.prepareStatement("SELECT FOUND_ROWS()");
			rs2 = stmt2.executeQuery();
			if (rs2.next())
				count = rs2.getInt(1);

			map.put("totalRows", count);
			map.put("data", teamlist);

		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs2);
			resourceManager.close(stmt2);
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return map;
	}

	@Override
	public int changeStatus(int id, int status, Connection conn) {
		PreparedStatement pstmt = null;
		int rows = 0;
		try {
			status=(status == 1)?0:1;
			pstmt = conn.prepareStatement( SQLUPDATESTATUS );
			pstmt.setInt(1, status);
			pstmt.setString(2, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setInt(3, id);
			rows = pstmt.executeUpdate();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));

		} finally {
			resourceManager.close(pstmt);
		}
		return rows;
	}
}