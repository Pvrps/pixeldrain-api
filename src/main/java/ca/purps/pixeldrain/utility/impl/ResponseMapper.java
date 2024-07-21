package ca.purps.pixeldrain.utility.impl;

import ca.purps.pixeldrain.model.ApiData;
import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ResponseMapper {

    public ObservableList<ApiData> parseJsonResponse(String jsonResponse) {
        ObservableList<ApiData> fileList = FXCollections.observableArrayList();

        JsonArray filesArray = JsonParser.parseString(jsonResponse).getAsJsonObject().getAsJsonArray("files");

        Gson gson = new GsonBuilder().create();
        for (JsonElement element : filesArray) {
            ApiData file = gson.fromJson(element, ApiData.class);
            fileList.add(file);
        }

        return fileList;
    }

}
