package in.otpl.dnb.util;

import in.otpl.dnb.dto.MobileNotificationDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FCMPushNotificationService {
	
	private static final Logger LOG = Logger.getLogger(FCMPushNotificationService.class);

	@Autowired
	private ConfigManager configManager;

	public String sendFCMNotification(MobileNotificationDto notification){
		String pushedRemarks = "";
		/*	try{
			URL url = new URL(configManager.getFcmServerUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization","key="+configManager.getFcmServerApiKey());
			conn.setRequestProperty("Content-Type","application/json");*/

			JSONObject infoJson = new JSONObject();
			infoJson.put("title", notification.getTitle());
			infoJson.put("body", notification.getBody());
			
			JSONObject dataJson = new JSONObject();
			dataJson.put("title", notification.getTitle());
			dataJson.put("body", notification.getBody());
			
			//infoJson.put("sound", notification.getSound());
			/*infoJson.put("click_action", notification.getClickAction());
			infoJson.put("color", "#53c4bc");
			infoJson.put("icon", "ic_launcher");*/
			//infoJson.put("main_picture", notification.getImgUrl());
			
			/*JSONObject json = new JSONObject();
			//json.put("to", notification.getDeviceToken().trim());
			json.put("registration_ids", notification.getDeviceTokens());
			if(notification.getDeviceType() == ServerConstants.DEVICE_TYPE_ANDROID)
				json.put("data", dataJson);
			else
				json.put("notification", infoJson);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(json.toString());
			wr.flush();

			int status = 0;
			if( null != conn ){
				status = conn.getResponseCode();
			}*/

			/*if(status != 0){
				if(status == 200){//Success
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					pushedRemarks = reader.readLine();
					//LOG.info("Notification Response : " + pushedRemarks);
				}else if(status == 401){//Client Side Error
					LOG.error("Notification Response : [ errorCode=ClientError ]");
				}else if(status == 501){//Server side error
					LOG.error("Notification Response : [ errorCode=ServerError ]");
				}else if( status == 503){//server side error
					LOG.error("Notification Response : FCM Service is Unavailable");
				}else{
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					LOG.error(reader.readLine());
				}
			}*/

		/*}catch(MalformedURLException mlfexception){// Protocol Error
			LOG.error("Error occurred while sending push Notification!.." + mlfexception.getMessage());
		}catch(IOException mlfexception){//URL problem
			LOG.error("Reading URL, Error occurred while sending push Notification!.." + mlfexception.getMessage());
		}catch(JSONException jsonexception){//Message format error
			LOG.error("Message Format, Error occurred while sending push Notification!.." + jsonexception.getMessage());
		}catch (Exception exception) {
			LOG.error("Error occurred while sending push Notification!.." + exception.getMessage());
		}*/
		return pushedRemarks;
	}

}