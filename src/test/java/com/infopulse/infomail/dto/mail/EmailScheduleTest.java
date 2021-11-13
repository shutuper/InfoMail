package com.infopulse.infomail.dto.mail;

import org.junit.jupiter.api.Test;

class EmailScheduleTest {
    @Test
    void toDTO_butEmailScheduleEmpty() {
        new EmailSchedule().toDTO();
    }

    @Test
    void fromDTO_EmailScheduleDTOEmpty() {
        EmailSchedule.fromDTO(new EmailScheduleDTO());
    }
}
