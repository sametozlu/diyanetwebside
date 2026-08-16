package gov.diyanet.portal.modules.hadith;

import gov.diyanet.portal.common.api.PageParams;
import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.common.exception.NotFoundException;
import gov.diyanet.portal.config.CacheConfig;
import gov.diyanet.portal.integrations.HadithApiClient;
import gov.diyanet.portal.integrations.HadithApiClient.RemoteHadith;
import gov.diyanet.portal.integrations.HadithApiClient.Section;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HadithService {

	private final HadithRepository hadithRepository;
	private final HadithCategoryRepository categoryRepository;
	private final HadithApiClient hadithApiClient;

	@Transactional(readOnly = true)
	public PagedResponse<HadithDto> list(Integer page, Integer size, String category, String q) {
		var pageable = PageParams.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
		String cat = blankToNull(category);
		String query = blankToNull(q);
		Page<Hadith> result = (cat == null && query == null)
				? hadithRepository.findAll(pageable)
				: hadithRepository.search(cat, query, pageable);
		return PagedResponse.of(result.map(this::toDto));
	}

	@Transactional(readOnly = true)
	public HadithDto getBySlug(String slug) {
		return toDto(hadithRepository.findBySlug(slug)
				.orElseThrow(() -> new NotFoundException("Hadis bulunamadı: " + slug)));
	}

	@Transactional(readOnly = true)
	public HadithDto daily() {
		long count = hadithRepository.count();
		if (count == 0) {
			throw new NotFoundException("Hadis bulunamadı");
		}
		int index = LocalDate.now().getDayOfYear() % (int) count;
		Page<Hadith> page = hadithRepository.findAll(PageRequest.of(index, 1, Sort.by("id")));
		if (page.isEmpty()) {
			throw new NotFoundException("Hadis bulunamadı");
		}
		return toDto(page.getContent().getFirst());
	}

	@Transactional(readOnly = true)
	public List<CategoryDto> categories() {
		return categoryRepository.findAll().stream()
				.map(c -> new CategoryDto(c.getId(), c.getName(), c.getSlug()))
				.toList();
	}

	@Cacheable(CacheConfig.HADITH_REMOTE)
	public List<Section> remoteSections() {
		return hadithApiClient.sections();
	}

	@Cacheable(value = CacheConfig.HADITH_REMOTE, key = "'sec-' + #sectionId")
	public List<RemoteHadith> remoteSection(String sectionId) {
		return hadithApiClient.section(sectionId);
	}

	private HadithDto toDto(Hadith h) {
		String cat = h.getCategory() == null ? null : h.getCategory().getSlug();
		return new HadithDto(h.getId(), h.getSlug(), h.getTitle(), h.getTextAr(), h.getTextTr(),
				h.getSource(), h.getNarrator(), cat, h.getPublishedAt());
	}

	private static String blankToNull(String value) {
		return value == null || value.isBlank() ? null : value;
	}

	public record HadithDto(
			Long id, String slug, String title, String textAr, String textTr,
			String source, String narrator, String category, Instant publishedAt) {
	}

	public record CategoryDto(Long id, String name, String slug) {
	}
}
