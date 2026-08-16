package gov.diyanet.portal.integrations;

import tools.jackson.databind.JsonNode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class AladhanClient {

	private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private final RestClient client;
	private final boolean enabled;
	private final int method;

	public AladhanClient(
			@Qualifier("aladhanRestClient") RestClient client,
			@Value("${app.integrations.aladhan.enabled}") boolean enabled,
			@Value("${app.integrations.aladhan.method}") int method) {
		this.client = client;
		this.enabled = enabled;
		this.method = method;
	}

	public boolean enabled() {
		return enabled;
	}

	public Optional<Snapshot> timings(Double lat, Double lng, String city, LocalDate date) {
		if (lat != null && lng != null) {
			Optional<Snapshot> byCoord = timingsByCoordinates(lat, lng, date);
			if (byCoord.isPresent()) {
				return byCoord;
			}
		}
		return timingsByCity(city, date);
	}

	public Optional<Snapshot> timingsByCoordinates(double lat, double lng, LocalDate date) {
		if (!enabled) {
			return Optional.empty();
		}
		try {
			String uri = UriComponentsBuilder.fromPath("/timings/{date}")
					.queryParam("latitude", lat)
					.queryParam("longitude", lng)
					.queryParam("method", method)
					.queryParam("school", 1)
					.queryParam("timezonestring", "Europe/Istanbul")
					.buildAndExpand(date.format(DATE))
					.toUriString();
			return parseTimings(client.get().uri(uri).retrieve().body(JsonNode.class));
		} catch (Exception ex) {
			log.warn("Aladhan timings by coordinates failed: {}", ex.getMessage());
			return Optional.empty();
		}
	}

	public Optional<Snapshot> timingsByCity(String city, LocalDate date) {
		if (!enabled || city == null || city.isBlank()) {
			return Optional.empty();
		}
		try {
			String uri = UriComponentsBuilder.fromPath("/timingsByCity/{date}")
					.queryParam("city", city)
					.queryParam("country", "Turkey")
					.queryParam("method", method)
					.queryParam("school", 1)
					.queryParam("timezonestring", "Europe/Istanbul")
					.buildAndExpand(date.format(DATE))
					.toUriString();
			return parseTimings(client.get().uri(uri).retrieve().body(JsonNode.class));
		} catch (Exception ex) {
			log.warn("Aladhan timings failed for {}: {}", city, ex.getMessage());
			return Optional.empty();
		}
	}

	public List<DaySnapshot> calendar(Double lat, Double lng, String city, int year, int month) {
		if (lat != null && lng != null) {
			List<DaySnapshot> byCoord = calendarByCoordinates(lat, lng, year, month);
			if (!byCoord.isEmpty()) {
				return byCoord;
			}
		}
		return calendarByCity(city, year, month);
	}

	public List<DaySnapshot> calendarByCoordinates(double lat, double lng, int year, int month) {
		if (!enabled) {
			return List.of();
		}
		try {
			String uri = UriComponentsBuilder.fromPath("/calendar/{year}/{month}")
					.queryParam("latitude", lat)
					.queryParam("longitude", lng)
					.queryParam("method", method)
					.queryParam("school", 1)
					.queryParam("timezonestring", "Europe/Istanbul")
					.buildAndExpand(year, month)
					.toUriString();
			return parseCalendar(client.get().uri(uri).retrieve().body(JsonNode.class));
		} catch (Exception ex) {
			log.warn("Aladhan calendar by coordinates failed: {}", ex.getMessage());
			return List.of();
		}
	}

	public List<DaySnapshot> calendarByCity(String city, int year, int month) {
		if (!enabled || city == null || city.isBlank()) {
			return List.of();
		}
		try {
			String uri = UriComponentsBuilder.fromPath("/calendarByCity/{year}/{month}")
					.queryParam("city", city)
					.queryParam("country", "Turkey")
					.queryParam("method", method)
					.queryParam("school", 1)
					.buildAndExpand(year, month)
					.toUriString();
			return parseCalendar(client.get().uri(uri).retrieve().body(JsonNode.class));
		} catch (Exception ex) {
			log.warn("Aladhan calendar failed for {}: {}", city, ex.getMessage());
			return List.of();
		}
	}

	private static Optional<Snapshot> parseTimings(JsonNode root) {
		if (root == null || root.path("code").asInt() != 200) {
			return Optional.empty();
		}
		JsonNode t = root.path("data").path("timings");
		JsonNode hijri = root.path("data").path("date").path("hijri");
		return Optional.of(new Snapshot(
				parseTime(t.path("Fajr").asText(t.path("Imsak").asText())),
				parseTime(t.path("Sunrise").asText()),
				parseTime(t.path("Dhuhr").asText()),
				parseTime(t.path("Asr").asText()),
				parseTime(t.path("Maghrib").asText()),
				parseTime(t.path("Isha").asText()),
				hijri.path("day").asInt(),
				hijri.path("month").path("number").asInt(),
				hijri.path("year").asInt(),
				"aladhan"));
	}

	private static List<DaySnapshot> parseCalendar(JsonNode root) {
		if (root == null || root.path("code").asInt() != 200) {
			return List.of();
		}
		List<DaySnapshot> days = new ArrayList<>();
		for (JsonNode item : root.path("data")) {
			JsonNode t = item.path("timings");
			String gregorian = item.path("date").path("gregorian").path("date").asText();
			LocalDate date = LocalDate.parse(gregorian, DATE);
			days.add(new DaySnapshot(
					date,
					parseTime(t.path("Fajr").asText(t.path("Imsak").asText())),
					parseTime(t.path("Sunrise").asText()),
					parseTime(t.path("Dhuhr").asText()),
					parseTime(t.path("Asr").asText()),
					parseTime(t.path("Maghrib").asText()),
					parseTime(t.path("Isha").asText()),
					item.path("date").path("hijri").path("day").asInt(),
					item.path("date").path("hijri").path("month").path("number").asInt(),
					item.path("date").path("hijri").path("year").asInt()));
		}
		return days;
	}

	public Optional<HijriDate> hijriFor(LocalDate gregorian) {
		if (!enabled) {
			return Optional.empty();
		}
		try {
			JsonNode root = client.get()
					.uri("/gToH/{date}", gregorian.format(DATE))
					.retrieve()
					.body(JsonNode.class);
			JsonNode hijri = root == null ? null : root.path("data").path("hijri");
			if (hijri == null || hijri.isMissingNode()) {
				return Optional.empty();
			}
			return Optional.of(new HijriDate(
					hijri.path("day").asInt(),
					hijri.path("month").path("number").asInt(),
					hijri.path("year").asInt(),
					formatHijri(hijri)));
		} catch (Exception ex) {
			log.warn("Aladhan gToH failed: {}", ex.getMessage());
			return Optional.empty();
		}
	}

	public Optional<LocalDate> gregorianFor(int day, int month, int year) {
		if (!enabled) {
			return Optional.empty();
		}
		try {
			String hijri = "%02d-%02d-%d".formatted(day, month, year);
			JsonNode root = client.get().uri("/hToG/{date}", hijri).retrieve().body(JsonNode.class);
			String g = root == null ? "" : root.path("data").path("gregorian").path("date").asText();
			if (g.isBlank()) {
				return Optional.empty();
			}
			return Optional.of(LocalDate.parse(g, DATE));
		} catch (Exception ex) {
			log.warn("Aladhan hToG failed: {}", ex.getMessage());
			return Optional.empty();
		}
	}

	private static LocalTime parseTime(String raw) {
		if (raw == null || raw.isBlank()) {
			throw new IllegalArgumentException("empty time");
		}
		String clock = raw.trim().split("[ (]")[0];
		String[] parts = clock.split(":");
		return LocalTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
	}

	private static String formatHijri(JsonNode hijri) {
		if (hijri == null || hijri.isMissingNode()) {
			return "";
		}
		String day = hijri.path("day").asText();
		String month = hijri.path("month").path("en").asText();
		String year = hijri.path("year").asText();
		String tr = switch (month.toLowerCase()) {
			case "muharram" -> "Muharrem";
			case "safar" -> "Safer";
			case "rabi' al-awwal", "rabi al-awwal" -> "Rebiülevvel";
			case "rabi' al-thani", "rabi al-thani" -> "Rebiülahir";
			case "jumada al-ula", "jumada al-awwal" -> "Cemaziyelevvel";
			case "jumada al-akhirah", "jumada al-thani" -> "Cemaziyelahir";
			case "rajab" -> "Recep";
			case "sha'ban", "shaban" -> "Şaban";
			case "ramadan" -> "Ramazan";
			case "shawwal" -> "Şevval";
			case "dhu al-qi'dah", "dhul qidah" -> "Zilkade";
			case "dhu al-hijjah", "dhul hijjah" -> "Zilhicce";
			default -> month;
		};
		return (day + " " + tr + " " + year).trim();
	}

	public record Snapshot(
			LocalTime imsak,
			LocalTime gunes,
			LocalTime ogle,
			LocalTime ikindi,
			LocalTime aksam,
			LocalTime yatsi,
			int hijriDay,
			int hijriMonth,
			int hijriYear,
			String source) {
	}

	public record DaySnapshot(
			LocalDate date,
			LocalTime imsak,
			LocalTime gunes,
			LocalTime ogle,
			LocalTime ikindi,
			LocalTime aksam,
			LocalTime yatsi,
			int hijriDay,
			int hijriMonth,
			int hijriYear) {
	}

	public record HijriDate(int day, int month, int year, String display) {
	}
}
