package ca.purps.pixeldrain.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;

public class ImageViewController {

    @FXML
    private ImageView imageView;

    public void setImageData(byte[] data) {
        Image image = new Image(new ByteArrayInputStream(data));
        imageView.setImage(image);
    }

    public void closeImageView() {
        ((Stage) imageView.getScene().getWindow()).close();
    }

}
