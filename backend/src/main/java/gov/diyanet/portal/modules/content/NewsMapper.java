package gov.diyanet.portal.modules.content;

import gov.diyanet.portal.modules.content.NewsDtos.CategoryDto;
import gov.diyanet.portal.modules.content.NewsDtos.NewsDetail;
import gov.diyanet.portal.modules.content.NewsDtos.NewsSummary;

public final class NewsMapper {

	private NewsMapper() {
	}

	public static CategoryDto toCategory(NewsCategory category) {
		if (category == null) {
			return null;
		}
		return new CategoryDto(category.getId(), category.getName(), category.getSlug());
	}

	public static NewsSummary toSummary(News news) {
		return new NewsSummary(
				news.getId(),
				news.getTitle(),
				news.getSlug(),
				news.getSummary(),
				news.getImageUrl(),
				toCategory(news.getCategory()),
				news.getPublishedAt(),
				news.isFeatured(),
				news.getReadCount(),
				news.getLocale(),
				news.getStatus());
	}

	public static NewsDetail toDetail(News news) {
		return toDetail(news, java.util.List.of());
	}

	public static NewsDetail toDetail(News news, java.util.List<NewsSummary> related) {
		return new NewsDetail(
				news.getId(),
				news.getTitle(),
				news.getSlug(),
				news.getSummary(),
				news.getBody(),
				news.getImageUrl(),
				toCategory(news.getCategory()),
				news.getPublishedAt(),
				news.isFeatured(),
				news.getReadCount(),
				news.getLocale(),
				news.getStatus(),
				related);
	}
}
