package gov.diyanet.portal.modules.prayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record PrayerTimesResponse(
		String province,
		String district,
		String cityLabel,
		LocalDate date,
		String hijriDate,
		Integer hijriDay,
		Integer hijriMonth,
		Integer hijriYear,
		Times times,
		NextPrayer nextPrayer,
		String currentPrayer,
		String source,
		String disclaimer) {

	public record Times(
			LocalTime imsak,
			LocalTime gunes,
			LocalTime ogle,
			LocalTime ikindi,
			LocalTime aksam,
			LocalTime yatsi) {
	}

	public record NextPrayer(String name, LocalTime time, long remainingSeconds) {
	}

	public record CalendarResponse(
			String province,
			String district,
			String cityLabel,
			int year,
			int month,
			List<Day> days,
			String source,
			String disclaimer) {
	}

	public record Day(LocalDate date, String hijriDate, Integer hijriDay, Integer hijriMonth, Integer hijriYear, Times times) {
	}
}
