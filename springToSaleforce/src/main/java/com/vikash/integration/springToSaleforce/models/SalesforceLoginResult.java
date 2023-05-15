package com.vikash.integration.springToSaleforce.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SalesforceLoginResult {
    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "instance_url")
    private String instanceUrl;
}
