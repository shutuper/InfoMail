package com.infopulse.infomail.dto.api.emails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailsIdsDTO {

	private List<Long> ids;

}
