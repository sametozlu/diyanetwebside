package gov.diyanet.portal.modules.catalog;

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
@Table(name = "services")
public class DigitalService extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String slug;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "text")
	private String summary;

	private String href;
	private String icon;
	private String category;

	@Column(name = "sort_order", nullable = false)
	@Builder.Default
	private int sortOrder = 0;
}
