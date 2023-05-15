package com.vikash.integration.springToSaleforce.events;

import com.vikash.integration.springToSaleforce.models.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactEvent {
    private Contact contact;
}
