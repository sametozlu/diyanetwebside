package gov.diyanet.portal.modules.event;

import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.modules.event.EventService.EventDto;
import gov.diyanet.portal.modules.event.EventService.EventWriteRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

	private final EventService eventService;

	@GetMapping
	public PagedResponse<EventDto> list(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		return eventService.list(page, size, null, null);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EventDto create(@Valid @RequestBody EventWriteRequest request) {
		return eventService.create(request);
	}

	@PutMapping("/{id}")
	public EventDto update(@PathVariable Long id, @Valid @RequestBody EventWriteRequest request) {
		return eventService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		eventService.delete(id);
	}
}
