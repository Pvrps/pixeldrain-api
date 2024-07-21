package ca.purps.pixeldrain.utility.impl;

import ca.purps.pixeldrain.exception.AppException;
import ca.purps.pixeldrain.exception.AppRuntimeException;
import ca.purps.pixeldrain.model.ApiData;
import ca.purps.pixeldrain.model.ApiResponse;
import javafx.collections.ObservableList;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@Log4j2
@SuppressWarnings({"java:S2647"})
public class ApiClient {

    private enum RequestMethod {
        GET,
        DELETE
    }

    private static final String BASE_URL = "https://pixeldrain.com/api";

    private final String token;
    private final ResponseMapper responseMapper = new ResponseMapper();

    public ApiClient(String token) {
        this.token = token;
    }

    public ObservableList<ApiData> getListOfFiles() throws AppException {
        ApiResponse apiResponse = sendRequest(
                BASE_URL + "/user/files",
                RequestMethod.GET);

        ObservableList<ApiData> models = responseMapper.parseJsonResponse(apiResponse.getText());
        models.forEach(this::getThumbnailData);

        return models;
    }

    public ApiResponse getFileData(ApiData model) throws AppException {
        return sendRequest(
                BASE_URL + String.format("/file/%s", model.getId()),
                RequestMethod.GET);
    }

    public boolean deleteFile(ApiData model) throws AppException {
        ApiResponse apiResponse = sendRequest(
                BASE_URL + String.format("/file/%s", model.getId()),
                RequestMethod.DELETE);

        return apiResponse.getCode() == 200;
    }

    private void getThumbnailData(ApiData model) {
        try {
            ApiResponse apiResponse = sendRequest(
                    BASE_URL + String.format("/file/%s/thumbnail", model.getId()),
                    RequestMethod.GET);

            model.setThumbnailData(apiResponse.getData());
        } catch (Exception e) {
            throw new AppRuntimeException(e);
        }
    }

    private ApiResponse sendRequest(String apiUrl, RequestMethod requestMethod) throws AppException {
        try {
            log.info(requestMethod.name() + " - " + apiUrl);
            URL url = URI.create(apiUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(requestMethod.name());
            connection.setRequestProperty("Authorization", "Basic " + token);
            connection.setRequestProperty("Referer", "https://pixeldrain.com");

            int responseCode = connection.getResponseCode();
            String contentType = connection.getContentType();

            try (InputStream inputStream = connection.getInputStream(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                byte[] responseData = byteArrayOutputStream.toByteArray();

                ApiResponse response = ApiResponse.builder()
                        .code(responseCode)
                        .type(contentType)
                        .data(responseData)
                        .build();

                log.debug("API Response - " + response);

                return response;
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
    }
}
