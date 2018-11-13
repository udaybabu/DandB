/**
 * EncryptionComponent.java is to encrypt and decrypt the text value
 */
package in.otpl.dnb.util;


import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import org.apache.log4j.Logger;

/**
 * @author Shaik Shabeer
 *
 */
public class EncryptionUtil {

		private static String characterEncoding = null;
	    private static BASE64Encoder base64Encoder = null;
	    private static BASE64Decoder base64Decoder = null;
	   // private static SecretKey key = null;
	    private static boolean instantiated = false;
	    protected  static final Logger log = Logger.getLogger( EncryptionUtil.class );
	    private static String algorithm = "DESede";
        private static Key key = null;
        private static Cipher cipher = null;
	private  static void instantiate() throws Exception
	{
		try
		{
		if(! instantiated)
		{
			characterEncoding = null;
		    base64Encoder = null;
		    base64Decoder = null;
		    key = null;

		    base64Encoder = new BASE64Encoder();
		    base64Decoder = new BASE64Decoder();

		    key = KeyGenerator.getInstance(algorithm).generateKey();
            cipher = Cipher.getInstance(algorithm);

		}
		instantiated = true;
	}catch (Exception e) {
		log.error(ErrorLogHandler.getStackTraceAsString(e));
			}
	}

	public  static String getDecryptedString(String encString) throws Exception	{
		String decString = null;
		try
		{
		instantiate();
		characterEncoding = "ASCII";
		cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding", "SunJCE");
		cipher.init(Cipher.DECRYPT_MODE, key);
		Security.addProvider( new com.sun.crypto.provider.SunJCE() );
		byte[] encryptedPasswordBytes = base64Decoder.decodeBuffer(encString);
        byte[] passwordBytes = cipher.doFinal(encryptedPasswordBytes);
        String recoveredPassword = new String(passwordBytes, characterEncoding);
        decString =  recoveredPassword;
	}
    catch (Exception e) {
    	log.error(ErrorLogHandler.getStackTraceAsString(e));
		
	}
		return decString;

	}

	public static String getEncrryptedString(String textString) throws Exception {
		String encString = null;
		try
		{
		instantiate();
		characterEncoding = "ASCII";
		cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding", "SunJCE");
        cipher.init(Cipher.ENCRYPT_MODE, key);
		Security.addProvider( new com.sun.crypto.provider.SunJCE() );
		byte[] passwordBytes = textString.getBytes(characterEncoding);
        byte[] encryptedPasswordBytes = cipher.doFinal(passwordBytes);
        String encodedEncryptedPassword = base64Encoder.encode(encryptedPasswordBytes);
        encString =  encodedEncryptedPassword;
		}
        catch (Exception e) {
        	log.error(ErrorLogHandler.getStackTraceAsString(e));
			
		}
		return encString;
	}

}
