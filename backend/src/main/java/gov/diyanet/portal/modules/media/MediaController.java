package gov.diyanet.portal.modules.media;

import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.modules.media.MediaService.MediaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

	private final MediaService mediaService;

	@GetMapping
	public PagedResponse<MediaDto> list(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) String type) {
		return mediaService.list(page, size, type);
	}

	@GetMapping("/{slug}")
	public MediaDto get(@PathVariable String slug) {
		return mediaService.getBySlug(slug);
	}
}
