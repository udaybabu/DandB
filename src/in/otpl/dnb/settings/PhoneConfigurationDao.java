package in.otpl.dnb.settings;

import java.sql.Connection;

public interface PhoneConfigurationDao {

	public PhoneConfigurationDto[] findByDynamicWhere(String sql, Object[] sqlParams,Connection conn) throws Exception;
}
