package gov.diyanet.portal.modules.search;

import java.util.List;

public record SearchResponse(String query, String type, int total, List<SearchGroup> groups) {

	public SearchResponse(String query, List<SearchGroup> groups) {
		this(query, null, groups.stream().mapToInt(g -> g.items().size()).sum(), groups);
	}

	public record SearchGroup(String type, List<SearchItem> items) {
	}

	public record SearchItem(String slug, String title, String summary, String href) {
	}
}
