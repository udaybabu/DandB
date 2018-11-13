package in.otpl.dnb.controller;

import in.otpl.dnb.util.PlatformUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HTTPErrorHandler{

	private static final Logger LOG = Logger.getLogger(HTTPErrorHandler.class);
	
	@RequestMapping(value="/400")
	public String error400(HttpServletRequest request, HttpServletResponse response, Exception ex){
		LOG.error("HTTP Status 400 on "+PlatformUtil.getClientIpAddress(request));
		return "err-400";
	}
	
	@RequestMapping(value="/401")
	public String error401(HttpServletRequest request, HttpServletResponse response, Exception ex){
		LOG.error("HTTP Status 401 on "+PlatformUtil.getClientIpAddress(request));
		return "err-401";
	}
	
	@RequestMapping(value="/404")
	public String error404(HttpServletRequest request, HttpServletResponse response, Exception ex){
		LOG.error("HTTP Status 404 on "+PlatformUtil.getClientIpAddress(request));
		return "err-404";
	}
	
	@RequestMapping(value="/405")
	public String error405(HttpServletRequest request, HttpServletResponse response, Exception ex){
		LOG.error("HTTP Status 405 on "+PlatformUtil.getClientIpAddress(request));
		return "err-405";
	}

	@RequestMapping(value="/500")
	public String error500(HttpServletRequest request, HttpServletResponse response, Exception ex){
		LOG.error("HTTP Status 500 on "+PlatformUtil.getClientIpAddress(request));
		return "err-500";
	}
	
	@RequestMapping(value="/502")
	public String error502(HttpServletRequest request, HttpServletResponse response, Exception ex){
		LOG.error("HTTP Status 502 on "+PlatformUtil.getClientIpAddress(request));
		return "err-502";
	}
	
	@RequestMapping(value="/503")
	public String error503(HttpServletRequest request, HttpServletResponse response, Exception ex){
		LOG.error("HTTP Status 503 on "+PlatformUtil.getClientIpAddress(request));
		return "err-503";
	}

}