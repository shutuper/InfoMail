package com.infopulse.infomail.dto.api.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobNamesDTO {

	@NotEmpty
	private List<String> jobNames;

}
