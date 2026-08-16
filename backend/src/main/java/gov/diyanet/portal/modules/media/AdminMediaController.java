package gov.diyanet.portal.modules.media;

import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.modules.media.MediaService.MediaDto;
import gov.diyanet.portal.modules.media.MediaService.MediaWriteRequest;
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
@RequestMapping("/api/admin/media")
@RequiredArgsConstructor
public class AdminMediaController {

	private final MediaService mediaService;

	@GetMapping
	public PagedResponse<MediaDto> list(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		return mediaService.list(page, size, null);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MediaDto create(@Valid @RequestBody MediaWriteRequest request) {
		return mediaService.create(request);
	}

	@PutMapping("/{id}")
	public MediaDto update(@PathVariable Long id, @Valid @RequestBody MediaWriteRequest request) {
		return mediaService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		mediaService.delete(id);
	}
}
