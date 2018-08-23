package com.t3c.anchel.client.wsclient.services.dashboard;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONException;

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
import com.t3c.anchel.client.model.dashboard.FileDetailsDTO;
import com.t3c.anchel.client.model.dashboard.JTableDto;
import com.t3c.anchel.client.model.dashboard.WorkGroupDTO;
import com.t3c.anchel.client.utils.consts.ApplicationConstants;
import com.t3c.anchel.client.wsclient.controller.auth.UserSessionCache;

public class DashboardService {
	final static Logger OUT = Logger.getLogger(DashboardService.class);
	ObjectMapper mapper = null;
	StringBuilder urlBuffer = null;
	ResponseObject response = null;

	public List<FileDetailsDTO> getMyFiles(String username)
			throws JsonParseException, JsonMappingException, IOException {
		mapper = new ObjectMapper();
		urlBuffer = new StringBuilder();
		OUT.debug("Getting a My file for user :" + username);
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.MY_FILE);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.get(ClientResponse.class);
		if (resp.getStatus() != 200) {
			OUT.error("Unable to connect to the server");
			throw new IOException();
		} else {
			String jsonString = resp.getEntity(String.class);
			List<FileDetailsDTO> myFilesList = mapper.readValue(jsonString, new TypeReference<List<FileDetailsDTO>>() {
			});
			if (restClient != null) {
				restClient.destroy();
			}
			OUT.debug("Getting a My file for user :" + username + " found :"
					+ (myFilesList.size() > 0 ? myFilesList.size() : "NOT FOUND"));
			return myFilesList;
		}
	}

	public Object getReceivedFiles(String username) throws IOException {
		OUT.debug("Getting a file for user :" + username);
		mapper = new ObjectMapper();
		urlBuffer = new StringBuilder();

		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.RECEIVED_FILE);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.get(ClientResponse.class);
		if (resp.getStatus() != 200) {
			OUT.error("Unable to connect to the server");
			throw new IOException();
		} else {
			String jsonString = resp.getEntity(String.class);
			List<FileDetailsDTO> sharedFiles = mapper.readValue(jsonString, new TypeReference<List<FileDetailsDTO>>() {
			});
			if (restClient != null) {
				restClient.destroy();
			}
			OUT.debug("Getting a file for user :" + username + " found :"
					+ (sharedFiles.size() > 0 ? sharedFiles.size() : "NOT FOUND"));
			return sharedFiles;
		}
	}

	public void deleteMyFiles(String groupuuid, String uuid, String username, String selectedNodeName)
			throws IOException {
		mapper = new ObjectMapper();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		if (selectedNodeName.equals("My Files")) {
			urlBuffer.append(ApplicationConstants.MY_FILE);
			urlBuffer.append("/" + uuid);
		} else if (selectedNodeName.equals("Received Files")) {
			urlBuffer.append(ApplicationConstants.RECEIVED_FILE);
			urlBuffer.append("/" + uuid);
		} else {
			urlBuffer.append(ApplicationConstants.WORK_GROUP);
			urlBuffer.append("/" + groupuuid);
			urlBuffer.append("/nodes/" + uuid);
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

	public ResponseObject downloadMyFiles(String groupid, String uuid, String username, String selectedNodeName)
			throws IOException {
		mapper = new ObjectMapper();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		if (selectedNodeName.equals("My Files")) {
			urlBuffer.append(ApplicationConstants.MY_FILE);
			urlBuffer.append("/" + uuid);
			urlBuffer.append("/" + "download");
		} else if (selectedNodeName.equals("Received Files")) {
			urlBuffer.append(ApplicationConstants.RECEIVED_FILE);
			urlBuffer.append("/" + uuid);
			urlBuffer.append("/" + "download");
		} else {
			urlBuffer.append(ApplicationConstants.WORK_GROUP);
			urlBuffer.append("/" + groupid);
			urlBuffer.append("/nodes/" + uuid + "/download");
		}

		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.get(ClientResponse.class);
		response = new ResponseObject();
		if (resp.getStatus() != 200 && restClient != null) {
			OUT.error("File Downloading is Failed");
			response.setStatus(ApplicationConstants.getFailure());
			restClient.destroy();
		} else {
			InputStream inputStream = resp.getEntity(InputStream.class);
			response.setInputStream(inputStream);
			response.setStatus(ApplicationConstants.getSuccess());
		}
		return response;
	}

	public ResponseObject renameMyFiles(String groupid, String uuid, String username, String renameString)
			throws JSONException {
		mapper = new ObjectMapper();
		response = new ResponseObject();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");
		Gson gson = new Gson();
		String jsonInString = null;
		urlBuffer.append(baseURL);
		if (groupid == null || groupid.equals("")) {
			AuthorDetailsDTO detailsDTO = new AuthorDetailsDTO();
			detailsDTO.setName(renameString);
			jsonInString = gson.toJson(detailsDTO);
			urlBuffer.append(ApplicationConstants.MY_FILE);
			urlBuffer.append("/" + uuid);
		} else {
			urlBuffer.append(ApplicationConstants.WORK_GROUP);
			urlBuffer.append("/" + groupid);
			urlBuffer.append("/nodes/" + uuid);
			WorkGroupDTO groupDTO = new WorkGroupDTO();
			groupDTO.setName(renameString);
			groupDTO.setUuid(uuid);
			groupDTO.setWorkGroup(groupid);
			groupDTO.setType("DOCUMENT");
			jsonInString = gson.toJson(groupDTO);
		}
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.type("application/json").put(ClientResponse.class, jsonInString);
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

	@SuppressWarnings("unchecked")
	public ResponseObject uploadMyFiles(File fileToUpload, String username) throws IOException {
		mapper = new ObjectMapper();
		response = new ResponseObject();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");
		String identifier = UUID.randomUUID().toString();
		String fileSize = String.valueOf(fileToUpload.length());
		String fileName = fileToUpload.getName();

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.UPLOAD_FILE);
		final ClientConfig config = new DefaultClientConfig();
		Client restClient = Client.create(config);
		WebResource webResource = restClient.resource(urlBuffer.toString());

		FileInputStream fis = null;
		FormDataBodyPart fileDataBodyPart;
		ByteArrayInputStream bis;
		int chunksize = 1048576;
		fis = new FileInputStream(fileToUpload);
		int fileSize1 = (int) fileToUpload.length();
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
						.field("workGroupUuid", "", MediaType.TEXT_PLAIN_TYPE)
						.field("workGroupParentNodeUuid", "", MediaType.TEXT_PLAIN_TYPE)
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
					.field("workGroupUuid", "", MediaType.TEXT_PLAIN_TYPE)
					.field("workGroupParentNodeUuid", "", MediaType.TEXT_PLAIN_TYPE)
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

	public Object getFileActivity(JTableDto tableDto, String username) throws IOException, JSONException {

		mapper = new ObjectMapper();
		urlBuffer = new StringBuilder();
		OUT.debug("Getting a My file for user :" + username);
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		String workgroupID = tableDto.getWorkgroupId();
		if (workgroupID == null || workgroupID.isEmpty()) {
			urlBuffer.append(ApplicationConstants.MY_FILE);
			urlBuffer.append("/" + tableDto.getUuid() + "/audit");
		} else {
			urlBuffer.append(ApplicationConstants.WORK_GROUP);
			urlBuffer.append("/" + workgroupID + "/nodes/");
			urlBuffer.append(tableDto.getUuid() + "/audit");
		}
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.get(ClientResponse.class);
		if (resp.getStatus() != 200) {
			OUT.error("Unable to connect to the server");
			throw new IOException();
		} else {
			String jsonString = resp.getEntity(String.class);
			JsonNode rootNode = mapper.readTree(jsonString);
			if (restClient != null) {
				restClient.destroy();
			}
			OUT.debug("Getting a file activity for user :" + username + " found :"
					+ (rootNode.size() > 0 ? rootNode.size() : "NOT FOUND"));
			return rootNode;
		}
	}

}
