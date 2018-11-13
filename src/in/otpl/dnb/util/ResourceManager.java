package in.otpl.dnb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class ResourceManager {

	private static final Logger LOG = Logger.getLogger(ResourceManager.class);
	
	@Autowired
	private ConfigManager configManager;
	
	private DataSource dataSource;
	private MessageSource messageSource;
	
	public synchronized Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

	public void close(Connection conn){
		try{
			if(conn != null)
				conn.close();
		}catch(Exception e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}

	public void close(PreparedStatement pstmt){
		try {
			if (pstmt != null)
				pstmt.close();
		}catch (SQLException sqle){
			LOG.error(sqle);
		}
	}

	public void close(Statement stmt){
		try {
			if (stmt != null)
				stmt.close();
		}catch (SQLException e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}

	public void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		}catch (SQLException e){
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}
	}
	
	public String getMessage(String key){
		Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, new Object[0], locale);
	}
	
	public String getMessage(String key, Object[] args){
		Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, locale);
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public MessageSource getMessageSource() {
		return messageSource;
	}
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public synchronized Connection getDnbConnection() throws SQLException, ClassNotFoundException {
		String dnburl = configManager.getDnbDbUrl();
		String dnbport = configManager.getDnbDbPort();
		String dnbdatabase = configManager.getDnbDbName();
		String dnbuser = configManager.getDnbDbUser();
		String dnbpass = configManager.getDnbDbPassword();
		String dbClass = configManager.getDnbDbClass();
		Class.forName(dbClass);
		String dbURL = "jdbc:sqlserver://"+dnburl+":"+dnbport+";DatabaseName="+dnbdatabase;
		return DriverManager.getConnection(dbURL, dnbuser, dnbpass);
	}

}