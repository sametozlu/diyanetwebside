package gov.diyanet.portal.modules.prayer;

import gov.diyanet.portal.modules.prayer.PrayerTimesResponse.CalendarResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prayer-times")
@RequiredArgsConstructor
public class PrayerTimesController {

	private final PrayerTimesService prayerTimesService;

	@GetMapping
	public PrayerTimesResponse get(
			@RequestParam(name = "province", required = false) String province,
			@RequestParam(required = false) String district,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return prayerTimesService.getTimes(province, district, date);
	}

	@GetMapping("/calendar")
	public CalendarResponse calendar(
			@RequestParam(name = "province", required = false) String province,
			@RequestParam(required = false) String district,
			@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month) {
		return prayerTimesService.getCalendar(province, district, year, month);
	}
}
