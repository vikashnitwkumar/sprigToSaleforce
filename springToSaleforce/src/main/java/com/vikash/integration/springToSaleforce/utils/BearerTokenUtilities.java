package com.vikash.integration.springToSaleforce.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vikash.integration.springToSaleforce.configs.SalesforceConfigurationProperties;
import com.vikash.integration.springToSaleforce.models.SalesforceLoginResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class BearerTokenUtilities {
    private BearerTokenUtilities() { }

    private static final String TOKEN_URL =  "https://login.salesforce.com/services/oauth2/token";

    public static SalesforceLoginResult loginToSalesforce(CloseableHttpClient closeableHttpClient, SalesforceConfigurationProperties salesforceConfigurationProperties, ObjectMapper objectMapper) throws Exception {
        List<NameValuePair> loginParams = new ArrayList<>();
        loginParams.add(new BasicNameValuePair("client_id", salesforceConfigurationProperties.getConsumerKey()));
        loginParams.add(new BasicNameValuePair("client_secret", salesforceConfigurationProperties.getConsumerSecret()));
        loginParams.add(new BasicNameValuePair("grant_type", "password"));
        loginParams.add(new BasicNameValuePair("username", salesforceConfigurationProperties.getUsername()));
        loginParams.add(new BasicNameValuePair("password", salesforceConfigurationProperties.getPassword()));

        HttpPost post = new HttpPost(TOKEN_URL);
        post.setEntity(new UrlEncodedFormEntity(loginParams));

        HttpResponse httpResponse = closeableHttpClient.execute(post);
        SalesforceLoginResult salesforceLoginResult = objectMapper.readValue(httpResponse.getEntity().getContent(), SalesforceLoginResult.class);

        log.debug("salesforceLoginResult={}", salesforceLoginResult);
        return salesforceLoginResult;
    }
}
