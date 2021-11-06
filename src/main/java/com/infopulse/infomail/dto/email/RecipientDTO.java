package com.infopulse.infomail.dto.email;

import com.infopulse.infomail.models.emails.RecipientType;
import lombok.Data;

@Data
public class RecipientDTO {

    private String mailAddress;
    private RecipientType recipientType;

    public RecipientDTO() {
    }
}
