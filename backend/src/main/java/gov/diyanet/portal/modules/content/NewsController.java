package gov.diyanet.portal.modules.content;

import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.modules.content.NewsDtos.NewsDetail;
import gov.diyanet.portal.modules.content.NewsDtos.NewsSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

	private final NewsService newsService;

	@GetMapping
	public PagedResponse<NewsSummary> list(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) Boolean featured) {
		return newsService.list(page, size, category, featured);
	}

	@GetMapping("/categories")
	public java.util.List<NewsDtos.CategoryDto> categories() {
		return newsService.categories();
	}

	@GetMapping("/most-read")
	public java.util.List<NewsSummary> mostRead() {
		return newsService.mostRead();
	}

	@GetMapping("/{slug}")
	public NewsDetail get(@PathVariable String slug) {
		return newsService.getBySlug(slug);
	}
}
