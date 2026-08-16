package gov.diyanet.portal.modules.sermon;

import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.modules.sermon.SermonService.SermonDto;
import gov.diyanet.portal.modules.sermon.SermonService.SermonWriteRequest;
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
@RequestMapping("/api/admin/sermons")
@RequiredArgsConstructor
public class AdminSermonController {

	private final SermonService sermonService;

	@GetMapping
	public PagedResponse<SermonDto> list(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		return sermonService.list(page, size);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SermonDto create(@Valid @RequestBody SermonWriteRequest request) {
		return sermonService.create(request);
	}

	@PutMapping("/{id}")
	public SermonDto update(@PathVariable Long id, @Valid @RequestBody SermonWriteRequest request) {
		return sermonService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		sermonService.delete(id);
	}
}
