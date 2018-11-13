package in.otpl.dnb.settings;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.otpl.dnb.util.ErrorLogHandler;
import in.otpl.dnb.util.ResourceManager;


@Service
public class PhoneConfigurationLogicImpl implements PhoneConfigurationLogic{
	
	private static final Logger LOG = Logger.getLogger(PhoneConfigurationLogicImpl.class);
	
	@Autowired
	private ResourceManager resourceManager;
	@Autowired
	private PhoneConfigurationDao phoneConfigurationDao;
	
	@Override
	public int getIntParamValue(String paramName,int custId) {
		int paramValue = 0;
		Connection conn = null;
		try {
			conn = resourceManager.getConnection();
			Object[] sqlParams = new Object[]{paramName,custId};
			String sql = " param_name = ? AND customer_id = ? ";
			PhoneConfigurationDto[] dto = phoneConfigurationDao.findByDynamicWhere(sql, sqlParams,conn);
			
			if(dto != null && dto.length > 0) {
				paramValue = dto[0].getIntParamValue();
			}
 		}catch (Exception e) {
			LOG.error(ErrorLogHandler.getStackTraceAsString(e));
		}finally{
			resourceManager.close(conn);
		}
		return paramValue;
	}

}
