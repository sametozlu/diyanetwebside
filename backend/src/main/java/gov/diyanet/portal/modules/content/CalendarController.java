package gov.diyanet.portal.modules.content;

import gov.diyanet.portal.modules.content.CalendarService.ReligiousDay;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

	private final CalendarService calendarService;

	@GetMapping("/religious-days")
	public List<ReligiousDay> religiousDays() {
		return calendarService.religiousDays();
	}
}
