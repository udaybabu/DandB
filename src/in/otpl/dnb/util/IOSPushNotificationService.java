package in.otpl.dnb.util;

import in.otpl.dnb.dto.MobileNotificationDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;

@Service
public class IOSPushNotificationService{

	private static final Logger LOG = Logger.getLogger(IOSPushNotificationService.class);

	private Resource certResource;
	private String certPassword;
	private boolean production; 
	private static ApnsService service;

	public Resource getCertResource() {
		return certResource;
	}
	public void setCertResource(Resource certResource) {
		this.certResource = certResource;
	}
	public String getCertPassword() {
		return certPassword;
	}
	public void setCertPassword(String certPassword) {
		this.certPassword = certPassword;
	}
	public boolean isProduction() {
		return production;
	}
	public void setProduction(boolean production) {
		this.production = production;
	}
	
	/****** Advance ******/
	public void createApnsService(){
		try {
			InputStream in = certResource.getInputStream();
			if(production){
				service =
						APNS.newService()
						.withCert(in, certPassword)
						.withProductionDestination()
						.build();
			}else{
				service =
						APNS.newService()
						.withCert(in, certPassword)
						.withSandboxDestination()
						.build();
			}
			//this.service.start();
		} catch (IOException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}

	public void stopApnsService(){
		//this.service.stop();
	}

	public String pushNotification(MobileNotificationDto notification) {
		String pushedRemarks = "";
		try{
			if(service != null){
				pushedRemarks = doSendNotification(notification);
			}else{
				LOG.info("################### IOS Push Service Not Loaded ###################");
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return pushedRemarks;
	}

	public void pushNotifications(List<MobileNotificationDto> notifications) {
		try{
			if(service != null){
				for (MobileNotificationDto notification : notifications) {
					doSendNotification(notification);
				}
			}else{
				LOG.info("################### IOS Push Service Not Loaded ###################");
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}

	public void silentPushNotification(MobileNotificationDto notification) {
		try{
			if(service != null){
				doSilentNotification(notification);
			}else{
				LOG.info("################### IOS Push Service Not Loaded ###################");
			}
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}

	private String doSendNotification(MobileNotificationDto notification) {
		String pushedRemarks = "";
		try{
			PayloadBuilder payloadBuilder = APNS.newPayload();
			payloadBuilder = payloadBuilder.badge(notification.getBadge());
			payloadBuilder = payloadBuilder.sound(notification.getSound());
			if (notification.getBody() != null) {
				payloadBuilder = payloadBuilder.alertBody(notification.getBody());
			}
			String payload = payloadBuilder.build();
			ApnsNotification result = service.push(notification.getDeviceToken(), payload);
			pushedRemarks = result.toString();
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return pushedRemarks;
	}

	public String doSilentNotification(MobileNotificationDto notification){
		String pushedRemarks = "";
		try{
			String payload = "{\"aps\":{\"content-available\":1}}";
			ApnsNotification result = service.push(notification.getDeviceToken(), payload);
			pushedRemarks = result.toString();
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return pushedRemarks;
	}

	/****** Basic ******/
	/*
	Here are the methods for pushing simple notifications:

	1.  alert (message, keystore, password, production, devices): push a simple alert message
	2.  badge (badge, keystore, password, production, devices): push a badge number
	3.  sound (sound, keystore, password, production, devices): push a sound name
	4.  combined (message, badge, sound, keystore, password, production, devices): push a alert+badge+sound notification
	5.  contentAvailable (keystore, password, production, devices): notify Apple's News stand application
	6.  test (keystore, password, production, devices): push useful debug information 

	{"badge":+1,"alert":"Have a nice day","sound":"default"}

	If you create custom payloads yourself, you can use the following methods to push them:

	1.  payload (payload, keystore, password, production, devices): push a single payload to devices
	2.  payload (payload, keystore, password, production, numberOfThreads, devices): use the built-in multithreaded transmission engine to push a single payload to lots of devices using n threads
	3.  payloads (keystore, password, production, payloadDevicePairs): push payloads to paired devices
	4.  payloads (keystore, password, production, numberOfThreads, payloadDevicePairs): use the built-in multithreaded transmission engine to push payloads to paired devices 
	 */


	public void doSendNotification(String deviceToken, String message){
		try{
			List<PushedNotification> notifications = Push.alert(message, certResource.getFile(), certPassword, production, deviceToken.replace(" ", ""));
			for (PushedNotification notification : notifications) {
				if (notification.isSuccessful()){
					LOG.info("Push notification sent successfully to: " + notification.getDevice().getToken());
				}else{
					String invalidToken = notification.getDevice().getToken();
					LOG.info("Invalid Token " + invalidToken);
					Exception theProblem = notification.getException();
					LOG.error(theProblem);
					ResponsePacket theErrorResponse = notification.getResponse();
					if (theErrorResponse != null){
						LOG.error(theErrorResponse.getMessage());
					}
				}
			}
		} catch (KeystoreException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} catch (CommunicationException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} catch (IOException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}

}