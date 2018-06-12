package com.t3c.anchel.client.wsclient.services.dashboard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.t3c.anchel.client.model.common.ResponseObject;
import com.t3c.anchel.client.model.dashboard.FileDetailsDTO;
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
		}
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

	public Object getReceivedFiles(String username) throws JsonParseException, JsonMappingException, IOException {
		OUT.debug("Getting a shared file for user :" + username);
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
		}
		String jsonString = resp.getEntity(String.class);
		List<FileDetailsDTO> sharedFiles = mapper.readValue(jsonString, new TypeReference<List<FileDetailsDTO>>() {
		});
		if (restClient != null) {
			restClient.destroy();
		}
		OUT.debug("Getting a shared file for user :" + username + " found :"
				+ (sharedFiles.size() > 0 ? sharedFiles.size() : "NOT FOUND"));
		return sharedFiles;
	}

	public Object deleteMyFiles(String uuid, String username)
			throws JsonParseException, JsonMappingException, IOException {
		mapper = new ObjectMapper();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.MY_FILE);
		urlBuffer.append("/" + uuid);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.delete(ClientResponse.class);
		if (resp.getStatus() != 200) {
			OUT.error("Unable to connect to the server");
		}
		String jsonString = resp.getEntity(String.class);
		FileDetailsDTO fileDetailsDTO = mapper.readValue(jsonString, FileDetailsDTO.class);
		if (restClient != null && jsonString != null) {
			restClient.destroy();
		}
		return fileDetailsDTO;
	}

	public ResponseObject downloadMyFiles(String uuid, String username) throws IOException {
		mapper = new ObjectMapper();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.MY_FILE);
		urlBuffer.append("/" + uuid);
		urlBuffer.append("/" + "download");
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.get(ClientResponse.class);
		response = new ResponseObject();
		if (resp.getStatus() != 200) {
			OUT.error("File Downloading is Failed");
			response.setStatus(ApplicationConstants.getFailure());
		}
		InputStream inputStream = resp.getEntity(InputStream.class);
		response.setInputStream(inputStream);
		response.setStatus(ApplicationConstants.getSuccess());
		if (restClient != null) {
			restClient.destroy();
		}
		return response;

	}

	public ResponseObject renameMyFiles(String uuid, String username, String renameString) {
		mapper = new ObjectMapper();
		response = new ResponseObject();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");
		Gson gson = new Gson();
		FileDetailsDTO detailsDTO = new FileDetailsDTO();
		detailsDTO.setName(renameString);
		String jsonInString = gson.toJson(detailsDTO);
		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.MY_FILE);
		urlBuffer.append("/" + uuid);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.type("application/json").put(ClientResponse.class, jsonInString);
		if (resp.getStatus() != 200) {
			OUT.error("File Downloading is Failed");
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
	public ResponseObject uploadMyFiles(File fileToUpload, String username) {
		mapper = new ObjectMapper();
		response = new ResponseObject();
		urlBuffer = new StringBuilder();
		String baseURL = UserSessionCache.getInstance().doGet(username + "_url");
		String auth64 = UserSessionCache.getInstance().doGet(username + "_base");

		String identifier = UUID.randomUUID().toString();
		String fileSize = String.valueOf(fileToUpload.length());
		String fileName = fileToUpload.getName();

		FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", fileToUpload,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		fileDataBodyPart.setContentDisposition(
				FormDataContentDisposition.name("file").fileName(fileToUpload.getName()).build());

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

		urlBuffer.append(baseURL);
		urlBuffer.append(ApplicationConstants.UPLOAD_FILE);
		final ClientConfig config = new DefaultClientConfig();
		Client restClient = Client.create(config);
		WebResource webResource = restClient.resource(urlBuffer.toString());
		ClientResponse resp = webResource.accept("application/json").header("Authorization", "Basic " + auth64)
				.type("multipart/form-data").post(ClientResponse.class, multiPart);
		if (resp.getStatus() == 200) {
			OUT.info("File uploading is success");
			response.setStatus(ApplicationConstants.getSuccess());
		}
		else {
			OUT.error("File upload is failed " + resp.getStatusInfo());
			response.setStatus(ApplicationConstants.getFailure());
		}

		return response;
	}
}
