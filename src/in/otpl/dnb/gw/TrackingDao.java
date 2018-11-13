package in.otpl.dnb.gw;

import java.sql.Connection;

public interface TrackingDao {

	public long insert(TrackingDto dto, Connection conn);
}
