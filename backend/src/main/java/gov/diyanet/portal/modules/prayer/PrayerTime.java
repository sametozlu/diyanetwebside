package gov.diyanet.portal.modules.prayer;

import gov.diyanet.portal.common.domain.BaseEntity;
import gov.diyanet.portal.modules.province.Province;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
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
@Table(name = "prayer_times")
public class PrayerTime extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "province_id", nullable = false)
	private Province province;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private LocalTime imsak;

	@Column(nullable = false)
	private LocalTime gunes;

	@Column(nullable = false)
	private LocalTime ogle;

	@Column(nullable = false)
	private LocalTime ikindi;

	@Column(nullable = false)
	private LocalTime aksam;

	@Column(nullable = false)
	private LocalTime yatsi;
}
