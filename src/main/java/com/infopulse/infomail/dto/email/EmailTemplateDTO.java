package com.infopulse.infomail.dto.email;

import lombok.Data;

@Data
public class EmailTemplateDTO {
    private String subject;
    private String body;

    public EmailTemplateDTO() {
    }
}
