/*
 * $Id: Base64.java 3122 2009-09-04 09:49:12Z sonal.agarwal $
 */
package in.otpl.dnb.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class Base64 {
	
	/*private Base64() {}

	// Mapping table from 6-bit nibbles to Base64 characters.
	private static char[] map1 = new char[64];
	static {
		int i = 0;
		for (char c = 'A'; c <= 'Z'; c++)
			map1[i++] = c;
		for (char c = 'a'; c <= 'z'; c++)
			map1[i++] = c;
		for (char c = '0'; c <= '9'; c++)
			map1[i++] = c;
		map1[i++] = '+';
		map1[i++] = '/';
	}

	// Mapping table from Base64 characters to 6-bit nibbles.
	private static byte[] map2 = new byte[128];
	static {
		for (int i = 0; i < map2.length; i++)
			map2[i] = -1;
		for (int i = 0; i < 64; i++)
			map2[map1[i]] = (byte) i;
	}

	*//**
	 * Encodes a string into Base64 format. No blanks or line breaks are
	 * inserted.
	 * 
	 * @param s
	 *            a String to be encoded.
	 * @return A String with the Base64 encoded data.
	 *//*
	public static String encodeString(String s) {
		return new String(encode(s.getBytes()));
	}

	*//**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted.
	 * 
	 * @param in
	 *            an array containing the data bytes to be encoded.
	 * @return A character array with the Base64 encoded data.
	 *//*
	public static char[] encode(byte[] in) {
		return encode(in, in.length);
	}

	*//**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted.
	 * 
	 * @param in
	 *            an array containing the data bytes to be encoded.
	 * @param iLen
	 *            number of bytes to process in <code>in</code>.
	 * @return A character array with the Base64 encoded data.
	 *//*
	public static char[] encode(byte[] in, int iLen) {
		int oDataLen = (iLen * 4 + 2) / 3; // output length without padding
		int oLen = ((iLen + 2) / 3) * 4; // output length including padding
		char[] out = new char[oLen];
		int ip = 0;
		int op = 0;
		while (ip < iLen) {
			int i0 = in[ip++] & 0xff;
			int i1 = ip < iLen ? in[ip++] & 0xff : 0;
			int i2 = ip < iLen ? in[ip++] & 0xff : 0;
			int o0 = i0 >>> 2;
			int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
			int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
			int o3 = i2 & 0x3F;
			out[op++] = map1[o0];
			out[op++] = map1[o1];
			out[op] = op < oDataLen ? map1[o2] : '=';
			op++;
			out[op] = op < oDataLen ? map1[o3] : '=';
			op++;
		}
		return out;
	}

	*//**
	 * Decodes a string from Base64 format.
	 * 
	 * @param s
	 *            a Base64 String to be decoded.
	 * @return A String containing the decoded data.
	 * @throws IllegalArgumentException
	 *             if the input is not valid Base64 encoded data.
	 *//*
	public static String decodeString(String s) {
		return new String(decode(s));
	}

	*//**
	 * Decodes a byte array from Base64 format.
	 * 
	 * @param s
	 *            a Base64 String to be decoded.
	 * @return An array containing the decoded data bytes.
	 * @throws IllegalArgumentException
	 *             if the input is not valid Base64 encoded data.
	 *//*
	public static byte[] decode(String s) {
		return decode(s.toCharArray());
	}

	*//**
	 * Decodes a byte array from Base64 format. No blanks or line breaks are
	 * allowed within the Base64 encoded data.
	 * 
	 * @param in
	 *            a character array containing the Base64 encoded data.
	 * @return An array containing the decoded data bytes.
	 * @throws IllegalArgumentException
	 *             if the input is not valid Base64 encoded data.
	 *//*
	public static byte[] decode(char[] in) {
		int iLen = in.length;
		if (iLen % 4 != 0)
			throw new IllegalArgumentException(
					"Length of Base64 encoded input string is not a multiple of 4.");
		while (iLen > 0 && in[iLen - 1] == '=')
			iLen--;
		int oLen = (iLen * 3) / 4;
		byte[] out = new byte[oLen];
		int ip = 0;
		int op = 0;
		while (ip < iLen) {
			int i0 = in[ip++];
			int i1 = in[ip++];
			int i2 = ip < iLen ? in[ip++] : 'A';
			int i3 = ip < iLen ? in[ip++] : 'A';
			if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
				throw new IllegalArgumentException(
						"Illegal character in Base64 encoded data.");
			int b0 = map2[i0];
			int b1 = map2[i1];
			int b2 = map2[i2];
			int b3 = map2[i3];
			if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
				throw new IllegalArgumentException(
						"Illegal character in Base64 encoded data.");
			int o0 = (b0 << 2) | (b1 >>> 4);
			int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
			int o2 = ((b2 & 3) << 6) | b3;
			out[op++] = (byte) o0;
			if (op < oLen)
				out[op++] = (byte) o1;
			if (op < oLen)
				out[op++] = (byte) o2;
		}
		return out;
	}*/
	static final char[] charTab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        .toCharArray();

	public static String encode(byte[] data) {
	    return encode(data, 0, data.length, null).toString();
	}
	
	/** Encodes the part of the given byte array denoted by start and
	 len to the Base64 format.  The encoded data is appended to the
	 given StringBuffer. If no StringBuffer is given, a new one is
	 created automatically. The StringBuffer is the return value of
	 this method. */
	
	public static StringBuffer encode(byte[] data, int start, int len,
	        StringBuffer buf) {
	
	    if (buf == null)
	        buf = new StringBuffer(data.length * 3 / 2);
	
	    int end = len - 3;
	    int i = start;
	    int n = 0;
	
	    while (i <= end) {
	        int d = ((((int) data[i]) & 0x0ff) << 16)
	                | ((((int) data[i + 1]) & 0x0ff) << 8)
	                | (((int) data[i + 2]) & 0x0ff);
	
	        buf.append(charTab[(d >> 18) & 63]);
	        buf.append(charTab[(d >> 12) & 63]);
	        buf.append(charTab[(d >> 6) & 63]);
	        buf.append(charTab[d & 63]);
	
	        i += 3;
	
	        if (n++ >= 14) {
	            n = 0;
	            buf.append("\r\n");
	        }
	    }
	
	    if (i == start + len - 2) {
	        int d = ((((int) data[i]) & 0x0ff) << 16)
	                | ((((int) data[i + 1]) & 255) << 8);
	
	        buf.append(charTab[(d >> 18) & 63]);
	        buf.append(charTab[(d >> 12) & 63]);
	        buf.append(charTab[(d >> 6) & 63]);
	        buf.append("=");
	    } else if (i == start + len - 1) {
	        int d = (((int) data[i]) & 0x0ff) << 16;
	
	        buf.append(charTab[(d >> 18) & 63]);
	        buf.append(charTab[(d >> 12) & 63]);
	        buf.append("==");
	    }
	
	    return buf;
	}
	
	static int decode(char c) {
	
	    if (c >= 'A' && c <= 'Z')
	        return ((int) c) - 65;
	    else if (c >= 'a' && c <= 'z')
	        return ((int) c) - 97 + 26;
	    else if (c >= '0' && c <= '9')
	        return ((int) c) - 48 + 26 + 26;
	    else
	        switch (c) {
	        case '+':
	            return 62;
	        case '/':
	            return 63;
	        case '=':
	            return 0;
	        default:
	            throw new RuntimeException("unexpected code: " + c);
	        }
	}
	
	/** Decodes the given Base64 encoded String to a new byte array.
	 The byte array holding the decoded data is returned. */
	
	public static byte[] decode(String s) {
	
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    try {
	
	        decode(s, bos);
	
	    } catch (IOException e) {
	        throw new RuntimeException();
	    }
	    return bos.toByteArray();
	}
	
	public static void decode(String s, OutputStream os) throws IOException {
	    int i = 0;
	
	    int len = s.length();
	
	    while (true) {
	        while (i < len && s.charAt(i) <= ' ')
	            i++;
	
	        if (i == len)
	            break;
	
	        int tri = (decode(s.charAt(i)) << 18)
	                + (decode(s.charAt(i + 1)) << 12)
	                + (decode(s.charAt(i + 2)) << 6)
	                + (decode(s.charAt(i + 3)));
	
	        os.write((tri >> 16) & 255);
	        if (s.charAt(i + 2) == '=')
	            break;
	        os.write((tri >> 8) & 255);
	        if (s.charAt(i + 3) == '=')
	            break;
	        os.write(tri & 255);
	
	        i += 4;
	    }
	}
	public static void main(String[] args) {
//		try{
//		Base64.decode("rEcsAfCnaUM95NPKud1jHY6TC6u+f27QKyzJXGLpeSEnnAOBObNY6JNXweIoo+9oSvJnKXL/VtVRcDnbKU3B0A/AVxyPMpvynFSZMYLQ1UMxGIZFm2EMf/35yF2s3emvzsjwd1URcraZrj3sT+jsDcFuYpazmyzlCJ4siPAMMDXwy5kALabkIpG55VGszZQCoD523OL2JN1T1ylErDWg8cAmvIX93cQiYfY5lgqYXV8x660UkkSo7y7b9N3lE9VBbt2vlPm6GXo=");
//		}catch(Exception e){
//		log.error(ErrorLogHandler.getStackTraceAsString(e));
//		}
	}

} // end class Base64Coder
