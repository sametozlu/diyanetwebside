package gov.diyanet.portal.modules.content;

import gov.diyanet.portal.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "news")
public class News extends BaseEntity {

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, unique = true)
	private String slug;

	@Column(columnDefinition = "text")
	private String summary;

	@Column(columnDefinition = "text")
	private String body;

	@Column(name = "image_url")
	private String imageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private NewsCategory category;

	@Column(name = "published_at")
	private Instant publishedAt;

	@Column(nullable = false)
	@Builder.Default
	private boolean featured = false;

	@Column(name = "read_count", nullable = false)
	@Builder.Default
	private int readCount = 0;

	@Column(nullable = false, length = 5)
	@Builder.Default
	private String locale = "tr";

	@Column(nullable = false)
	@Builder.Default
	private String status = "PUBLISHED";
}
