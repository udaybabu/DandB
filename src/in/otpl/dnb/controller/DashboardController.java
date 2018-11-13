package in.otpl.dnb.controller;

import in.otpl.dnb.gw.CurrentUserTracking;
import in.otpl.dnb.interceptor.BaseController;
import in.otpl.dnb.report.ReportLogic;
import in.otpl.dnb.user.UserLogic;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.URLConstants;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardController extends BaseController{

	private static final Logger LOG = Logger.getLogger(DashboardController.class);
	
	@Autowired
	private UserLogic userLogic;
	@Autowired
	private ReportLogic reportLogic;
	@Autowired
	private ResourceManager resourceManager;

	@RequestMapping(value=URLConstants.DASHBOARD,method=RequestMethod.GET)
	public ModelAndView dashboard(HttpServletRequest request,HttpServletResponse response, Model model){
		try{
			int customerId = getCustomerId(request);
			long userId = getLoggedInUserId(request);
			int userTypeId = getLoggedInUserTypeId(request);
			if(userId > 0){
			Map<String, Object> userMap = reportLogic.getAllUserTrackingReport(customerId, userId, userTypeId);
				@SuppressWarnings("unchecked")
				List<CurrentUserTracking> allUserMapData = (List<CurrentUserTracking>) userMap.get("data");
				model.addAttribute("allUserMapData", allUserMapData);
			}
				
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return new ModelAndView("dashboard");
	}
}
