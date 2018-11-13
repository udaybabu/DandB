package in.otpl.dnb.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

import in.otpl.dnb.gw.CurrentUserTrackingForm;
import in.otpl.dnb.interceptor.BaseController;
import in.otpl.dnb.user.TeamDto;
import in.otpl.dnb.user.TeamLogic;
import in.otpl.dnb.user.UserDto;
import in.otpl.dnb.user.UserLogic;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.ExcelSheetDataBean;
import in.otpl.dnb.util.ExcelUtil;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.SessionConstants;
import in.otpl.dnb.util.URLConstants;

@Controller
public class ReportController extends BaseController {

	private static final Logger LOG = Logger.getLogger(ReportController.class);

	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private TeamLogic teamLogic;
	@Autowired
	private UserLogic userLogic;
	@Autowired
	private ReportLogic reportLogic;

	/** User Tracking **/
	@RequestMapping(value = URLConstants.REPORT_USER_TRACKING, method = RequestMethod.GET)
	public ModelAndView trackingList(HttpServletRequest request, HttpServletResponse response, Model model,
			UserTrackingForm userTrackingForm) {
		UserDto[] userDtos = userLogic.getAllUserListByCusidStatType(getCustomerId(request),
				getLoggedInUserTypeId(request), getLoggedInUserId(request), SessionConstants.STATUS_ACTIVE);
		try {
			String today = PlatformUtil.getRealDateToShortYearMonthDate(new Date());
			userTrackingForm.setDateFrom(today + " 00:00");
			userTrackingForm.setDateTo(today + " 23:59");
			model.addAttribute("itemsPerPageList", PlatformUtil.getItemsPerPageList());
			model.addAttribute("userList", userDtos);
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}

		return new ModelAndView("reportUserTracking", "userTrackingForm", userTrackingForm);
	}

