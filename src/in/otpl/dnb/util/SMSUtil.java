package in.otpl.dnb.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SMSUtil {

	private static final Logger LOG = Logger.getLogger(SMSUtil.class);
	
	@Autowired
	private ConfigManager configManager;
	
	public String sendSmsIndia(String mobNo, String message){
		String resp = "";
		/*try{
			String smsUrl = configManager.getSmsApiUrlIndia();
			smsUrl += "?username="+configManager.getSmsApiUsernameIndia();
			smsUrl += "&password="+configManager.getSmsApiPasswordIndia();
			smsUrl += "&sender="+configManager.getSmsApiSenderIndia();
			smsUrl += "&type=TEXT";
			smsUrl += "&mobile="+mobNo;
			smsUrl += "&message="+URLEncoder.encode(message, "UTF8");;
			
			URL url = new URL(smsUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			String line;
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while ((line = rd.readLine()) != null){
				resp += line;
			}
			rd.close(); 
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		*/
		LOG.info("Result: " + resp);
		return resp;
	}

}
