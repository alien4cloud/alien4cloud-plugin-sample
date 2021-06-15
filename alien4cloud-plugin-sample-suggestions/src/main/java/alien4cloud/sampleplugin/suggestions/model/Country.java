package alien4cloud.sampleplugin.suggestions.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

    private String name;
    private String alpha3Code;

}
