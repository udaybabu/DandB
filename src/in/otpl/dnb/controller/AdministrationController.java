package in.otpl.dnb.controller;

import in.otpl.dnb.logic.AdministrationLogic;
import in.otpl.dnb.logic.CommonLogic;
import in.otpl.dnb.user.TeamLogic;
import in.otpl.dnb.user.UserDto;
import in.otpl.dnb.user.UserLogic;
import in.otpl.dnb.user.UserLoginForm;
import in.otpl.dnb.util.ConfigManager;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;
import in.otpl.dnb.util.SessionConstants;
import in.otpl.dnb.util.URLConstants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdministrationController {

	private static final Logger LOG = Logger.getLogger(AdministrationController.class);

	@Autowired
	private AdministrationLogic administrationLogic;
	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private ConfigManager configManager;
	@Autowired
	private CommonLogic commonLogic;
	@Autowired
	private TeamLogic teamLogic;
	@Autowired
	private UserLogic userLogic;

	/* Login */
	@RequestMapping(value=URLConstants.LOGIN,method=RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request,HttpServletResponse response,Model model){
		LOG.info("Client IP: "+PlatformUtil.getClientIpAddress(request));
		UserLoginForm userLoginForm = new UserLoginForm();
		model.addAttribute(userLoginForm);
		return new ModelAndView("login", "userLoginForm", userLoginForm);
	}

	@RequestMapping(value=URLConstants.LOGIN_AUTH,method={RequestMethod.POST,RequestMethod.GET})
	public String loginAuth(HttpServletRequest request,HttpServletResponse response,Model model,UserLoginForm userLoginForm){
		try{
			String userName = userLoginForm.getUserName().trim();
			String password = userLoginForm.getPassword().trim();
			UserDto user = administrationLogic.authorize(configManager.getDnbCustId(), userName,password);
			if(user != null && user.getId() > 0 && user.getStatus() == SessionConstants.STATUS_ACTIVE){
				HttpSession session = request.getSession(true);
				setUserDetailsInSession(session, user);
				session.setAttribute(SessionConstants.IN_SESSION, true);
				return "redirect:"+URLConstants.DASHBOARD;
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		String errorMessage = resourceManager.getMessage("errors.login.invalid");
		LOG.info(errorMessage);
		model.addAttribute("message", errorMessage);
		model.addAttribute(userLoginForm);
		return "login";
	}

	private void setUserDetailsInSession(HttpSession session, UserDto user) {
		LOG.info("setUserDetailsInSession");
		LOG.info("Customer Id: "+user.getCustomerId());
		LOG.info("User Id: " +user.getId());
		LOG.info("Name: " +user.getName());
		LOG.info("User Type: "+user.getUserType());
		LOG.info("EmailId: " +user.getEmailAddress());

		session.setAttribute(SessionConstants.CUSTOMER_ID, user.getCustomerId());
		session.setAttribute(SessionConstants.CUSTOMER_NAME, "D&B Mobility");
		session.setAttribute(SessionConstants.USER_ID, user.getId());
		session.setAttribute(SessionConstants.USER_FIRST_NAME, user.getFirstName());
		session.setAttribute(SessionConstants.USER_LAST_NAME, user.getLastName());
		session.setAttribute(SessionConstants.DISPLAY_NAME, user.getName());
		session.setAttribute(SessionConstants.LOGIN_NAME, user.getLoginName());
		session.setAttribute(SessionConstants.USER_TYPE, user.getUserType());
		session.setAttribute(SessionConstants.USER_TYPE_ID, user.getUserTypeId());
		session.setAttribute(SessionConstants.TEAM_ID, user.getTeamId());
	}
	
	@RequestMapping(value=URLConstants.LOGOUT,method=RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response,Model model){
		LOG.info("Client IP: "+PlatformUtil.getClientIpAddress(request));
		Cookie[] cookies = request.getCookies();
		if (cookies != null){
			for (int i = 0; i < cookies.length; i++) {
				if ( !(cookies[i].getValue().equals(""))){
					Cookie cookie = new Cookie(cookies[i].getName(),"");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
		HttpSession session = request.getSession();
		session.setAttribute(SessionConstants.IN_SESSION, false);
		session.removeAttribute(SessionConstants.CUSTOMER_NAME);
		if(session.getAttribute(SessionConstants.DISPLAY_NAME) != null)
			LOG.info(session.getAttribute(SessionConstants.DISPLAY_NAME)+" Logged out");
		else
			LOG.info("Logged out due to Session Timed out");
		session.invalidate();
		return "redirect:"+URLConstants.LOGIN;
	}

}