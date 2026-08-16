package gov.diyanet.portal.modules.content;

import gov.diyanet.portal.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "menu_items")
public class MenuItem extends BaseEntity {

	@Column(nullable = false)
	private String label;

	private String href;

	@Column(name = "sort_order", nullable = false)
	@Builder.Default
	private int sortOrder = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private MenuItem parent;

	@Column(nullable = false, length = 5)
	@Builder.Default
	private String locale = "tr";
}
