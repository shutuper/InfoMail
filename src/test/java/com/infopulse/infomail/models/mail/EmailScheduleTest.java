package com.infopulse.infomail.models.mail;

import com.infopulse.infomail.dto.api.schedule.EmailScheduleDTO;
import com.infopulse.infomail.models.schedule.EmailSchedule;
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
