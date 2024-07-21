package ca.purps.pixeldrain.controller;

import ca.purps.pixeldrain.App;
import ca.purps.pixeldrain.component.FileListCell;
import ca.purps.pixeldrain.exception.AppException;
import ca.purps.pixeldrain.exception.AppRuntimeException;
import ca.purps.pixeldrain.model.ApiData;
import ca.purps.pixeldrain.model.ApiResponse;
import ca.purps.pixeldrain.utility.impl.ApiClient;
import ca.purps.pixeldrain.utility.impl.Configuration;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.SystemUtils;

import java.util.Objects;

@Log4j2
public class FileViewController {

    @FXML
    public ListView<ApiData> fileListView;

    private final Configuration configuration;
    private final ApiClient apiClient;
    private final ObservableList<ApiData> fileList;

    public FileViewController() throws AppException {
        configuration = new Configuration(SystemUtils.getUserHome() + "/pixeldrain_api/config");

        if (configuration.getData() == null) {
            throw new AppException("Config could not be loaded.");
        }

        apiClient = new ApiClient(configuration.getData().getToken());

        fileList = apiClient.getListOfFiles();
    }

    @FXML
    private void initialize() {
        fileListView.setItems(fileList);
        fileListView.setCellFactory(param -> {
            FileListCell cell = new FileListCell();
            cell.setViewButtonAction(this::viewButtonClick);
            cell.setDeleteButtonAction(this::deleteButtonClick);
            return cell;
        });

        fileListView.getStylesheets()
                .add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());
    }

    private void viewButtonClick(ApiData apiData) {
        try {
            if (FileListCell.VIEWABLE_MIMES.contains(apiData.getMimeType())) {
                ApiResponse response = apiClient.getFileData(apiData);

                openImageView(response.getData());
            } else {
                throw new AppRuntimeException(apiData.getMimeType() + " is not yet supported.");
            }
        } catch (Exception e) {
            throw new AppRuntimeException(e);
        }
    }

    private void openImageView(byte[] data) throws AppException {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("image-view.fxml"));
            Parent root = loader.load();
            ImageViewController imageViewerController = loader.getController();
            imageViewerController.setImageData(data);

            Stage stage = new Stage();
            stage.setTitle("Image Viewer");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    private void deleteButtonClick(ApiData apiData) {
        try {
            if (apiClient.deleteFile(apiData)) {
                fileList.remove(apiData);
            }
        } catch (Exception e) {
            throw new AppRuntimeException(e);
        }
    }

}