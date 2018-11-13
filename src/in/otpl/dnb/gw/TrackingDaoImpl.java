package in.otpl.dnb.gw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.PlatformUtil;
import in.otpl.dnb.util.ResourceManager;


@Repository
public class TrackingDaoImpl implements TrackingDao{
	 
	private static final Logger LOG = Logger.getLogger(TrackingDaoImpl.class);

	private final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( id, customer_id, lattitude, longitude, altitude, accuracy, speed, direction, client_time, db_time, user_id, landmark_id, event_name, is_valid, is_cellsite, cellsite_loc, status ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	public String getTableName(){
		return "tracking";
	}
	
	@Autowired
	private ResourceManager resourceManager;
	
	@Override
	public long insert(TrackingDto dto, Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement( SQL_INSERT, Statement.RETURN_GENERATED_KEYS );
			int index = 1;
			stmt.setLong( index++, dto.getId() );
			stmt.setInt( index++, dto.getCustomerId() );
			stmt.setString( index++, dto.getLattitude() );
			stmt.setString( index++, dto.getLongitude() );
			stmt.setString( index++, dto.getAltitude() );
			stmt.setString( index++, dto.getAccuracy() );
			stmt.setString( index++, dto.getSpeed() );
			stmt.setString( index++, dto.getDirection() );
			stmt.setString(index++,	PlatformUtil.getRealDateToSQLDateTime(new Date()));
			stmt.setString(index++,	PlatformUtil.getRealDateToSQLDateTime(new Date()));
			stmt.setLong( index++, dto.getUserId() );
			if (dto.isLandmarkIdNull()) {
				stmt.setNull( index++, java.sql.Types.INTEGER );
			} else {
				stmt.setInt( index++, dto.getLandmarkId() );
			}
			stmt.setString( index++, dto.getEventName() );
			stmt.setInt( index++, dto.getIsValid() );
			stmt.setShort( index++, dto.getIsCellsite() );
			stmt.setString(index++, dto.getCellsiteLocAddres());
			stmt.setString(index++, dto.getStatus());
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setId( rs.getInt( 1 ) );
			}
			
		} catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		} finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);	
		}
		return dto.getId();
	}
}