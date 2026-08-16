package gov.diyanet.portal.modules.content;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public final class NewsDtos {

	private NewsDtos() {
	}

	public record CategoryDto(Long id, String name, String slug) {
	}

	public record NewsSummary(
			Long id,
			String title,
			String slug,
			String summary,
			String imageUrl,
			CategoryDto category,
			Instant publishedAt,
			boolean featured,
			int readCount,
			String locale,
			String status) {
	}

	public record NewsDetail(
			Long id,
			String title,
			String slug,
			String summary,
			String body,
			String imageUrl,
			CategoryDto category,
			Instant publishedAt,
			boolean featured,
			int readCount,
			String locale,
			String status,
			java.util.List<NewsSummary> related) {
	}

	public record NewsWriteRequest(
			@NotBlank String title,
			@NotBlank String slug,
			String summary,
			String body,
			String imageUrl,
			Long categoryId,
			Boolean featured,
			String locale,
			String status) {
	}
}
