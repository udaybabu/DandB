package in.otpl.dnb.util;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailUtil {

	@Autowired
	private ConfigManager configManager;

	private static final Logger LOG = Logger.getLogger(EmailUtil.class);

	public boolean sendEmail(String to, String bcc, String cc, String subject, String body, String fileDetails){
		boolean check = false;
		/*	try {
			Properties properties = new Properties();
			properties.put("mail.smtp.host", configManager.getEmailSmtpHost());
			properties.put("mail.smtp.port", configManager.getEmailSmtpPort());
			properties.put("mail.smtp.auth",configManager.isEmailAuth());
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			Session session = null;
			if(configManager.isEmailAuth()){
				final String userName = configManager.getEmailSmtpUsername();
				final String password = configManager.getEmailSmtpPassword();
				session = Session.getInstance(properties, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(userName,password);
					}
				});
			}else{
				session = Session.getDefaultInstance(properties, null);
			}

			if(session != null){
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(configManager.getEmailSmtpUsername()));

				String allEmailIds = to;
				String[] tos = to.split(",");
				for (int i = 0; i < tos.length; i++){
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(tos[i]));
				}
				if(bcc != null && bcc != ""){
					allEmailIds += ","+bcc;
					String[] bcc_address = bcc.split(",");
					for(int i = 0; i < bcc_address.length; i++){
						message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc_address[i]));
					}
				}
				if(cc != null && cc != ""){
					allEmailIds += ","+cc;
					String[] cc_address = cc.split(",");
					for(int i = 0; i < cc_address.length; i++){
						message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc_address[i]));
					}
				}

				message.setSubject(subject);
				message.setSentDate(new Date());

				String header = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"><meta name=\"viewport\" "
						+ "content=\"width=device-width\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"
						+ "<meta name=\"x-apple-disable-message-reformatting\"><!--<title></title>--><!--[if mso]>"
						+ "<style>*{font-family: sans-serif !important;}</style><![endif]--><style>html,body{margin: 0 auto !important;"
						+ "padding: 0 !important;height: 100% !important;width: 100% !important;}* {-ms-text-size-adjust: 100%;"
						+ "-webkit-text-size-adjust: 100%;}div[style*=\"margin: 16px 0\"] {margin:0 !important;}"
						+ "table,td {mso-table-lspace: 0pt !important;mso-table-rspace: 0pt !important;}table {border-spacing: 0 !important;"
						+ "border-collapse: collapse !important;table-layout: fixed !important;margin: 0 auto !important;}"
						+ "table table table {table-layout: auto;}img {-ms-interpolation-mode:bicubic;}*[x-apple-data-detectors] "
						+ "{color: inherit !important;text-decoration: none !important;}.x-gmail-data-detectors,.x-gmail-data-detectors *,"
						+ ".aBn {border-bottom: 0 !important;cursor: default !important;}.a6S {display: none !important;"
						+ "opacity: 0.01 !important;}img.g-img + div {display:none !important;}.button-link {text-decoration: none !important;}"
						+ "@media only screen and (min-device-width: 375px) and (max-device-width: 413px) {.email-container "
						+ "{min-width: 375px !important;}}@media screen and (max-width: 600px) {.email-container {width: 100% !important;"
						+ "margin: auto !important;}.fluid {max-width: 100% !important;height: auto !important;margin-left: auto !important;"
						+ "margin-right: auto !important;}.stack-column,stack-column-center {display: block !important;width: 100% !important;"
						+ "max-width: 100% !important;direction: ltr !important;}.stack-column-center {text-align: center !important;}"
						+ ".center-on-narrow {text-align: center !important;display: block !important;margin-left: auto !important;"
						+ "margin-right: auto !important;float: none !important;}table.center-on-narrow {display: inline-block !important;}}"
						+ "</style></head>"
						+ "<body width=\"100%\" bgcolor=\"#020327\" style=\"margin: 0; mso-line-height-rule: exactly;\">"
						+ "<center style=\"width: 100%; background: #020327; text-align: left;\"><table role=\"presentation\" "
						+ "aria-hidden=\"true\" aria-hidden=\"true\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" "
						+ "width=\"600\" style=\"margin: auto;\" class=\"email-container\"><tr><td style=\"padding: 20px 0; text-align: center\">"
						+ "<a href='http://www.quikformz.com' target='_blank'><img src=\"cid:qfimage\" aria-hidden=\"true\" width=\"200\" "
						+ "height=\"46\" alt=\"www.quikformz.com\" border=\"0\" style=\"height: auto; background: #dddddd; "
						+ "font-family: sans-serif; font-size: 15px; line-height: 20px; color: #555555;\"></a></td></tr></table>"
						+ "<table role=\"presentation\" aria-hidden=\"true\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" "
						+ "width=\"600\" style=\"margin: auto;\" class=\"email-container\"><tr><td bgcolor=\"#ffffff\" "
						+ "style=\"padding: 40px; text-align: left; font-family: sans-serif; font-size: 15px; line-height: 20px; "
						+ "color: #555555;\">";

				String footer = "</td></tr><tr><td bgcolor=\"#ffffff\" style=\"padding-left: 40px; text-align: left; font-family: sans-serif; "
						+ "font-size: 15px; line-height: 20px; color: #555555;\">Regards,<br>quikformz support</td></tr><tr>"
						+ "<td bgcolor=\"#ffffff\" style=\"padding-left: 40px; padding-bottom:40px; text-align: left; font-family: "
						+ "sans-serif; font-size: 12px; line-height: 20px; color: #948585;\"><br>Note: This is an automated email. "
						+ "Please do not respond.<br>For any queries write to: <a href=\"mailto:qfsupport@orientindia.net\" target=\"_blank\">"
						+ "qfsupport@orientindia.net</a></td></tr></table><table role=\"presentation\" aria-hidden=\"true\" cellspacing=\"0\" "
						+ "cellpadding=\"0\" border=\"0\" align=\"center\" width=\"600\" style=\"margin: auto;\" class=\"email-container\">"
						+ "<tr><td style=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; line-height:18px; "
						+ "text-align: center; color: #888888;\" class=\"x-gmail-data-detectors\">"
						+ "<a href='http://www.orientindia.com' target='_blank'><img src=\"cid:orientimage\" aria-hidden=\"true\" "
						+ "width=\"200\" height=\"48\" alt=\"Orient Technologies Pvt. Ltd.\" border=\"0\" align=\"center\" "
						+ "style=\"width: 100%; max-width: 200px; height: auto; background: #dddddd; font-family: sans-serif; "
						+ "font-size: 15px; line-height: 20px; color: #555555;\" class=\"g-img\"></a>"
						+ "<br><br>&#169; Orient Technologies Pvt. Ltd.</td></tr></table></center></body></html>";

				// Set the email body
				String finalBody = header + body + footer;

				// This mail has 2 part, the BODY and the embedded image
				//Multipart multipart = new MimeMultipart();
				MimeMultipart multipart = new MimeMultipart("related");

				// First part (HTML)
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(finalBody, "text/html");
				multipart.addBodyPart(messageBodyPart);//Add

				// Second part (Image)
				messageBodyPart = new MimeBodyPart();
				DataSource fds = new FileDataSource(configManager.getEmailHeaderLogoLoc());
				messageBodyPart.setDataHandler(new DataHandler(fds));
				messageBodyPart.setHeader("Content-ID", "<qfimage>");
				multipart.addBodyPart(messageBodyPart);

				messageBodyPart = new MimeBodyPart();
				DataSource fds2 = new FileDataSource(configManager.getEmailFooterLogoLoc());
				messageBodyPart.setDataHandler(new DataHandler(fds2));
				messageBodyPart.setHeader("Content-ID", "<orientimage>");
				multipart.addBodyPart(messageBodyPart);

				// Set the email attachment file
				if(fileDetails != null && !fileDetails.trim().equals("")){
					String[] filename = fileDetails.split(",");
					for(int i = 0; i < filename.length; i++){
						MimeBodyPart attachmentPart = new MimeBodyPart();
						FileDataSource fileDataSource = new FileDataSource(filename[i]) {
							@Override
							public String getContentType() {
								return "application/octet-stream";
							}
						};
						attachmentPart.setDataHandler(new DataHandler(fileDataSource));
						attachmentPart.setFileName(fileDataSource.getName());
						multipart.addBodyPart(attachmentPart);
					}
				}

				// put everything together
				message.setContent(multipart);
				Transport.send(message);

				LOG.info("e-mail sents to: "+allEmailIds);
				check = true;
			}

		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}

		return check;
	}*/
		return check;
	}
}