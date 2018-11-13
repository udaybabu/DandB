package in.otpl.dnb.interceptor;

import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ServerConstants;
import in.otpl.dnb.util.SessionConstants;
import in.otpl.dnb.util.URLConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class BaseController extends HandlerInterceptorAdapter {

	private static final Logger LOG = Logger.getLogger(BaseController.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		LOG.info("Client IP: "+PlatformUtil.getClientIpAddress(request));
		String uri = request.getRequestURI();
		LOG.info("URI: "+uri);
		boolean check = true;
		try {
			HttpSession session = request.getSession(true);
			if(session.getAttribute(SessionConstants.IN_SESSION) != null && (boolean) session.getAttribute(SessionConstants.IN_SESSION)){
				
				if(uri.contains(URLConstants.DASHBOARD) && getLoggedInUserId(request) > 0){

				}else if(uri.contains(URLConstants.USER) && getLoggedInUserTypeId(request) == SessionConstants.USER_TYPE_ID_ADMIN ){
				
				}else if(uri.contains(URLConstants.TEAM) && getLoggedInUserTypeId(request) == SessionConstants.USER_TYPE_ID_ADMIN ){

				}else if(uri.contains(URLConstants.REPORT_USER_TRACKING) && ((getLoggedInUserTypeId(request) == SessionConstants.USER_TYPE_ID_ADMIN )) || (getLoggedInUserTypeId(request) == SessionConstants.USER_TYPE_ID_LEAD )){
					
				}else if(uri.contains(URLConstants.ENQUIRY)  && getLoggedInUserId(request) > 0){
					
				}else if(uri.contains(URLConstants.REPORT_LOGIN)  && getLoggedInUserTypeId(request) == SessionConstants.USER_TYPE_ID_ADMIN ){
					
				}else if(uri.contains(URLConstants.REPORT_ALLUSERMAP)  && getLoggedInUserTypeId(request) == SessionConstants.USER_TYPE_ID_ADMIN ){
					
				}else{
					check = false;
				}
			}else{
				check = false;
			}
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}

		if(!check)
			response.sendRedirect(request.getContextPath()+ "/logout");
		return check;
	}

	public int getCustomerId(HttpServletRequest request){
		Integer customerId = (Integer)getSessionAttribute(request, SessionConstants.CUSTOMER_ID);
		return customerId == null ? 0 : customerId;
	}

	public int getAccountTypeId(HttpServletRequest request){
		Integer accId = (Integer)getSessionAttribute(request, SessionConstants.ACCOUNT_TYPE_ID);
		return accId == null ? ServerConstants.ACCOUNT_TYPE_DEMO : accId;
	}

	public long getLoggedInUserId(HttpServletRequest request){
		Long userId = (Long)getSessionAttribute(request, SessionConstants.USER_ID);
		return userId == null ? 0 : userId.longValue();		
	}

	public String getLoggedInUserLastName(HttpServletRequest request){
		return (String)getSessionAttribute(request, SessionConstants.USER_LAST_NAME);
	}

	public String getLoggedInUserFullName(HttpServletRequest request){
		return (String)getSessionAttribute(request, SessionConstants.DISPLAY_NAME);
	}

	public String getLoggedInUserFName(HttpServletRequest request){
		return (String)getSessionAttribute(request, SessionConstants.USER_FIRST_NAME);
	}

	public int getLoggedInUserTypeId(HttpServletRequest request){
		Integer usertypeId = (Integer)getSessionAttribute(request, SessionConstants.USER_TYPE_ID);
		return usertypeId == null ? 0 : usertypeId;
	}

	public String getLoggedInUserType(HttpServletRequest request){
		return (String)getSessionAttribute(request, SessionConstants.USER_TYPE);
	}

	public long getLoggedInUserTeamId(HttpServletRequest request){
		Long teamId = (Long)getSessionAttribute(request, SessionConstants.TEAM_ID);
		return teamId == null ? 0 : teamId;
	}

	private Object getSessionAttribute(HttpServletRequest request, String param){
		return request.getSession(true).getAttribute(param);
	}

	public boolean isClient(HttpServletRequest request){
		Integer client = (Integer)getSessionAttribute(request, ServerConstants.WEB_CONFIGURATION_CLIENT);
		return client == 1 ? true : false;
	}
	public String getLoginName(HttpServletRequest request){
		return (String)getSessionAttribute(request, SessionConstants.LOGIN_NAME);
	}

}
