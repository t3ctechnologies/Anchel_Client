package com.t3c.anchel.client.wsclient.controller.dashboard;

import org.apache.log4j.Logger;

import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.services.dashboard.DashboardService;

public class DashboardController {
	final static Logger OUT = Logger.getLogger(DashboardController.class);
	ResponseObject response = null;

	public ResponseObject getMyFiles(String username) {
		OUT.info("Getting My Files for user :" + username + " started.");
		try {
			response = new ResponseObject();
			response.setResponseObject(new DashboardService().getMyFiles(username));
			response.setStatus(ApplicationConstants.getSuccess());
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		OUT.info("RESP:Getting My Files completed.");
		return response;
	}

	public ResponseObject getReceivedFiles(String username) {
		OUT.info("Getting received files for user: "+ username + " started.");
		try{
			response = new ResponseObject();
			response.setResponseObject(new DashboardService().getReceivedFiles(username));
			response.setStatus(ApplicationConstants.getSuccess());
		}
		catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		OUT.info("RESP:Getting received files completed.");
		return response;
	}

}
