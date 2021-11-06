package com.infopulse.infomail.dto.email;

import com.infopulse.infomail.models.emails.EmailSchedule;
import lombok.Data;

import java.util.List;

@Data
public class EmailDTO {
    private List<RecipientDTO> recipients;
    private EmailTemplateDTO emailTemplate;
    private EmailSchedule emailSchedule;

    public EmailDTO() {
    }
}
