package ca.purps.pixeldrain.utility.impl;

import ca.purps.pixeldrain.exception.AppException;
import ca.purps.pixeldrain.exception.AppRuntimeException;
import ca.purps.pixeldrain.model.ConfigData;
import ca.purps.pixeldrain.utility.HardwareFactory;
import javafx.scene.control.TextInputDialog;

import javax.crypto.AEADBadTagException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Configuration {

    private final Encryption encryption = new Encryption(HardwareFactory.getSerialNumber());

    private ConfigData configData;

    public Configuration(String configPathName) throws AppException {
        loadConfig(Paths.get(configPathName));
    }

    public ConfigData getData() {
        return configData;
    }

    private void loadConfig(Path configPath) throws AppException {
        try {
            if (Files.exists(configPath)) {
                try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(configPath.toFile()))) {
                    configData = (ConfigData) inputStream.readObject();
                    configData = configData
                            .toBuilder()
                            .token(encryption.decrypt(configData.getToken()))
                            .build();
                }
            } else {
                showPrompt(configPath);
            }
        } catch (Exception e) {
            Exception exception = e;

            if (exception.getCause() instanceof AEADBadTagException) {
                try {
                    Files.deleteIfExists(configPath);
                    loadConfig(configPath);
                    return;
                } catch (Exception e2) {
                    exception = e2;
                }
            }

            throw new AppException(exception);
        }
    }

    private void showPrompt(Path configPath) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("API Token");
        dialog.setHeaderText("Configuration file not found.");
        dialog.setContentText("Please enter your API token:");

        dialog.showAndWait().ifPresent(token -> createConfig(configPath, token));
    }

    private void createConfig(Path configPath, String token) {
        try {
            configData = ConfigData.builder()
                    .token(Base64.getEncoder().encodeToString((":" + token).getBytes()))
                    .build();

            ConfigData encryptedData = configData
                    .toBuilder()
                    .token(encryption.encrypt(configData.getToken()))
                    .build();

            Files.createDirectories(configPath.getParent());
            Files.createFile(configPath);

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(configPath.toFile()))) {
                outputStream.writeObject(encryptedData);
            }
        } catch (Exception e) {
            throw new AppRuntimeException(e);
        }
    }

}