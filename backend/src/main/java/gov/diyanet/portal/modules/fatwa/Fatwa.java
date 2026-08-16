package gov.diyanet.portal.modules.fatwa;

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
@Table(name = "fatwas")
public class Fatwa extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String slug;

	@Column(nullable = false, columnDefinition = "text")
	private String question;

	@Column(nullable = false, columnDefinition = "text")
	private String answer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private FatwaCategory category;

	@Column(name = "published_at")
	private Instant publishedAt;
}
