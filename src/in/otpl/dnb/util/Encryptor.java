package in.otpl.dnb.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

public class Encryptor {	
	
	private BufferedBlockCipher cipher;
    private KeyParameter key;
    private static BASE64Encoder base64Encoder = new BASE64Encoder();
    private static BASE64Decoder base64Decoder = new BASE64Decoder();
    static Logger log = Logger.getLogger(Encryptor.class);
    
    public static Encryptor getEncryptor(String key){
    	return new Encryptor(key);
    }
    
    public static Encryptor getEncryptor(byte[] key){
    	return new Encryptor(key);
    }

    public Encryptor(byte[] key){
    	cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESEngine()));    	
    	this.key = new KeyParameter(key);    	
    }
    
    public Encryptor(String key){
        this(key.getBytes());
    }
    
    public synchronized byte[] encrypt(byte[] data)throws CryptoException{
    	if(data == null || data.length == 0){
    		return new byte[0];
    	}
    	cipher.init(true, key);
    	return callCipher(data);    	
    }
    
    public String encryptString(String data) throws CryptoException {   
    	String result = "";
    	if(data != null && data.length() > 0){
    		byte[] res =  encrypt(data.getBytes());    		
        	result = base64Encoder.encode(res);
    	}    	
		return result;
    }

    private byte[] callCipher(byte[] data) throws CryptoException{
    	int size = cipher.getOutputSize(data.length);
    	int dtatLength = data.length;
    	byte[] result = new byte[size];
    	int objectLength = cipher.processBytes(data, 0, dtatLength, result, 0);    	
    	objectLength += cipher.doFinal(result, objectLength);    	
    	if( objectLength < size ){
    		byte[] tmp = new byte[objectLength];
    		System.arraycopy(result, 0, tmp, 0, objectLength);
    		result = tmp;
    	}
    	return result;    	
    }
    
    public synchronized byte[] decrypt(byte[] data ) throws CryptoException {
    	if( data == null || data.length == 0 ){
    		return new byte[0];
    	}
    	cipher.init( false, key );
    	return callCipher( data );
    }

    public String decryptString(String data) throws CryptoException, IOException {
    	if( data == null || data.length() == 0 ){
    		return "";
    	}    
    	/* Note: we can replace sun.misc.BASE64Decoder with org.apache.commons.codec.binary
    	 * To do this replace commons-codec-1.3 with commons-codec-1.4
    	 */
    	byte[] bytes =  base64Decoder.decodeBuffer(data);
    	return new String(decrypt(bytes));
    }
    
    public String decryptString(byte[] data) throws CryptoException, IOException {
    	if( data == null || data.length == 0 ){
    		return "";
    	}     	    	
    	return new String(decrypt(data));
    }
    
    
    // for testing
    public static void main(String args[]){
    	
    	try{
        	Encryptor encryptor = new Encryptor("7c5D59bF");        	
        	String test = new String("jsonString={\"phoneModel\":\"Nokia6131NFC/05.10\",\"passwd\":\"123456\",\"ptn\":\"88\",\"appVersion\":\"1.0.0\",\"callType\":\"get\",\"imei\":\"\",\"pId\":\"1.0\",\"ts\":\"1251883062593\",\"cId\":\"85\",\"cVer\":\"1.0\",\"callName\":\"login\"}");
        	System.out.println("Original string :\n" +test); 
        	/* encryption and encoding */
        	String encryptedStr = encryptor.encryptString(test);
        	System.out.println("\nEncoded string :\n" +encryptedStr); 
        	//This will be sent to client
        	String finalStr = URLEncoder.encode(encryptedStr, "UTF-8");
        	System.out.println("\nEncrypted string (send to client / received from client) :\n" +encryptedStr); 
        	
        	/* decoding and decryption */
        	String decodedStr = URLDecoder.decode(finalStr, "UTF-8");
        	System.out.println("\nDecoded string :\n" +decodedStr);   
        	String decryptedStr = encryptor.decryptString(decodedStr);
        	System.out.println("\nDecrypted string :\n" +decryptedStr);          	
        	
    	}catch (Exception e) {
			log.error(ErrorLogHandler.getStackTraceAsString(e));
		}    	
    }
}
