package com.t3c.anchel.client.wsclient.services.dashboard;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.model.dashboard.AuthorDetailsDTO;
import com.t3c.anchel.client.model.dashboard.WorkGroupDTO;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.controller.auth.UserSessionCache;

public class WorkgroupService {

	final static Logger OUT = Logger.getLogger(WorkgroupService.class);
	ObjectMapper mapper = null;
	StringBuilder urlBuffer = null;
	ResponseObject response = null;

	public ResponseObject uploadFiles(File file, String username, String groupid, String folderid) throws IOException {
		String folderUuid;
		if (folderid == null) {
			folderUuid = new String("");
		} else {
			folderUuid = folderid;
		}
		mapper = new ObjectMapper();
		response = new ResponseObject();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");
		String identifier = UUID.randomUUID().toString();
		String fileSize = String.valueOf(file.length());
		String fileName = file.getName();

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.UPLOAD_FILE);
		final ClientConfig config = new DefaultClientConfig();
		Client restClient = Client.create(config);
		WebResource webResource = restClient.resource(urlBuffer.toString());

		FileInputStream fis = null;
		FormDataBodyPart fileDataBodyPart;
		ByteArrayInputStream bis;
		int chunksize = 1048576;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		int fileSize1 = (int) file.length();
		ClientResponse resp = null;
		byte[] data_byte = null;
		int bytesRead;
		if (fileSize1 > 1048576) {
			int parts = fileSize1 / 1048576;
			for (int i = 1; i <= parts; i++) {
				if (i == parts) {
					int remsize = fileSize1 - (1048576 * parts);
					chunksize = 1048576 + remsize;
				}
				data_byte = new byte[chunksize];
				bytesRead = fis.read(data_byte);
				bis = new ByteArrayInputStream(data_byte, 0, bytesRead);
				fileDataBodyPart = new FormDataBodyPart("file", bis, MediaType.APPLICATION_OCTET_STREAM_TYPE);
				final MultiPart multiPart = new FormDataMultiPart()
						.field("flowChunkNumber", String.valueOf(i), MediaType.TEXT_PLAIN_TYPE)
						.field("asyncTask", "true", MediaType.TEXT_PLAIN_TYPE)
						.field("workGroupUuid", groupid, MediaType.TEXT_PLAIN_TYPE)
						.field("workGroupParentNodeUuid", folderUuid, MediaType.TEXT_PLAIN_TYPE)
						.field("flowChunkSize", "1048576", MediaType.TEXT_PLAIN_TYPE)
						.field("flowTotalChunks", String.valueOf(parts), MediaType.TEXT_PLAIN_TYPE)
						.field("flowIdentifier", identifier, MediaType.TEXT_PLAIN_TYPE)
						.field("flowCurrentChunkSize", String.valueOf(chunksize), MediaType.TEXT_PLAIN_TYPE)
						.field("flowTotalSize", fileSize, MediaType.TEXT_PLAIN_TYPE)
						.field("flowFilename", fileName, MediaType.TEXT_PLAIN_TYPE)
						.field("flowRelativePath", fileName, MediaType.TEXT_PLAIN_TYPE).bodyPart(fileDataBodyPart);

				resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
						.type("multipart/form-data").post(ClientResponse.class, multiPart);

			}
		} else {
			fileDataBodyPart = new FormDataBodyPart("file", fis, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			final MultiPart multiPart = new FormDataMultiPart().field("flowChunkNumber", "1", MediaType.TEXT_PLAIN_TYPE)
					.field("asyncTask", "true", MediaType.TEXT_PLAIN_TYPE)
					.field("workGroupUuid", groupid, MediaType.TEXT_PLAIN_TYPE)
					.field("workGroupParentNodeUuid", folderUuid, MediaType.TEXT_PLAIN_TYPE)
					.field("flowChunkSize", "1048576", MediaType.TEXT_PLAIN_TYPE)
					.field("flowTotalChunks", "1", MediaType.TEXT_PLAIN_TYPE)
					.field("flowIdentifier", identifier, MediaType.TEXT_PLAIN_TYPE)
					.field("flowCurrentChunkSize", fileSize, MediaType.TEXT_PLAIN_TYPE)
					.field("flowTotalSize", fileSize, MediaType.TEXT_PLAIN_TYPE)
					.field("flowFilename", fileName, MediaType.TEXT_PLAIN_TYPE)
					.field("flowRelativePath", fileName, MediaType.TEXT_PLAIN_TYPE).bodyPart(fileDataBodyPart);

			resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
					.type("multipart/form-data").post(ClientResponse.class, multiPart);
		}

		JsonNode rootNode = mapper.readTree(resp.getEntity(String.class));
		String lastChunk = rootNode.get("lastChunk").toString();
		if (resp.getStatus() == 200 && lastChunk.equals("true")) {
			OUT.info("File uploading is success");
			response.setStatus(ApplicationConstants.getSuccess());
			fis.close();
			return response;
		} else {
			OUT.error("File upload is failed " + resp.getStatusInfo());
			response.setStatus(ApplicationConstants.getFailure());
			fis.close();
			return response;
		}
	}

