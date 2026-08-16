package gov.diyanet.portal.modules.province;

import gov.diyanet.portal.modules.province.ProvinceService.ProvinceDetail;
import gov.diyanet.portal.modules.province.ProvinceService.ProvinceSummary;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provinces")
@RequiredArgsConstructor
public class ProvinceController {

	private final ProvinceService provinceService;

	@GetMapping
	public List<ProvinceSummary> list() {
		return provinceService.list();
	}

	@GetMapping("/{slug}")
	public ProvinceDetail get(@PathVariable String slug) {
		return provinceService.getBySlug(slug);
	}
}
