package gov.diyanet.portal.modules.event;

import gov.diyanet.portal.common.api.PageParams;
import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.common.exception.NotFoundException;
import gov.diyanet.portal.modules.province.ProvinceRepository;
import gov.diyanet.portal.modules.search.SearchService;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

	private final EventRepository eventRepository;
	private final EventCategoryRepository categoryRepository;
	private final ProvinceRepository provinceRepository;
	private final SearchService searchService;

	@Transactional(readOnly = true)
	public PagedResponse<EventDto> list(Integer page, Integer size, String province, String category) {
		var pageable = PageParams.of(page, size, Sort.by(Sort.Direction.ASC, "startsAt"));
		return PagedResponse.of(eventRepository
				.search(blankToNull(province), blankToNull(category), pageable)
				.map(this::toDto));
	}

	@Transactional(readOnly = true)
	public EventDto getBySlug(String slug) {
		return toDto(eventRepository.findBySlug(slug)
				.orElseThrow(() -> new NotFoundException("Etkinlik bulunamadı: " + slug)));
	}

	@Transactional
	public EventDto create(EventWriteRequest request) {
		if (eventRepository.existsBySlug(request.slug())) {
			throw new IllegalArgumentException("Bu slug zaten kullanılıyor");
		}
		Event saved = eventRepository.save(apply(new Event(), request));
		index(saved);
		return toDto(saved);
	}

	@Transactional
	public EventDto update(Long id, EventWriteRequest request) {
		Event entity = eventRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Etkinlik bulunamadı: " + id));
		Event saved = eventRepository.save(apply(entity, request));
		index(saved);
		return toDto(saved);
	}

	@Transactional
	public void delete(Long id) {
		if (!eventRepository.existsById(id)) {
			throw new NotFoundException("Etkinlik bulunamadı: " + id);
		}
		searchService.remove("event", id);
		eventRepository.deleteById(id);
	}

	private Event apply(Event entity, EventWriteRequest request) {
		entity.setSlug(request.slug());
		entity.setTitle(request.title());
		entity.setSummary(request.summary());
		entity.setBody(request.body());
		entity.setStartsAt(request.startsAt());
		entity.setEndsAt(request.endsAt());
		entity.setLocation(request.location());
		if (request.provinceId() != null) {
			entity.setProvince(provinceRepository.findById(request.provinceId())
					.orElseThrow(() -> new NotFoundException("İl bulunamadı")));
		}
		if (request.categoryId() != null) {
			entity.setCategory(categoryRepository.findById(request.categoryId())
					.orElseThrow(() -> new NotFoundException("Kategori bulunamadı")));
		}
		return entity;
	}

	private void index(Event e) {
		searchService.index("event", e.getId(), e.getSlug(), e.getTitle(), e.getSummary(), e.getBody());
	}

	private EventDto toDto(Event e) {
		String cat = e.getCategory() == null ? null : e.getCategory().getSlug();
		String prov = e.getProvince() == null ? null : e.getProvince().getSlug();
		return new EventDto(e.getId(), e.getSlug(), e.getTitle(), e.getSummary(), e.getBody(),
				e.getStartsAt(), e.getEndsAt(), e.getLocation(), prov, cat);
	}

	private static String blankToNull(String value) {
		return value == null || value.isBlank() ? null : value;
	}

	public record EventDto(
			Long id, String slug, String title, String summary, String body,
			Instant startsAt, Instant endsAt, String location, String province, String category) {
	}

	public record EventWriteRequest(
			@NotBlank String slug,
			@NotBlank String title,
			String summary,
			String body,
			Instant startsAt,
			Instant endsAt,
			String location,
			Long provinceId,
			Long categoryId) {
	}
}
