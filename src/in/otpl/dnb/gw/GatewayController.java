package in.otpl.dnb.gw;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import in.otpl.dnb.util.ClientRequestConstants;
import in.otpl.dnb.util.ConfigManager;
import in.otpl.dnb.util.Encryptor;
import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.InvalidSessionException;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ProtocolMismatchException;
import in.otpl.dnb.util.URLConstants;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

@Controller
public class GatewayController {

	private static final Logger LOG = Logger.getLogger(GatewayController.class);
	
	@Autowired
	private GatewayLogic gatewayLogic;
	@Autowired
	private ConfigManager configManager;
	
	@RequestMapping(value = URLConstants.MOBILE_GATEWAY, method={RequestMethod.POST})
	public JSON execute(HttpServletRequest request, HttpServletResponse response,@RequestParam("jsonString") String jsonString) { 
		LOG.info("Client IP: "+PlatformUtil.getClientIpAddress(request));
		String uri = request.getRequestURI();
		LOG.info("URI: "+uri);
		LOG.info("JSON::"+jsonString);
		JSONObject jsonRequestObject = getJSONObject(jsonString,response);
		if(jsonRequestObject != null){
			boolean decryptResponse = false;
			if(jsonRequestObject.has(ClientRequestConstants.DECRYPT_RESPONSE))
				decryptResponse = "yes".equalsIgnoreCase(getParam(request, jsonRequestObject, ClientRequestConstants.DECRYPT_RESPONSE));
			String callType = "", callName = "";
			try {
				String protocolId = getParam(request, jsonRequestObject, ClientRequestConstants.PROTOCOL_ID);
				String clientVersion = getParam(request, jsonRequestObject, ClientRequestConstants.CLIENT_VERSION);
				callType = getParam(request, jsonRequestObject, ClientRequestConstants.CALL_TYPE);
				callName = getParam(request, jsonRequestObject, ClientRequestConstants.CALL_NAME);

				if(!callName.equals(ClientRequestConstants.LOGO_GET)){
					response.setContentType("text/html;charset=UTF-8");
				} else {
					response.setContentType("image/jpeg");
				}
				gatewayLogic.validateHeader(protocolId, clientVersion, callType, callName);
			} catch(ProtocolMismatchException pME) {
				Map<String, String> errorResponse = new HashMap<String, String>(2);
				errorResponse.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_INVALID_CALL);
				errorResponse.put(ClientRequestConstants.MESSAGE_STRING, pME.getErrorMessage());
				String errorResponseString = JSONObject.fromObject(errorResponse).toString();
				try {
					writeOutput(errorResponseString, response, decryptResponse);
				} catch (IOException e) {
					LOG.error(e);
				}
				return null;
			}catch (Exception e) {
				Map<String, String> errorResponse = new HashMap<String, String>(2);
				errorResponse.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_INVALID_CALL);
				errorResponse.put(ClientRequestConstants.MESSAGE_STRING, e.getMessage());
				String errorResponseString = JSONObject.fromObject(errorResponse).toString();
				try {
					writeOutput(errorResponseString, response, decryptResponse);
				} catch (IOException ie) {
					LOG.error(ie);
				}
				return null;
			}
			try{
				validateSession(request,jsonRequestObject);//Session Check
				if(callType.equals(ClientRequestConstants.GET_CALL_TYPE)) {
					handleGetRequest(request, response, callName, jsonRequestObject, decryptResponse);
				}else {
					handleSendRequest(request, response, callName, jsonRequestObject, decryptResponse);
				}
			}catch (Exception e) {
				Map<String, String> errorResponse = new HashMap<String, String>(2);
				errorResponse.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_INVALID_SESSION);
				errorResponse.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_INVALID_SESSION_MSG);
				String errorResponseString = JSONObject.fromObject(errorResponse).toString();
				try {
					writeOutput(errorResponseString, response, decryptResponse);
				} catch (IOException ie) {
					LOG.error(ie);
				}
				return null;
			}
		}
		return null;
	}
	
	private JSONObject getJSONObject(String jsonString,HttpServletResponse response) {
		JSONObject jsonRequestObject = null;
		if(jsonString != null) {
			try {
				jsonRequestObject = JSONObject.fromObject(jsonString);
			} catch(Exception e) {
				LOG.error("Unparsable JSON");
				HashMap<String, String> errorResponse = new HashMap<String, String>(2);
				errorResponse.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_TECHNICAL_PROBLEM);
				errorResponse.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_TECHNICAL_PROBLEM_MSG);
				String errorResponseString = JSONObject.fromObject(errorResponse).toString();
				try {
					writeOutput(errorResponseString, response, true);
				} catch (IOException e1) {
					LOG.error("Unable to write the response");
				}
			}
		}
		return jsonRequestObject;
	}
	
	public void writeOutput(String string, HttpServletResponse response, boolean decryptResponse) throws IOException {
		String result = string;
		if(decryptResponse){
			Encryptor encryptor = Encryptor.getEncryptor(ClientRequestConstants.ENCRYPTION_KEY);
			try{
				result = encryptor.encryptString(string);
				result = URLEncoder.encode(result, "UTF-8");
			}catch (Exception e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		LOG.info(result);
		response.getWriter().print(result);
	}
	
	private String getParam(HttpServletRequest request,	JSONObject jsonRequestObject, String paramName) {
		String paramValue = "";
		if(jsonRequestObject == null) {
			paramValue = request.getParameter(paramName);
		} else {
			try {
				paramValue = jsonRequestObject.getString(paramName);
			} catch(Exception e) {
				LOG.error("Parameter '"+paramName+"' not found");
			}
		}
		return paramValue;
	}
	
	private int getIntParam(HttpServletRequest request,	JSONObject jsonRequestObject, String paramName) {
		int paramValue = 0;
		if(jsonRequestObject == null) {
			paramValue = Integer.parseInt(request.getParameter(paramName));
		} else {
			try {
				paramValue = jsonRequestObject.getInt(paramName);
			} catch(Exception e) {
				LOG.error("Parameter '"+paramName+"' not found");
			}
		}
		return paramValue;
	}

	private long getLongParam(HttpServletRequest request, JSONObject jsonRequestObject, String paramName) {
		long paramValue = 0;
		if(jsonRequestObject == null) {
			paramValue = Integer.parseInt(request.getParameter(paramName));
		} else {
			try {
				paramValue = jsonRequestObject.getInt(paramName);
			} catch(Exception e) {
				LOG.error("Parameter '"+paramName+"' not found");
			}
		}
		return paramValue;
	}
	
	private void validateSession(HttpServletRequest request,JSONObject jsonRequestObject) throws InvalidSessionException {
		String callName = getParam(request, jsonRequestObject, ClientRequestConstants.CALL_NAME);
		if(!callName.equals(ClientRequestConstants.LOGIN_GET)){
			int customerId = getIntParam(request, jsonRequestObject, ClientRequestConstants.CUSTOMER_ID);
			long uId = getLongParam(request, jsonRequestObject, ClientRequestConstants.USER_ID);
			String sessionId = getParam(request, jsonRequestObject, ClientRequestConstants.SESSION_ID);
			String callType = getParam(request, jsonRequestObject, ClientRequestConstants.CALL_TYPE);
			final int SESSION_ID_LENGTH = 6;
			if(configManager.getDnbCustId() != customerId){
				throw new InvalidSessionException("Customer Inactive from Server");
			}
			if(sessionId == null ||  sessionId.trim().length()< SESSION_ID_LENGTH) {
				throw new InvalidSessionException("session timed out or unauthorised session");
			}else{
				if(!gatewayLogic.isValidSession(customerId,uId,sessionId)){
					String remarks = "Invalide Session Id";
					if(callType.equals(ClientRequestConstants.SEND_CALL_TYPE) && jsonRequestObject.has("eventData")) {
						try{
							JSONObject jsonEventObject = jsonRequestObject.getJSONObject("eventData");
							if(!jsonEventObject.isEmpty()){
								if(jsonEventObject.has("eventId")){
									long uniqueId = jsonEventObject.getLong("uniqueId");
									gatewayLogic.rejectedJsonBackup(customerId, uId,Integer.parseInt(sessionId), uniqueId, jsonRequestObject.toString(), remarks);
								}
							}
						}catch (Exception e) {
							
						}
					}
					LOG.info("unauthorised session");
					throw new InvalidSessionException("session timed out or unauthorised session");
				}
			}
		}
	}
	
	private void handleSendRequest(HttpServletRequest request, HttpServletResponse response, String callName, JSONObject jsonRequestObject, boolean decryptResponse) throws Exception {
		JSONObject result = null;
		LOG.info("callName ::"+callName);
		try{
			int customerId = getIntParam(request, jsonRequestObject, ClientRequestConstants.CUSTOMER_ID);
			long uId = getLongParam(request, jsonRequestObject, ClientRequestConstants.USER_ID);
			String ts = getParam(request, jsonRequestObject, ClientRequestConstants.CLIENT_TIME_STAMP);
			String userAgent = getParam(request, jsonRequestObject, ClientRequestConstants.CLIENT_PHONE_MODEL);
			String clientVer = getParam(request, jsonRequestObject, ClientRequestConstants.CLIENT_VERSION);
			if(callName.equals(ClientRequestConstants.ACKNOWLEDGEMENT)){
				JSONObject jsonMessageObject = jsonRequestObject.getJSONObject(ClientRequestConstants.MESSAGE);
				String paramName = getParam(request, jsonMessageObject, ClientRequestConstants.CALL_NAME);
				String paramValues = getParam(request, jsonMessageObject, ClientRequestConstants.PARAM_VALUES);
				result = gatewayLogic.updateAcknowledgement(customerId, ts, uId, paramName, paramValues);
			}else if(callName.equals(ClientRequestConstants.DNB_DOC_MEDIA_SYNC)){
				JSONObject dataObject = jsonRequestObject.getJSONObject(ClientRequestConstants.CM_MEDIA_DATA);
				result = gatewayLogic.dnbMmediaDataSync(customerId, uId, dataObject);
			}
			//DNB data insertion
			else if(callName.equals(ClientRequestConstants.DNB_EVENT_DETAILS)){
				if(jsonRequestObject.containsKey(ClientRequestConstants.DNB_FORM_DATA)){
					result = gatewayLogic.insertDNBFormData(customerId,ts,uId, jsonRequestObject);
				}else{
					HashMap<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_INVALID_CALL);
					resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_INVALID_CALLNAME_MSG);
				}
			}
			// For Normal flow
			else if(callName.equals(ClientRequestConstants.EVENT_SEND) || callName.equalsIgnoreCase(ClientRequestConstants.EVENT_TRACKING) || callName.equalsIgnoreCase(ClientRequestConstants.EVENT_APP_EXIT)){
				LOG.info("Event & Location Data");
				JSONObject jsonLocObject = jsonRequestObject.getJSONObject("locData");
				int updatedBy = 0;
					String eventName="";
					JSONObject jsonEventObject = jsonRequestObject.getJSONObject("eventData");
					if(jsonRequestObject.containsKey(ClientRequestConstants.EVENT_NAME)) eventName = jsonRequestObject.getString("eventName");
					result = gatewayLogic.insertEventData(customerId, ts, uId, jsonEventObject, jsonLocObject, userAgent, clientVer,updatedBy,eventName);
			}else{
				HashMap<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_INVALID_CALL);
				resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_INVALID_CALLNAME_MSG);
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put(ClientRequestConstants.RESPONSE_CODE_STRING, ClientRequestConstants.ERROR_JSON);
			resultMap.put(ClientRequestConstants.MESSAGE_STRING, ClientRequestConstants.ERROR_JSON_MSG);
			result = JSONObject.fromObject(resultMap);
		}
		writeOutput(result.toString(), response, decryptResponse);
	}
	
	private void handleGetRequest(HttpServletRequest request, HttpServletResponse response, String callName, JSONObject jsonRequestObject, boolean decryptResponse) throws IOException {
		int customerId = getIntParam(request, jsonRequestObject, ClientRequestConstants.CUSTOMER_ID);
		long userId = (jsonRequestObject.containsKey(ClientRequestConstants.USER_ID))?getLongParam(request, jsonRequestObject, ClientRequestConstants.USER_ID):0;
		JSONObject result = null;
		LOG.info( "callName ::"+callName);
		if(callName.equals(ClientRequestConstants.LOGIN_GET)) {
			String userLoginId = getParam(request, jsonRequestObject, ClientRequestConstants.USER_LOGIN_ID);
			String password = getParam(request, jsonRequestObject, ClientRequestConstants.PASSWORD);
			String userAgent = getParam(request, jsonRequestObject, ClientRequestConstants.CLIENT_PHONE_MODEL);
			String clientVer = getParam(request, jsonRequestObject, ClientRequestConstants.CLIENT_VERSION);
			String ts = getParam(request, jsonRequestObject, ClientRequestConstants.CLIENT_TIME_STAMP);
			String iemei = (jsonRequestObject.containsKey(ClientRequestConstants.IMEI))?getParam(request, jsonRequestObject, ClientRequestConstants.IMEI):"";
			String lat = getParam(request, jsonRequestObject, ClientRequestConstants.CLIENT_LAT);
			String lon = getParam(request, jsonRequestObject, ClientRequestConstants.CLIENT_LON);

			if(lat == null || lat.equals("")) lat = "NA";
			if(lon == null || lon.equals("")) lon = "NA";

			result = gatewayLogic.validateLogin(customerId, userLoginId, password, userAgent, true, clientVer, ts, iemei, lat, lon);
			if(ClientRequestConstants.SUCCESS_LOGIN.equals(result.getString(ClientRequestConstants.RESPONSE_CODE_STRING))){
				userId = result.getLong(ClientRequestConstants.USER_ID);
				String loginFrom = "mob";
				result.put(ClientRequestConstants.SESSION_ID, gatewayLogic.generateSessionId(customerId, userId,loginFrom));
			}
			writeOutput(result.toString(), response, decryptResponse);
		}else if(callName.equals(ClientRequestConstants.SETTINGS_GET_STRING)) {
			String ts = getParam(request, jsonRequestObject, ClientRequestConstants.CLIENT_TIME_STAMP);
			result = gatewayLogic.getSettingsForUser(customerId, userId,ts);
			writeOutput(result.toString(), response, decryptResponse);
		}else if(callName.equals(ClientRequestConstants.UPDATE_GET)){
			result = gatewayLogic.updateCheckCall(userId,customerId);
			writeOutput(result.toString(), response, decryptResponse);
		}else if(callName.equalsIgnoreCase(ClientRequestConstants.DNB_DOC_LIST)){
			result = gatewayLogic.getDNBDocCheckList();
			writeOutput(result.toString(), response, decryptResponse);
		}else if(callName.equalsIgnoreCase(ClientRequestConstants.DNB_ENQUIRY_GET)){
			boolean isDownload = false;
			result = gatewayLogic.getEnquiryForUser(customerId, userId, isDownload);
			writeOutput(result.toString(), response, decryptResponse);
		}else if(callName.equalsIgnoreCase(ClientRequestConstants.DNB_ENQUIRY_REGET)){
			boolean isDownload = true;
			result = gatewayLogic.getEnquiryForUser(customerId, userId, isDownload);
			writeOutput(result.toString(), response, decryptResponse);
		}else if(callName.equalsIgnoreCase(ClientRequestConstants.DNB_REMOVED_ENQUIRY_LIST)){
			result = gatewayLogic.getRemovedEnquiryNos(customerId, userId);
			writeOutput(result.toString(), response, decryptResponse);
		}
	}
}