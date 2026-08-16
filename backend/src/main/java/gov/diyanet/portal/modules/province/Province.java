package gov.diyanet.portal.modules.province;

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
@Table(name = "provinces")
public class Province extends BaseEntity {

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String slug;

	@Column(name = "plate_code", nullable = false, unique = true)
	private int plateCode;

	private Double lat;
	private Double lng;
	private String address;
	private String phone;
	private String email;
	private String website;

	@Column(columnDefinition = "text")
	private String about;
}
