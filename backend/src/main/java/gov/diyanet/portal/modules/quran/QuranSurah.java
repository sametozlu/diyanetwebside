package gov.diyanet.portal.modules.quran;

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
@Table(name = "quran_surahs")
public class QuranSurah extends BaseEntity {

	@Column(nullable = false, unique = true)
	private int number;

	@Column(name = "name_ar", nullable = false)
	private String nameAr;

	@Column(name = "name_tr", nullable = false)
	private String nameTr;

	@Column(name = "name_en", nullable = false)
	private String nameEn;

	@Column(name = "ayah_count", nullable = false)
	private int ayahCount;

	@Column(name = "revelation_type", nullable = false, length = 16)
	private String revelationType;

	@Column(name = "juz_start")
	private Integer juzStart;
}
