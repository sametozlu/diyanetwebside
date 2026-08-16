package gov.diyanet.portal.modules.publication;

import gov.diyanet.portal.common.api.PageParams;
import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.common.exception.NotFoundException;
import gov.diyanet.portal.modules.search.SearchService;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PublicationService {

	private final PublicationRepository publicationRepository;
	private final PublicationCategoryRepository categoryRepository;
	private final SearchService searchService;

	@Transactional(readOnly = true)
	public PagedResponse<PublicationDto> list(Integer page, Integer size, String type, String q) {
		var pageable = PageParams.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
		String typeKey = blankToNull(type);
		String query = blankToNull(q);
		var result = query == null
				? publicationRepository.searchByType(typeKey, pageable)
				: publicationRepository.search(typeKey, query, pageable);
		return PagedResponse.of(result.map(this::toDto));
	}

	@Transactional(readOnly = true)
	public PublicationDto getBySlug(String slug) {
		return toDto(publicationRepository.findBySlug(slug)
				.orElseThrow(() -> new NotFoundException("Yayın bulunamadı: " + slug)));
	}

	@Transactional
	public PublicationDto create(PublicationWriteRequest request) {
		if (publicationRepository.existsBySlug(request.slug())) {
			throw new IllegalArgumentException("Bu slug zaten kullanılıyor");
		}
		Publication saved = publicationRepository.save(apply(new Publication(), request));
		index(saved);
		return toDto(saved);
	}

	@Transactional
	public PublicationDto update(Long id, PublicationWriteRequest request) {
		Publication entity = publicationRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Yayın bulunamadı: " + id));
		Publication saved = publicationRepository.save(apply(entity, request));
		index(saved);
		return toDto(saved);
	}

	@Transactional
	public void delete(Long id) {
		if (!publicationRepository.existsById(id)) {
			throw new NotFoundException("Yayın bulunamadı: " + id);
		}
		searchService.remove("publication", id);
		publicationRepository.deleteById(id);
	}

	private Publication apply(Publication entity, PublicationWriteRequest request) {
		entity.setSlug(request.slug());
		entity.setTitle(request.title());
		entity.setSummary(request.summary());
		entity.setBody(request.body());
		entity.setAuthor(request.author());
		entity.setPublishedAt(request.publishedAt() == null ? Instant.now() : request.publishedAt());
		entity.setCoverUrl(request.coverUrl());
		entity.setFileUrl(request.fileUrl());
		entity.setType(request.type() == null ? "BOOK" : request.type());
		if (request.categoryId() != null) {
			entity.setCategory(categoryRepository.findById(request.categoryId())
					.orElseThrow(() -> new NotFoundException("Kategori bulunamadı")));
		}
		return entity;
	}

	private void index(Publication p) {
		searchService.index("publication", p.getId(), p.getSlug(), p.getTitle(), p.getSummary(), p.getBody());
	}

	private PublicationDto toDto(Publication p) {
		String cat = p.getCategory() == null ? null : p.getCategory().getSlug();
		return new PublicationDto(p.getId(), p.getSlug(), p.getTitle(), p.getSummary(), p.getBody(),
				p.getAuthor(), p.getPublishedAt(), p.getCoverUrl(), p.getFileUrl(), p.getType(), cat);
	}

	private static String blankToNull(String value) {
		return value == null || value.isBlank() ? null : value;
	}

	public record PublicationDto(
			Long id, String slug, String title, String summary, String body, String author,
			Instant publishedAt, String coverUrl, String fileUrl, String type, String category) {
	}

	public record PublicationWriteRequest(
			@NotBlank String slug,
			@NotBlank String title,
			String summary,
			String body,
			String author,
			Instant publishedAt,
			String coverUrl,
			String fileUrl,
			String type,
			Long categoryId) {
	}
}
