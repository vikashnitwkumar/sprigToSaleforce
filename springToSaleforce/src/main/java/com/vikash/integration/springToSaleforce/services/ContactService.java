package com.vikash.integration.springToSaleforce.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vikash.integration.springToSaleforce.configs.SalesforceConfigurationProperties;
import com.vikash.integration.springToSaleforce.events.ContactEventPublisher;
import com.vikash.integration.springToSaleforce.models.Contact;
import com.vikash.integration.springToSaleforce.models.PatchUpdates;
import com.vikash.integration.springToSaleforce.models.SalesforceLoginResult;
import com.vikash.integration.springToSaleforce.models.SalesforceResponse;
import com.vikash.integration.springToSaleforce.utils.BearerTokenUtilities;
import com.vikash.integration.springToSaleforce.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContactService {
    public static final String QUERY_PATH = "/services/data/v52.0/";

    private final ContactEventPublisher contactEventPublisher;
    private final CloseableHttpClient closeableHttpClient;
    private final ObjectMapper objectMapper;
    private final SalesforceConfigurationProperties salesforceConfigurationProperties;

    @Cacheable("contacts")
    public Contact getContact(String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new NullPointerException("id cannot be null");
        }

        SalesforceLoginResult salesforceLoginResult = BearerTokenUtilities.loginToSalesforce(closeableHttpClient, salesforceConfigurationProperties, objectMapper);

        URIBuilder builder = new URIBuilder(salesforceLoginResult.getInstanceUrl());
        builder.setPath(QUERY_PATH + "sobjects/Contact/" + id);

        HttpGet get = new HttpGet(builder.build());
        get.setHeader("Authorization", "Bearer " + salesforceLoginResult.getAccessToken());

        HttpResponse httpResponse = closeableHttpClient.execute(get);
        HttpUtils.checkResponse(httpResponse);

        Contact contact = objectMapper.readValue(httpResponse.getEntity().getContent(), Contact.class);

        log.debug("contact={}", contact);
        return contact;
    }

    @Cacheable("contacts")
    public List<Contact> getContacts() throws Exception {
        SalesforceLoginResult salesforceLoginResult = BearerTokenUtilities.loginToSalesforce(closeableHttpClient, salesforceConfigurationProperties, objectMapper);

        URIBuilder builder = new URIBuilder(salesforceLoginResult.getInstanceUrl());
        builder.setPath(QUERY_PATH + "query").setParameter("q", Contact.CONTACT_QUERY);

        HttpGet get = new HttpGet(builder.build());
        get.setHeader("Authorization", "Bearer " + salesforceLoginResult.getAccessToken());

        HttpResponse httpResponse = closeableHttpClient.execute(get);
        HttpUtils.checkResponse(httpResponse);

        SalesforceResponse salesforceResponse = objectMapper.readValue(httpResponse.getEntity().getContent(), SalesforceResponse.class);

        List<Contact> contacts = salesforceResponse.getRecords();

        log.debug("contacts={}", contacts);
        return contacts;
    }

    @CacheEvict(value = "contacts", allEntries = true)
    public Contact updateContact(String id, PatchUpdates patchUpdates) throws Exception {
        log.debug("updateContact(id={}, patchUpdates={})", id, patchUpdates);

        if (StringUtils.isEmpty(id)) {
            throw new NullPointerException("id cannot be null");
        }


        if (MapUtils.isEmpty(patchUpdates)) {
            throw new NullPointerException("patchUpdates cannot be null");
        }

        SalesforceLoginResult salesforceLoginResult = BearerTokenUtilities.loginToSalesforce(closeableHttpClient, salesforceConfigurationProperties, objectMapper);

        URIBuilder builder = new URIBuilder(salesforceLoginResult.getInstanceUrl());
        builder.setPath(QUERY_PATH + "sobjects/Contact/" + id);

        HttpPatch patch = new HttpPatch(builder.build());
        patch.setHeader("Authorization", "Bearer " + salesforceLoginResult.getAccessToken());
        patch.setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);

        patch.setEntity(new StringEntity(objectMapper.writeValueAsString(patchUpdates)));

        HttpResponse httpResponse = closeableHttpClient.execute(patch);
        HttpUtils.checkResponse(httpResponse);

        Contact contact = getContact(id);
        contactEventPublisher.publishContactEvent(contact);
        return contact;
    }
}
