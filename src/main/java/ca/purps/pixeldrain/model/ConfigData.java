package ca.purps.pixeldrain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class ConfigData implements Serializable {

    private String token;

}
