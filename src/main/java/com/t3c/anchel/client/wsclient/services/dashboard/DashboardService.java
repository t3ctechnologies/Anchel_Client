package com.t3c.anchel.client.wsclient.services.dashboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.JProgressBar;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
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
		if(inputStream.available() == 0){
			response.setStatus(ApplicationConstants.getFailure());
		}
		else {
			response.setStatus(ApplicationConstants.getSuccess());
		}
		return response;

	}

	public ResponseObject renameMyFiles(String uuid, String username, String renameString) {
		mapper = new ObjectMapper();
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
		response = new ResponseObject();
		if (resp.getStatus() != 200) {
			OUT.error("File Downloading is Failed");
			response.setStatus(ApplicationConstants.getFailure());
		}else {
			response.setStatus(ApplicationConstants.getSuccess());
		}
		InputStream inputStream = resp.getEntity(InputStream.class);
		response.setInputStream(inputStream);
		return response;
	}
}
