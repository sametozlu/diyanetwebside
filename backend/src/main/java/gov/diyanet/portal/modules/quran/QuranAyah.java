package gov.diyanet.portal.modules.quran;

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
@Table(name = "quran_ayahs")
public class QuranAyah extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "surah_id", nullable = false)
	private QuranSurah surah;

	@Column(nullable = false)
	private int number;

	@Column(name = "text_ar", nullable = false, columnDefinition = "text")
	private String textAr;

	@Column(name = "text_tr", nullable = false, columnDefinition = "text")
	private String textTr;

	private Integer juz;
	private Integer page;
}
