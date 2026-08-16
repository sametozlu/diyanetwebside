package gov.diyanet.portal.integrations;

import tools.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class HadithApiClient {

	private final RestClient client;
	private final boolean enabled;
	private final String edition;

	public HadithApiClient(
			@Qualifier("hadithRestClient") RestClient client,
			@Value("${app.integrations.hadith.enabled}") boolean enabled,
			@Value("${app.integrations.hadith.edition}") String edition) {
		this.client = client;
		this.enabled = enabled;
		this.edition = edition;
	}

	public boolean enabled() {
		return enabled;
	}

	public List<Section> sections() {
		if (!enabled) {
			return List.of();
		}
		try {
			JsonNode root = client.get().uri("/editions/{ed}/sections.json", edition).retrieve().body(JsonNode.class);
			if (root == null) {
				return List.of();
			}
			JsonNode metadata = root.path("metadata").path("sections");
			List<Section> list = new ArrayList<>();
			for (String key : metadata.propertyNames()) {
				if (!"0".equals(key)) {
					list.add(new Section(key, metadata.path(key).asText()));
				}
			}
			return list;
		} catch (Exception ex) {
			log.warn("Hadith sections failed: {}", ex.getMessage());
			return List.of();
		}
	}

	public List<RemoteHadith> section(String sectionId) {
		if (!enabled) {
			return List.of();
		}
		try {
			JsonNode root = client.get()
					.uri("/editions/{ed}/sections/{id}.json", edition, sectionId)
					.retrieve()
					.body(JsonNode.class);
			if (root == null) {
				return List.of();
			}
			List<RemoteHadith> list = new ArrayList<>();
			for (JsonNode h : root.path("hadiths")) {
				int num = h.path("hadithnumber").asInt();
				String text = h.path("text").asText();
				if (text.isBlank()) {
					continue;
				}
				list.add(new RemoteHadith(
						edition + "-" + sectionId + "-" + num,
						"Buhârî " + num,
						text,
						"Sahîh-i Buhârî",
						null));
			}
			return list;
		} catch (Exception ex) {
			log.warn("Hadith section {} failed: {}", sectionId, ex.getMessage());
			return List.of();
		}
	}

	public record Section(String id, String name) {
	}

	public record RemoteHadith(String slug, String title, String textTr, String source, String narrator) {
	}
}
