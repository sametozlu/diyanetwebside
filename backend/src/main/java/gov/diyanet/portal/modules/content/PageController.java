package gov.diyanet.portal.modules.content;

import gov.diyanet.portal.modules.content.PageService.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pages")
@RequiredArgsConstructor
public class PageController {

	private final PageService pageService;

	@GetMapping("/{slug}")
	public PageDto get(@PathVariable String slug) {
		return pageService.getBySlug(slug);
	}
}
