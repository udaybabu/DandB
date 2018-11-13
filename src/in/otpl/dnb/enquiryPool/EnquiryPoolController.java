package in.otpl.dnb.enquiryPool;

import in.otpl.dnb.dao.CommonDao;
import in.otpl.dnb.interceptor.BaseController;
import in.otpl.dnb.logic.CommonLogic;
import in.otpl.dnb.user.UserDto;
import in.otpl.dnb.user.UserLogic;
import in.otpl.dnb.util.ConfigManager;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.ExcelSheetDataBean;
import in.otpl.dnb.util.ExcelUtil;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.URLConstants;
import in.otpl.dnb.util.SessionConstants;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

@Controller
public class EnquiryPoolController extends BaseController {

	private static final Logger LOG = Logger.getLogger(EnquiryPoolController.class);

	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private ConfigManager configManager;
	@Autowired
	private UserLogic userLogic;
	@Autowired
	private EnquiryLogic enquiryLogic;
	@Autowired
	private CommonLogic commonLogic;
	@Autowired
	private CommonDao commonDao;

	@RequestMapping(value = URLConstants.ENQUIRY, method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView task(HttpServletRequest request, HttpServletResponse response, Model model,EnquiryForm enquiryForm) {
		try {
			enquiryForm.setDateFrom(PlatformUtil.getRealDateToShortYearMonthDate(new Date()) + " 00:00");
			enquiryForm.setDateTo(PlatformUtil.getRealDateToShortYearMonthDate(new Date()) + " 23:59");
			UserDto[] userDtos = userLogic.getAllUserListByCusidStatType(getCustomerId(request),getLoggedInUserTypeId(request), getLoggedInUserId(request), SessionConstants.STATUS_ACTIVE);
			model.addAttribute("userList", userDtos);
			model.addAttribute("itemPerPageList", PlatformUtil.getItemsPerPageList());
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return new ModelAndView("enquiryPool", "enquiryForm", enquiryForm);
	}

	/***** Enquiry List *****/
	@RequestMapping(value = URLConstants.ENQUIRY_LIST, method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView viewTask(HttpServletRequest request, HttpServletResponse response, Model model,
			EnquiryForm enquiryForm) {
		int page = 1, rowcount = 0;
		return enquiryList(request, enquiryForm, model, page, rowcount);
	}

	@RequestMapping(value = URLConstants.ENQUIRY_LISTS + "/{page}/{rowcount}", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, Model model, EnquiryForm enquiryForm, @PathVariable("page") int page, @PathVariable("rowcount") int rowcount) {
		return enquiryList(request, enquiryForm, model, page, rowcount);
	}

	@SuppressWarnings("unchecked")
	private ModelAndView enquiryList(HttpServletRequest request, EnquiryForm enquiryForm, Model model, int page, int rowcount) {
		int records = 0;
		List<EnquiryDto> enquiryDto = new ArrayList<EnquiryDto>();
		int customerId = getCustomerId(request);
		String userType = getLoggedInUserType(request);
		String loginName = getLoginName(request);
		UserDto[] userDtos = userLogic.getAllUserListByCusidStatType(getCustomerId(request),getLoggedInUserTypeId(request), getLoggedInUserId(request), SessionConstants.STATUS_ACTIVE);
		model.addAttribute("userList", userDtos);
		try {
			if(rowcount > 0){
				enquiryForm.setOffset(rowcount*(page-1));
				enquiryForm.setRowcount(rowcount);
			}else{
				enquiryForm.setOffset(rowcount * (page - 1));
			}
			Map<String, Object> map = enquiryLogic.getAssignmentPool(enquiryForm, customerId, loginName, userType,getLoggedInUserId(request));
			if (map != null) {
				enquiryDto = (List<EnquiryDto>) map.get("data");
				records = (Integer) map.get("totalRows");
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		model.addAttribute("data", enquiryDto);
		model.addAttribute("itemPerPageList", PlatformUtil.getItemsPerPageList());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalRows", records);
		model.addAttribute("rowcount", enquiryForm.getRowcount());
		return new ModelAndView("enquiryPool", "enquiryForm", enquiryForm);
	}

	@RequestMapping(value = URLConstants.ENQUIRY_DOWNLOAD_EXCEL, method = RequestMethod.GET)
	public void excel(HttpServletRequest request, HttpServletResponse response, Model model, EnquiryForm enquiryForm){
		int customerId = getCustomerId(request);
		enquiryForm.setRowcount(0);
		String loginName = getLoggedInUserLastName(request);
		String userType = getLoggedInUserType(request);
		Map<String, Object> map = enquiryLogic.getAssignmentPool(enquiryForm, customerId, loginName, userType,getLoggedInUserId(request));
		@SuppressWarnings("unchecked")
		List<EnquiryDto> enquirylist = (List<EnquiryDto>) map.get("data");
		Map<String, String> columnHeadings = new LinkedHashMap<String, String>();
		columnHeadings.put("dnbMasterId", "DNB MID");
		columnHeadings.put("qfStatus", "Current QF Status");
		columnHeadings.put("caseStatusVal", "WFM Status");
		columnHeadings.put("leadEmpName", "Lead Name");
		columnHeadings.put("userName", "FOS Name");
		columnHeadings.put("enquiryNo", "Enquiry Number");
		columnHeadings.put("caseId", "Case Number");
		columnHeadings.put("caseType", "Case Type");
		columnHeadings.put("dateOfSiteVisit", "Date Of Site Visit");
		columnHeadings.put("eqnuiryReceivedTime", "Enquiry Received Time");
		columnHeadings.put("aspPdfLink", "PDF Link");
		columnHeadings.put("webLink", "WEB Link");

		String sheetHeading = "ENQUIRYPOOL(s)";
		String sheetName = "ENQUIRYPOOL(s)";
		try {
			ExcelSheetDataBean sheetDataBean = new ExcelSheetDataBean(enquirylist, sheetHeading, sheetName,
					columnHeadings);
			List<ExcelSheetDataBean> sheetDataList = new ArrayList<ExcelSheetDataBean>();
			sheetDataList.add(sheetDataBean);
			response.setContentType("application/octet");

			response.setHeader("Content-disposition", "attachment; filename=EnquiryPoolReport.xlsx");

			ExcelUtil excelUtils = new ExcelUtil();
			excelUtils.generateExcel(sheetDataList, response.getOutputStream());
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = URLConstants.ENQUIRY_DOWNLOAD_PDF, method = RequestMethod.GET)
	public void pdf(Model model, HttpServletRequest request, HttpServletResponse response, EnquiryForm enquiryForm){
		try {
			enquiryForm.setRowcount(0);
			int customerId = getCustomerId(request);
			model.addAttribute(enquiryForm);
			String loginName = getLoggedInUserLastName(request);
			String userType = getLoggedInUserType(request);
			Map<String, Object> map = enquiryLogic.getAssignmentPool(enquiryForm, customerId, loginName, userType, getLoggedInUserId(request));
			List<EnquiryDto> enquirylist = (List<EnquiryDto>) map.get("data");
			Document document = new Document(PageSize.A4.rotate());
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=EnquiryPoolReport.pdf");
			PdfWriter.getInstance(document, response.getOutputStream());
			String[] headers = { resourceManager.getMessage("enquirypool.label.dnbMid"),
					resourceManager.getMessage("enquirypool.label.currentQfStatus"),
					resourceManager.getMessage("enquirypool.label.wfmStatus"),
					resourceManager.getMessage("enquirypool.label.leadEmployeeName"),
					resourceManager.getMessage("enquirypool.label.fosName"),
					resourceManager.getMessage("enquirypool.label.enquiryNumber"),
					resourceManager.getMessage("enquirypool.label.caseNumber"),
					resourceManager.getMessage("enquirypool.label.caseType"),
					resourceManager.getMessage("enquirypool.label.dateOfSiteVisit"),
					resourceManager.getMessage("enquirypool.label.enquiryReceivedTime")};
			PdfPTable table = new PdfPTable(headers.length);
			PdfPCell cell = new PdfPCell();
			cell.setPhrase(new Phrase("ENQUIRYPOOL(s)".toUpperCase(),
					FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD)));
			cell.setColspan(headers.length);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(10.0f);
			table.addCell(cell);
			for (int i = 0; i < headers.length; i++) {
				String header = headers[i];
				PdfPCell hcell = new PdfPCell();
				hcell.setGrayFill(0.9f);
				hcell.setPhrase(
						new Phrase(header.toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
				table.addCell(hcell);
			}
			table.setSpacingBefore(30.0f);
			table.setSpacingAfter(30.0f);
			for (int i = 0; i < enquirylist.size(); i++) {
				table.addCell(String.valueOf(enquirylist.get(i).getDnbMasterId()));
				table.addCell(enquirylist.get(i).getQfStatus());
				table.addCell(enquirylist.get(i).getCaseStatusVal());
				table.addCell(enquirylist.get(i).getLeadEmpName());
				table.addCell(enquirylist.get(i).getUserName());
				table.addCell(enquirylist.get(i).getEnquiryNo());
				table.addCell(String.valueOf(enquirylist.get(i).getCaseId()));
				table.addCell(enquirylist.get(i).getCaseType());
				table.addCell(enquirylist.get(i).getDateOfSiteVisit());
				table.addCell(enquirylist.get(i).getEqnuiryReceivedTime());
			}
			document.open();
			document.add(table);
			document.close();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}

	@RequestMapping(value = URLConstants.ENQUIRY_CASE_HISTORY, method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView caseHistory(@RequestParam("dnbMasterId") int dnbMasterId, HttpServletRequest request, HttpServletResponse response, Model model, EnquiryForm enquiryForm){
		int customerId = getCustomerId(request);
		JSONArray caseHistoryArray = new JSONArray();
		try {
			caseHistoryArray = enquiryLogic.getCaseHistory(dnbMasterId, customerId);
			response.getWriter().print(caseHistoryArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = URLConstants.ENQUIRY_CASE_ASSIGNMENT_HISTORY, method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView caseAssignmentHistory(@RequestParam("dnbMasterId") int dnbMasterId, HttpServletRequest request, HttpServletResponse response, Model model, EnquiryForm enquiryForm){
		int customerId = getCustomerId(request);
		JSONArray caseAssignmentHistory = new JSONArray();
		try {
			caseAssignmentHistory = enquiryLogic.getCaseAssignmentHistory(dnbMasterId, customerId);
			response.getWriter().print(caseAssignmentHistory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = URLConstants.ENQUIRY_DETAILS, method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView enquiryDetails(@RequestParam("dnbMasterId") int dnbMasterId, HttpServletRequest request, HttpServletResponse response, Model model, EnquiryForm enquiryForm) {
		int customerId = getCustomerId(request);
		JSONObject enquiryDetails = null;
		try {
			enquiryDetails = enquiryLogic.getEnquiryDetails(dnbMasterId, customerId);
			response.getWriter().print(enquiryDetails);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = URLConstants.ENQUIRY_DOCUMENT_LIST, method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView documentList(@RequestParam("caseId") int caseId, HttpServletRequest request, HttpServletResponse response, Model model, EnquiryForm enquiryForm) {
		int customerId = getCustomerId(request);
		JSONArray documentList = new JSONArray();
		try {
			documentList = enquiryLogic.getDocumentList(customerId, caseId);
			response.getWriter().print(documentList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = URLConstants.ENQUIRY_ALL_USERS, method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView allUsers(HttpServletRequest request, HttpServletResponse response, Model model, EnquiryForm enquiryForm) {
		JSONArray userList = new JSONArray();
		UserDto[] dto = userLogic.getAllUserListByCusidStatType(getCustomerId(request),getLoggedInUserTypeId(request), getLoggedInUserId(request), SessionConstants.STATUS_ACTIVE);
		if (dto != null && dto.length > 0) {
			for (int i = 0; i < dto.length; i++) {
				JSONObject tempObj = new JSONObject();
				tempObj.put("id", dto[i].getId());
				tempObj.put("uname", dto[i].getFirstName() + " " + dto[i].getLastName());
				userList.add(tempObj);
			}
		}
		try {
			response.getWriter().print(userList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = URLConstants.ENQUIRY_SAVE, method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView save(@RequestParam("uid") long uid, @RequestParam("startDate") String startDate,
			@RequestParam("enquiryName") String enquiryName, @RequestParam("dnbMasterId") long dnbMasterId,
			@RequestParam("asphId") long asphId, @RequestParam("isReassigned") int isReassigned,
			@RequestParam("enquiryDetailsId") long enquiryDetailsId, HttpServletRequest request,
			HttpServletResponse response, Model model, EnquiryForm enquiryForm) {

		String enquiryStartTime = startDate;
		long newEnquiryDetailId = 0;
		long rows = 0;
		int customerId = getCustomerId(request);
		int loggedInUserId = (int) getLoggedInUserId(request);
		long userLoginId=getLoggedInUserId(request);
		long enquiryDetailsIds = request.getParameter("enquiryDetailsId") != null ? Long.parseLong(request.getParameter("enquiryDetailsId")) : 0;
		try {
			newEnquiryDetailId = enquiryLogic.saveUpdateEnquiry(uid, enquiryName, dnbMasterId, asphId, isReassigned, enquiryDetailsIds, customerId, loggedInUserId, enquiryStartTime);
			rows = enquiryLogic.updateDNBAssignmentPoolHistory(customerId, uid, asphId, newEnquiryDetailId);
			String subjectAdd = "Case Assigned ";
			response.getWriter().print(newEnquiryDetailId);
			if (isReassigned == 1)
				subjectAdd = "Case Reassigned ";
			if (rows > 0) {
				JSONObject enquiryDetails = enquiryLogic.getEnquiryDetails(dnbMasterId, customerId);
				String emailAddress = "", subject = "", body = "", correspondendtName = "", loginName = "";
				UserDto user = userLogic.getUserDetails(userLoginId);

				if (user != null) {
					emailAddress = user.getEmailAddress();
					correspondendtName = user.getFirstName() + " " + user.getLastName();
				}
				if (!emailAddress.equals("") && enquiryDetails.size() > 0) {
					subject = "" + subjectAdd + " for "
							+ (enquiryDetails.containsKey("corporateName") ? enquiryDetails.getString("corporateName")
									: "")
									+ " - for Site Visit and Documents Collection - "
									+ (enquiryDetails.containsKey("entityAddress") ? enquiryDetails.getString("entityAddress")
											: "")
											+ "";
					body = "<!doctype html><html><head><meta charset='utf-8'></head>" + "<body><p>Hi "
							+ correspondendtName + ",</p>"
							+ "<p>Kindly check the status updated online & initiate for Site visit.</p>"
							+ "<p>The name of the Corporate cannot be disclosed for Non Buy in & For Buy in we can disclose the corporate Name. </p>"
							+ "<p>Subject contact details:-</p>" + "<table border='0' width='800px'>"
							+ "<tr><td><b>Enquiry Name </b></td><td>"
							+ (enquiryDetails.containsKey("entityCompName") ? enquiryDetails.getString("entityCompName")
									: "")
									+ "</td></tr>" + "<tr><td><b>Address</b></td><td>"
									+ (enquiryDetails.containsKey("entityAddress") ? enquiryDetails.getString("entityAddress")
											: "")
											+ "</td></tr>" + "<tr><td><b>State</b></td><td>"
											+ (enquiryDetails.containsKey("state_name") ? enquiryDetails.getString("state_name") : "")
											+ "</td></tr>" + "<tr><td><b>City</b></td><td>"
											+ (enquiryDetails.containsKey("city_name") ? enquiryDetails.getString("city_name") : "")
											+ "</td></tr>" + "<tr><td><b>Pincode</b></td><td>"
											+ (enquiryDetails.containsKey("pincode") ? enquiryDetails.getString("pincode") : "")
											+ "</td></tr>" + "<tr><td><b>Telephone</b></td>"
											+ (enquiryDetails.containsKey("contactNumber") ? enquiryDetails.getString("contactNumber")
													: "")
													+ "<td></td></tr>" + "<tr><td><b>Email Id</b></td><td><u>"
													+ (enquiryDetails.containsKey("custEmailAddress")
															? enquiryDetails.getString("custEmailAddress")
																	: "")
																	+ "</u></td></tr>" + "<tr><td><b>Web Address</b></td><td>"
																	+ (enquiryDetails.containsKey("web_address") ? enquiryDetails.getString("web_address") : "")
																	+ "</td></tr>" + "<tr><td><b>Contact Person</b></td><td>"
																	+ (enquiryDetails.containsKey("contactPersonName")
																			? enquiryDetails.getString("contactPersonName")
																					: "")
																					+ "</td></tr>" + "<tr><td><b>Remarks:</b></td><td>"
																					+ (enquiryDetails.containsKey("workflow_remarks")
																							? enquiryDetails.getString("workflow_remarks")
																									: "")
																									+ "</td></tr></table>"
																									+ "<br><p>Speak to the subject before going for the visit to reconfirmthe below:</p>"
																									+ "<ul type='disc'><li>Date and time of appointment </li><li>Address of visit</li><li>Correct Name of the company</li>"
																									+ "<li>Contact Person / Signing Authority to be present during the visit</li></ul><br>"
																									+ "<p>Please ensure the below:</p><ul type='circle'><li>The status of the appointment / visit needs to be updated on workflow "
																									+ "(On time , rescheduled with reasons)</li><li>The CCAM reports needs to be delivered within 2 working days "
																									+ "(where the Corporate name is not disclosed)<br> from the date of receipt of enquiry so please ensure the visit is done accordingly</li>"
																									+ "<li>Please ensure to collect all documents as per the checklist attached and at least the basic documents like <br>"
																									+ "VAT , CST / LST , Partnership / MOA AOA along with <b>Audited financials for last 3 financial years.</b></li>"
																									+ "<li>Incases if subject declines / defers to provide the Requisite details the same needs to be mentioned online with reasons </li>"
																									+ "<li>Incaseof any discrepancy or resistance during the visit please reach out to me from the visit location.</li>"
																									+ "<li>For all J&J cases in the FCPA all pages need to be signed and stamped by the signing authority and <br>specifically the page where there are 2 boxes "
																									+ "need to be signed and stamped on both the boxes</li></ul><br><p>Please let me know if any further clarifications are required</p><br>"
																									+ "<p>Thanks ";
					body += "and Regards,</p><p>" + user.getFirstName() + " " + user.getLastName()
							+ " <br></p></body></html>";
					commonLogic.insertEmailDetails(getCustomerId(request), emailAddress, "", "", subject, body);
				} 
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return null;
	}

	@RequestMapping(value = URLConstants.ENQUIRY_REPORT_MEDIA+"/{mediaType}/{mediaId}", method = {RequestMethod.GET,RequestMethod.POST})
	public void getPicExtension(@PathVariable("mediaType")String mediaType,@PathVariable("mediaId")String mediaId,HttpServletRequest request, HttpServletResponse response,EnquiryForm enquiryForm,Model model) throws SQLException {
		LOG.info("Client IP: "+PlatformUtil.getClientIpAddress(request));
		try{
			String docMediaPath = configManager.getMediaFilePath();
			if(mediaId != null && !mediaId.equals("")){ 
				if(mediaType.equalsIgnoreCase("jpg")){
					response.setContentType("image/jpg");
					response.setHeader("Content-Disposition", "inline; filename=img.jpg;");
				}else if(mediaType.equalsIgnoreCase("pdf")){ 
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "inline; filename=doc.pdf;");
				}

				JSONObject mediaObject = commonLogic.getDNBMediaDetails("",0,0,"",mediaId);
				if(mediaObject != null && !mediaObject.isEmpty()){
					JSONArray mediaArray = JSONArray.fromObject(mediaObject.getString("mediaArray"));
					if(mediaArray != null && mediaArray.size() >0){
						for(int k = 0 ; k < mediaArray.size() ; k++){
							JSONObject mObj = mediaArray.getJSONObject(k);
							docMediaPath += mObj.getString("mPath");
						}
					}
				}

				BufferedInputStream origin = new BufferedInputStream(new FileInputStream(docMediaPath));
				int length;
				final int four_byte = 4096;
				byte[] data = new byte[four_byte];
				while ((length = origin.read(data)) != -1) {
					response.getOutputStream().write(data, 0, length);
				}
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}
	
	@RequestMapping(value = URLConstants.ENQUIRY_REPORT_HTML+"/{dnbMasterDataId}", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView getHTMLExtension(@PathVariable("dnbMasterDataId")long dnbMasterDataId,HttpServletRequest request, HttpServletResponse response,EnquiryForm enquiryForm,Model model) {
		try{
			Map<String ,Object> map= enquiryLogic.getMediaInfoExtension(dnbMasterDataId);
			model.addAttribute("svrData",(String) map.get("svrData"));
			model.addAttribute("ccamData", (String) map.get("ccamData"));
			model.addAttribute("docData", (String) map.get("docData"));
			model.addAttribute("uniqueKey", (String) map.get("uniqueKey"));
			model.addAttribute("enquiryId", (String) map.get("enquiryId"));
			model.addAttribute("assignmentId", (String) map.get("assignmentId"));
			model.addAttribute("ccamComments", (String) map.get("ccamComments"));
			model.addAttribute("docMediaArray", (JSONArray) map.get("docMediaArray"));
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return new ModelAndView("enquiryLinkReport");
	}
	
	@RequestMapping(value = URLConstants.ENQUIRY_REPORT_PDF+"/{dnbMasterDataId}", method = {RequestMethod.GET,RequestMethod.POST})
	public void getPDFExtension(@PathVariable("dnbMasterDataId")long dnbMasterDataId,HttpServletRequest request, HttpServletResponse response,EnquiryForm enquiryForm,Model model) throws IOException {
		LOG.info("Client IP: "+PlatformUtil.getClientIpAddress(request));
		String mediaLink = ConfigManager.getBaseServerURL()+URLConstants.ENQUIRY_REPORT_HTML+dnbMasterDataId;
		String body = doLinktoHTMLParse(mediaLink);
		body = body.replace("&", "&amp;");
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("application/pdf");
		response.setHeader("CONTENT-DISPOSITION", "attachment; filename="+dnbMasterDataId+".pdf;");
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();
			XMLWorkerHelper.getInstance().parseXHtml(writer, document,new StringReader(body));
			document.close();
		}catch(Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}

	public String doLinktoHTMLParse(String targetURL) {
		String body = "";
		try {
			URL url = new URL(targetURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.getOutputStream();
			con.getInputStream();
			String line;

			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while ((line = rd.readLine()) != null){
				body += line;
			}
			rd.close(); 
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return body;
	}

}