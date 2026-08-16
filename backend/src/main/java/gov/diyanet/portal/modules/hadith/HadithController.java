package gov.diyanet.portal.modules.hadith;

import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.integrations.HadithApiClient.RemoteHadith;
import gov.diyanet.portal.integrations.HadithApiClient.Section;
import gov.diyanet.portal.modules.hadith.HadithService.HadithDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hadith")
@RequiredArgsConstructor
public class HadithController {

	private final HadithService hadithService;

	@GetMapping
	public PagedResponse<HadithDto> list(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String q) {
		return hadithService.list(page, size, category, q);
	}

	@GetMapping("/daily")
	public HadithDto daily() {
		return hadithService.daily();
	}

	@GetMapping("/categories")
	public List<HadithService.CategoryDto> categories() {
		return hadithService.categories();
	}

	@GetMapping("/remote/sections")
	public List<Section> remoteSections() {
		return hadithService.remoteSections();
	}

	@GetMapping("/remote/sections/{id}")
	public List<RemoteHadith> remoteSection(@PathVariable String id) {
		return hadithService.remoteSection(id);
	}

	@GetMapping("/{slug}")
	public HadithDto get(@PathVariable String slug) {
		return hadithService.getBySlug(slug);
	}
}
