package com.vikash.integration.springToSaleforce.events;

import com.vikash.integration.springToSaleforce.models.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ContactEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishContactEvent(Contact contact) {
        applicationEventPublisher.publishEvent(new ContactEvent(contact));
    }
}
