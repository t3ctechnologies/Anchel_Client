package com.t3c.anchel.client.wsclient.controller.dashboard;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.model.dashboard.JTableDto;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.services.dashboard.DashboardService;

public class DashboardController {
	final static Logger OUT = Logger.getLogger(DashboardController.class);
	ResponseObject response = null;

	public ResponseObject getMyFiles(String username) {
		OUT.info("Getting My Files for user :" + username + " is started.");
		try {
			response = new ResponseObject();
			response.setResponseObject(new DashboardService().getMyFiles(username));
			response.setStatus(ApplicationConstants.getSuccess());
			OUT.info("RESP:Getting My Files is completed.");
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		return response;
	}

	public ResponseObject getReceivedFiles(String username) {
		OUT.info("Getting received files for user: " + username + " is started.");
		try {
			response = new ResponseObject();
			response.setResponseObject(new DashboardService().getReceivedFiles(username));
			response.setStatus(ApplicationConstants.getSuccess());
			OUT.info("RESP:Getting received files is completed.");
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		return response;
	}

	public ResponseObject deleteMyFiles(String groupuuid, String uuid, String username, String selectedNodeName) {
		OUT.info("Deleting file with a uuid: " + uuid + " is started.");
		try {
			response = new ResponseObject();
			new DashboardService().deleteMyFiles(groupuuid, uuid, username, selectedNodeName);
			response.setStatus(ApplicationConstants.getSuccess());
			OUT.info("RESP:Deleting file is completed.");
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		return response;
	}

	public ResponseObject downloadMyFiles(String groupid, String uuid, String username, String selectedNodeName) {
		OUT.info("Downloading file with a uuid: " + uuid + " is started.");
		response = new ResponseObject();
		try {
			response = new DashboardService().downloadMyFiles(groupid, uuid, username, selectedNodeName);
			OUT.info("RESP:Downloading file is completed.");
		} catch (IOException e) {
			response.setStatus(ApplicationConstants.getFailure());
			OUT.error(e.getStackTrace());
		}
		return response;
	}

	public ResponseObject renameMyFiles(String groupid, String uuid, String username, String renameString) {
		OUT.info("Renaming file with a uuid: " + uuid + " is started.");
		response = new ResponseObject();
		try {
			response = new DashboardService().renameMyFiles(groupid, uuid, username, renameString);
			OUT.info("RESP:Renaming file is completed.");
		} catch (JSONException e) {
			OUT.error(e.getStackTrace());
		}
		return response;
	}

	public ResponseObject uploadMyFiles(File file, String username) {
		OUT.info("Uploading files to myfiles with file name: " + file + " is started.");
		response = new ResponseObject();
		try {
			response = new DashboardService().uploadMyFiles(file, username);
			OUT.info("RESP: Uploading files to myfiles with file name: " + file + " is completed.");
		} catch (IOException e) {
			OUT.error(e.getStackTrace());
		}
		return response;
	}

	public ResponseObject getFileActivity(JTableDto tableDto, String username) {
		OUT.info("Getting files activity for user: " + username + " started.");
		try {
			response = new ResponseObject();
			response.setResponseObject(new DashboardService().getFileActivity(tableDto, username));
			response.setStatus(ApplicationConstants.getSuccess());
			OUT.info("RESP:Getting file activity is completed.");
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		return response;
	}
}