	public ResponseObject copyFiles(String groupid, String fileid, String parentid, String username)
			throws JSONException {

		mapper = new ObjectMapper();
		response = new ResponseObject();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.WORK_GROUP);
		urlBuffer.append("/" + groupid + "/nodes/" + fileid + "/copy?destinationNodeUuid=" + parentid);

		JSONObject jObject = new JSONObject();
		jObject.put("type", "DOCUMENT");

		final ClientConfig config = new DefaultClientConfig();
		Client restClient = Client.create(config);
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.type("application/json").post(ClientResponse.class, jObject.toString());
		if (resp.getStatus() != 200) {
			OUT.error("File Renaming is Failed");
			response.setStatus(ApplicationConstants.getFailure());
		} else {
			response.setStatus(ApplicationConstants.getSuccess());
		}
		if (restClient != null) {
			restClient.destroy();
		}
		return response;
	}

	public List<WorkGroupDTO> getWorkgroups(String username)
			throws JsonParseException, JsonMappingException, IOException {
		mapper = new ObjectMapper();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.WORK_GROUP);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.get(ClientResponse.class);
		if (resp.getStatus() != 200) {
			OUT.error("Unable to connect to the server");
		}
		String jsonString = resp.getEntity(String.class);
		List<WorkGroupDTO> workGroupDTOs = mapper.readValue(jsonString, new TypeReference<List<WorkGroupDTO>>() {
		});
		if (restClient != null) {
			restClient.destroy();
		}
		OUT.debug("Getting a workgroup for user :" + username + " found :"
				+ (workGroupDTOs.size() > 0 ? workGroupDTOs.size() : "NOT FOUND"));
		return workGroupDTOs;
	}

	public List<WorkGroupDTO> getWorkgroupFolders(String workGroupUuid, String username)
			throws JsonParseException, JsonMappingException, IOException {
		mapper = new ObjectMapper();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.WORK_GROUP);
		urlBuffer.append("/" + workGroupUuid + "/nodes");
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.get(ClientResponse.class);
		if (resp.getStatus() != 200) {
			OUT.error("Unable to connect to the server");
			new IOException();
		}
		String jsonString = resp.getEntity(String.class);
		List<WorkGroupDTO> workGroupDTOs = mapper.readValue(jsonString, new TypeReference<List<WorkGroupDTO>>() {
		});
		if (restClient != null) {
			restClient.destroy();
		}
		OUT.debug("Getting a workgroup folder details for user :" + username + " found :"
				+ (workGroupDTOs.size() > 0 ? workGroupDTOs.size() : "NOT FOUND"));
		return workGroupDTOs;
	}

	public List<WorkGroupDTO> getWorkgroupFiles(String workGroupUuid, String folderUuid, String username)
			throws JsonParseException, JsonMappingException, IOException {
		mapper = new ObjectMapper();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.WORK_GROUP);
		urlBuffer.append("/" + workGroupUuid + "/nodes?parent=" + folderUuid);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.get(ClientResponse.class);
		if (resp.getStatus() != 200) {
			OUT.error("Unable to connect to the server");
			throw new IOException();
		} else {
			String jsonString = resp.getEntity(String.class);
			List<WorkGroupDTO> workGroupDTOs = mapper.readValue(jsonString, new TypeReference<List<WorkGroupDTO>>() {
			});
			if (restClient != null) {
				restClient.destroy();
			}
			OUT.debug("Getting a workgroup folder details for user :" + username + " found :"
					+ (workGroupDTOs.size() > 0 ? workGroupDTOs.size() : "NOT FOUND"));
			return workGroupDTOs;
		}
	}

	public ResponseObject createWorkgroup(String workgroupName, String username) throws JSONException {
		mapper = new ObjectMapper();
		response = new ResponseObject();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");
		JSONObject json = new JSONObject();
		json.put("name", workgroupName);
		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.WORK_GROUP);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.type("application/json").post(ClientResponse.class, json.toString());
		if (resp.getStatus() != 200) {
			OUT.error("Creation of workgroup is Failed");
			response.setStatus(ApplicationConstants.getFailure());
		} else {
			response.setStatus(ApplicationConstants.getSuccess());
		}
		if (restClient != null) {
			restClient.destroy();
		}
		return response;
	}

	public ResponseObject createFolder(String folderName, String groupid, String parent, String username)
			throws JSONException {
		mapper = new ObjectMapper();
		response = new ResponseObject();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");
		JSONObject json = new JSONObject();
		json.put("name", folderName);
		json.put("type", "FOLDER");
		json.put("parent", parent);
		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.WORK_GROUP);
		urlBuffer.append("/" + groupid + "/nodes");
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.type("application/json").post(ClientResponse.class, json.toString());
		if (resp.getStatus() != 200) {
			OUT.error("Creation of workgroup is Failed");
			response.setStatus(ApplicationConstants.getFailure());
		} else {
			response.setStatus(ApplicationConstants.getSuccess());
		}
		if (restClient != null) {
			restClient.destroy();
		}
		return response;
	}

	public WorkGroupDTO getParentID(String workGroupUuid, String username)
			throws JSONException, JsonParseException, JsonMappingException, IOException {
		mapper = new ObjectMapper();
		WorkGroupDTO workGroupDTOs = null;
		response = new ResponseObject();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");
		JSONObject json = new JSONObject();
		json.put("name", "New folder");
		json.put("type", "FOLDER");
		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.WORK_GROUP);
		urlBuffer.append("/" + workGroupUuid + "/nodes?dryRun=true");
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.type("application/json").post(ClientResponse.class, json.toString());
		if (resp.getStatus() != 200) {
			OUT.error("Creation of workgroup is Failed");
			response.setStatus(ApplicationConstants.getFailure());
		} else {
			String jsonString = resp.getEntity(String.class);
			workGroupDTOs = mapper.readValue(jsonString, new TypeReference<WorkGroupDTO>() {
			});
			response.setStatus(ApplicationConstants.getSuccess());
		}
		if (restClient != null) {
			restClient.destroy();
		}
		return workGroupDTOs;
	}

	public void deleteNode(String workGroupUuid, String folderUuid, String username) throws IOException {
		mapper = new ObjectMapper();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");
		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.WORK_GROUP);

		if (folderUuid == null || folderUuid.equals(" ")) {
			urlBuffer.append("/" + workGroupUuid);
		} else {
			urlBuffer.append("/" + workGroupUuid);
			urlBuffer.append("/nodes/" + folderUuid);
		}
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.delete(ClientResponse.class);
		if (resp.getStatus() != 200) {
			OUT.error("Unable to connect to the server");
			throw new IOException();
		}
		if (restClient != null) {
			restClient.destroy();
		}
	}

	public ResponseObject renameNode(String workGroupUuid, String folderUuid, String renameString, String username) throws IOException {
		mapper = new ObjectMapper();
		response = new ResponseObject();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");
		Gson gson = new Gson();
		String jsonInString = null;
		urlBuffer.append(baseURL);
		if (folderUuid == null || folderUuid.equals("")) {
			AuthorDetailsDTO detailsDTO = new AuthorDetailsDTO();
			detailsDTO.setName(renameString);
			jsonInString = gson.toJson(detailsDTO);
			urlBuffer.append(ApplicationConstants.WORK_GROUP);
			urlBuffer.append("/" + workGroupUuid);
		} else {
			urlBuffer.append(ApplicationConstants.WORK_GROUP);
			urlBuffer.append("/" + workGroupUuid);
			urlBuffer.append("/nodes/" + folderUuid);
			WorkGroupDTO groupDTO = new WorkGroupDTO();
			groupDTO.setName(renameString);
			groupDTO.setUuid(folderUuid);
			groupDTO.setWorkGroup(workGroupUuid);
			groupDTO.setType("FOLDER");
			jsonInString = gson.toJson(groupDTO);
		}
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.type("application/json").put(ClientResponse.class, jsonInString);
		if (resp.getStatus() != 200) {
			OUT.error("File Renaming is Failed");
			throw new IOException();
		} else {
			response.setStatus(ApplicationConstants.getSuccess());
		}
		if (restClient != null) {
			restClient.destroy();
		}
		return response;
	}
}
