package in.otpl.dnb.user;
import in.otpl.dnb.interceptor.BaseController;
import in.otpl.dnb.user.TeamLogic;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.ExcelSheetDataBean;
import in.otpl.dnb.util.ExcelUtil;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.URLConstants;
import in.otpl.dnb.user.TeamDto;
import in.otpl.dnb.user.TeamForm;
import in.otpl.dnb.user.UserDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

@Controller
public class TeamController extends BaseController {

	private static final Logger LOG = Logger.getLogger(TeamController.class);

	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private TeamLogic teamLogic;
	@Autowired
	private UserLogic userLogic;

	@RequestMapping(value = URLConstants.TEAM_CREATE, method = RequestMethod.GET)
	public ModelAndView page(HttpServletRequest request, HttpServletResponse response, Model model, TeamForm teamForm) {
		UserDto[] teamLeads = userLogic.getTeamLeads(getCustomerId(request));
		UserDto[] availableUsers = userLogic.getAvailableUsersTeams(getCustomerId(request));
		model.addAttribute("availableUsers", availableUsers);
		model.addAttribute("teamLeads", teamLeads);
		return new ModelAndView("teamCreate", "teamForm", teamForm);
	}

	@RequestMapping(value = URLConstants.TEAM_SAVE, method = { RequestMethod.POST })
	public ModelAndView add(HttpServletRequest request, HttpServletResponse response, Model model, TeamForm teamForm,
			BindingResult result) {
		List<String> succMessage = new ArrayList<String>();
		List<String> errMessage = new ArrayList<String>();
		int customerId = getCustomerId(request);
		TeamDto teamDto = new TeamDto();
		UserDto[] teamLeads = userLogic.getTeamLeads(customerId);
		model.addAttribute("teamLeads", teamLeads);
		try {
			teamForm.setCustomerId(customerId);
			TeamValidation formValidation = new TeamValidation();
			formValidation.validate(teamForm, result);
			if (result.hasErrors()) {
				return new ModelAndView("teamCreate", "teamForm", teamForm);
			}

			if (teamLogic.checkAvailabilityTeam(teamForm.getTeamName().trim(), teamForm.getCustomerId()) > 0) {
				errMessage.add(resourceManager.getMessage("team.error.already.exist"));
				model.addAttribute("errMessage", errMessage);
				return new ModelAndView("teamList", "teamForm", teamForm);
			}
			teamDto = teamLogic.createTeam(customerId, teamForm);
			if (teamDto.getId() > 0) {
				Object[] obj = new Object[1];
				obj[0] = teamDto.getName();
				succMessage.add(resourceManager.getMessage("team.message.createSuccess", obj));
				model.addAttribute("succMessage", succMessage);
				model.addAttribute("itemPerPageList", PlatformUtil.getItemsPerPageList());
				return new ModelAndView("teamList", "teamForm", teamForm);
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		Object[] obj = new Object[1];
		obj[0] = teamDto.getName();
		errMessage.add(resourceManager.getMessage("team.error.create.failed", obj));
		model.addAttribute("errMessage", errMessage);
		return new ModelAndView("teamCreate", "teamForm", teamForm);
	}

	@RequestMapping(value = URLConstants.TEAM_EDIT + "/{id}", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView edit(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response, Model model, TeamForm teamForm) {
		int customerId = getCustomerId(request);
		TeamDto teamDto = teamLogic.getTeamById(id, customerId);
		teamForm.setTeamId(teamDto.getId());
		teamForm.setTeamName(teamDto.getName());
		teamForm.setTeamLeadId(teamDto.getLeadId());

		UserDto[] teamLeads = userLogic.getTeamLeads(customerId);
		UserDto[] selectedUsers = userLogic.getSelectedUsersByTeamId(customerId, id);
		UserDto[] availableUsers = null;
		String users = "";
		if (selectedUsers != null && selectedUsers.length > 0) {
			for (UserDto user : selectedUsers) {
				if (users != "")
					users += ",";
				users += user.getId();
			}
		}

		if (teamForm.getTeamLeadId() > 0) {
			if (users != "")
				users += ",";
			users += teamForm.getTeamLeadId();
		}

		if (users != "") {
			availableUsers = userLogic.getAvailableUsersTeams(customerId, users);
		} else {
			availableUsers = userLogic.getAvailableUsersTeams(customerId);
		}
		model.addAttribute("teamLeads", teamLeads);
		model.addAttribute("availableUsers", availableUsers);
		model.addAttribute("selectedUsers", selectedUsers);

		return new ModelAndView("teamEdit", "teamForm", teamForm);
	}

	@RequestMapping(value = URLConstants.TEAM_UPDATE, method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView update(HttpServletRequest request, HttpServletResponse response, Model model, TeamForm teamForm,
			BindingResult result) {
		List<String> succMessage = new ArrayList<String>();
		List<String> errMessage = new ArrayList<String>();
		int customerId = getCustomerId(request);
		TeamDto teamDto = new TeamDto();
		try {
			TeamValidation formValidation = new TeamValidation();
			formValidation.validate(teamForm, result);
			if (result.hasErrors()) {
				return new ModelAndView("teamEdit", "teamForm", teamForm);
			}
			if (teamLogic.checkAvailabilityTeamEdit(teamForm.getTeamId(), customerId,
					teamForm.getTeamName().trim()) > 0) {
				errMessage.add(resourceManager.getMessage("team.error.already.exist"));
				model.addAttribute("errMessage", errMessage);
				return new ModelAndView("teamEdit", "teamForm", teamForm);
			}

			teamDto = teamLogic.updateTeam(customerId, teamForm);
			if (teamDto.getId() > 0) {
				Object[] obj = new Object[1];
				obj[0] = teamDto.getName();
				succMessage.add(resourceManager.getMessage("team.message.updateSuccess", obj));
				model.addAttribute("succMessage", succMessage);
				model.addAttribute("itemPerPageList", PlatformUtil.getItemsPerPageList());
				return new ModelAndView("teamList", "teamForm", teamForm);
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		Object[] obj = new Object[1];
		obj[0] = teamDto.getName();
		errMessage.add(resourceManager.getMessage("team.Updation.Failed", obj));
		model.addAttribute("errMessage", errMessage);
		return new ModelAndView("teamEdit", "teamForm", teamForm);
	}

	@RequestMapping(value = URLConstants.TEAM, method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView view(HttpServletRequest request, HttpServletResponse response, Model model, TeamForm teamForm) {
		model.addAttribute("itemPerPageList", PlatformUtil.getItemsPerPageList());
		return new ModelAndView("teamList", "teamForm", teamForm);
	}

	@RequestMapping(value = URLConstants.TEAM_LIST, method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, Model model, TeamForm teamForm) {
		int page = 1, rowcount = 0;
		return lists(teamForm, model, page, rowcount, request);
	}

	@RequestMapping(value = URLConstants.TEAM_LIST + "/{page}/{rowcount}", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, Model model, TeamForm teamForm,
			@PathVariable("page") int page, @PathVariable("rowcount") int rowcount) {
		return lists(teamForm, model, page, rowcount, request);
	}

	@SuppressWarnings("unchecked")
	private ModelAndView lists(TeamForm teamForm, Model model, int page, int rowcount, HttpServletRequest request) {
		int records = 0;
		List<TeamDto> teamlist = new ArrayList<TeamDto>();
		UserDto[] availableUsers = userLogic.getAvailableUsersTeams(getCustomerId(request));
		model.addAttribute("availableUsers", availableUsers);
		try {
			int customerId = getCustomerId(request);
			teamForm.setOffset(teamForm.getRowcount() * (page - 1));
			Map<String, Object> map = teamLogic.getTeamList(customerId, teamForm);
			if (map != null) {
				teamlist = (List<TeamDto>) map.get("data");
				records = (Integer) map.get("totalRows");
						     }
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		model.addAttribute("data", teamlist);
		model.addAttribute("itemPerPageList", PlatformUtil.getItemsPerPageList());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalRows", records);
		model.addAttribute("rowcount", teamForm.getRowcount());
		return new ModelAndView("teamList", "teamForm", teamForm);
	}

	@RequestMapping(value =URLConstants.TEAM_DOWNLOAD_XLSX, method = RequestMethod.GET)
	public void excel(HttpServletRequest request,HttpServletResponse response,Model model,TeamForm teamForm) throws IOException{
		teamForm.setRowcount(0);
		int customerId = getCustomerId(request);
		model.addAttribute(teamForm);
		Map<String, Object> map= teamLogic.getTeamList(customerId,teamForm);
		@SuppressWarnings("unchecked")
		List<TeamDto> teamlist = (List<TeamDto>) map.get("data");
		Map<String, String> columnHeadings = new LinkedHashMap<String, String>();
		columnHeadings.put("name", resourceManager.getMessage("team.label.teamName"));
		columnHeadings.put("leadName", resourceManager.getMessage("team.label.teamLead"));
		columnHeadings.put("members", resourceManager.getMessage("team.label.memberName"));
		columnHeadings.put("strStatus", resourceManager.getMessage("team.label.status"));

		String sheetHeading = "TEAM(s)";
		String sheetName = "TEAM(s)";

		ExcelSheetDataBean sheetDataBean = new ExcelSheetDataBean(teamlist, sheetHeading, sheetName, columnHeadings);
		List<ExcelSheetDataBean> sheetDataList = new ArrayList<ExcelSheetDataBean>();

		sheetDataList.add(sheetDataBean);
		response.setContentType("application/octet");
		response.setHeader("Content-disposition", "attachment; filename=TeamList.xlsx");

		ExcelUtil excelUtil = new ExcelUtil();
		excelUtil.generateExcel(sheetDataList, response.getOutputStream());
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value =URLConstants.TEAM_DOWNLOAD_PDF, method = RequestMethod.GET)
	public void pdf(Model model,HttpServletRequest request,HttpServletResponse response,TeamForm teamForm){
		try {
			teamForm.setRowcount(0);
			int customerId = getCustomerId(request);
			model.addAttribute(teamForm);
			Map<String, Object> map = teamLogic.getTeamList(customerId,teamForm);
			List<TeamDto> teamlist = (List<TeamDto>) map.get("data");
			Document document = new Document(PageSize.A4.rotate());
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=TeamList.pdf" );
			PdfWriter.getInstance(document, response.getOutputStream());
			String [] headers={resourceManager.getMessage("team.label.teamName"),
					resourceManager.getMessage("team.label.teamLead"),
					resourceManager.getMessage("team.label.memberName"),
					resourceManager.getMessage("team.label.status")};
			PdfPTable table = new PdfPTable(headers.length);
			PdfPCell cell = new PdfPCell();
			cell.setPhrase(new Phrase("TEAM(s)".toUpperCase(),FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD)));
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
			for(int i=0;i<teamlist.size();i++){
				table.addCell(teamlist.get(i).getName());
				table.addCell(teamlist.get(i).getLeadName());
				table.addCell(teamlist.get(i).getMembers());
				table.addCell(teamlist.get(i).getStrStatus());
			}
			document.open();
			document.add(table);
			document.close();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}

	@RequestMapping(value = URLConstants.TEAM_STATUS, method = {RequestMethod.POST,RequestMethod.GET})
	public String status(@RequestParam("name") String name,@RequestParam("id") int id,@RequestParam("status") int status,HttpServletRequest request,HttpServletResponse response, TeamForm teamForm,Model model ) {
		int customerId = getCustomerId(request);
		UserDto[] selectedUsers = userLogic.getSelectedUsersByTeamId(customerId, id);
		String users = "";
		if(selectedUsers != null && selectedUsers.length > 0){
			for(UserDto user: selectedUsers){
				if(users != "")
					users += ",";
				users += user.getId();
			}
		}
		try{
			int rows = 0;
			if(id > 0) {
				rows = teamLogic.changeStatus(id, status,users);
			}
			if(rows > 0) {
				status = (status == 0)?1:0;
			}
				response.getWriter().print(status);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return null;
	}
}
