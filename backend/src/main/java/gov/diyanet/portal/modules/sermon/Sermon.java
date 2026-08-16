package gov.diyanet.portal.modules.sermon;

import gov.diyanet.portal.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
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
@Table(name = "sermons")
public class Sermon extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String slug;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "text")
	private String summary;

	@Column(columnDefinition = "text")
	private String body;

	private String preacher;

	@Column(name = "sermon_date")
	private LocalDate sermonDate;

	@Column(name = "pdf_url")
	private String pdfUrl;

	@Column(name = "audio_url")
	private String audioUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private SermonCategory category;
}
