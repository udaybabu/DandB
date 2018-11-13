package in.otpl.dnb.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorLogHandler {

	public static String getStackTraceAsString(Exception exception){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.print("Reason : [ ");
		pw.print(exception.getClass().getName());
		pw.print(" ] ");
		pw.print("\nRoot Cause : "+exception.getMessage());
		pw.print("\nCaused By : ");
		String error = sw.toString();
		return error;
	}
	
}
