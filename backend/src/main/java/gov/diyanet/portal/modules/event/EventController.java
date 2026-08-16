package gov.diyanet.portal.modules.event;

import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.modules.event.EventService.EventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

	private final EventService eventService;

	@GetMapping
	public PagedResponse<EventDto> list(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) String province,
			@RequestParam(required = false) String category) {
		return eventService.list(page, size, province, category);
	}

	@GetMapping("/{slug}")
	public EventDto get(@PathVariable String slug) {
		return eventService.getBySlug(slug);
	}
}
