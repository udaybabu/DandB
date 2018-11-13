package in.otpl.dnb.scheduler;

import in.otpl.dnb.logic.CommonLogic;
import in.otpl.dnb.util.ConfigManager;
import in.otpl.dnb.util.ErrorLogHandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonSchedulerService {

	private static final Logger LOG = Logger.getLogger(CommonSchedulerService.class);

	@Autowired
	private CommonLogic commonLogic;
	@Autowired
	private ConfigManager configManager;
	
	public void masterDataFetch() {
		if(configManager.isSchedulerMasterDataFetch()){
			LOG.info("Master Data Fetch Scheduler");
			try {
				int customerId = configManager.getDnbCustId();
				//int interval = configManager.getSchedulerMasterDataFetchInterval();
				commonLogic.masterDataFetch(customerId);
			} catch (Exception e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
			LOG.info("Master Data Fetch Scheduler Finished");
		}
	}

	public void enquiryPoolFetch() {
		if(configManager.isSchedulerEnquiryPoolFetch()){
			LOG.info("Enquiry Pool Fetch Scheduler");
			try {
				int customerId = configManager.getDnbCustId();
				commonLogic.enquiryPoolFetch(customerId);
			} catch (Exception e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
			LOG.info("Enquiry Pool Fetch Scheduler Finished");
		}
	}
	
	public void pdfCreator() {
		if(configManager.isSchedulerPdfCreator()){
			LOG.info("PDF Creator Scheduler");
			try{
				int customerId = configManager.getDnbCustId();
				/*commonLogic.pdfCreator(customerId);*/
			}catch(Exception e){
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
			LOG.info("PDF Creator Scheduler Finished");
		}
	}

	public void dataManipulator(){
		if(configManager.isSchedulerDataManipulate()){
			LOG.info("Data Manipulator Scheduler");
			try{
				int customerId = configManager.getDnbCustId();
				int dnbMasterId = 0;
				commonLogic.dataManipulator(customerId,dnbMasterId);
			}catch(Exception e){
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
			LOG.info("Data Manipulator Scheduler Finished");
		}
	}

	public void enquiryDataCompletion(){
		if(configManager.isSchedulerEnquiryDataCompletion()){
			LOG.info("Enquiry Data Completion Scheduler");
			try {
				int customerId = configManager.getDnbCustId();
				commonLogic.enquiryDataCompletion(customerId);
			} catch (Exception e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
			LOG.info("Enquiry Data Completion Scheduler Finished");
		}
	}

	public void reassignment() {
		if(configManager.isSchedulerReassignment()){
			LOG.info("Reassignment Scheduler");
			try {
				int customerId = configManager.getDnbCustId();
				commonLogic.reassignment(customerId);
			} catch (Exception e) {
				LOG.error(ErrorLogHandler.getStackTraceAsString(e));
			}
			LOG.info("Reassignment Scheduler Finished");
		}
	}

}
