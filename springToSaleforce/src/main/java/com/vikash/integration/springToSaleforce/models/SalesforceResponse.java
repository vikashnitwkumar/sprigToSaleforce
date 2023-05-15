package com.vikash.integration.springToSaleforce.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SalesforceResponse {
    int totalSize;

    // TODO - introduce SalesforceObject to handle other items
    List<Contact> records;
}
