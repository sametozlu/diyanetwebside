package gov.diyanet.portal.modules.fatwa;

import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.modules.fatwa.FatwaService.FatwaDto;
import gov.diyanet.portal.modules.fatwa.FatwaService.FatwaWriteRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/fatwas")
@RequiredArgsConstructor
public class AdminFatwaController {

	private final FatwaService fatwaService;

	@GetMapping
	public PagedResponse<FatwaDto> list(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		return fatwaService.list(page, size, null, null);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FatwaDto create(@Valid @RequestBody FatwaWriteRequest request) {
		return fatwaService.create(request);
	}

	@PutMapping("/{id}")
	public FatwaDto update(@PathVariable Long id, @Valid @RequestBody FatwaWriteRequest request) {
		return fatwaService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		fatwaService.delete(id);
	}
}
