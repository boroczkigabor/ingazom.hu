package org.atos.commutermap.network.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.atos.commutermap.dao.model.BaseClass;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorMessage extends BaseClass {

    public final String ID;
    public final String text;

    public ErrorMessage(@JsonProperty("ID") String ID,
                        @JsonProperty("Szoveg") String text) {
        this.ID = ID;
        this.text = text;
    }
}
