package in.otpl.dnb.util;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

public class PlatformUtil {

	private static final Logger LOG = Logger.getLogger(PlatformUtil.class);
	private static final String PLACES_API_BASE = "http://maps.googleapis.com/maps/api/geocode/json";

	private static LinkedHashMap<String,Integer> features = new LinkedHashMap<String, Integer>();

	public static final String DATE_FULL_TIME = "dd-MM-yyyy HH:mm:ss";
	public static final String DATE_FULL_TIME2 = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FULL_TIME3 = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DATE_FULL_TIME4 = "yy/MM/dd HH:mm:ss";
	public static final String DATE_FULL_TIME5 = "dd-MM-yyyy hh:mm:ss a";

	public static final String DATE_SHORT_TIME = "dd-MM-yyyy HH:mm";
	public static final String DATE_SHORT_TIME2 = "yyyy-MM-dd HH:mm";
	public static final String DATE_SHORT_TIME3 = "dd-MM-yy HH:mm";
	public static final String DATE_SHORT_TIME4 = "dd-MMM-yy HH:mm";// CB "dd-MMM-yy HH:mm";
	public static final String DATE_SHORT_TIME_MODIFIED = "yyyy-MM-dd HH:mm";// CB "dd-MMM-yy HH:mm";
	public static final String DATE_SHORT_TIME_MODIFIED2 = "dd-MMM-yyyy HH:mm";// CB "dd-MMM-yy HH:mm";
	public static final String DATE_SHORT_TIME5 = "dd-MM-yyyy hh:mm a";
	public static final String DATE_SHORT_TIME6 = "dd-MMM-yy hh:mm a";

	public static final String DATE_MONTH_YEAR = "dd-MMM-yy";
	public static final String DATE_MONTH_YEAR_FORMAT = "dd-MM-yyyy";
	public static final String DATE_MONTH_YEAR2 = "dd.MM.yyyy";
	public static final String DATE_MONTH_YEAR3 = "dd-MM-yyyy";
	public static final String DATE_MONTH_YEAR4 = "dd-MMM-yyyy";

	public static final String YEAR_MONTH_DATE = "yyyy-MM-dd";
	public static final String YEAR_MONTH_DATE2 = "yy/MM/dd";
	public static final String MONTH_DATE = "MMMM dd";
	public static final String MONTH_DATE2 = "MM-dd";
    public static final String YEAR = "yyyy";
    
	public static final String FULL_TIME = "HH:mm:ss";
	public static final String FULL_TIME2 = "hh:mm:ss a";
	public static final String SHORT_TIME = "HH:mm";

	public static final String DATE_SHORT = "dd-MM-yyyy HH:mm";
	public static final String DATE_FULL_TIME12 = "dd-MM-yyyy hh:mm:ss a";
	public static final String DATE_SHORT_TIME12 = "dd-MM-yyyy hh:mm a";
	public static final String FULL_TIME12 = "hh:mm:ss a";
	public static final String SHORT_TIME12 = "hh:mm a";
	public static final String DATE_TIME_FORMAT1 = "dd-MMM-yy hh:mm a";
	public static final String DATE_TIME_FORMAT2 = "dd-MM-yy HH:mm";

	private static final int MINS = 60;
	private static final int SECONDS = 60;
	private static final int MINSDIGIT = 10;
	private static final int HOURSDIGIT = 10;

	public static final String SHORT_TIME2 = "hh:mm a";

