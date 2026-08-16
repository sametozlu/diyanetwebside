package gov.diyanet.portal.modules.search;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

	@GetMapping
	public SearchResponse search(
			@RequestParam(name = "q", defaultValue = "") String q,
			@RequestParam(defaultValue = "20") int limit,
			@RequestParam(required = false) String type) {
		return searchService.search(q, limit, type);
	}
}
