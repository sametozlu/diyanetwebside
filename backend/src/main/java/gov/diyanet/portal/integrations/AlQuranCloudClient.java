package gov.diyanet.portal.integrations;

import tools.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class AlQuranCloudClient {

	private final RestClient client;
	private final boolean enabled;
	private final String editionAr;
	private final String editionTr;

	public AlQuranCloudClient(
			@Qualifier("alquranRestClient") RestClient client,
			@Value("${app.integrations.alquran.enabled}") boolean enabled,
			@Value("${app.integrations.alquran.edition-ar}") String editionAr,
			@Value("${app.integrations.alquran.edition-tr}") String editionTr) {
		this.client = client;
		this.enabled = enabled;
		this.editionAr = editionAr;
		this.editionTr = editionTr;
	}

	public boolean enabled() {
		return enabled;
	}

	public List<RemoteSurah> listSurahs() {
		if (!enabled) {
			return List.of();
		}
		try {
			JsonNode root = client.get().uri("/surah").retrieve().body(JsonNode.class);
			if (root == null || root.path("code").asInt() != 200) {
				return List.of();
			}
			List<RemoteSurah> list = new ArrayList<>();
			for (JsonNode n : root.path("data")) {
				list.add(new RemoteSurah(
						n.path("number").asInt(),
						n.path("name").asText(),
						n.path("englishNameTranslation").asText(),
						n.path("englishName").asText(),
						n.path("numberOfAyahs").asInt(),
						n.path("revelationType").asText(),
						null));
			}
			return list;
		} catch (RestClientException ex) {
			log.warn("AlQuran list failed: {}", ex.getMessage());
			return List.of();
		}
	}

	public Optional<RemoteSurahDetail> getSurah(int number) {
		if (!enabled || number < 1 || number > 114) {
			return Optional.empty();
		}
		try {
			String uri = UriComponentsBuilder.fromPath("/surah/{n}/editions/{editions}")
					.buildAndExpand(number, editionAr + "," + editionTr)
					.toUriString();
			JsonNode root = client.get().uri(uri).retrieve().body(JsonNode.class);
			if (root == null || root.path("code").asInt() != 200) {
				return Optional.empty();
			}
			JsonNode data = root.path("data");
			JsonNode ar = data.get(0);
			JsonNode tr = data.size() > 1 ? data.get(1) : ar;
			if (ar == null) {
				return Optional.empty();
			}
			List<RemoteAyah> ayahs = new ArrayList<>();
			JsonNode arAyahs = ar.path("ayahs");
			JsonNode trAyahs = tr.path("ayahs");
			for (int i = 0; i < arAyahs.size(); i++) {
				JsonNode a = arAyahs.get(i);
				String textTr = i < trAyahs.size() ? trAyahs.get(i).path("text").asText() : "";
				ayahs.add(new RemoteAyah(
						a.path("numberInSurah").asInt(),
						a.path("text").asText(),
						textTr,
						a.path("juz").isMissingNode() ? null : a.path("juz").asInt(),
						a.path("page").isMissingNode() ? null : a.path("page").asInt()));
			}
			return Optional.of(new RemoteSurahDetail(
					ar.path("number").asInt(),
					ar.path("name").asText(),
					tr.path("englishNameTranslation").asText(ar.path("englishNameTranslation").asText()),
					ar.path("englishName").asText(),
					ar.path("numberOfAyahs").asInt(),
					ar.path("revelationType").asText(),
					ayahs.isEmpty() ? null : ayahs.getFirst().juz(),
					ayahs));
		} catch (RestClientException ex) {
			log.warn("AlQuran surah {} failed: {}", number, ex.getMessage());
			return Optional.empty();
		}
	}

	public List<RemoteSearchHit> search(String query) {
		if (!enabled || query == null || query.trim().length() < 2) {
			return List.of();
		}
		try {
			JsonNode root = client.get()
					.uri("/search/{q}/all/{edition}", query.trim(), editionTr)
					.retrieve()
					.body(JsonNode.class);
			if (root == null || root.path("code").asInt() != 200) {
				return List.of();
			}
			List<RemoteSearchHit> hits = new ArrayList<>();
			for (JsonNode m : root.path("data").path("matches")) {
				hits.add(new RemoteSearchHit(
						m.path("surah").path("number").asInt(),
						m.path("surah").path("englishNameTranslation").asText(),
						m.path("surah").path("name").asText(),
						m.path("numberInSurah").asInt(),
						"",
						m.path("text").asText()));
				if (hits.size() >= 40) {
					break;
				}
			}
			return hits;
		} catch (RestClientException ex) {
			log.warn("AlQuran search failed: {}", ex.getMessage());
			return List.of();
		}
	}

	public record RemoteSurah(
			int number, String nameAr, String nameTr, String nameEn,
			int ayahCount, String revelationType, Integer juzStart) {
	}

	public record RemoteAyah(int number, String textAr, String textTr, Integer juz, Integer page) {
	}

	public record RemoteSurahDetail(
			int number, String nameAr, String nameTr, String nameEn,
			int ayahCount, String revelationType, Integer juzStart,
			List<RemoteAyah> ayahs) {
	}

	public record RemoteSearchHit(
			int surahNumber, String surahNameTr, String surahNameAr,
			int ayahNumber, String textAr, String textTr) {
	}
}
