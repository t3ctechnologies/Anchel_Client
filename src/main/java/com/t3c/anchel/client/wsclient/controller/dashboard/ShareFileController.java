package com.t3c.anchel.client.wsclient.controller.dashboard;

import org.apache.log4j.Logger;

import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.model.dashboard.ShareCreationDto;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.services.dashboard.ShareFileService;

public class ShareFileController {

	final static Logger OUT = Logger.getLogger(ShareFileController.class);
	ResponseObject response = null;

	public ResponseObject getAllUsers(String username) {
		OUT.info("Getting ldap users email id's  is started.");
		response = new ResponseObject();
		try {
			response.setResponseObject(new ShareFileService().getAllUsers(username));
			response.setStatus(ApplicationConstants.getSuccess());
			OUT.info("RESP:Getting ldap users email id's is completed.");
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		return response;
	}

	public ResponseObject shareFile(ShareCreationDto shareDto, String username) {
		OUT.info("File sharing is started.");
		response = new ResponseObject();
		try {
			response.setResponseObject(new ShareFileService().shareFile(shareDto, username));
			response.setStatus(ApplicationConstants.getSuccess());
			OUT.info("RESP:File sharing is completed.");
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		return response;
	}
	
	public ResponseObject shareFileDetails(String username, String uuid) {
		OUT.info("Getting shared file details is started.");
		response = new ResponseObject();
		try {
			response.setResponseObject(new ShareFileService().shareFileDetails(username, uuid));
			response.setStatus(ApplicationConstants.getSuccess());
			OUT.info("RESP:Getting shared file details is completed.");
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		return response;
	}
}
