package gov.diyanet.portal.modules.search;

import gov.diyanet.portal.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "search_index")
public class SearchIndex extends BaseEntity {

	@Column(name = "entity_type", nullable = false, length = 64)
	private String entityType;

	@Column(name = "entity_id", nullable = false)
	private Long entityId;

	private String slug;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "text")
	private String summary;

	@Column(columnDefinition = "text")
	private String body;

	@Column(nullable = false, length = 5)
	@Builder.Default
	private String locale = "tr";
}