	@RequestMapping(value = URLConstants.REPORT_USER_TRACKING_LIST, method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView viewMessage(HttpServletRequest request, HttpServletResponse response, Model model,
			UserTrackingForm userTrackingForm) {
		int page = 1, rowcount = 0;
		int customerId = getCustomerId(request);
		userTrackingForm.setCustomerId(customerId);
		UserDto[] userDtos = userLogic.getAllUserListByCusidStatType(getCustomerId(request),
				getLoggedInUserTypeId(request), getLoggedInUserId(request), SessionConstants.STATUS_ACTIVE);
		model.addAttribute("itemsPerPageList", PlatformUtil.getItemsPerPageList());
		model.addAttribute("userList", userDtos);
		return userTrackingList(request, userTrackingForm, model, page, rowcount);
	}

	@RequestMapping(value = URLConstants.REPORT_USER_TRACKING_LIST + "/{page}/{rowcount}", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, Model model,
			UserTrackingForm userTrackingForm, @PathVariable("page") int page, @PathVariable("rowcount") int rowcount) {
		int customerId = getCustomerId(request);
		userTrackingForm.setCustomerId(customerId);
		UserDto[] userDtos = userLogic.getAllUserListByCusidStatType(getCustomerId(request),
				getLoggedInUserTypeId(request), getLoggedInUserId(request), SessionConstants.STATUS_ACTIVE);
		model.addAttribute("userList", userDtos);
		model.addAttribute("itemsPerPageList", PlatformUtil.getItemsPerPageList());
		return userTrackingList(request, userTrackingForm, model, page, rowcount);
	}

	private ModelAndView userTrackingList(HttpServletRequest request, UserTrackingForm userTrackingForm, Model model,
			int page, int rowcount) {
		int records = 0;
		UserTrackingDto[] userTrackingDto = null;
		try {
			if (rowcount > 0) {
				userTrackingForm.setOffset(rowcount * (page - 1));
				userTrackingForm.setRowcount(rowcount);
			} else {
				userTrackingForm.setOffset(userTrackingForm.getRowcount() * (page - 1));
			}
			Map<String, Object> map = reportLogic.getUserTrackingList(userTrackingForm, getCustomerId(request));
			if (map != null) {
				userTrackingDto = (UserTrackingDto[]) map.get("data");
				records = (Integer) map.get("totalRows");
			}
			UserDto[] userDtos = userLogic.getAllUserListByCusidStatType(getCustomerId(request),
					getLoggedInUserTypeId(request), getLoggedInUserId(request), SessionConstants.STATUS_ACTIVE);
			model.addAttribute("userList", userDtos);
			model.addAttribute("currentPage", page);
			model.addAttribute("data", userTrackingDto);
			model.addAttribute("totalRows", records);
			if (rowcount > 0) {
				model.addAttribute("rowcount", rowcount);
			} else {
				model.addAttribute("rowcount", userTrackingForm.getRowcount());
			}
			model.addAttribute("itemsPerPageList", PlatformUtil.getItemsPerPageList());
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return new ModelAndView("reportUserTracking", "userTrackingForm", userTrackingForm);
	}

	@RequestMapping(value = URLConstants.REPORT_USER_TRACKING_DOWNLOAD_EXCEL, method = RequestMethod.GET)
	public void excelUserTracking(HttpServletRequest request, HttpServletResponse response, Model model,
			UserTrackingForm userTrackingForm) throws IOException {
		UserTrackingDto[] reportUserTracking = null;
		int customerId = getCustomerId(request);
		userTrackingForm.setCustomerId(customerId);
		userTrackingForm.setRowcount(0);
		;
		Map<String, Object> map = reportLogic.getUserTrackingList(userTrackingForm, getCustomerId(request));
		reportUserTracking = (UserTrackingDto[]) map.get("data");
		Map<String, String> columnHeadings = new LinkedHashMap<String, String>();
		String sheetHeading = null;
		String sheetName = null;
		columnHeadings.put("clientTime", resourceManager.getMessage("user.tracking.report.date&Time"));
		columnHeadings.put("fullName", resourceManager.getMessage("user.tracking.report.user"));
		columnHeadings.put("eventName", resourceManager.getMessage("user.tracking.report.description"));
		columnHeadings.put("bccStatus", resourceManager.getMessage("user.tracking.report.status"));
		columnHeadings.put("latitude", resourceManager.getMessage("user.tracking.report.latitude"));
		columnHeadings.put("longitude", resourceManager.getMessage("user.tracking.report.longitude"));
		columnHeadings.put("speed", resourceManager.getMessage("user.tracking.report.speed"));
		columnHeadings.put("accuracy", resourceManager.getMessage("user.tracking.report.accuracy"));

		sheetHeading = resourceManager.getMessage("user.tracking.report.heading");
		sheetName = resourceManager.getMessage("user.tracking.report.heading");

		ExcelSheetDataBean sheetDataBean = new ExcelSheetDataBean(Arrays.asList(reportUserTracking), sheetHeading,
				sheetName, columnHeadings);
		List<ExcelSheetDataBean> sheetDataList = new ArrayList<ExcelSheetDataBean>();
		sheetDataList.add(sheetDataBean);
		response.setContentType("application/octet");

		response.setHeader("Content-disposition", "attachment; filename=USER(S)TrackingReport.xlsx");

		ExcelUtil excelUtils = new ExcelUtil();
		excelUtils.generateExcel(sheetDataList, response.getOutputStream());
	}

	@RequestMapping(value = URLConstants.REPORT_USER_TRACKING_DOWNLOAD_PDF, method = RequestMethod.GET)
	public void pdfUserTracking(Model model, HttpServletRequest request, HttpServletResponse response,
			UserTrackingForm userTrackingForm) throws IOException {
		try {
			UserTrackingDto[] userTrackingReport = null;
			int customerId = getCustomerId(request);
			userTrackingForm.setCustomerId(customerId);
			userTrackingForm.setRowcount(0);
			Map<String, Object> map = reportLogic.getUserTrackingList(userTrackingForm, getCustomerId(request));
			userTrackingReport = (UserTrackingDto[]) map.get("data");
			Document document = new Document(PageSize.A4.rotate());
			response.setContentType("application/pdf");

			response.setHeader("Content-disposition", "attachment; filename=USER(s)TrackingReport.pdf");

			PdfWriter.getInstance(document, response.getOutputStream());

			String[] headers = { resourceManager.getMessage("user.tracking.report.date&Time"),
					resourceManager.getMessage("user.tracking.report.user"),
					resourceManager.getMessage("user.tracking.report.description"),
					resourceManager.getMessage("user.tracking.report.status"),
					resourceManager.getMessage("user.tracking.report.latitude"),
					resourceManager.getMessage("user.tracking.report.longitude"),
					resourceManager.getMessage("user.tracking.report.speed"),
					resourceManager.getMessage("user.tracking.report.accuracy") };

			PdfPTable table = new PdfPTable(headers.length);
			PdfPCell cell = new PdfPCell();
			cell.setPhrase(new Phrase(resourceManager.getMessage("user.tracking.report.heading").toUpperCase(),
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
			for (UserTrackingDto userTrackingInfo : userTrackingReport) {
				table.addCell(userTrackingInfo.getClientTime());
				table.addCell(userTrackingInfo.getFullName());
				table.addCell(userTrackingInfo.getEventName());
				table.addCell(userTrackingInfo.getBccStatus());
				table.addCell(userTrackingInfo.getLatitude());
				table.addCell(userTrackingInfo.getLongitude());
				table.addCell(userTrackingInfo.getSpeed());
				table.addCell(userTrackingInfo.getAccuracy());
			}
			document.open();
			document.add(table);
			document.close();

		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}

	/** All User Map **/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = URLConstants.REPORT_ALLUSERMAP, method = RequestMethod.GET)
	public ModelAndView dnbMap(HttpServletRequest request, HttpServletResponse response, AllUserMapForm userMapForm,Model model) {
		long userId = getLoggedInUserId(request);
		userMapForm.setUserId(userId);
		int customerId = getCustomerId(request);
		userMapForm.setCustomerId(customerId);
		if (userId > 0) {
			Map<String, Object> map = reportLogic.getAllUserTrackingReport(userMapForm);
			List<CurrentUserTrackingForm> data = (List<CurrentUserTrackingForm>) map.get("data");
			model.addAttribute("data", data);
			model.addAttribute("customerId", customerId);
			model.addAttribute("totalRows", (Integer) map.get("totalRows"));
			UserDto[] users = userLogic.getAllUserListByCusidStatType(customerId, getLoggedInUserTypeId(request),
			userId, SessionConstants.STATUS_ACTIVE);
			model.addAttribute("userDetails", users);
			HashMap<Long, String> teamMap = new HashMap<Long, String>();
			TeamDto[] teams = teamLogic.getTeams(customerId);
			for (int i = 0; i < teams.length; i++) {
			TeamDto team = teams[i];
			teamMap.put(team.getId(), team.getName());
			model.addAttribute("teamDetails", teamMap);
			}
		}
		return new ModelAndView("reportAllUserMap", "userMapForm", userMapForm);
	}
	
	/* Login Details */
	@RequestMapping(value=URLConstants.REPORT_LOGIN,method=RequestMethod.GET)
	public ModelAndView loginDetails(HttpServletRequest request,HttpServletResponse response,Model model,LoginDetailsForm loginDetailsForm){
	model.addAttribute("itemsPerPageList",(PlatformUtil.getItemsPerPageList()));
    return new ModelAndView("loginDetails","loginDetailsForm",loginDetailsForm);
	}
	
	@RequestMapping(value=URLConstants.REPORT_LOGIN_LIST,method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView listLoginDetails(HttpServletRequest request,HttpServletResponse response,Model model,LoginDetailsForm loginDetailsForm){
	int page = 1,rowcount=0;
	model.addAttribute("itemsPerPageList",(PlatformUtil.getItemsPerPageList()));
	return listsLoginDetails(request,response,loginDetailsForm,model,page,rowcount);
	}
	
	@RequestMapping(value = URLConstants.REPORT_LOGIN_LISTS  + "/{page}/{rowcount}", method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView listLoginDetails(HttpServletRequest request,HttpServletResponse response,Model model,LoginDetailsForm loginDetailsForm,@PathVariable("page") int page,@PathVariable("rowcount") int rowcount) {
	return listsLoginDetails(request,response,loginDetailsForm,model,page,rowcount);
	}
	
	@SuppressWarnings("unchecked")
	private ModelAndView listsLoginDetails(HttpServletRequest request,HttpServletResponse response,LoginDetailsForm loginDetailsForm,Model model,int page,int rowcount){
		int records = 0;
		loginDetailsForm.setUserId(getLoggedInUserId(request));
		List<LoginDetailsDto>  dto=null;
		try {
			loginDetailsForm.setOffset(loginDetailsForm.getRowcount() * (page - 1));
			Map<String, Object> map = reportLogic.getLoginDetailList(loginDetailsForm,getCustomerId(request));
				dto =  (List<LoginDetailsDto>) map.get("data");
				records = (Integer) map.get("totalRows");
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		if (rowcount > 0) {
			model.addAttribute("rowcount", rowcount);
		} else {
			model.addAttribute("rowcount", loginDetailsForm.getRowcount());
		}
		model.addAttribute("data",dto);
		model.addAttribute("action", loginDetailsForm.getTypeData());
		model.addAttribute("totalRows",records);
		model.addAttribute("currentPage", page);
		model.addAttribute("itemsPerPageList",(PlatformUtil.getItemsPerPageList()));
		return new ModelAndView("loginDetails","loginDetailsForm",loginDetailsForm);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = URLConstants.REPORT_LOGIN_DOWNLOAD_EXCEL, method = RequestMethod.GET)
	public void excelLoginDetails(HttpServletRequest request, HttpServletResponse response, Model model,LoginDetailsForm loginDetailsForm) throws IOException {
		loginDetailsForm.setUserId(getLoggedInUserId(request));
		loginDetailsForm.setRowcount(0);
		Map<String, Object> map =  reportLogic.getLoginDetailList(loginDetailsForm,getCustomerId(request));
		List<LoginDetailsDto> loginDetailsDto = new ArrayList<LoginDetailsDto>();
		if (map != null) {
			loginDetailsDto =  (List<LoginDetailsDto>) map.get("data");
		}
		Map<String, String> columnHeadings = new LinkedHashMap<String, String>();
		String sheetHeading = null;
		String sheetName = null;
		columnHeadings.put("userName", resourceManager.getMessage("login.details.name"));
		columnHeadings.put("teamName", resourceManager.getMessage("login.details.team"));
		columnHeadings.put("modificationTime", resourceManager.getMessage("login.details.date"));
		columnHeadings.put("noDays", resourceManager.getMessage("login.details.days"));
		sheetHeading = resourceManager.getMessage("login.details.message.heading");
		sheetName = resourceManager.getMessage("login.details.message.heading");

		ExcelSheetDataBean sheetDataBean = new ExcelSheetDataBean(loginDetailsDto, sheetHeading, sheetName,columnHeadings);
		List<ExcelSheetDataBean> sheetDataList = new ArrayList<ExcelSheetDataBean>();
		sheetDataList.add(sheetDataBean);
		response.setContentType("application/octet");
		response.setHeader("Content-disposition", "attachment; filename=Login_Details.xlsx");
		ExcelUtil excelUtils = new ExcelUtil();
		excelUtils.generateExcel(sheetDataList, response.getOutputStream());
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = URLConstants.REPORT_LOGIN_DOWNLOAD_PDF, method = RequestMethod.GET)
	public void pdfLoginDetails(Model model, HttpServletRequest request, HttpServletResponse response,LoginDetailsForm loginDetailsForm) throws IOException {
		try {
			loginDetailsForm.setUserId(getLoggedInUserId(request));
			loginDetailsForm.setRowcount(0);
			Map<String, Object> map = reportLogic.getLoginDetailList(loginDetailsForm,getCustomerId(request));
			List<LoginDetailsDto> loginDetailsDto = new ArrayList<LoginDetailsDto>();
			if (map != null) {
				loginDetailsDto =  (List<LoginDetailsDto>) map.get("data");
			}
			com.itextpdf.text.Document document = new com.itextpdf.text.Document();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=Login_Details.pdf");

			PdfWriter.getInstance(document, response.getOutputStream());
			String[] headers = { resourceManager.getMessage("login.details.name"),
					resourceManager.getMessage("login.details.team"),
				    resourceManager.getMessage("login.details.date"),
				    resourceManager.getMessage("login.details.days")};

			PdfPTable table = new PdfPTable(headers.length);
			PdfPCell cell = new PdfPCell();
			cell.setPhrase(new Phrase(resourceManager.getMessage("login.details.message.heading").toUpperCase(),FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD)));
			cell.setColspan(headers.length);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(10.0f);
			table.addCell(cell);
			for (int i = 0; i < headers.length; i++) {
				String header = headers[i];
				PdfPCell hcell = new PdfPCell();
				hcell.setGrayFill(0.9f);
				hcell.setPhrase(new Phrase(header.toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
				table.addCell(hcell);
			}
			table.setSpacingBefore(30.0f);
			table.setSpacingAfter(30.0f);
			for (LoginDetailsDto loginInfo : loginDetailsDto) {
				table.addCell(loginInfo.getUserName());
				table.addCell(loginInfo.getTeamName());
				table.addCell(loginInfo.getModificationTime());
				table.addCell(String.valueOf(loginInfo.getNoDays()));
			}
			document.open();
			document.add(table);
			document.close();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}
}