package gov.diyanet.portal.modules.publication;

import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.modules.publication.PublicationService.PublicationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/publications")
@RequiredArgsConstructor
public class PublicationController {

	private final PublicationService publicationService;

	@GetMapping
	public PagedResponse<PublicationDto> list(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String q) {
		return publicationService.list(page, size, type, q);
	}

	@GetMapping("/{slug}")
	public PublicationDto get(@PathVariable String slug) {
		return publicationService.getBySlug(slug);
	}
}
