package gov.diyanet.portal.modules.sermon;

import gov.diyanet.portal.common.api.PageParams;
import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.common.exception.NotFoundException;
import gov.diyanet.portal.modules.search.SearchService;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SermonService {

	private final SermonRepository sermonRepository;
	private final SermonCategoryRepository categoryRepository;
	private final SearchService searchService;

	@Transactional(readOnly = true)
	public PagedResponse<SermonDto> list(Integer page, Integer size) {
		var pageable = PageParams.of(page, size, Sort.by(Sort.Direction.DESC, "sermonDate"));
		return PagedResponse.of(sermonRepository.findAll(pageable).map(this::toDto));
	}

	@Transactional(readOnly = true)
	public SermonDto getBySlug(String slug) {
		return toDto(sermonRepository.findBySlug(slug)
				.orElseThrow(() -> new NotFoundException("Hutbe bulunamadı: " + slug)));
	}

	@Transactional
	public SermonDto create(SermonWriteRequest request) {
		if (sermonRepository.existsBySlug(request.slug())) {
			throw new IllegalArgumentException("Bu slug zaten kullanılıyor");
		}
		Sermon saved = sermonRepository.save(apply(new Sermon(), request));
		index(saved);
		return toDto(saved);
	}

	@Transactional
	public SermonDto update(Long id, SermonWriteRequest request) {
		Sermon entity = sermonRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Hutbe bulunamadı: " + id));
		Sermon saved = sermonRepository.save(apply(entity, request));
		index(saved);
		return toDto(saved);
	}

	@Transactional
	public void delete(Long id) {
		if (!sermonRepository.existsById(id)) {
			throw new NotFoundException("Hutbe bulunamadı: " + id);
		}
		searchService.remove("sermon", id);
		sermonRepository.deleteById(id);
	}

	private Sermon apply(Sermon entity, SermonWriteRequest request) {
		entity.setSlug(request.slug());
		entity.setTitle(request.title());
		entity.setSummary(request.summary());
		entity.setBody(request.body());
		entity.setPreacher(request.preacher());
		entity.setSermonDate(request.sermonDate());
		entity.setPdfUrl(request.pdfUrl());
		entity.setAudioUrl(request.audioUrl());
		if (request.categoryId() != null) {
			entity.setCategory(categoryRepository.findById(request.categoryId())
					.orElseThrow(() -> new NotFoundException("Kategori bulunamadı")));
		}
		return entity;
	}

	private void index(Sermon s) {
		searchService.index("sermon", s.getId(), s.getSlug(), s.getTitle(), s.getSummary(), s.getBody());
	}

	private SermonDto toDto(Sermon s) {
		String cat = s.getCategory() == null ? null : s.getCategory().getSlug();
		return new SermonDto(s.getId(), s.getSlug(), s.getTitle(), s.getSummary(), s.getBody(),
				s.getPreacher(), s.getSermonDate(), s.getPdfUrl(), s.getAudioUrl(), cat);
	}

	public record SermonDto(
			Long id, String slug, String title, String summary, String body, String preacher,
			LocalDate sermonDate, String pdfUrl, String audioUrl, String category) {
	}

	public record SermonWriteRequest(
			@NotBlank String slug,
			@NotBlank String title,
			String summary,
			String body,
			String preacher,
			LocalDate sermonDate,
			String pdfUrl,
			String audioUrl,
			Long categoryId) {
	}
}
