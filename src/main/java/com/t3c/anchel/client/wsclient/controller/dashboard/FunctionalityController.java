package com.t3c.anchel.client.wsclient.controller.dashboard;

import org.apache.log4j.Logger;

import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.services.dashboard.FunctionalityService;

public class FunctionalityController {

	final static Logger OUT = Logger.getLogger(FunctionalityController.class);
	ResponseObject response = null;

	public ResponseObject findAll(String username) {
		OUT.info("Getting rolebased functionalities for user :" + username + " started.");
		response = new ResponseObject();
		try {
			response.setResponseObject(new FunctionalityService().findAll(username));
			response.setStatus(ApplicationConstants.getSuccess());
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		OUT.info("RESP:Getting rolebased functionalities completed.");
		return response;
	}
}
