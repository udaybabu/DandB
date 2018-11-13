package in.otpl.dnb.user;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import in.otpl.dnb.interceptor.BaseController;
import in.otpl.dnb.logic.AdministrationLogic;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.URLConstants;
import in.otpl.dnb.user.TeamDto;
import in.otpl.dnb.user.UserForm;
import in.otpl.dnb.user.UserTypeDto;
import in.otpl.dnb.user.UserController;
import in.otpl.dnb.user.UserDto;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.SessionConstants;
import in.otpl.dnb.util.ExcelSheetDataBean;
import in.otpl.dnb.util.ExcelUtil;
import in.otpl.dnb.user.UserFormValidation;
import in.otpl.dnb.util.ExcelParser;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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

@Controller
public class UserController extends BaseController{
	
	private static final Logger LOG = Logger.getLogger(UserController.class);
	
	@Autowired
	private UserLogic userLogic;
	@Autowired
	private TeamLogic teamLogic;
	@Autowired
	private AdministrationLogic administrationLogic;
	@Autowired
	private ResourceManager resourceManager;
	
	@RequestMapping(value = URLConstants.USER, method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView showUser(HttpServletRequest request, HttpServletResponse response,UserForm userForm,Model model){
		TeamDto[] teamDto = null ;
		int customerId = getCustomerId(request);
		teamDto= teamLogic.getTeams(customerId);
		UserTypeDto[] userTypes =null;
		boolean isAdmin = true;
	    boolean isLead = true;
	    HttpSession session = request.getSession();
	    String UserType=(String) session.getAttribute(SessionConstants.USER_TYPE);
		try{
			if(UserType.equals(SessionConstants.USER_TYPE_LEAD))
				  userTypes=userLogic.getUserTypesForLead(isLead);
				else
				 userTypes=userLogic.getAllUserTypes(isAdmin);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		model.addAttribute("teamList",teamDto);
		model.addAttribute("userTypes",userTypes);
		model.addAttribute("itemsPerPageList",(PlatformUtil.getItemsPerPageList()));
		return new ModelAndView("userList","userForm",userForm);
	}
	@RequestMapping(value = URLConstants.USER_LIST, method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response,UserForm userForm,Model model) {
		int page = 1, rowcount = 0;
		return lists(request,response,userForm, model, page,rowcount);
	}

	@RequestMapping(value = URLConstants.USER_LISTS+"{page}/{rowcount}", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView list(@PathVariable("page") int page,@PathVariable("rowcount") int rowcount,HttpServletRequest request,HttpServletResponse response,UserForm userForm,Model model) {
		return lists(request,response,userForm, model,page,rowcount);
	}

	@SuppressWarnings("unchecked")
	private ModelAndView lists(HttpServletRequest request,HttpServletResponse response,UserForm userForm,Model model,int page,int rowcount){
		int records = 0;
		List<UserDto> userDto  = new ArrayList<UserDto>();
		UserTypeDto[] userTypes =null;
		boolean isAdmin = true;
		boolean isLead = true;
		HttpSession session = request.getSession();
	    String UserType=(String) session.getAttribute(SessionConstants.USER_TYPE);
		TeamDto[] teamDto= teamLogic.getTeams(getCustomerId(request));
		userForm.setUserCreatedBy(getLoggedInUserId(request));
		try {
			if(UserType.equals(SessionConstants.USER_TYPE_LEAD))
				  userTypes=userLogic.getUserTypesForLead(isLead);
				else
				 userTypes=userLogic.getAllUserTypes(isAdmin);
			if(rowcount > 0){
				userForm.setOffset(rowcount*(page-1));
				userForm.setRowcount(rowcount);
			}else{
				userForm.setOffset(userForm.getRowcount()*(page-1));
			}
			Map<String, Object> map = userLogic.userList(getCustomerId(request), userForm, (getLoggedInUserId(request)));
			if(map != null){
				userDto = (List<UserDto>) map.get("data");
				records = (Integer)map.get("totalRows");
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		model.addAttribute("currentPage", page);
		model.addAttribute("data",userDto);
		model.addAttribute("totalRows",records);
		if(rowcount > 0){
			model.addAttribute("rowcount",rowcount);
		}else{
			model.addAttribute("rowcount",userForm.getRowcount());
		}
		model.addAttribute("userTypes",userTypes);
		model.addAttribute("teamList",teamDto);
		model.addAttribute("itemsPerPageList", PlatformUtil.getItemsPerPageList());
		return new ModelAndView("userList");
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = URLConstants.USER_DOWNLOAD_EXCEL, method = RequestMethod.GET)
	public void excel(HttpServletRequest request,HttpServletResponse response,Model model,UserForm userForm) throws IOException {
		List<UserDto> userDto  = new ArrayList<UserDto>(); 
		userForm.setUserCreatedBy(getLoggedInUserId(request));
		userForm.setRowcount(0);
		Map<String, Object> map = userLogic.userList(getCustomerId(request), userForm, getLoggedInUserId(request));
		model.addAttribute(userForm);
		userDto = (List<UserDto>) map.get("data");
		List<UserDto> users=new ArrayList<UserDto>();
		for(UserDto user:userDto){
			if(user.getUserTypeId() != 4){
			users.add(user);
			}
		}
		Map<String, String> columnHeadings = new LinkedHashMap<String, String>();
		columnHeadings.put("name", resourceManager.getMessage("user.label.name"));
		columnHeadings.put("loginName", resourceManager.getMessage("user.label.loginid"));
		columnHeadings.put("userType", resourceManager.getMessage("user.label.usertype"));
		columnHeadings.put("teamName", resourceManager.getMessage("user.label.team"));
		columnHeadings.put("employeeNumber", resourceManager.getMessage("user.label.employeenumber"));
		columnHeadings.put("strStatus", resourceManager.getMessage("user.label.status"));
		columnHeadings.put("emailAddress", resourceManager.getMessage("user.label.emailid"));
		columnHeadings.put("ptn", resourceManager.getMessage("user.label.phone"));
		columnHeadings.put("designation", resourceManager.getMessage("user.label.designation"));
		columnHeadings.put("imei", resourceManager.getMessage("user.label.IMEI.number"));
		columnHeadings.put("phoneAppVersion", resourceManager.getMessage("user.label.phoneversion"));
		columnHeadings.put("handsetDetails", resourceManager.getMessage("user.label.phoneModelId"));
		columnHeadings.put("appVersionUpdateTime", resourceManager.getMessage("user.label.appVersionUpdateTime"));

		String sheetHeading = "User Listing";
		String sheetName = "User Listing";

		ExcelSheetDataBean sheetDataBean = new ExcelSheetDataBean(users, sheetHeading, sheetName, columnHeadings);
		List<ExcelSheetDataBean> sheetDataList = new ArrayList<ExcelSheetDataBean>();

		sheetDataList.add(sheetDataBean);
		response.setContentType("application/octet");
		response.setHeader("Content-disposition", "attachment; filename=UserList.xlsx");

		ExcelUtil excelUtil = new ExcelUtil();
		excelUtil.generateExcel(sheetDataList, response.getOutputStream());
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = URLConstants.USER_DOWNLOAD_PDF, method = RequestMethod.GET)
	public void pdf(Model model,HttpServletRequest request,HttpServletResponse response,UserForm userForm) throws IOException {
		String[] headers ={""};
		try {
			userForm.setRowcount(0);
			userForm.setUserCreatedBy(getLoggedInUserId(request));
			List<UserDto> userDto  = new ArrayList<UserDto>(); 
			Map<String, Object> map = userLogic.userList(getCustomerId(request), userForm, getLoggedInUserId(request));
			model.addAttribute(userForm);
			userDto = (List<UserDto>) map.get("data");
			Document document = new Document(PageSize.A4.rotate());
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=UserList.pdf" );
			PdfWriter.getInstance(document, response.getOutputStream());
				headers =new String[]{
					resourceManager.getMessage("user.label.name"),
					resourceManager.getMessage("user.label.userid"),
					resourceManager.getMessage("user.label.usertype"),
					resourceManager.getMessage("user.label.team"),
					resourceManager.getMessage("user.label.employeenumber"),
					resourceManager.getMessage("user.label.status"),
					resourceManager.getMessage("user.label.emailid"),
					resourceManager.getMessage("user.label.phone"),
					resourceManager.getMessage("user.label.designation"),
					resourceManager.getMessage("user.label.IMEI.number"),
					resourceManager.getMessage("user.label.phoneversion"),
					resourceManager.getMessage("user.label.phoneModelId"),
					resourceManager.getMessage("user.label.appVersionUpdateTime")};
			PdfPTable table = new PdfPTable(headers.length);
			PdfPCell cell = new PdfPCell();
			cell.setPhrase(new Phrase("User List".toUpperCase(),FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD)));
			cell.setColspan(headers.length);
			cell.setHorizontalAlignment (Element.ALIGN_CENTER);
			cell.setPadding (10.0f);
			table.addCell(cell);
			for (int i = 0; i < headers.length; i++) {
				String header = headers[i];
				PdfPCell hcell = new PdfPCell();
				hcell.setGrayFill(0.9f);
				hcell.setPhrase(new Phrase(header.toUpperCase(),FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD)));
				table.addCell(hcell);
			}
			table.setSpacingBefore(30.0f); 
			table.setSpacingAfter(30.0f); 
			for (UserDto userInfo : userDto) {
					if(userInfo.getUserTypeId() != 4){
				table.addCell(userInfo.getName());
				table.addCell(userInfo.getLoginName());
				table.addCell(userInfo.getUserType());
				table.addCell(userInfo.getTeamName());
				table.addCell(userInfo.getEmployeeNumber());
				table.addCell(userInfo.getStrStatus());
				table.addCell(userInfo.getEmailAddress());
				table.addCell(userInfo.getPtn());
				table.addCell(userInfo.getDesignation());
				table.addCell(userInfo.getImei());
				table.addCell(userInfo.getPhoneAppVersion());
				table.addCell(userInfo.getHandsetDetails());
				table.addCell(userInfo.getAppVersionUpdateTime());
			}
			}
			document.open();
			document.add(table);
			document.close();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}
	@RequestMapping(value = URLConstants.USER_STATUS, method = {RequestMethod.POST,RequestMethod.GET})
	public String status(@RequestParam("id") long id,@RequestParam("status") int status,HttpServletRequest request,HttpServletResponse response, TeamForm teamForm,Model model ) {
		int customerId=getCustomerId(request);
		long rows = 0;
		try{
			rows = userLogic.changeStatus(id, customerId, status);
			if(rows > 0) {
				status = (status == 0)?1:0;
			}
			response.getWriter().print(status);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return null;
	}
	@RequestMapping(value = URLConstants.USER_CREATE,method=RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request, HttpServletResponse response, UserForm userForm,Model model){
		UserTypeDto[] userTypes =null;
		boolean isAdmin = true;
	    boolean isLead = true;
	    HttpSession session = request.getSession();
	    String UserType=(String) session.getAttribute(SessionConstants.USER_TYPE);
		try{
			if(UserType.equals(SessionConstants.USER_TYPE_LEAD))
				  userTypes=userLogic.getUserTypesForLead(isLead);
				else
				 userTypes=userLogic.getAllUserTypes(isAdmin);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		model.addAttribute("userTypes",userTypes);
		return new ModelAndView("userCreate","userform",userForm);
	}
	
	@RequestMapping(value = URLConstants.USER_SAVE,method=RequestMethod.POST)
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response, UserForm userForm,Model model,BindingResult result){
		UserTypeDto[] userTypes =null;
		boolean isAdmin = true;
	    boolean isLead = true;
	    HttpSession session = request.getSession();
	    String UserType=(String) session.getAttribute(SessionConstants.USER_TYPE);
		TeamDto[] teamDto = null ;
		List<String> successMessage = new ArrayList<String>();
		List<String> failureMessage = new ArrayList<String>();
		userForm.setCustomerId(getCustomerId(request));
		int customerId = getCustomerId(request);
		userForm.setFlag("save");
		try{
			teamDto= teamLogic.getTeams(customerId);
			if(UserType.equals(SessionConstants.USER_TYPE_LEAD))
				  userTypes=userLogic.getUserTypesForLead(isLead);
				else
				 userTypes=userLogic.getAllUserTypes(isAdmin);
			Object[] obj = new Object[1];
			obj[0] = userForm.getName();
			
			UserFormValidation formValidation =new UserFormValidation();
			formValidation.validate(userForm, result);
			if (result.hasErrors()) {
				model.addAttribute("userTypes", userTypes);
				return new ModelAndView("userCreate","userForm",userForm);
			}
			if(userForm.getLoginName() != null && !(userForm.getLoginName().trim().equals(""))){
				if (userLogic.validateLoginName(getCustomerId(request),userForm.getLoginName().trim())){
					model.addAttribute("loginNameMsg",resourceManager.getMessage("user.message.loginNameExist"));
					model.addAttribute("userTypes", userTypes);
					return new ModelAndView("userCreate","userForm",userForm);
				}
			}
			if(userForm.getEmployeeNumber() != null && !(userForm.getEmployeeNumber().trim().equals(""))){
				if (userLogic.validateEmployeeNumber(userForm.getEmployeeNumber().trim(), getCustomerId(request))){
					model.addAttribute("empNoMsg",resourceManager.getMessage("user.message.employeeNumberExist"));
					model.addAttribute("userTypes", userTypes);
					return new ModelAndView("userCreate","userForm",userForm);
				}
			}
			if(userForm.getPtn() != null && !(userForm.getPtn().trim().equals("")) ){
				if (userLogic.validatePhoneNumber(userForm.getPtn().trim(), getCustomerId(request))){
					model.addAttribute("phoneMsg",resourceManager.getMessage("user.message.phonenumberExists"));
					model.addAttribute("userTypes", userTypes);
					return new ModelAndView("userCreate","userForm",userForm);
				}
			}
			if(userForm.getImei() != null && !(userForm.getImei().trim().equals(""))){
				if (userLogic.validateIMEINumber(userForm.getImei(),getCustomerId(request))){
					model.addAttribute("imeiMsg",resourceManager.getMessage("user.message.IMEINumberExists")); 
					model.addAttribute("userTypes", userTypes);
					return new ModelAndView("userCreate","userForm",userForm);
				}
			}
			if(userForm.getEmailAddress() != null && !(userForm.getEmailAddress().trim().trim().equals(""))){
				if (userLogic.validateEmailIdAddress(userForm.getEmailAddress().trim(), getCustomerId(request))){
					model.addAttribute("emailMsg",resourceManager.getMessage("user.message.emailAddressExists"));
					model.addAttribute("userTypes", userTypes);
					return new ModelAndView("userCreate","userForm",userForm);
				}
			}
			userForm.setUserCreatedBy(getLoggedInUserId(request));
			long rows = userLogic.createUser(userForm);
			if(rows > 0){
				successMessage.add(resourceManager.getMessage("user.message.create.success",obj));
			}else{
				failureMessage.add(resourceManager.getMessage("user.message.create.failure",obj));
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		model.addAttribute("succMessage", successMessage);
		model.addAttribute("failureMsg", failureMessage);
		model.addAttribute("teamList",teamDto);
		model.addAttribute("userTypes",userTypes);
		model.addAttribute("itemsPerPageList",(PlatformUtil.getItemsPerPageList()));
		return new ModelAndView("userList","userForm",new UserForm());
	}
	@RequestMapping(value = URLConstants.USER_EDIT+"{userId}",method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("userId") long userId,HttpServletRequest request, HttpServletResponse response,UserForm userForm,Model model){
		UserDto dto = null;
		try{
			dto = userLogic.getUserDetails(userId);
			userForm.setUserId(dto.getId());
			userForm.setFirstName(dto.getFirstName());
			userForm.setLastName(dto.getLastName());
			userForm.setEmailAddress(dto.getEmailAddress());
			userForm.setEmployeeNumber(dto.getEmployeeNumber());
			userForm.setDesignation(dto.getDesignation());
			userForm.setCustomerId(dto.getCustomerId());
			userForm.setUserTypeId(dto.getUserTypeId());
			userForm.setLoginName(dto.getLoginName());
			userForm.setPtn(dto.getPtn());
			userForm.setImei(dto.getImei());
			userForm.setUserType(dto.getUserType());
			model.addAttribute(userForm);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return new ModelAndView("userEdit", "userForm", userForm);
	}
	
	@RequestMapping(value = URLConstants.USER_UPDATE,method=RequestMethod.POST)
	public ModelAndView update(HttpServletRequest request, HttpServletResponse response,UserForm userForm,Model model,BindingResult result){
		UserTypeDto[] userTypes =null;
		TeamDto[] teamDto = null ;
		boolean isAdmin = (getLoggedInUserTypeId(request)==SessionConstants.USER_TYPE_ID_ADMIN);
	    boolean isLead =  (getLoggedInUserTypeId(request)==SessionConstants.USER_TYPE_ID_LEAD);;
	    HttpSession session = request.getSession();
	    String UserType=(String) session.getAttribute(SessionConstants.USER_TYPE);
		List<String> successMessage = new ArrayList<String>();
		List<String> failureMessage = new ArrayList<String>();
		int customerId = getCustomerId(request);
		long rows = 0;
		try{
			teamDto= teamLogic.getTeams(customerId);
			if(UserType.equals(SessionConstants.USER_TYPE_LEAD))
				  userTypes=userLogic.getUserTypesForLead(isLead);
				else
				 userTypes=userLogic.getAllUserTypes(isAdmin);
			Object[] obj = new Object[1];
			obj[0] = userForm.getName();
			UserFormValidation formValidation =new UserFormValidation();
			formValidation.validate(userForm, result);
			if (result.hasErrors()) {
				model.addAttribute("userTypes",userTypes);
				return new ModelAndView("userEdit","userForm",userForm);
			}
			if(userForm.getEmployeeNumber() != null && !(userForm.getEmployeeNumber().trim().equals(""))){
				if (userLogic.validateEmployeeNumberEdit(userForm.getEmployeeNumber().trim(), getCustomerId(request), userForm.getUserId())){
					model.addAttribute("empNoMsg",resourceManager.getMessage("user.message.employeeNumberExist"));
					model.addAttribute("userTypes",userTypes);
					return new ModelAndView("userEdit","userForm",userForm);
				}
			}
			if(userForm.getPtn() != null && !(userForm.getPtn().trim().equals("")) ){
				if (userLogic.validatePhoneNumberEdit(userForm.getPtn().trim(), getCustomerId(request), userForm.getUserId())){
					model.addAttribute("phoneMsg",resourceManager.getMessage("user.message.phonenumberExists"));
					model.addAttribute("userTypes",userTypes);
					return new ModelAndView("userEdit","userForm",userForm);
				}
			}
			if(userForm.getImei() != null && !(userForm.getImei().trim().equals(""))){
				if (userLogic.validateIMEINumberEdit(userForm.getImei(), getCustomerId(request), userForm.getUserId())){
					model.addAttribute("imeiMsg",resourceManager.getMessage("user.message.IMEINumberExists")); 
					model.addAttribute("userTypes",userTypes);
					return new ModelAndView("userEdit","userForm",userForm);
				}
			}
			if(userForm.getEmailAddress() != null && !(userForm.getEmailAddress().trim().trim().equals(""))){
				if (userLogic.validateEmailIdEdit(userForm.getEmailAddress().trim(), getCustomerId(request), userForm.getUserId())){
					model.addAttribute("emailMsg",resourceManager.getMessage("user.message.emailAddressExists"));
					model.addAttribute("userTypes",userTypes);
					return new ModelAndView("userEdit","userForm",userForm);
				}
			}
			userForm.setId(userForm.getUserId());
			rows = userLogic.updateUser(userForm);
			if(rows > 0){
				successMessage.add(resourceManager.getMessage("user.message.update.success",obj));
			}
			else{
				failureMessage.add(resourceManager.getMessage("user.message.update.failure",obj));
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		model.addAttribute("succMessage", successMessage);
		model.addAttribute("failureMsg", failureMessage);
		model.addAttribute("teamList",teamDto);
		model.addAttribute("itemsPerPageList",(PlatformUtil.getItemsPerPageList()));
		model.addAttribute("userTypes",userTypes);
		return new ModelAndView("userList","userForm",new UserForm());
	}
	
	@RequestMapping(value = URLConstants.USER_DOWNLOAD_SAMPLE, method={RequestMethod.GET,RequestMethod.POST})
	public void downloadSample(HttpServletRequest request, HttpServletResponse response, UserForm userForm) throws IOException{
		final int fourKiloByte = 4*1024;
		FileInputStream fis = null;
		try {
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("application/octet-stream");
			String filePath = "/views/excel/UploadUser.xlsx";
			String pathName=request.getServletContext().getRealPath(filePath);
			response.setHeader("CONTENT-DISPOSITION", "attachment; filename=UploadUser.xlsx");
			fis = new FileInputStream(pathName);
			byte[] buf = new byte[fourKiloByte]; // 4K buffer
			int bytesRead;
			while ((bytesRead = fis.read(buf)) != -1)
				out.write(buf, 0, bytesRead);
			out.close();
			fis.close();
		}catch(Exception ex){
			LOG.error(ErrorLogHandler.getStackTraceAsString(ex));
		}
	}

	@RequestMapping(value = URLConstants.USER_UPLOAD, method=RequestMethod.GET)
	public ModelAndView showUserUpload(HttpServletRequest request, HttpServletResponse response, Model model, UserForm userForm){
		return new ModelAndView("userUpload","userForm", userForm );
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = URLConstants.USER_UPLOAD_FILE, method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response,UserForm userForm, BindingResult results, Model model,	BindingResult result) throws IOException {
		List<String> successMessage = new ArrayList<String>();
		List<String> failureMessage = new ArrayList<String>();
		boolean check = true;
		String header=null;
		int customerId=getCustomerId(request);
		int checkId =0;
		try{
			Object[] obj = new Object[2];
			if (PlatformUtil.isEmpty(file.getOriginalFilename()) || file.getSize() == 0){
				failureMessage.add(resourceManager.getMessage("user.label.nofile"));
				model.addAttribute("userNameMsg", failureMessage);
				return new ModelAndView("userUpload");
			}

			boolean headerCheck = userLogic.checkUserColumnHeader(file.getBytes());
			if(!headerCheck){
				failureMessage.add(resourceManager.getMessage("common.message.invalidFileFormat"));
				model.addAttribute("userNameMsg", failureMessage);
				return new ModelAndView("userUpload");
			}

			ExcelParser parser = new ExcelParser();
			List<Map<String, String>> rows = parser.parse(file.getBytes());
			if (rows.isEmpty()) {
				failureMessage.add(resourceManager.getMessage("user.label.empty"));
				model.addAttribute("userNameMsg", failureMessage);
				return new ModelAndView("userUpload");
			}
			Iterator<Map<String, String>> iterator = rows.iterator();
			int rowCount = 1;
			while (iterator.hasNext()) {
				rowCount++;
				try{
					Map<String, String> map = (HashMap<String, String>) iterator.next();
					String fname = (String) map.get("First Name");
					String lname= (String) map.get("Last Name");
					String userType= (String) map.get("User Type");
					String empNo = (String) map.get("Employee Number");
					String emailId = (String) map.get("Email Id");
					String mobileNo = (String) map.get("Mobile Number");
					String loginId = (String) map.get("Login Id");
					String password = (String) map.get("Password");
					String designation = (String) map.get("Designation");
					String imei = (String) map.get("IMEI");
					fname = (fname != null)?fname.trim():"";
					lname = (lname != null)?lname.trim():"";
					userType = (userType != null)?userType.trim():"";
					empNo = (empNo != null)?empNo.trim():"";
					emailId = (emailId != null)?emailId.trim():"";
					mobileNo = (mobileNo != null)? mobileNo.trim():"";
					loginId = (loginId != null)?loginId.trim():"";
					password= (password != null)?password.trim():"";
					designation= (designation != null)?designation.trim():"";
					imei= (imei != null)? imei.trim():"";
					obj[0]=rowCount;
					if(fname.equals("")|| lname.equals("") || userType.equals("") || empNo.equals("") || emailId.equals("") || mobileNo.equals("") || imei.equals("") || loginId.equals("") || password.equals("") || designation.equals("")){
						if(fname.equals("")){
							failureMessage.add(resourceManager.getMessage("user.file.errors.firstname.required",obj));
							model.addAttribute("userNameMsg", failureMessage);
						}
						if(lname.equals("")){
							failureMessage.add(resourceManager.getMessage("user.file.errors.lastname.required",obj));
							model.addAttribute("userNameMsg", failureMessage);
						}
						if(userType.equals("")){
							failureMessage.add(resourceManager.getMessage("user.file.errors.userType.required",obj));
							model.addAttribute("userNameMsg", failureMessage);
						}
						if(empNo.equals("")){
							failureMessage.add(resourceManager.getMessage("user.file.errors.employeenumber.required",obj));
							model.addAttribute("userNameMsg", failureMessage);
						}
						if(emailId.equals("")){
							failureMessage.add(resourceManager.getMessage("user.file.errors.email.required",obj));
							model.addAttribute("userNameMsg", failureMessage);
						}
						if(mobileNo.equals("")){
							failureMessage.add(resourceManager.getMessage("user.file.errors.phonenumber.required",obj));
							model.addAttribute("userNameMsg", failureMessage);
						}
						if(imei.equals("")){
							failureMessage.add(resourceManager.getMessage("user.errors.imei.required",obj));
							model.addAttribute("userNameMsg", failureMessage);
						}
						if(loginId.equals("")){
							failureMessage.add(resourceManager.getMessage("user.file.errors.loginid.required",obj));
							model.addAttribute("userNameMsg", failureMessage);
						}
						if(password.equals("")){
							failureMessage.add(resourceManager.getMessage("user.file.errors.password.required",obj));
							model.addAttribute("userNameMsg", failureMessage);
						}
						if(designation.equals("")){
							failureMessage.add(resourceManager.getMessage("user.file.errors.designation.required",obj));
							model.addAttribute("userNameMsg", failureMessage);
						}
					}else{
						UserForm form = new UserForm();
						form.setFirstName(fname);
						form.setLastName(lname);
						form.setUserType(userType);
						form.setUserTypeId(SessionConstants.USER_TYPE_ID_USER);
						form.setEmployeeNumber(empNo);
						form.setEmailAddress(emailId);
						form.setPtn(mobileNo);
						form.setLoginName(loginId);
						form.setPassword(password);
						form.setDesignation(designation);
						form.setCustomerId(customerId);	
						form.setImei(imei);	
						UserFormValidation formvalidation = new UserFormValidation();
						obj[0]=rowCount;
						boolean isValid= formvalidation.validation(form);
						boolean loginName = userLogic.validateLoginName(customerId,form.getLoginName().trim());
						boolean employeeNo = userLogic.validateEmployeeNumber(form.getEmployeeNumber().trim(), customerId);
						boolean emailAddress = userLogic.validateEmailIdAddress(form.getEmailAddress().trim(), customerId);
						boolean mobileNumber =userLogic.validatePhoneNumber(form.getPtn().trim(), customerId);
						boolean imeiNo = userLogic.validateIMEINumber(form.getImei(),customerId);
						if (!isValid) {
							obj[0]=rowCount;
							obj[1]=form.getKeyValue();
							if (form.getValidateType() == 2){
								failureMessage.add(resourceManager.getMessage("user.file.error.incorrectdata",obj));
							}
							model.addAttribute("userNameMsg", failureMessage);
						}
						else if(loginName || employeeNo ||  emailAddress || mobileNumber || imeiNo){
							obj[0]=rowCount;
							if(employeeNo){
								failureMessage.add(resourceManager.getMessage("user.file.message.employeeNumberExist", obj));
								model.addAttribute("userNameMsg", failureMessage);
							}
							if(emailAddress){
								failureMessage.add(resourceManager.getMessage("user.file.message.emailAddressExists", obj));
								model.addAttribute("userNameMsg", failureMessage);
							}
							if(mobileNumber){
								failureMessage.add(resourceManager.getMessage("user.file.message.phonenumberExists", obj));
								model.addAttribute("userNameMsg", failureMessage);
							}
							if(loginName){
								failureMessage.add(resourceManager.getMessage("user.file.message.loginNameExist", obj));
								model.addAttribute("userNameMsg", failureMessage);
							}
							if(imeiNo){
								failureMessage.add(resourceManager.getMessage("user.file.message.imeiExist", obj));
								model.addAttribute("userNameMsg", failureMessage);
							}
						}
						else{
							obj[0]=rowCount;
							form.setUserCreatedBy(getLoggedInUserId(request));
							long row =userLogic.createUser(form);
							if(row > 0){
								successMessage.add(resourceManager.getMessage("user.file.message.upload.success",obj));
							}else{
								failureMessage.add(resourceManager.getMessage("user.file.message.upload.failure",obj));
								model.addAttribute("userNameMsg", failureMessage);
							}
						}
					}
				}
				catch(Exception e){
					LOG.error(ErrorLogHandler.getStackTraceAsString(e));
				}
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		model.addAttribute("succMessage", successMessage);
		return new ModelAndView("userUpload");
	}
	
	@RequestMapping(value = URLConstants.USER_PASSWORD_CHANGE,method=RequestMethod.GET)
	public ModelAndView changePassword(HttpServletRequest request, HttpServletResponse response, UserForm userForm, Model model){
		return new ModelAndView("userPassChange");
	}

	@RequestMapping(value = URLConstants.USER_PASSWORD_UPDATE,method=RequestMethod.POST)
	public ModelAndView updatePassword(HttpServletRequest request, HttpServletResponse response, UserForm userForm, Model model, BindingResult result){
		List<String> successMessage = new ArrayList<String>();
		List<String> errMessage = new ArrayList<String>();
		int customerId = getCustomerId(request);
		try{
			UserFormValidation formValidation = new UserFormValidation();
			formValidation.validatePassword(userForm, result);
			if (result.hasErrors()) {
				return new ModelAndView("userPassChange","userForm",new UserForm());
			}
			int check = administrationLogic.authorizeUser(customerId, getLoggedInUserId(request), userForm.getOldPassword());
			if(check == 0){
				errMessage.add(resourceManager.getMessage("user.errors.current.password.wrong"));
			}else{
				int rows = userLogic.userChangePassword(getLoggedInUserId(request), userForm.getPassword());
				if(rows > 0)
					successMessage.add(resourceManager.getMessage("user.message.password.update"));
				else
					errMessage.add(resourceManager.getMessage("user.errors.password.update.failed"));
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		model.addAttribute("succMessage", successMessage);
		model.addAttribute("errMessage", errMessage);
		return new ModelAndView("userPassChange","userForm",new UserForm());
	}
}
