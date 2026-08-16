package gov.diyanet.portal.modules.catalog;

import gov.diyanet.portal.common.exception.NotFoundException;
import gov.diyanet.portal.config.CacheConfig;
import gov.diyanet.portal.modules.search.SearchService;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DigitalServiceService {

	private final DigitalServiceRepository repository;
	private final SearchService searchService;

	@Cacheable(CacheConfig.SERVICES)
	@Transactional(readOnly = true)
	public List<ServiceDto> list() {
		return repository.findAllByOrderBySortOrderAsc().stream().map(this::toDto).toList();
	}

	@CacheEvict(value = CacheConfig.SERVICES, allEntries = true)
	@Transactional
	public ServiceDto create(ServiceWriteRequest request) {
		if (repository.existsBySlug(request.slug())) {
			throw new IllegalArgumentException("Bu slug zaten kullanılıyor");
		}
		DigitalService saved = repository.save(apply(new DigitalService(), request));
		index(saved);
		return toDto(saved);
	}

	@CacheEvict(value = CacheConfig.SERVICES, allEntries = true)
	@Transactional
	public ServiceDto update(Long id, ServiceWriteRequest request) {
		DigitalService entity = repository.findById(id)
				.orElseThrow(() -> new NotFoundException("Hizmet bulunamadı: " + id));
		DigitalService saved = repository.save(apply(entity, request));
		index(saved);
		return toDto(saved);
	}

	@CacheEvict(value = CacheConfig.SERVICES, allEntries = true)
	@Transactional
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new NotFoundException("Hizmet bulunamadı: " + id);
		}
		searchService.remove("service", id);
		repository.deleteById(id);
	}

	private DigitalService apply(DigitalService entity, ServiceWriteRequest request) {
		entity.setSlug(request.slug());
		entity.setTitle(request.title());
		entity.setSummary(request.summary());
		entity.setHref(request.href());
		entity.setIcon(request.icon());
		entity.setCategory(request.category());
		entity.setSortOrder(request.sortOrder() == null ? 0 : request.sortOrder());
		return entity;
	}

	private void index(DigitalService s) {
		searchService.index("service", s.getId(), s.getSlug(), s.getTitle(), s.getSummary(), s.getHref());
	}

	private ServiceDto toDto(DigitalService s) {
		return new ServiceDto(s.getId(), s.getSlug(), s.getTitle(), s.getSummary(), s.getHref(), s.getIcon(),
				s.getCategory(), s.getSortOrder());
	}

	public record ServiceDto(
			Long id, String slug, String title, String summary, String href, String icon, String category, int sortOrder) {
	}

	public record ServiceWriteRequest(
			@NotBlank String slug,
			@NotBlank String title,
			String summary,
			String href,
			String icon,
			String category,
			Integer sortOrder) {
	}
}