	//Real Date to "02-JUN-12"
	public static String getDateMonthYear(){
		Date date = new Date();
		SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR);
		return sdfDestination.format(date);
	}

	public static String getMonthEndDate(int year,int month){
		Calendar c = Calendar.getInstance();      
		c.set(year,month,1); //------>  
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));  
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_MONTH_YEAR);  
		System.out.println("date "+sdf.format(c.getTime()));
		return sdf.format(c.getTime());
	}

	public static String getMonthStratDate(int year,int month){
		Calendar c = Calendar.getInstance();      
		c.set(year,month,1); //------>  
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));  
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_MONTH_YEAR);  
		System.out.println("date "+sdf.format(c.getTime()));
		return sdf.format(c.getTime());
	}

	public static String getTimeForHTML(String time){
		String times="";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_SHORT_TIME4);
			Long timeVal=Long.valueOf(time);
			Date date=new Date(timeVal);
			times = sdf.format(date);
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return times;
	}

	public static String getDateMonthYear(Date date){
		SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR);
		return sdfDestination.format(date);
	}

	public static String getDateMonthYear2(Date date){
		SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR2);
		return sdfDestination.format(date);
	}

	// "dd-MMM-yy" to "yyyy-mm-dd"
	public static String getYearMonthDate(String date){
		try {
			SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_MONTH_YEAR);
			Date Mdate = sdfSource.parse(date);
			SimpleDateFormat sdfDestination = new SimpleDateFormat(YEAR_MONTH_DATE);
			return sdfDestination.format(Mdate);
		} catch (ParseException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return "";
	}

	// "dd-MM-yyyy" to "yyyy-mm-dd"
	public static String getYYYYMMDD(String strDate){
		if(strDate!=null){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_MONTH_YEAR3);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(YEAR_MONTH_DATE);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}
	// "dd-MMM-yyyy" to "yyyy-mm-dd"
	public static String getYearMonthDayDate(String date){
		try {
			SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_MONTH_YEAR4);
			Date Mdate = sdfSource.parse(date);
			SimpleDateFormat sdfDestination = new SimpleDateFormat(YEAR_MONTH_DATE);
			return sdfDestination.format(Mdate);
		} catch (ParseException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return "";
	}
	// "yyyy-mm-dd" to "yyyy-MM-dd"
	public static String getDateMonthYear(String date){
		try {
			SimpleDateFormat sdfSource = new SimpleDateFormat(YEAR_MONTH_DATE);
			Date Mdate = sdfSource.parse(date);
			SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR);
			return sdfDestination.format(Mdate);
		} catch (ParseException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return "";
	}
	// "yyyy-MM-dd" to "dd-MMM-yyyy"
	public static String getDayMonthYearDate(String date){
		try {
			SimpleDateFormat sdfSource = new SimpleDateFormat(YEAR_MONTH_DATE);
			Date Mdate = sdfSource.parse(date);
			SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR4);
			return sdfDestination.format(Mdate);
		} catch (ParseException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return "";
	}
	// "yyyy-mm-dd" to "MMMM dd"
	public static String getFullMonthDate(String date){
		try {
			SimpleDateFormat sdfSource = new SimpleDateFormat(YEAR_MONTH_DATE);
			Date Mdate = sdfSource.parse(date);
			SimpleDateFormat sdfDestination = new SimpleDateFormat(MONTH_DATE);
			return sdfDestination.format(Mdate);
		} catch (ParseException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return "";
	}

	// "yyyy-MM-dd HH:mm:ss" to "dd-MM-yyyy HH:mm"
	public static String getTimestampToSimpleDateTime(String strDate){
		if(strDate != null && !("").equals(strDate.trim())){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_FULL_TIME2);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}

	// "yyyy-MM-dd HH:mm:ss" to "dd-MMM-yyyy"
	public static String getTimestampToDDMMYYYY(String strDate){
		if(strDate != null && !("").equals(strDate.trim())){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_FULL_TIME2);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR4);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}
	
	// "yyyy-MM-dd HH:mm:ss" to "dd-MMM-yyyy HH:mm"
	public static String getTimeSimpleDateTime(String strDate){
		if(strDate != null && !("").equals(strDate.trim())){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_FULL_TIME2);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME_MODIFIED2);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}

	// "dd-MM-yyyy HH:mm" TO "yyyy-MM-dd HH:mm:ss"
	public static String getSimpleDateTimeToTimestamp(String strDate){
		if(strDate!=null){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_SHORT_TIME);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_FULL_TIME2);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}
	
	// "dd-MM-yyyy HH:mm" TO "yyyy-MM-dd "
	public static String getTimestampToSimpleDate(String strDate){
		if(strDate != null && !("").equals(strDate.trim())){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_FULL_TIME2);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}

	// "yyyy-MM-dd HH:mm:ss.SSS" to "dd-MM-yyyy HH:mm"
	public static String getDateTimeInddMMYYYY(String strDate){
		if(strDate!=null){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_FULL_TIME3);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}

	//  "dd-MMM-yyyy HH:mm" to yyyy-MM-dd HH:mm
	public static String getDateTimeInYYYYMMDDHHMM(String strDate){
		if(strDate!=null){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_SHORT_TIME_MODIFIED2);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_FULL_TIME2);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}

	public static String getDateTimeInYYYYMMDD(String strDate){
		if(strDate!=null){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_SHORT_TIME);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME2);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}

	//  "dd-MMM-yyyy HH:mm" to yyyy-MM-dd HH:mm:ss
	public static String getDateTimeInYYYYMMDDTODDMMYYYY(String strDate){
		if(strDate!=null){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_SHORT_TIME4);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME2);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}

	// "yyyy-MM-dd HH:mm:ss.SSS" to "dd-MMM-yyyy HH:mm"
	public static String getDateTimeInddMMMYYYY(String strDate){
		if(strDate!=null){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_FULL_TIME3);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME_MODIFIED2);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}

	// "dd-mm-yyyy HH:mm" to "dd-MMM-yyyy HH:mm" CB
	public static String getDateTimeInddMMMYYYYHHmm(String strDate){
		if(strDate!=null){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_SHORT_TIME);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME_MODIFIED2);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}

	// "yyyy-MM-dd HH:mm:ss.SSS" to "yyyy-MM-dd"
	public static String getDateTimeInDBFormat(String strDate){
		if(strDate!=null){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_FULL_TIME3);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(YEAR_MONTH_DATE);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}


	// "yyyy-MM-dd HH:mm:ss.SSS" to "dd-MM-yyyy "
	public static String getDateTimeddMMYYYY(String strDate){
		if(strDate!=null){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_FULL_TIME3);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR3);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}

	// "yyyy-MM-dd" to "dd-MM-yyyy "
	public static String getDateDDMMYYYY(String strDate){
		if(strDate!=null){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(YEAR_MONTH_DATE);
				Date date = sdfSource.parse(strDate);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR3);
				return sdfDestination.format(date);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
		}
		return "";
	}

	//Real Date to "dd-MM-yyyy HH:mm"
	public static String getDateInddMMYYYY(Date strDate) throws ParseException{
		if(strDate!=null){
			SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME);
			return sdfDestination.format(strDate);
		}
		return "";
	}

	//Real Date to "dd-MMM-yyyy"
	public static String getDateInddMMMYYYY(Date strDate) throws ParseException{
		if(strDate!=null){
			SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR4);
			return sdfDestination.format(strDate);
		}
		return "";
	}

	public static String getTime(String difTime) throws ParseException{
		SimpleDateFormat sdfDestination = new SimpleDateFormat(FULL_TIME);
		Date date = sdfDestination.parse(difTime);
		return sdfDestination.format(date);
	}

	public static String getGraceTime(String entryTime,int minToAdd) throws ParseException{
		SimpleDateFormat sdfDestination = new SimpleDateFormat(SHORT_TIME);
		Date d = sdfDestination.parse(entryTime); 
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MINUTE, minToAdd);
		String newTime = sdfDestination.format(cal.getTime());
		return newTime;
	}

	public static long getDateInLong(String difTime) throws ParseException{
		SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME2);
		Date date = sdfDestination.parse(difTime);
		return date.getTime();
	}
	public static long getDateWithSecInLong(String difTime) throws ParseException{
		SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_FULL_TIME2);
		Date date = sdfDestination.parse(difTime);
		return date.getTime();
	}

	public static Date getDateInDate(String time) throws ParseException{
		SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME2);
		return sdfDestination.parse(time);
	}

	public static long getHrMinInLong(String time) throws ParseException{
		SimpleDateFormat sdfDestination = new SimpleDateFormat(SHORT_TIME);
		Date date = sdfDestination.parse(time);
		return date.getTime();
	}

	public static long getHrMinSecInLong(String difTime) throws ParseException{
		SimpleDateFormat sdfDestination = new SimpleDateFormat(FULL_TIME);
		Date date = sdfDestination.parse(difTime);
		return date.getTime();
	}

	public String getRequiredFormat(Date date){
		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String s1 = formatter.format(date);
		String[] temp = s1.split("-");
		String newdateFormat = temp[0]+"-"+temp[1]+"-"+temp[2]+" "+temp[3]+":"+temp[4]+":"+temp[5];
		return newdateFormat;
	}

	public static String getRequiredFormatForAlert(Date date,int uiHour,int uiMinute){
		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String s1 = formatter.format(date);
		String[] temp = s1.split("-");
		if(String.valueOf(uiHour).length()==1){
			temp[3] = "0"+String.valueOf(uiHour);
		}else{
			temp[3] = String.valueOf(uiHour);
		}
		if(String.valueOf(uiMinute).length()==1){
			temp[4] = "0"+String.valueOf(uiMinute);
		}else{
			temp[4] = String.valueOf(uiMinute);
		}
		String newdateFormat = temp[0]+"-"+temp[1]+"-"+temp[2]+" "+temp[3]+":"+temp[4];
		return newdateFormat;
	}

	//Real Date : "Sat Jun 02 17:45:27 IST 2012" to "yyyy-MM-dd"
	public static String getRealDateToShortYearMonthDate(Date date){
		SimpleDateFormat sdfDestination = new SimpleDateFormat(YEAR_MONTH_DATE);
		return sdfDestination.format(date);
	}

	// "13/06/02 23:00:25" to Real Date : "Sat Jun 02 17:45:27 IST 2012"
	public static Date getShortYearMonthDateWithTimeToRealDate(String dateTime){
		SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_FULL_TIME4);
		Date date = null;
		try {
			date = sdfDestination.parse(dateTime);
		} catch (ParseException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return date;
	}

	//"2012-06-02 17:46:03" to Real Date : "Sat Jun 02 17:45:27 IST 2012"
	public static Date getSQLDateTimeToRealDate(String dateTime){
		SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_FULL_TIME2);
		Date date = null;
		try {
			date = sdfDestination.parse(dateTime);
		} catch (ParseException e) {
			date = new Date();
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return date;
	}

	public static Date getSQLDateToRealDate(String date){
		SimpleDateFormat sdfDestination = new SimpleDateFormat(YEAR_MONTH_DATE);
		Date date1 = null;
		try {
			date1 = sdfDestination.parse(date);
		} catch (ParseException e) {
			date1 = new Date();
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return date1;
	}

	//Real Date : "Sat Jun 02 17:45:27 IST 2012" to "2012-06-02 17:46:03"
	public static String getRealDateToSQLDateTime(Date date){
		SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_FULL_TIME2);
		return sdfDestination.format(date);
	}

	//Real Date : "Sat Jun 02 17:45:27 IST 2012" to "2012-06-02 17:46"
	public static String getRealDateToSQLDateTimeWithoutSec(Date date){
		SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME2);
		return sdfDestination.format(date);
	}

	//Get time as hh:mm
	public static String getTime(Date date){
		SimpleDateFormat sdfDestination = new SimpleDateFormat(FULL_TIME);
		return sdfDestination.format(date);	
	}
	//Get time as hh:mm Activity
	public static String getTimeActivity(Date date){
		SimpleDateFormat sdfDestination = new SimpleDateFormat(SHORT_TIME);
		return sdfDestination.format(date);	
	}

	//Get date as yyyy-MM-dd
	public static String getDate(Date date){
		SimpleDateFormat sdfDestination = new SimpleDateFormat(YEAR_MONTH_DATE);
		return sdfDestination.format(date);	
	}

	public static String emptyString(String field){
		if(field == null || field.trim().equals("null") || field.trim().equals("")){
			return "";
		}else{
			return field.trim();
		}
	}

	public static boolean isNotEmpty(String field){
		if(field != null && !field.trim().equals("null") && !field.trim().equals("")){
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String field){
		if(field == null || field.equals("")){
			return true;
		}
		return false;
	}

	public static String initialCap(String field) {
		String capsName=field.substring(0, 1).toUpperCase() + field.substring(1);
		return capsName;
	}

	public static boolean isNumeric(String field){
		if(field == null || field.equals(""))
			return false;
		String pattern = "^[0-9.]*$";
		return Pattern.matches(pattern, field);
	}

	public static String formatDate(Date date, String format){
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	public static Date getDate(String source) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_SHORT_TIME4);
		return sdf.parse(source);
	}

	public static Date getDateModified(String source) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_SHORT_TIME_MODIFIED);
		return sdf.parse(source);
	}

	public static Date getDate1(String source) throws Exception {
		SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FULL_TIME2);
		return sdf1.parse(source);
	}

	public static Date getDate(String source,SimpleDateFormat format) throws Exception {
		return format.parse(source);
	}

	public static Date getArDate(String source) throws Exception {
		SimpleDateFormat sdfa = new SimpleDateFormat(DATE_SHORT_TIME3);
		return sdfa.parse(source);
	}

	public static String format(Date date,String format){
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	public static double getGreadCircleDistance(double lat1, double lon1, double lat2, double lon2) {
		final int RADIUS_KILOMETERS = 6731;
		final int MAX_RADIUS_VALUE = 1000;
		int r = RADIUS_KILOMETERS; // km
		double dLat = Math.toRadians(lat2-lat1);
		double dLon = Math.toRadians(lon2-lon1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
				Math.sin(dLon/2) * Math.sin(dLon/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		double d = r * c;
		return d * MAX_RADIUS_VALUE;
	}

	public static String getCommaSeperatedString(String[] sArray) {
		StringBuffer sb = new StringBuffer();
		List<String> list = new ArrayList<String>();
		int index=0;
		String fullString = "";
		if (sArray == null || sArray.length == 0 ) {
			return fullString;
		} 

		for (int i = 0; i < sArray.length; i++) {
			if(isEmpty(sArray[i]))
				continue;
			else{
				list.add(sArray[i].trim());
			}
		}

		if(list.size()> 0){
			for(index=0;index<list.size()-1;index++) {
				sb.append(list.get(index));
				sb.append(",");
			}
			sb.append(list.get(index));
			fullString = sb.toString();
		}
		return fullString;
	}

	public static String getCommaSeperatedString(long[] sArray) {
		List<Long> list = new ArrayList<Long>();
		int index=0;
		String fullString = "";
		if (sArray == null || sArray.length == 0 ) {
			return fullString;
		} 
		for (long i:sArray) {
			list.add(i);
		}

		if(list.size()> 0){
			for(index=0;index<list.size();index++) {
				if(fullString != "")
					fullString += ",";
				fullString += list.get(index);
			}
		}
		return fullString;
	}


	public static String getCommaSeperatedString(List<String>list) {
		int index=0;
		final int BUFF_VALUE = 100;
		StringBuffer sb = new StringBuffer(BUFF_VALUE);
		String fullString = "";
		if(list.size()> 0){
			for(index=0;index<list.size()-1;index++) {
				if(list.get(index) == null ||list.get(index) == "")continue;
				sb.append(list.get(index));
				sb.append(", ");
			}
			sb.append(list.get(index));
			fullString = sb.toString();
		}
		return fullString;
	}

	public static String getCommaSeperatedLong(List<Long> list) {
		int index=0;
		final int BUFF_VALUE = 100;
		StringBuffer sb = new StringBuffer(BUFF_VALUE);
		String fullString = "";
		if(list.size()> 0){
			for(index=0;index<list.size()-1;index++) {
				if(list.get(index) == null ||list.get(index) == 0)continue;
				sb.append(list.get(index));
				sb.append(", ");
			}
			sb.append(list.get(index));
			fullString = sb.toString();
		}
		return fullString;
	}

	public static String[] getArrayFromCommaSeperatedString(String commaSeperatedString) {
		String[] values = null;
		commaSeperatedString=commaSeperatedString.replace('[', ',');
		commaSeperatedString=commaSeperatedString.replace(']', ' ');
		commaSeperatedString=commaSeperatedString.replace(';', ',');
		if(commaSeperatedString != null && commaSeperatedString !=""){
			values = commaSeperatedString.split(",");
		}
		return values;
	}


	public static String encode(String stText)throws Exception {
		if(PlatformUtil.isEmpty(stText)){
			return null;
		}
		return Base64.encodeBase64String(stText.getBytes());
	}

	public static String decode(String stBase64Encoded)throws Exception {
		if(PlatformUtil.isEmpty(stBase64Encoded)){
			return null;
		}
		return toString(Base64.decodeBase64(stBase64Encoded));
	}

	private static String toString(byte[] bytes)throws Exception {
		StringBuilder sb = new StringBuilder(bytes.length);
		for( byte b : bytes ) {
			sb.append((char)b);
		}
		return sb.toString();
	}

	public static boolean isAlphanumeric(String field) {
		if(field == null || field == "") 
			return false;
		return (field.matches("[a-zA-Z0-9]*"));
	}

	public static int generateRandomNumber(int noOfDigits){
		int randomNo = 0;
		final int MAX_DIGIT = 11;
		if(noOfDigits > 0  && noOfDigits <MAX_DIGIT){
			Random random = new Random();
			final double baseNo = 10d;
			int lowerLimit = (int)Math.pow(baseNo, (double)(noOfDigits -1));
			int upperLimit = (int)(Math.pow(baseNo, (double)(noOfDigits))) -1;
			do{
				randomNo = random.nextInt(upperLimit);
			}while(randomNo < lowerLimit);
		}
		return randomNo;
	}	

	public static String getRandomNumber(int noOfDigits){
		return RandomStringUtils.randomNumeric(noOfDigits);
	}

	public static String getRandomAlphaNumeric(int noOfDigits){
		return RandomStringUtils.randomAlphanumeric(noOfDigits);
	}

	public static String getRandomAlphabets(int noOfDigits){
		return RandomStringUtils.randomAlphabetic(noOfDigits);
	}

	public static String stripAlphaFromAlphaNumeric(String alphanumeric) {
		String numeric = "";
		if(alphanumeric == null || alphanumeric == "") return alphanumeric;
		numeric = alphanumeric.replaceAll("[a-zA-Z]", "");
		return numeric;
	}

	public static LinkedHashMap<String,String> getItemsPerPageList(){
		LinkedHashMap<String,String> records = new LinkedHashMap<String, String>();
		records.put("0","All");
		records.put("10","10");
		records.put("20","20");
		records.put("30","30");
		records.put("40","40");
		records.put("50","50");
		records.put("60","60");
		records.put("70","70");
		records.put("80","80");
		records.put("90","90");
		records.put("100","100");
		return records;
	}

	public static LinkedHashMap<String,String> getActionType(){
		LinkedHashMap<String,String> records = new LinkedHashMap<String, String>();
		records.put("2","Pending");
		records.put("1","Approved");
		records.put("0","Rejected");
		return records;
	}

	public static LinkedHashMap<String,String> getRemarkType(){
		LinkedHashMap<String,String> records = new LinkedHashMap<String, String>();
		records.put("1","Approve");
		records.put("0","Reject");
		return records;
	}

	public static LinkedHashMap<String,String> getReportType(){
		LinkedHashMap<String,String> records = new LinkedHashMap<String, String>();
		records.put("0", "User(s) Created");
		records.put("5", "Team(s) Created");
		records.put("6", "FormField(s) Created");
		records.put("1", "Form(s) Created");
		records.put("7", "FormGroup(s) Created");
		records.put("4", "Task(s) Created");
		records.put("2", "Message(s) Sent");
		records.put("3", "Activity(s) Received");
		return records;
	}

	public static LinkedHashMap<String,String> getExpiryTimeList(){
		LinkedHashMap<String,String> records = new LinkedHashMap<String, String>();
		records.put("6","6 Hrs");
		records.put("12","12 Hrs");
		records.put("24","1 Day");
		records.put("168","1 Week");
		records.put("720","1 Month");
		records.put("8760","1 Year");
		return records;
	}

	public static LinkedHashMap<String,String> getEndTimeList(){
		LinkedHashMap<String,String> records = new LinkedHashMap<String, String>();
		records.put("12","12");
		records.put("24","24");
		records.put("48","28");
		records.put("72","72");
		return records;
	}

	public static Map<String,String> getHoursList() {
		Map<String,String> hours = new LinkedHashMap<String,String>();

		hours.put("00", "00");
		hours.put("01", "01");
		hours.put("02", "02");
		hours.put("03", "03");
		hours.put("04", "04");
		hours.put("05", "05");
		hours.put("06", "06");
		hours.put("07", "07");
		hours.put("08", "08");
		hours.put("09", "09");
		hours.put("10", "10");
		hours.put("11", "11");
		hours.put("12", "12");
		hours.put("13", "13");
		hours.put("14", "14");
		hours.put("15", "15");
		hours.put("16", "16");
		hours.put("17", "17");
		hours.put("18", "18");
		hours.put("19", "19");
		hours.put("20", "20");
		hours.put("21", "21");
		hours.put("22", "22");
		hours.put("23", "23");

		return hours;
	}

	public static  Map<String,String> getMinutesList() {
		Map<String,String> mins = new LinkedHashMap<String,String>();

		mins.put("00", "00");
		mins.put("01", "01");
		mins.put("02", "02");
		mins.put("03", "03");
		mins.put("04", "04");
		mins.put("05", "05");
		mins.put("06", "06");
		mins.put("07", "07");
		mins.put("08", "08");
		mins.put("09", "09");
		mins.put("10", "10");
		mins.put("11", "11");
		mins.put("12", "12");
		mins.put("13", "13");
		mins.put("14", "14");
		mins.put("15", "15");
		mins.put("16", "16");
		mins.put("17", "17");
		mins.put("18", "18");
		mins.put("19", "19");
		mins.put("20", "20");
		mins.put("21", "21");
		mins.put("22", "22");
		mins.put("23", "23");
		mins.put("24", "24");
		mins.put("25", "25");
		mins.put("26", "26");
		mins.put("27", "27");
		mins.put("28", "28");
		mins.put("29", "29");
		mins.put("30", "30");
		mins.put("31", "31");
		mins.put("32", "32");
		mins.put("33", "33");
		mins.put("34", "34");
		mins.put("35", "35");
		mins.put("36", "36");
		mins.put("37", "37");
		mins.put("38", "38");
		mins.put("39", "39");
		mins.put("40", "40");
		mins.put("41", "41");
		mins.put("42", "42");
		mins.put("43", "43");
		mins.put("44", "44");
		mins.put("45", "45");
		mins.put("46", "46");
		mins.put("47", "47");
		mins.put("48", "48");
		mins.put("49", "49");
		mins.put("50", "50");
		mins.put("51", "51");
		mins.put("52", "52");
		mins.put("53", "53");
		mins.put("54", "54");
		mins.put("55", "55");
		mins.put("56", "56");
		mins.put("57", "57");
		mins.put("58", "58");
		mins.put("59", "59");
		return mins;
	}

	public static LinkedHashMap<String,String> getAlertTimeList(){
		LinkedHashMap<String,String> records = new LinkedHashMap<String, String>();
		records.put("Every 15 min.","15");
		records.put("Every 30 min.","30");
		records.put("Every 1 hr","60");
		records.put("Every 2 hrs","120");
		records.put("Every 3 hrs","180");
		records.put("Every 4 hrs","240");
		records.put("Every 5 hrs","300");
		records.put("Every 6 hrs","360");
		records.put("Every 7 hrs","420");
		records.put("Every 8 hrs","480");
		records.put("Every 9 hrs","540");
		records.put("Every 10 hrs","600");
		records.put("Every 11 hrs","660");
		records.put("Every 12 hrs","720");
		return records;
	}

	public static LinkedHashMap<String,String> getLandMarkRange(){
		LinkedHashMap<String,String> records = new LinkedHashMap<String, String>();
		records.put("2 Km","2");
		records.put("3 Km","3");
		records.put("4 Km","4");
		records.put("5 Km","5");
		return records;
	}

	public static LinkedHashMap<String,String> getRemarkList(){
		LinkedHashMap<String,String> records = new LinkedHashMap<String, String>();
		records.put("NO ACTION","NO ACTION");
		records.put("APPROVED","APPROVE");
		records.put("REJECTED","REJECT");
		return records;
	}

	public static LinkedHashMap<String,String> getStatusType(){
		LinkedHashMap<String,String> records = new LinkedHashMap<String, String>();
		records.put("-1","All");
		records.put("0","Pending");
		records.put("1","Completed");
		records.put("2","Reopen");
		records.put("3","Invalid");
		records.put("4","Incomplete");
		return records;
	}

	public static int getInt(String str){
		return Integer.parseInt(str.trim());
	}

	public static String getSubstring(Object string, int length){
		if(string == null) return "";
		String str = (String)string;
		return str.length() > length ? str.substring(0, length-1) + " ..." : str;
	}

	public static double getDoubleValueFromString(String strValue) {
		double value = 0;
		if(isNumeric(strValue)) {
			value = Double.parseDouble(strValue);
		}
		return value;
	}

	public static boolean compareEnteryAndExitTime(String entry_time,String exit_time){
		boolean status=false;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_SHORT_TIME2);
			Date entryDate = sdf.parse(entry_time);
			Date exitDate = sdf.parse(exit_time);
			if(entryDate.compareTo(exitDate)==0){
				LOG.info("entryDate and exitDate are same");
				status=true;
			}
		}catch(ParseException ex){
			LOG.info(ErrorLogHandler.getStackTraceAsString(ex));
		}
		return status;
	}

	public static String getEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return getRealDateToSQLDateTime(calendar.getTime());
	}

	public static String getStartOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return getRealDateToSQLDateTime(calendar.getTime());
	}
	
	public static String getStartOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return getRealDateToSQLDateTime(calendar.getTime());
	}

	//"yyyy-MM-dd HH:mm:ss" to "dd-MMM-yy"
	public static String getDateMonthYearString(String date){
		String convertedDate="";
		try {
			if(date != null && !date.trim().equals("")){
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_FULL_TIME2);
				Date Mdate = sdfSource.parse(date);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR);
				convertedDate=sdfDestination.format(Mdate);
			}
		} catch (ParseException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return convertedDate;
	}

	//"yyyy-MM-dd HH:mm:ss" to "dd-MM-yyyy"
	public static String getDateMonthYearFormat(String date){
		String convertedDate="";
		try {
			if(date != null && !date.trim().equals("")){
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_FULL_TIME2);
				Date Mdate = sdfSource.parse(date);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR_FORMAT);
				convertedDate=sdfDestination.format(Mdate);
			}
		} catch (ParseException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return convertedDate;
	}

	//"yyyy-MM-dd HH:mm:ss" to "dd-MM-yyyy HH:mm"
	public static String getDateMonthYearHHMM(String date){
		String convertedDate="";
		try {
			if(date != null && !date.trim().equals("")){
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_FULL_TIME2);
				Date Mdate = sdfSource.parse(date);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME);
				convertedDate=sdfDestination.format(Mdate);
			}
		} catch (ParseException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return convertedDate;
	}

	public static String getDateMonthYearStrings(String date){
		String convertedDate="";
		try {
			if(date != null && !date.trim().equals("")){
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_SHORT_TIME2);
				Date Mdate = sdfSource.parse(date);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR);
				convertedDate=sdfDestination.format(Mdate);
			}
		} catch (ParseException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return convertedDate;

	}

	/*public static String getDateMonthYear(String date){
		String convertedDate="";
		try {
			if(date != null && !date.trim().equals("")){
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_MONTH_YEAR3);
				Date Mdate = sdfSource.parse(date);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_SHORT_TIME2);
				convertedDate=sdfDestination.format(Mdate);
			}
		} catch (ParseException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return convertedDate;
	}*/

	public static String getHourMinFromString(String hrMin){
		String convertedHrMin="";
		try {
			if(hrMin != null && !hrMin.trim().equals("")){
				SimpleDateFormat sdfSource = new SimpleDateFormat(SHORT_TIME);
				Date hrMint = sdfSource.parse(hrMin);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(SHORT_TIME);
				convertedHrMin=sdfDestination.format(hrMint);
			}
		} catch (ParseException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return convertedHrMin;
	}

	public static String getDateString(String date){
		String convertedDate="";
		try {
			if(date != null && !date.trim().equals("")){
				SimpleDateFormat sdfSource = new SimpleDateFormat(YEAR_MONTH_DATE);
				Date Mdate = sdfSource.parse(date);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR);
				convertedDate=sdfDestination.format(Mdate);
			}
		} catch (ParseException e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return convertedDate;
	}

	public static String getHourMint(int minutes){
		String convertedDate="";
		String startTime = "00:00";
		int h = minutes / 60 + Integer.valueOf(startTime.substring(0,1));
		int m = minutes % 60 + Integer.valueOf(startTime.substring(3,4));
		String hr=h>9?""+h:"0"+h;
		String min=m>9?""+m:"0"+m;
		convertedDate = hr+":"+min;

		return convertedDate;
	}

	public static int getDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek;
	}

	public void putFeatureKeys(String key,int value){
		features.put(key, value);
	}

	public static LinkedHashMap<String,Integer> getFeatureKeys(){
		features.put(ServerConstants.WEB_CONFIGURATION_GEOFENCE,0);
		features.put(ServerConstants.WEB_CONFIGURATION_SMART_JOB_ZONE,0);
		features.put(ServerConstants.WEB_CONFIGURATION_ALERT,0);
		features.put(ServerConstants.WEB_CONFIGURATION_ATTENDANCE,0);
		features.put(ServerConstants.WEB_CONFIGURATION_CONTENT_MANAGEMENT,0);
		features.put(ServerConstants.WEB_CONFIGURATION_EVENT_DATA_AUTH, 0);
		features.put(ServerConstants.WEB_CONFIGURATION_EVENT_DATA_EDIT, 0);
		features.put(ServerConstants.WEB_CONFIGURATION_CLIENT, 0);
		features.put(ServerConstants.WEB_CONFIGURATION_TASK, 0);
		features.put(ServerConstants.WEB_CONFIGURATION_WORKFLOWS, 0);
		features.put(ServerConstants.WEB_CONFIGURATION_ROUTE, 0);
		/*
		features.put(ServerConstants.WEB_CONFIGURATION_ROUTE,0);
		features.put(ServerConstants.WEB_SALES,0);
		features.put(ServerConstants.WEB_EXPENSE,0);
		features.put(ServerConstants.PHONE_CONFIGURATION_SMS, 0);*/
		return features;
	}

	public static boolean isContain(String source, String subItem){
		String pattern = "\\b"+subItem+"\\b";
		Pattern p=Pattern.compile(pattern);
		Matcher m=p.matcher(source);
		return m.find();
	}

	public static String getAddressBylatlng(String lat, String lon) {
		String finalAdress = "";
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);
			sb.append("?latlng="+lat+","+lon);
			sb.append("&sensor=true");
			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);

			}
			String addressData = jsonResults.toString();
			if(addressData != null && !addressData.equals("")){
				JSONObject jobj = JSONObject.fromObject(addressData);
				JSONArray array = jobj.getJSONArray("results");
				if(array.size() > 0)
					finalAdress = array.getJSONObject(0).getString("formatted_address");
				else
					LOG.info("Address not found from ("+lat+","+lon+")");
			}

		} catch (Exception e) {
			LOG.info("Address not found from ("+lat+","+lon+") "+e.getMessage());
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return finalAdress;
	}

	private static final String[] HEADERS_TO_TRY = { 
		"X-Forwarded-For",
		"Proxy-Client-IP",
		"WL-Proxy-Client-IP",
		"HTTP_X_FORWARDED_FOR",
		"HTTP_X_FORWARDED",
		"HTTP_X_CLUSTER_CLIENT_IP",
		"HTTP_CLIENT_IP",
		"HTTP_FORWARDED_FOR",
		"HTTP_FORWARDED",
		"HTTP_VIA",
	"REMOTE_ADDR" };

	public static String getClientIpAddress(HttpServletRequest request) {
		for (String header : HEADERS_TO_TRY) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}
		return request.getRemoteAddr();
	}

	public static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int)length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}
		is.close();
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+file.getName());
		}
		return bytes;
	}

	public static String fileDataBase64(File file) throws IOException{
		byte[] bytes = PlatformUtil.loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		return new String(encoded);
	}

	//Minutes to HH:mm
	public static String formatTime(int timeInMinute){
		String hourMinsStr = "00:00";
		if(timeInMinute > 0){			
			int mins = timeInMinute % SECONDS;
			int hrs = (timeInMinute - mins) / MINS;	
			if (mins < MINSDIGIT)
				mins = Integer.valueOf("0" + mins);
			if (hrs < HOURSDIGIT)
				hrs = Integer.valueOf("0" + hrs);
			hourMinsStr = hrs + ":" + mins;
		}
		return hourMinsStr;
	}

	//HH:mm to Minutes
	public static int formatTime(String timeInMinute){
		int time = 0;
		StringTokenizer st = new StringTokenizer(timeInMinute, ":,' '");
		while (st.hasMoreTokens()) {
			String hrs = st.nextToken();
			String min = st.nextToken();
			time = (Integer.valueOf(hrs) * MINS) + Integer.valueOf(min);
		}
		return time;
	}


	public Map<String, String> getGraceList() {
		Map<String, String> minutes = new LinkedHashMap<String, String>();
		minutes.put("00", "00");
		minutes.put("05", "05");
		minutes.put("10", "10");
		minutes.put("15", "15");
		minutes.put("20", "20");
		minutes.put("25", "25");
		minutes.put("30", "30");
		minutes.put("35", "35");
		minutes.put("40", "40");
		minutes.put("45", "45");
		minutes.put("50", "50");
		minutes.put("55", "55");
		minutes.put("60", "60");
		return minutes;
	}

	public Map<String, String> getLunchTimeList() {
		Map<String, String> minutes = new LinkedHashMap<String, String>();
		minutes.put("00", "00");
		minutes.put("30", "30");
		minutes.put("60", "60");

		return minutes;
	}

	public Map<String, String> getYearList() {
		Map<String, String> years = new LinkedHashMap<String, String>();
		years.put("2010", "2010");
		years.put("2011", "2011");
		years.put("2012", "2012");
		years.put("2013", "2013");
		years.put("2014", "2014");
		years.put("2015", "2015");
		years.put("2016", "2016");
		years.put("2017", "2017");
		years.put("2018", "2018");
		years.put("2019", "2019");
		years.put("2020", "2020");
		years.put("2021", "2021");

		return years;
	}

	public Map<String, String> getMonthList() {
		Map<String, String> months = new LinkedHashMap<String, String>();
		months.put("Jan", "0");
		months.put("Feb", "1");
		months.put("Mar", "2");
		months.put("Apr", "3");
		months.put("May", "4");
		months.put("Jun", "5");
		months.put("Jul", "6");
		months.put("Aug", "7");
		months.put("Sep", "8");
		months.put("Oct", "9");
		months.put("Nov", "10");
		months.put("Dec", "11");

		return months;
	}

	public Map<String, String> getHolidayTypeList() {
		Map<String, String> minutes = new LinkedHashMap<String, String>();
		minutes.put("None", "0");
		minutes.put("All", "1");
		minutes.put("Alternate", "2");

		return minutes;
	}

	public Map<String, String> getAlterTypeList() {
		Map<String, String> minutes = new LinkedHashMap<String, String>();
		minutes.put("None", "0");
		minutes.put("First & Third", "1");
		minutes.put("Second & Fourth", "2");

		return minutes;
	}

	public static int CurrentFYYear(){
		Calendar now = Calendar.getInstance();
		return now.get(Calendar.YEAR) - (now.get(Calendar.MONTH) < 4?1:0);
		/*String start=year + "-04-01";
		String end=(year+1) + "-03-31";*/
	}

	public static String CurrentMonthYear(){
		Calendar cal = Calendar.getInstance();
		return new SimpleDateFormat("MMM").format(cal.getTime())+", "+cal.get(Calendar.YEAR);
	}

	public static String MonthYear(Calendar cal){
		return new SimpleDateFormat("MMM").format(cal.getTime())+", "+cal.get(Calendar.YEAR);
	}

	public static String sha(String key){
		String value = "";
		try{
			if(key != null && !key.equals("")){
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.update(key.getBytes());
				byte byteData[] = md.digest();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < byteData.length; i++) {
					sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
				}
				value = sb.toString();
			}
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
		return value;
	}

	//Real Date : "Sat Jun 02 17:45:27 IST 2012" to "06-02"
	public static String getRealDateToMonthDate(Date date){
		SimpleDateFormat sdfDestination = new SimpleDateFormat(MONTH_DATE2);
		return sdfDestination.format(date);
	}
	// "yyyy-MM-dd HH:mm:ss.SSS" to "dd-MMM-yyyy "
		public static String getDateTimeddMMMYYYY(String strDate){
			if(strDate!=null){
				try {
					SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_FULL_TIME3);
					Date date = sdfSource.parse(strDate);
					SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR4);
					return sdfDestination.format(date);
				} catch (ParseException e) {
					LOG.error(ErrorLogHandler.getStackTraceAsString(e));
				}
			}
			return "";
		}
		// "dd-MMM-yyyy" to "dd-MM-yyyy"
		public static String getDayMonthYear(String date){
			try {
				SimpleDateFormat sdfSource = new SimpleDateFormat(DATE_MONTH_YEAR);
				Date Mdate = sdfSource.parse(date);
				SimpleDateFormat sdfDestination = new SimpleDateFormat(DATE_MONTH_YEAR_FORMAT);
				return sdfDestination.format(Mdate);
			} catch (ParseException e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
			return "";
		}
	public static String fileType(String type) {
		String fileType = "application/octet";
		switch (type) {
		case "jpg":
			fileType = "image/jpg";
			break;
		case "jpeg":
			fileType = "image/jpeg";
			break;
		case "png":
			fileType = "image/png";
			break;
		case "pdf":
			fileType = "application/pdf";// application/pdf, application/x-pdf,
			// application/acrobat,
			// applications/vnd.pdf, text/pdf,
			// text/x-pdf
			break;
		default:
			break;
		}
		return fileType;
	}
/*	public static UserDto[] getActiveUsers() {
		UseDto[] allUsers= ProfileCache.getInstance().get().getUsers();
		LinkedList<UserDto> activeUser = new LinkedList<UserDto>();
		if(allUsers != null){
			for(User user : allUsers) {
				if(user.getStatus() == 1) {
					activeUser.add(user);
				}
			}
		}
		UserDto[] activeUserArray = new UserDto[activeUser.size()];
		return activeUser.toArray(activeUserArray);
	}*/

}