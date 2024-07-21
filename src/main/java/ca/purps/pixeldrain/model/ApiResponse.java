package ca.purps.pixeldrain.model;

import ca.purps.pixeldrain.exception.AppException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class ApiResponse {

    private int code;
    private String type;
    @ToString.Exclude
    private byte[] data;

    @ToString.Include
    public int getSize() {
        return data.length;
    }

    public String getText() throws AppException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)))) {
            StringBuilder text = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
            }

            return text.toString();
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

}
