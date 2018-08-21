package com.t3c.anchel.client.wsclient.controller.dashboard;

import java.io.File;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.services.dashboard.DashboardService;
import com.t3c.anchel.client.wsclient.services.dashboard.WorkgroupService;

public class WorkgroupController {

	final static Logger OUT = Logger.getLogger(WorkgroupController.class);
	ResponseObject response = null;

	public ResponseObject uploadFiles(File file, String username, String workGroupUuid, String folderUuid) {
		OUT.info("Uploading files to workgroup with file name: " + file + " is started.");
		response = new ResponseObject();
		try {
			response = new WorkgroupService().uploadFiles(file, username, workGroupUuid, folderUuid);
		} catch (IOException e) {
			e.printStackTrace();
			OUT.error("Uploading files to workgroup with file name : " + file + " is failed.");
		}
		OUT.info("RESP: Uploading files to workgroup with file name: " + file + " is completed.");
		return response;
	}

	public ResponseObject copyFiles(String groupid, String fileid, String parentid, String username) {
		OUT.info("Copying files to workgroup with file id: " + fileid + " is started.");
		response = new ResponseObject();
		try {
			response = new WorkgroupService().copyFiles(groupid, fileid, parentid, username);
		} catch (JSONException e) {
			e.printStackTrace();
			OUT.error("Copying files to workgroup with file id: " + fileid + " is failed.");
		}
		OUT.info("RESP: Copying files to workgroup with file id: " + fileid + " is completed.");
		return response;
	}

	public ResponseObject getWorkgroups(String username) {
		OUT.info("Getting WorkGroup Details for a user: " + username + " is started.");
		response = new ResponseObject();
		try {
			response.setResponseObject(new WorkgroupService().getWorkgroups(username));
			response.setStatus(ApplicationConstants.getSuccess());
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		OUT.info("RESP: Getting WorkGroup Details for a user: " + username + " is completed.");
		return response;
	}

	public ResponseObject getWorkgroupFolders(String workGroupUuid, String username) {
		OUT.info("Getting WorkGroup Folder Details for a user: " + username + " is started.");
		response = new ResponseObject();
		try {
			response.setResponseObject(new WorkgroupService().getWorkgroupFolders(workGroupUuid, username));
			response.setStatus(ApplicationConstants.getSuccess());
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		OUT.info("RESP: Getting WorkGroup Folder Details for a user: " + username + " is completed.");
		return response;
	}

	public ResponseObject getWorkgroupFiles(String workGroupUuid, String folderUuid, String username) {
		OUT.info("Getting WorkGroup File Details for a user: " + username + " is started.");
		response = new ResponseObject();
		try {
			response.setResponseObject(new WorkgroupService().getWorkgroupFiles(workGroupUuid, folderUuid, username));
			response.setStatus(ApplicationConstants.getSuccess());
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		OUT.info("RESP: Getting WorkGroup File Details for a user: " + username + " is completed.");
		return response;
	}

	public ResponseObject createWorkgroup(String workgroupName, String username) {
		OUT.info("Creating workgroup with a name: " + workgroupName + " is started.");
		response = new ResponseObject();
		try {
			response = new WorkgroupService().createWorkgroup(workgroupName, username);
		} catch (JSONException e) {
			e.printStackTrace();
			response.setStatus(ApplicationConstants.getFailure());
		}
		OUT.info("RESP:Creation of workgroup is completed.");
		return response;
	}

	public ResponseObject createFolder(String folderName, String group, String parent, String username) {
		OUT.info("Creating folder with a name: " + folderName + " is started.");
		response = new ResponseObject();
		try {
			response = new WorkgroupService().createFolder(folderName, group, parent, username);
		} catch (JSONException e) {
			e.printStackTrace();
			response.setStatus(ApplicationConstants.getFailure());
		}
		OUT.info("RESP:Creation of folder is completed.");
		return response;
	}

	public ResponseObject getParentID(String workGroupUuid, String username) {
		OUT.info("Getting parentid for a workgroup: " + workGroupUuid + " is started.");
		response = new ResponseObject();
		try {
			response.setResponseObject(new WorkgroupService().getParentID(workGroupUuid, username));
		} catch (JSONException e) {
			e.printStackTrace();
			response.setStatus(ApplicationConstants.getFailure());
		} catch (JsonParseException e) {
			e.printStackTrace();
			response.setStatus(ApplicationConstants.getFailure());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			response.setStatus(ApplicationConstants.getFailure());
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(ApplicationConstants.getFailure());
		}
		OUT.info("RESP:Getting parentid for a workgroup is completed.");
		return response;
	}

	public ResponseObject deleteNode(String workGroupUuid, String folderUuid, String username) {
		OUT.info("Deleting folder with a uuid: " + folderUuid + " is started.");
		try {
			response = new ResponseObject();
			new WorkgroupService().deleteNode(workGroupUuid, folderUuid, username);
			response.setStatus(ApplicationConstants.getSuccess());
		} catch (Exception e) {
			OUT.error(e.getStackTrace());
			response.setStatus(ApplicationConstants.getFailure());
		}
		OUT.info("RESP:Deleting folder is completed.");
		return response;
	}

	public ResponseObject renameNode(String workGroupUuid, String folderUuid, String renameString, String username) {
		OUT.info("Renaming workgroup node with a uuid: " + workGroupUuid + " is started.");
		response = new ResponseObject();
		response = new WorkgroupService().renameNode(workGroupUuid, folderUuid, renameString, username);
		OUT.info("RESP:Renaming workgroup node is completed.");
		return response;
	}
}
