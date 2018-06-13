package com.t3c.anchel.client.wsclient.controller.dashboard;

import java.io.File;
import java.io.IOException;

import javax.swing.JProgressBar;

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
		OUT.info("Getting received files for user: " + username + " started.");
		try {
			response = new ResponseObject();
			response.setResponseObject(new DashboardService().getReceivedFiles(username));
			response.setStatus(ApplicationConstants.getSuccess());
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		OUT.info("RESP:Getting received files completed.");
		return response;
	}

	public ResponseObject deleteMyFiles(String uuid, String username, String selectedNodeName) {
		OUT.info("Deleting myfiles file with a uuid: " + uuid + " is started.");
		try {
			response = new ResponseObject();
			response.setResponseObject(new DashboardService().deleteMyFiles(uuid, username, selectedNodeName));
			response.setStatus(ApplicationConstants.getSuccess());
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		OUT.info("RESP:Deleting myfiles file is completed.");
		return response;
	}

	public ResponseObject downloadMyFiles(String uuid, String username, String selectedNodeName) {
		OUT.info("Downloading myfiles file with a uuid: " + uuid + " is started.");
		response = new ResponseObject();
			try {
				response = new DashboardService().downloadMyFiles(uuid, username, selectedNodeName);
			} catch (IOException e) {
				response.setStatus(ApplicationConstants.getFailure());
				e.printStackTrace();
			}
		OUT.info("RESP:Downloading myfiles file is completed.");
		return response;
	}

	public ResponseObject renameMyFiles(String uuid, String username, String renameString) {
		OUT.info("Renaming myfiles file with a uuid: " + uuid + " is started.");
		response = new ResponseObject();
			response = new DashboardService().renameMyFiles(uuid, username, renameString);
		OUT.info("RESP:Renaming myfiles file is completed.");
		return response;
	}

	public ResponseObject uploadMyFiles(File file, String username) {
		OUT.info("Uploading files to myfiles with file name: " + file + " is started.");
		response = new ResponseObject();
			response = new DashboardService().uploadMyFiles(file, username);
			OUT.info("RESP: Uploading files to myfiles with file name: " + file + " is completed.");
		return response;
	}

}
