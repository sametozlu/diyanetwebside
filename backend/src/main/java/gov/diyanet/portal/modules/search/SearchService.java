package gov.diyanet.portal.modules.search;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

	private final SearchIndexRepository searchIndexRepository;

	@Transactional(readOnly = true)
	public SearchResponse search(String query, int limit, String type) {
		String q = query == null ? "" : query.trim();
		if (q.length() < 2) {
			return new SearchResponse(q, type, 0, List.of());
		}
		int capped = Math.min(Math.max(limit, 1), 50);
		String typeFilter = type == null || type.isBlank() ? null : type.trim().toLowerCase(Locale.ROOT);
		List<SearchIndex> rows = searchIndexRepository.search(q, typeFilter, PageRequest.of(0, capped * 3));
		Map<String, List<SearchResponse.SearchItem>> grouped = new LinkedHashMap<>();
		int total = 0;
		for (SearchIndex row : rows) {
			List<SearchResponse.SearchItem> items = grouped.computeIfAbsent(row.getEntityType(), k -> new ArrayList<>());
			if (items.size() >= 8) {
				continue;
			}
			items.add(new SearchResponse.SearchItem(
					row.getSlug(),
					row.getTitle(),
					row.getSummary(),
					hrefFor(row.getEntityType(), row.getSlug())));
			total++;
		}
		List<SearchResponse.SearchGroup> groups = grouped.entrySet().stream()
				.map(e -> new SearchResponse.SearchGroup(e.getKey(), e.getValue()))
				.toList();
		return new SearchResponse(q, typeFilter, total, groups);
	}

	@Transactional(readOnly = true)
	public SearchResponse search(String query, int limit) {
		return search(query, limit, null);
	}

	@Transactional
	public void index(String type, Long id, String slug, String title, String summary, String body) {
		SearchIndex row = searchIndexRepository.findByEntityTypeAndEntityId(type, id)
				.orElseGet(SearchIndex::new);
		row.setEntityType(type);
		row.setEntityId(id);
		row.setSlug(slug);
		row.setTitle(title);
		row.setSummary(summary);
		row.setBody(body);
		row.setLocale("tr");
		searchIndexRepository.save(row);
	}

	@Transactional
	public void remove(String type, Long id) {
		searchIndexRepository.findByEntityTypeAndEntityId(type, id)
				.ifPresent(searchIndexRepository::delete);
	}

	static String hrefFor(String type, String slug) {
		if (slug == null) {
			return "/";
		}
		return switch (type.toLowerCase(Locale.ROOT)) {
			case "news" -> "/haberler/" + slug;
			case "hadith" -> "/hadis/" + slug;
			case "fatwa" -> "/fetva/" + slug;
			case "sermon" -> "/hutbeler/" + slug;
			case "publication" -> "/yayinlar/" + slug;
			case "event" -> "/etkinlikler/" + slug;
			case "media" -> "/medya/" + slug;
			case "page" -> "/sayfa/" + slug;
			case "province" -> "/il-muftulukleri/" + slug;
			case "service" -> "/hizmetler";
			case "quran" -> "/kuran/" + slug;
			default -> "/" + slug;
		};
	}
}
