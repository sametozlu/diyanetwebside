package gov.diyanet.portal.modules.content;

import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.modules.content.NewsDtos.NewsDetail;
import gov.diyanet.portal.modules.content.NewsDtos.NewsSummary;
import gov.diyanet.portal.modules.content.NewsDtos.NewsWriteRequest;
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
@RequestMapping("/api/admin/news")
@RequiredArgsConstructor
public class AdminNewsController {

	private final NewsService newsService;

	@GetMapping
	public PagedResponse<NewsSummary> list(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		return newsService.adminList(page, size);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public NewsDetail create(@Valid @RequestBody NewsWriteRequest request) {
		return newsService.create(request);
	}

	@PutMapping("/{id}")
	public NewsDetail update(@PathVariable Long id, @Valid @RequestBody NewsWriteRequest request) {
		return newsService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		newsService.delete(id);
	}
}
