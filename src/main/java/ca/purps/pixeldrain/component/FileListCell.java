package ca.purps.pixeldrain.component;

import ca.purps.pixeldrain.model.ApiData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"java:S110"})
public class FileListCell extends ListCell<ApiData> {
    public static final List<String> VIEWABLE_MIMES = Arrays.asList("image/png", "image/gif");

    private final HBox cellBox = new HBox(10);
    private final ImageView thumbnailImageView = new ImageView();
    private final Text fileNameText = new Text();
    private final Button viewButton = new Button("View");
    private final Button deleteButton = new Button("Delete");

    public FileListCell() {
        thumbnailImageView.setFitWidth(40);
        thumbnailImageView.setFitHeight(40);
        thumbnailImageView.setPreserveRatio(true);
        thumbnailImageView.setSmooth(true);

        HBox leftSide = new HBox(10);
        VBox imageContainer = new VBox(10);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.getChildren().add(thumbnailImageView);

        VBox textContainer = new VBox(10);
        textContainer.setAlignment(Pos.CENTER);
        textContainer.getChildren().add(fileNameText);
        leftSide.getChildren().addAll(imageContainer, textContainer);

        HBox rightSide = new HBox(10);
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(viewButton, deleteButton);
        rightSide.getChildren().addAll(buttonContainer);

        HBox.setHgrow(leftSide, Priority.ALWAYS);

        cellBox.getChildren().addAll(leftSide, rightSide);
        cellBox.setPadding(new Insets(10));
        setGraphic(cellBox);

        viewButton.setDisable(false);
    }

    @Override
    protected void updateItem(ApiData file, boolean empty) {
        super.updateItem(file, empty);

        if (empty || file == null) {
            setGraphic(null);
            setText(null);
        } else {
            viewButton.setDisable(!VIEWABLE_MIMES.contains(file.getMimeType()));

            if (file.getThumbnailData() != null && file.getThumbnailData().length > 0) {
                Image thumbnailImage = new Image(new ByteArrayInputStream(file.getThumbnailData()));
                thumbnailImageView.setImage(thumbnailImage);
            } else {
                thumbnailImageView.setImage(null); // No image data available
            }

            fileNameText.setText(file.getName());

            setGraphic(cellBox);
        }
    }

    public void setViewButtonAction(Consumer<ApiData> action) {
        viewButton.setOnAction(event -> {
            if (getItem() != null) {
                action.accept(getItem());
            }
        });
    }

    public void setDeleteButtonAction(Consumer<ApiData> action) {
        deleteButton.setOnAction(event -> {
            if (getItem() != null) {
                action.accept(getItem());
            }
        });
    }
}