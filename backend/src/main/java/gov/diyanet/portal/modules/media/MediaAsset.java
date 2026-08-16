package gov.diyanet.portal.modules.media;

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
@Table(name = "media")
public class MediaAsset extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String slug;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "text")
	private String summary;

	@Column(nullable = false, length = 32)
	private String type;

	@Column(name = "video_url")
	private String videoUrl;

	@Column(name = "thumbnail_url")
	private String thumbnailUrl;

	@Column(name = "duration_seconds")
	private Integer durationSeconds;

	@Column(name = "published_at")
	private Instant publishedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private MediaCategory category;
}
