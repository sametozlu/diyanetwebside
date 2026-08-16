package gov.diyanet.portal.modules.fatwa;

import gov.diyanet.portal.common.api.PageParams;
import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.common.exception.NotFoundException;
import gov.diyanet.portal.modules.search.SearchService;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FatwaService {

	private final FatwaRepository fatwaRepository;
	private final FatwaCategoryRepository categoryRepository;
	private final SearchService searchService;

	@Transactional(readOnly = true)
	public PagedResponse<FatwaDto> list(Integer page, Integer size, String category, String q) {
		var pageable = PageParams.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
		String cat = blankToNull(category);
		String query = blankToNull(q);
		Page<Fatwa> result = query == null
				? fatwaRepository.searchByCategory(cat, pageable)
				: fatwaRepository.search(cat, query, pageable);
		return PagedResponse.of(result.map(this::toSummary));
	}

	@Transactional(readOnly = true)
	public FatwaDto getBySlug(String slug) {
		Fatwa fatwa = fatwaRepository.findBySlug(slug)
				.orElseThrow(() -> new NotFoundException("Fetva bulunamadı: " + slug));
		List<FatwaDto> related = List.of();
		if (fatwa.getCategory() != null) {
			related = fatwaRepository
					.findTop5ByCategory_SlugAndSlugNotOrderByPublishedAtDesc(fatwa.getCategory().getSlug(), fatwa.getSlug())
					.stream()
					.map(this::toSummary)
					.toList();
		}
		return toDto(fatwa, related);
	}

	@Transactional(readOnly = true)
	public java.util.List<CategoryDto> categories() {
		return categoryRepository.findAll().stream()
				.map(c -> new CategoryDto(c.getId(), c.getName(), c.getSlug()))
				.toList();
	}

	@Transactional
	public FatwaDto create(FatwaWriteRequest request) {
		if (fatwaRepository.existsBySlug(request.slug())) {
			throw new IllegalArgumentException("Bu slug zaten kullanılıyor");
		}
		Fatwa saved = fatwaRepository.save(apply(new Fatwa(), request));
		index(saved);
		return toSummary(saved);
	}

	@Transactional
	public FatwaDto update(Long id, FatwaWriteRequest request) {
		Fatwa entity = fatwaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Fetva bulunamadı: " + id));
		Fatwa saved = fatwaRepository.save(apply(entity, request));
		index(saved);
		return toSummary(saved);
	}

	@Transactional
	public void delete(Long id) {
		if (!fatwaRepository.existsById(id)) {
			throw new NotFoundException("Fetva bulunamadı: " + id);
		}
		searchService.remove("fatwa", id);
		fatwaRepository.deleteById(id);
	}

	private Fatwa apply(Fatwa entity, FatwaWriteRequest request) {
		entity.setSlug(request.slug());
		entity.setQuestion(request.question());
		entity.setAnswer(request.answer());
		entity.setPublishedAt(request.publishedAt() == null ? Instant.now() : request.publishedAt());
		if (request.categoryId() != null) {
			entity.setCategory(categoryRepository.findById(request.categoryId())
					.orElseThrow(() -> new NotFoundException("Kategori bulunamadı")));
		}
		return entity;
	}

	private void index(Fatwa f) {
		searchService.index("fatwa", f.getId(), f.getSlug(), f.getQuestion(), f.getAnswer(), f.getAnswer());
	}

	private FatwaDto toSummary(Fatwa f) {
		return toDto(f, List.of());
	}

	private FatwaDto toDto(Fatwa f, List<FatwaDto> related) {
		String cat = f.getCategory() == null ? null : f.getCategory().getSlug();
		return new FatwaDto(f.getId(), f.getSlug(), f.getQuestion(), f.getAnswer(), cat, f.getPublishedAt(), related);
	}

	private static String blankToNull(String value) {
		return value == null || value.isBlank() ? null : value;
	}

	public record FatwaDto(
			Long id, String slug, String question, String answer, String category, Instant publishedAt,
			List<FatwaDto> related) {
	}

	public record CategoryDto(Long id, String name, String slug) {
	}

	public record FatwaWriteRequest(
			@NotBlank String slug,
			@NotBlank String question,
			@NotBlank String answer,
			Long categoryId,
			Instant publishedAt) {
	}
}
