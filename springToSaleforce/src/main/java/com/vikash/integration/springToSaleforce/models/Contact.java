package com.vikash.integration.springToSaleforce.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Contact {
    public static final String CONTACT_QUERY = "SELECT Name, Title, Department FROM Contact";

    @JsonProperty(value = "Name")
    private String name;

    @JsonProperty(value = "Title")
    private String title;

    @JsonProperty(value = "Department")
    private String department;

    private SalesforceAttributes attributes;

    public String getId() {
        if (attributes != null && attributes.getUrl() != null) {
            return StringUtils.substringAfterLast(attributes.getUrl(), "/");
        }

        return null;
    }
}
