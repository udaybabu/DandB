package in.otpl.dnb.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.ResourceManager;

@Repository
public class PhoneConfigurationDaoImpl implements PhoneConfigurationDao{

private static final Logger LOG = Logger.getLogger(PhoneConfigurationDaoImpl.class);

        private final String SQL_SELECT = "SELECT id, customer_id, param_name, string_param_value, int_param_value, creation_time, modification_time FROM " + getTableName() + "";	

        public String getTableName(){
    		return "phone_configuration";
    	}
        
	@Autowired
	private ResourceManager resourceManager;
	
	@Override
	public PhoneConfigurationDto[] findByDynamicWhere(String sql, Object[] sqlParams,Connection conn) throws Exception{
		PreparedStatement stmt = null;
		PhoneConfigurationDto[] dto = null;
		ResultSet rs = null;
		try {
			final String SQL = SQL_SELECT + " WHERE " + sql;
			stmt = conn.prepareStatement( SQL );
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
			rs = stmt.executeQuery();
			return fetchMultiResults(rs);
		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally {
			resourceManager.close(rs);
			resourceManager.close(stmt);
		}
		return dto;
	}
	
	private PhoneConfigurationDto[] fetchMultiResults(ResultSet rs) throws SQLException{
		List<PhoneConfigurationDto> resultList = new ArrayList<PhoneConfigurationDto>();
		while (rs.next()) {
			PhoneConfigurationDto dto = new PhoneConfigurationDto();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		PhoneConfigurationDto[] ret = new PhoneConfigurationDto[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	private void populateDto(PhoneConfigurationDto dto, ResultSet rs) throws SQLException{
		dto.setId(rs.getLong("id"));
		dto.setCustomerId(rs.getInt("customer_id"));
		dto.setParamName(rs.getString("param_name"));
		dto.setStringParamValue(rs.getString("string_param_value"));
		dto.setIntParamValue(rs.getInt("int_param_value"));
		dto.setCreationTime(rs.getString("creation_time"));
		dto.setModificationTime(rs.getString("modification_time"));
	}
	
}
