package gov.diyanet.portal.modules.sermon;

import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.modules.sermon.SermonService.SermonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sermons")
@RequiredArgsConstructor
public class SermonController {

	private final SermonService sermonService;

	@GetMapping
	public PagedResponse<SermonDto> list(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		return sermonService.list(page, size);
	}

	@GetMapping("/{slug}")
	public SermonDto get(@PathVariable String slug) {
		return sermonService.getBySlug(slug);
	}
}
