package gov.diyanet.portal.modules.publication;

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
@Table(name = "publications")
public class Publication extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String slug;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "text")
	private String summary;

	@Column(columnDefinition = "text")
	private String body;

	private String author;

	@Column(name = "published_at")
	private Instant publishedAt;

	@Column(name = "cover_url")
	private String coverUrl;

	@Column(name = "file_url")
	private String fileUrl;

	@Column(nullable = false, length = 32)
	private String type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private PublicationCategory category;
}
