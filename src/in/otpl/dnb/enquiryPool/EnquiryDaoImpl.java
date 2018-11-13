package in.otpl.dnb.enquiryPool;

import in.otpl.dnb.util.ConfigManager;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.URLConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EnquiryDaoImpl implements EnquiryDao {

	private static final Logger LOG = Logger.getLogger(EnquiryDaoImpl.class);

	@Autowired
	private ResourceManager resourceManager;

	@Override
	public Map<String, Object> getAssignmentPool(Connection conn, EnquiryForm enquiryForm, int customerId, String loginName, String userType, long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		ResultSet rst = null, rst1 = null;
		PreparedStatement pstmt = null, pstmt1 = null;
		List<EnquiryDto> enquirylist = new ArrayList<EnquiryDto>();
		String where = "", limit = "";
		int count = 0;
		try {
			int caseStatus = enquiryForm.getCaseStatus();
			long selUser = enquiryForm.getSelUser();
			where = "where dap.customer_id = ? and dap.created_time between ? and ? ";
			if (caseStatus > 0) {
				if (caseStatus == 1) {
					where += " and daph.is_processed = 1 ";
				} else if (caseStatus == 2) {
					where += " and daph.is_processed = 0 ";
				} else if (caseStatus == 3) {
					where += " and daph.is_processed = 2 ";
				} else if (caseStatus == 4) {
					where += " and dap.case_status = 0 and daph.is_processed = 2 ";
				} else if (caseStatus == 5) {
					where += " and dap.case_status = 2 ";
				} else if (caseStatus == 6) {
					where += " and dap.case_status = 1 ";
				}
			}

			if (userType.equalsIgnoreCase("Lead"))
				where += " and daph.lead_name = ? ";
			if (userType.equalsIgnoreCase("User"))
				where += " and daph.assigned_to = ? ";
			if (selUser > 0)
				where += " and daph.assigned_to = ? ";
			if (enquiryForm.getDnbMasterId() > 0)
				where += " and daph.dnb_master_id = ? ";
			if (PlatformUtil.isNotEmpty(enquiryForm.getEnquiryNo()))
				where += " and dap.enquiry_id = ? ";
			String select = "select SQL_CALC_FOUND_ROWS d.*, ifnull(dmd.id,0) dnb_master_data_id, ifnull(dmd.is_data_distributed, 0) is_data, "
					+ "dmd.created_time date_of_submission, ded.is_deleted, "
					+ "concat(u.first_name,' ',u.last_name) fos_name, concat(ul.first_name,' ',ul.last_name) lead_user_name "
					+ "from (select max(daph.id) id, dap.dnb_master_id, dap.enquiry_id, dap.case_id, dap.case_status, dcm.case_description, "
					+ "daph.date_of_site_visit, daph.created_time enquiry_received_time, dap.created_time, daph.lead_name, daph.assigned_to, "
					+ "daph.assignment_id, daph.is_processed "
					+ "from dnb_assignment_pool dap "
					+ "join dnb_casetype_master dcm on dcm.case_id = dap.case_id "
					+ "join dnb_assignment_pool_history daph ON daph.dnb_master_id = dap.dnb_master_id "
					+ where
					+ "group by dap.dnb_master_id, dap.enquiry_id, dap.case_id, dap.case_status, dcm.case_description, daph.date_of_site_visit, "
					+ "daph.created_time, dap.created_time, daph.lead_name, daph.assigned_to, daph.assignment_id, daph.is_processed "
					+ "order by dap.created_time asc) d "
					+ "left join user ul on d.lead_name = ul.login_name "
					+ "left join user u on d.assigned_to = u.id "
					+ "left join dnb_enquiry_details ded on d.assignment_id = ded.id "
					+ "left join dnb_master_data dmd on ded.id = dmd.assignment_id "
					+ "order by d.dnb_master_id, d.id asc";

			if (enquiryForm.getRowcount() > 0)
				limit += " limit ?, ? ";

			String sql = select + limit;

			pstmt = conn.prepareStatement(sql);
			int index = 1;
			pstmt.setInt(index++, customerId);
			pstmt.setString(index++, enquiryForm.getDateFrom());
			pstmt.setString(index++, enquiryForm.getDateTo());
			if (userType.equalsIgnoreCase("Lead"))
				pstmt.setString(index++, loginName);
			if (userType.equalsIgnoreCase("User"))
				pstmt.setLong(index++, userId);
			if (selUser > 0)
				pstmt.setLong(index++, selUser);
			if (enquiryForm.getDnbMasterId() > 0)
				pstmt.setLong(index++, enquiryForm.getDnbMasterId());
			if (PlatformUtil.isNotEmpty(enquiryForm.getEnquiryNo())) {
				pstmt.setString(index++, enquiryForm.getEnquiryNo().trim());
			}
			if (enquiryForm.getRowcount() > 0) {
				pstmt.setInt(index++, enquiryForm.getOffset());
				pstmt.setInt(index++, enquiryForm.getRowcount());
			}
			rst = pstmt.executeQuery();
			while (rst.next()) {
				EnquiryDto dto = new EnquiryDto();
				dto.setEnquiryNo(rst.getString("enquiry_id"));
				dto.setCaseId(rst.getInt("case_id"));
				dto.setCaseType(rst.getString("case_description"));
				dto.setCreatedDate(PlatformUtil.getTimeSimpleDateTime(rst.getString("created_time")));
				dto.setEqnuiryReceivedTime(PlatformUtil.getTimeSimpleDateTime(rst.getString("enquiry_received_time")));
				dto.setDateOfSiteVisit(PlatformUtil.getTimeSimpleDateTime(rst.getString("date_of_site_visit")));
				dto.setDateOfSubmission(PlatformUtil.getTimeSimpleDateTime(rst.getString("date_of_submission")));
				dto.setCaseStatus(rst.getInt("case_status"));
				dto.setUserName(rst.getString("fos_name"));
				dto.setLeadEmpName(rst.getString("lead_user_name"));
				dto.setUserId(rst.getLong("assigned_to"));
				dto.setUserType(userType);
				dto.setDnbMasterId(rst.getLong("dnb_master_id"));
				dto.setAsphId(rst.getLong("id"));
				dto.setIsProcessed(rst.getInt("is_processed"));
				dto.setIsDeleted(rst.getInt("is_deleted"));
				if(rst.getInt("is_deleted") != 0)
					dto.setQfStatus("Deleted / Re-assigned");
				else{
					if (rst.getInt("is_processed") == 0)
						dto.setQfStatus("Not Yet Assigned");
					else if (rst.getInt("is_processed") == 1)
						dto.setQfStatus("Assigned");
					else if (rst.getInt("is_processed") == 2)
						dto.setQfStatus("Completed");
				}
				if (dto.getIsProcessed() == 2 && dto.getCaseStatus() == 0) {
					dto.setCaseStatusVal("In Progress");
				} else if (dto.getCaseStatus() == 0) {
				} else if (dto.getCaseStatus() == 1) {
					dto.setCaseStatusVal("Accepted");
				} else if (dto.getCaseStatus() == 2) {
					dto.setCaseStatusVal("Rejected");
				}
				String startTime = rst.getString("date_of_site_visit");
				String dateFrom = PlatformUtil.getTimeSimpleDateTime(startTime);
				dto.setDateFrom(dateFrom);
				dto.setDnbMasterDataId(rst.getLong("dnb_master_data_id"));
				dto.setIsDataDistributed(rst.getInt("is_data"));
				enquirylist.add(dto);
			}
			pstmt1 = conn.prepareStatement("SELECT FOUND_ROWS()");
			rst1 = pstmt1.executeQuery();
			if (rst1.next()) {
				count = rst1.getInt(1);
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rst1);
			resourceManager.close(pstmt1);
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		map.put("totalRows", count);
		map.put("data", enquirylist);
		return map;
	}
	@Override
	public long saveUpdateEnquiry(long userId, String enquiryName, long dnbMasterId, long asphId, int isReassigned, long enquiryDetailsId, Connection conn, int customerId, int loggedInUserId, String enquiryStartTime) {
		ResultSet rst = null;
		PreparedStatement pstmt = null, pstmt1 = null;
		long rowId = 1;
		int index = 1;
		String sql = "";
		try {
			if (isReassigned == 1) {
				sql = "update dnb_enquiry_details set is_deleted = 1 , modified_time = ? where dnb_master_id = ? ";
				pstmt1 = conn.prepareStatement(sql);
				pstmt1.setString(1, PlatformUtil.getRealDateToSQLDateTime(new Date()));
				pstmt1.setLong(2, dnbMasterId);
				int updatedRow = pstmt1.executeUpdate();
				if (updatedRow > 0)
					LOG.info("Enquiry Details is_deleted flag updated for id "+ enquiryDetailsId);
			}
			sql = "insert into dnb_enquiry_details (customer_id, created_time, modified_time, asph_id, user_id, start_time, status, "
					+ "assigned_by, enquiry_name, dnb_master_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(index++, customerId);
			pstmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setString(index++, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			pstmt.setLong(index++, asphId);
			pstmt.setLong(index++, userId);
			pstmt.setString(index++, PlatformUtil.getDateTimeInYYYYMMDDHHMM(enquiryStartTime));
			pstmt.setInt(index++, 1);
			pstmt.setInt(index++, loggedInUserId);
			pstmt.setString(index++, enquiryName);
			pstmt.setLong(index++, dnbMasterId);
			pstmt.executeUpdate();
			rst = pstmt.getGeneratedKeys();
			if (rst != null && rst.next()) {
				rowId = rst.getLong(1);
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rst);
			resourceManager.close(pstmt);
			resourceManager.close(pstmt1);
		}
		return rowId;
	}
	@Override
	public JSONObject getEnquiryDetails(Connection conn, long dnbMasterId, int customerId) {
		JSONObject enquiryDetails = new JSONObject();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		try {
			String sql = "select dap.case_id, dap.entity_company_name, dap.entity_address, dap.contact_person_name, dap.contact_number, "
					+ "dap.customer_reference_number, dap.customer_email_address, dap.corporate_id, dap.dnb_master_id, dap.pincode, "
					+ "IFNULL(dap.web_address,'') web_address, IFNULL(dap.workflow_remarks,'') workflow_remarks, "
					+ "IFNULL(dcm.corporate_name,'') corporate_name, IFNULL(dcm2.city_name,'') city_name, IFNULL(dsm.state_name,'') state_name "
					+ "from dnb_assignment_pool dap "
					+ "left join dnb_corporate_master dcm on dap.corporate_id = dcm.corporate_id "
					+ "left join dnb_city_master dcm2 on dap.city_id = dcm2.city_id "
					+ "left join dnb_state_master dsm on dap.state_id = dsm.state_id "
					+ "where dap.customer_id = ? ";
			if (dnbMasterId > 0)
				sql += "and dap.dnb_master_id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			if (dnbMasterId > 0)
				pstmt.setLong(2, dnbMasterId);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				enquiryDetails.put("case_id", rst.getString("case_id"));
				enquiryDetails.put("entityCompName", rst.getString("entity_company_name"));
				enquiryDetails.put("entityAddress", rst.getString("entity_address"));
				enquiryDetails.put("contactPersonName", rst.getString("contact_person_name"));
				enquiryDetails.put("contactNumber", rst.getString("contact_number"));
				enquiryDetails.put("custCRNNumber", rst.getString("customer_reference_number"));
				enquiryDetails.put("custEmailAddress", rst.getString("customer_email_address"));
				enquiryDetails.put("corporateName", rst.getString("corporate_name"));
				enquiryDetails.put("dnbMasterId", rst.getString("dnb_master_id"));
				enquiryDetails.put("web_address", rst.getString("web_address"));
				enquiryDetails.put("workflow_remarks", rst.getString("workflow_remarks"));
				enquiryDetails.put("pincode", rst.getString("pincode"));
				enquiryDetails.put("city_name", rst.getString("city_name"));
				enquiryDetails.put("state_name", rst.getString("state_name"));
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		return enquiryDetails;
	}

	@Override
	public JSONArray getCaseHistory(Connection conn, long dnbMasterId) {
		JSONArray caseHistoryArray = new JSONArray();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		try {
			String sql = "select daph.id dnb_history_id, daph.date_of_site_visit, daph.dnb_master_id, daph.assignment_id, "
					+ "ifnull(dmd.id,0) dnb_master_data_id, "
					+ "ifnull(concat(u.first_name,' ',u.last_name),'') fos_name, "
					+ "ifnull(concat(ul.first_name,' ',ul.last_name),'') lead_user_name "
					+ "from dnb_assignment_pool_history daph "
					+ "left join user ul on daph.lead_name = ul.login_name "
					+ "left join user u on daph.assigned_to = u.id "
					+ "left join dnb_enquiry_details ded on daph.assignment_id = ded.id "
					+ "left join dnb_master_data dmd on ded.id = dmd.assignment_id ";
			if (dnbMasterId > 0)
				sql += "where daph.dnb_master_id = ? ";
			sql += "order by daph.id asc ";
			pstmt = conn.prepareStatement(sql);
			if (dnbMasterId > 0)
				pstmt.setLong(1, dnbMasterId);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				JSONObject tempObject = new JSONObject();
				long id = rst.getLong("dnb_history_id");
				String assignedTo = rst.getString("fos_name");
				String assignedBy = rst.getString("lead_user_name");
				tempObject.put("id", id);
				tempObject.put("dos", PlatformUtil.getTimeSimpleDateTime(rst.getString("date_of_site_visit")));
				tempObject.put("dnbMasterDataId", rst.getLong("dnb_master_data_id"));
				tempObject.put("assignedTo", assignedTo);
				tempObject.put("assignedBy", assignedBy);
				caseHistoryArray.add(tempObject);
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		return caseHistoryArray;
	}

	@Override
	public JSONArray getCaseAssignmentHistory(Connection conn, long dnbMasterId, int customerId) {
		JSONArray caseAssignmentHistoryArray = new JSONArray();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		try {
			String sql = "select ded.created_time, ded.is_deleted, ded.status, ifnull(concat(u.first_name,' ',u.last_name),'') fos_name, "
					+ "ifnull(concat(ul.first_name,' ',ul.last_name),'') lead_user_name "
					+ "from dnb_enquiry_details ded "
					+ "left join user ul on ded.assigned_by = ul.login_name "
					+ "left join user u on ded.user_id = u.id ";

			if (dnbMasterId > 0)
				sql += "where dnb_master_id = " + dnbMasterId + "";

			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				JSONObject tempObject = new JSONObject();
				tempObject.put("assigned_time", PlatformUtil.getTimeSimpleDateTime(rst.getString("created_time")));
				tempObject.put("fos_name", rst.getString("fos_name"));
				tempObject.put("lead_name", rst.getString("lead_user_name"));

				if (rst.getInt("status") == 1 && rst.getInt("is_deleted") == 1) {
					tempObject.put("status", "");
				} else if (rst.getInt("status") == 2) {
					tempObject.put("status", "Completed");
				} else {
					tempObject.put("status", "Assigned and In Progress");
				}

				if (rst.getInt("is_deleted") == 1)
					tempObject.put("is_reassigned", "Yes");
				else
					tempObject.put("is_reassigned", "No");
				caseAssignmentHistoryArray.add(tempObject);
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		return caseAssignmentHistoryArray;
	}

	@Override
	public JSONArray getDocumentList(Connection conn, int customerId, int caseId) {
		JSONArray documentList = new JSONArray();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		try {
			String sql = "select doc_id,doc_description from dnb_doc_checklist "
					+ "where doc_id in (select doc_id from dnb_doccase_master where case_id = "
					+ caseId + ") order by doc_id asc";
			;
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				JSONObject tempObject = new JSONObject();
				tempObject.put("doc_id", rst.getString("doc_id"));
				tempObject.put("doc_desc", rst.getString("doc_description"));
				documentList.add(tempObject);
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		return documentList;
	}
	@Override
	public void resetOldDNBData(Connection conn, long dnbMasterId,	int resetType, int customerId) {
		PreparedStatement pstmt = null, pstmt1 = null, pstmt2 = null, pstmt3 = null, pstmt4 = null;
		ResultSet rst = null, rst1 = null, rst2 = null;
		String ids = "";
		try {
			if (dnbMasterId > 0) {
				if (resetType == 1) {
					String hSql = "select group_concat(id) hids from dnb_assignment_pool_history where dnb_master_id = "+ dnbMasterId + " and is_processed != 2 and "
							+ "id < (select max(id) from dnb_assignment_pool_history where dnb_master_id = "+ dnbMasterId + ")";
					pstmt1 = conn.prepareStatement(hSql);
					rst1 = pstmt1.executeQuery();
					if (rst1.next()) {
						ids = rst1.getString("hids");
						if (ids != null && !ids.equals("")) {
							String uSQL = "update dnb_assignment_pool_history set is_deleted = 1 where dnb_master_id = "+ dnbMasterId + " and id in(" + ids + ")";
							pstmt2 = conn.prepareStatement(uSQL);
							pstmt2.executeUpdate();
						}
					}
				} else if (resetType == 2) {
					String eSql = "select group_concat(id) eids from dnb_enquiry_details where dnb_master_id = "+ dnbMasterId + " and status !=2 and "
							+ "id < (select max(id) from dnb_enquiry_details where dnb_master_id = " + dnbMasterId + ")";
					pstmt3 = conn.prepareStatement(eSql);
					rst2 = pstmt3.executeQuery();
					if (rst2.next()) {
						ids = rst2.getString("eids");
						if (ids != null && !ids.equals("")) {
							String uSQL = "update dnb_enquiry_details set is_deleted = 1 where dnb_master_id = "+ dnbMasterId + " and id in("+ ids + ") and is_deleted != 2";
							pstmt4 = conn.prepareStatement(uSQL);
							pstmt4.executeUpdate();
						}
					}
				}
			} else {
				String sql = "select dnb_master_id from dnb_assignment_pool where customer_id = " + customerId + " ";
				pstmt = conn.prepareStatement(sql);
				rst = pstmt.executeQuery();
				while (rst.next()) {
					long dnbMId = rst.getLong("dnb_master_id");
					if (resetType == 1) {
						ids = "";
						String hSql = "select group_concat(id) hids from dnb_assignment_pool_history "
								+ "where dnb_master_id = " + dnbMId + " and is_processed != 2 and " + "id < (select max(id) from dnb_assignment_pool_history where dnb_master_id = " + dnbMId + ")";
						pstmt1 = conn.prepareStatement(hSql);
						rst1 = pstmt1.executeQuery();
						if (rst1.next()) {
							ids = rst1.getString("hids");
							if (ids != null && !ids.equals("")) {
								String uSQL = "update dnb_assignment_pool_history set is_deleted = 1 where dnb_master_id = " + dnbMId + " and id in(" + ids + ")";
								pstmt2 = conn.prepareStatement(uSQL);
								pstmt2.executeUpdate();
							}
						}
					} else if (resetType == 2) {
						ids = "";
						String eSql = "select group_concat(id) eids from dnb_enquiry_details " + "where dnb_master_id = "+ dnbMId + " and status !=2 and "
								+ "id < (select max(id) from dnb_enquiry_details where dnb_master_id = " + dnbMId + ")";
						pstmt3 = conn.prepareStatement(eSql);
						rst2 = pstmt3.executeQuery();
						if (rst2.next()) {
							ids = rst2.getString("eids");
							if (ids != null && !ids.equals("")) {
								String uSQL = "update dnb_enquiry_details set is_deleted = 1 where dnb_master_id = " + dnbMId + " and id in(" + ids + ") and is_deleted != 2";
								pstmt4 = conn.prepareStatement(uSQL);
								pstmt4.executeUpdate();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rst2);
			resourceManager.close(pstmt4);
			resourceManager.close(pstmt3);
			resourceManager.close(rst1);
			resourceManager.close(pstmt2);
			resourceManager.close(pstmt1);
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
	}

	@Override
	public EnquiryDto[] getEnquiriesForUser(String sql, Object[] sqlParams,Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EnquiryDto[] enquiryDto = null;
		try {
			String SQL = "select ded.id, ded.customer_id, ded.asph_id, ded.user_id, ded.start_time, ded.status, ded.assigned_by, "
					+ "ded.modified_time, dnba.enquiry_id, dnba.entity_company_name, dnba.entity_address, dnba.contact_person_name, "
					+ "dnba.contact_number, dnba.customer_reference_number, dnba.corporate_id, ded.enquiry_name, ded.dnb_master_id, "
					+ "dnba.case_id, dcsm.case_description case_type, dcom.corporate_name corporate_name, dnbh.date_of_site_visit "
					+ "from dnb_enquiry_details ded "
					+ "left join dnb_assignment_pool dnba on (dnba.dnb_master_id = ded.dnb_master_id) "
					+ "left join dnb_assignment_pool_history as dnbh on (ded.asph_id = dnbh.id and dnbh.is_deleted = 0) "
					+ "left join dnb_casetype_master dcsm on dnba.case_id = dcsm.case_id "
					+ "left join dnb_corporate_master dcom on dnba.corporate_id = dcom.corporate_id "
					+ "where ded.is_deleted = 0 and ded.status = 1 and " + sql;

			stmt = conn.prepareStatement(SQL);
			for (int i = 0; sqlParams != null && i < sqlParams.length; i++) {
				stmt.setObject(i + 1, sqlParams[i]);
			}
			rs = stmt.executeQuery();
			enquiryDto = fetchMultiEnquires(rs);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return enquiryDto;
	}

	private EnquiryDto[] fetchMultiEnquires(ResultSet rs) throws SQLException {
		Collection<EnquiryDto> resultList = new ArrayList<EnquiryDto>();
		while (rs.next()) {
			EnquiryDto dto = new EnquiryDto();
			populateEnquires(dto, rs);
			resultList.add(dto);
		}
		EnquiryDto ret[] = new EnquiryDto[resultList.size()];
		resultList.toArray(ret);
		return ret;
	}

	private void populateEnquires(EnquiryDto dto, ResultSet rs) throws SQLException {
		dto.setId(rs.getLong("id"));
		dto.setCustomerId(rs.getInt("customer_id"));
		dto.setScheduledStartTime(rs.getString("start_time"));
		dto.setModificationTime(rs.getString("modified_time"));
		dto.setStatus(rs.getInt("status"));
		dto.setData("");
		dto.setDownloadTime(PlatformUtil.getRealDateToSQLDateTime(new Date()));
		dto.setAssignmentName(rs.getString("enquiry_name"));
		dto.setDownloaded(0);
		dto.setStatusName("");
		dto.setDbTime("");
		dto.setAsphId(rs.getLong("asph_id"));
		dto.setEnquiryNo(rs.getString("enquiry_id"));
		dto.setDateOfSiteVisit(rs.getString("date_of_site_visit"));
		dto.setEntityCompanyName(rs.getString("entity_company_name"));
		dto.setEntityAddress(rs.getString("entity_address"));
		dto.setContactPersonName(rs.getString("contact_person_name"));
		dto.setContactNumber(rs.getString("contact_number"));
		dto.setCustomerCRNNumber(rs.getString("customer_reference_number"));
		dto.setCorporateName(rs.getString("corporate_name"));
		dto.setCaseId(rs.getInt("case_id"));
		dto.setCaseType(rs.getString("case_type"));
		dto.setDnbMasterId(rs.getLong("dnb_master_id"));
	}

	@Override
	public int updateEnquiryDownloadStatus(Connection conn, String assignmentIds) {
		String sql = "update dnb_enquiry_details set is_downloaded = 1, downloaded_time = ? where id in (" + assignmentIds + ") ";
		PreparedStatement stmt = null;
		int rows = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			rows = stmt.executeUpdate();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(stmt);
		}
		return rows;
	}
	
	@Override
	public int updateRemovedEnquiryNos(Connection conn, String ids){
		PreparedStatement pstmt = null;
		int count = 0;
		try{
			String sql = "update dnb_enquiry_details set is_deleted = 2 where find_in_set (id,'"+ids+"')";
			pstmt = conn.prepareStatement(sql);
			count = pstmt.executeUpdate();
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(pstmt);
		}
		return count;
	}

	@Override
	public int updateAssignmentDownloadStatus(Connection conn, String assignmentIds) {
		String sql = "update assignment set downloaded = 1, download_time = ?  where id in (" + assignmentIds + ") ";
		PreparedStatement stmt = null;
		int rows = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, PlatformUtil.getRealDateToSQLDateTime(new Date()));
			rows = stmt.executeUpdate();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(stmt);
		}
		return rows;

	}

	@Override
	public Map<String, Object> getMediaInfoExtension(Connection conn, long dnbMasterDataId) {
		String svrData = "", ccamData = "", docData = "", uniqueKey = "", enquiryId = "", assignmentId = "", docIds = "", ccamComments = "";
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray docMediaArray = new JSONArray();
		try {
			String sql = "select svr_data,ccam_data,doc_data,unique_key,enquiry_id,assignment_id,doc_ids,ccam_comments from dnb_master_data where id = "+ dnbMasterDataId + "";
			pstmt = conn.prepareStatement(sql);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				svrData = rst.getString("svr_data");
				ccamData = rst.getString("ccam_data");
				docData = rst.getString("doc_data");
				uniqueKey = rst.getString("unique_key");
				enquiryId = rst.getString("enquiry_id");
				assignmentId = rst.getString("assignment_id");
				docIds = rst.getString("doc_ids");
				ccamComments = rst.getString("ccam_comments");
			}
			if (docIds != null && !docIds.equals("")) {
				docMediaArray = generateDocMediaArray(conn, docIds, enquiryId,
						assignmentId, uniqueKey);
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		map.put("svrData", svrData);
		map.put("ccamData", ccamData);
		map.put("docData", docData);
		map.put("uniqueKey", uniqueKey);
		map.put("enquiryId", enquiryId);
		map.put("assignmentId", assignmentId);
		map.put("docIds", docIds);
		map.put("ccamComments", ccamComments);
		map.put("docMediaArray", docMediaArray);
		return map;
	}

	private JSONArray generateDocMediaArray(Connection conn, String docIds, String enquiryId, String asId, String uId) {
		JSONArray docMediaArray = new JSONArray();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		long mediaId = 0;
		String mediaType = "";
		try {
			String[] docArray = docIds.split(",");
			for (String docId : docArray) {
				JSONObject jobj = new JSONObject();
				JSONArray mediaLinkArray = new JSONArray();
				String sql = "select id, media_type, seq_id from dnb_workflow_media where unique_key = ? and as_id = ? and enquiry_id = ? and doc_id = ? and is_deleted = 0 and app_rej != 2  and media_type != '' ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, uId);
				pstmt.setString(2, asId);
				pstmt.setString(3, enquiryId);
				pstmt.setString(4, docId);
				rst = pstmt.executeQuery();
				if (docId.equals("2")) { // if svr , json structure is little change
					JSONArray signatureArray = new JSONArray();
					while (rst.next()) {
						mediaId = rst.getLong("id");
						mediaType = rst.getString("media_type");
						JSONObject sigObj = new JSONObject();
						String mediaLink = ConfigManager.baseServerURL + URLConstants.ENQUIRY_REPORT_MEDIA + mediaType + "/" + mediaId;
						sigObj.put(rst.getString("seq_id"), mediaLink);
						signatureArray.add(sigObj);
					}
					jobj.put(docId, signatureArray);
				} else {
					while (rst.next()) {
						mediaId = rst.getLong("id");
						mediaType = rst.getString("media_type");
						String mediaLink = ConfigManager.baseServerURL + URLConstants.ENQUIRY_REPORT_MEDIA + mediaType + "/" + mediaId;
						mediaLinkArray.add(mediaLink);
					}
					jobj.put(docId, mediaLinkArray);
				}
				docMediaArray.add(jobj);
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rst);
			resourceManager.close(pstmt);
		}
		return docMediaArray;
	}
}
