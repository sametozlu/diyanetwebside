package gov.diyanet.portal.modules.media;

import gov.diyanet.portal.common.api.PageParams;
import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.common.exception.NotFoundException;
import gov.diyanet.portal.modules.search.SearchService;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MediaService {

	private final MediaAssetRepository mediaAssetRepository;
	private final MediaCategoryRepository categoryRepository;
	private final SearchService searchService;

	@Transactional(readOnly = true)
	public PagedResponse<MediaDto> list(Integer page, Integer size, String type) {
		var pageable = PageParams.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
		Page<MediaAsset> result = (type == null || type.isBlank())
				? mediaAssetRepository.findAll(pageable)
				: mediaAssetRepository.findByType(type, pageable);
		return PagedResponse.of(result.map(this::toDto));
	}

	@Transactional(readOnly = true)
	public MediaDto getBySlug(String slug) {
		return toDto(mediaAssetRepository.findBySlug(slug)
				.orElseThrow(() -> new NotFoundException("Medya bulunamadı: " + slug)));
	}

	@Transactional
	public MediaDto create(MediaWriteRequest request) {
		if (mediaAssetRepository.existsBySlug(request.slug())) {
			throw new IllegalArgumentException("Bu slug zaten kullanılıyor");
		}
		MediaAsset saved = mediaAssetRepository.save(apply(new MediaAsset(), request));
		index(saved);
		return toDto(saved);
	}

	@Transactional
	public MediaDto update(Long id, MediaWriteRequest request) {
		MediaAsset entity = mediaAssetRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Medya bulunamadı: " + id));
		MediaAsset saved = mediaAssetRepository.save(apply(entity, request));
		index(saved);
		return toDto(saved);
	}

	@Transactional
	public void delete(Long id) {
		if (!mediaAssetRepository.existsById(id)) {
			throw new NotFoundException("Medya bulunamadı: " + id);
		}
		searchService.remove("media", id);
		mediaAssetRepository.deleteById(id);
	}

	private MediaAsset apply(MediaAsset entity, MediaWriteRequest request) {
		entity.setSlug(request.slug());
		entity.setTitle(request.title());
		entity.setSummary(request.summary());
		entity.setType(request.type() == null ? "VIDEO" : request.type());
		entity.setVideoUrl(request.videoUrl());
		entity.setThumbnailUrl(request.thumbnailUrl());
		entity.setDurationSeconds(request.durationSeconds());
		entity.setPublishedAt(request.publishedAt() == null ? Instant.now() : request.publishedAt());
		if (request.categoryId() != null) {
			entity.setCategory(categoryRepository.findById(request.categoryId())
					.orElseThrow(() -> new NotFoundException("Kategori bulunamadı")));
		}
		return entity;
	}

	private void index(MediaAsset m) {
		searchService.index("media", m.getId(), m.getSlug(), m.getTitle(), m.getSummary(), m.getType());
	}

	private MediaDto toDto(MediaAsset m) {
		String cat = m.getCategory() == null ? null : m.getCategory().getSlug();
		return new MediaDto(m.getId(), m.getSlug(), m.getTitle(), m.getSummary(), m.getType(),
				m.getVideoUrl(), m.getThumbnailUrl(), m.getDurationSeconds(), m.getPublishedAt(), cat);
	}

	public record MediaDto(
			Long id, String slug, String title, String summary, String type, String videoUrl,
			String thumbnailUrl, Integer durationSeconds, Instant publishedAt, String category) {
	}

	public record MediaWriteRequest(
			@NotBlank String slug,
			@NotBlank String title,
			String summary,
			String type,
			String videoUrl,
			String thumbnailUrl,
			Integer durationSeconds,
			Instant publishedAt,
			Long categoryId) {
	}
}
