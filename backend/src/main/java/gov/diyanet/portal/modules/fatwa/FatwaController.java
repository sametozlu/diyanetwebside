package gov.diyanet.portal.modules.fatwa;

import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.modules.fatwa.FatwaService.FatwaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fatwas")
@RequiredArgsConstructor
public class FatwaController {

	private final FatwaService fatwaService;

	@GetMapping
	public PagedResponse<FatwaDto> list(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String q) {
		return fatwaService.list(page, size, category, q);
	}

	@GetMapping("/categories")
	public java.util.List<FatwaService.CategoryDto> categories() {
		return fatwaService.categories();
	}

	@GetMapping("/{slug}")
	public FatwaDto get(@PathVariable String slug) {
		return fatwaService.getBySlug(slug);
	}
}
