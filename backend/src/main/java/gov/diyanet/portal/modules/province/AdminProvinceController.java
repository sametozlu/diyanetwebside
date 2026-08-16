package gov.diyanet.portal.modules.province;

import gov.diyanet.portal.modules.province.ProvinceService.ProvinceSummary;
import gov.diyanet.portal.modules.province.ProvinceService.ProvinceWriteRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/provinces")
@RequiredArgsConstructor
public class AdminProvinceController {

	private final ProvinceService provinceService;

	@GetMapping
	public List<ProvinceSummary> list() {
		return provinceService.list();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProvinceSummary create(@Valid @RequestBody ProvinceWriteRequest request) {
		return provinceService.create(request);
	}

	@PutMapping("/{id}")
	public ProvinceSummary update(@PathVariable Long id, @Valid @RequestBody ProvinceWriteRequest request) {
		return provinceService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		provinceService.delete(id);
	}
}
