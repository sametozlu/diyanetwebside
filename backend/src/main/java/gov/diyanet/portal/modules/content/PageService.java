package gov.diyanet.portal.modules.content;

import gov.diyanet.portal.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PageService {

	private final PageRepository pageRepository;

	@Transactional(readOnly = true)
	public PageDto getBySlug(String slug) {
		PageEntity page = pageRepository.findBySlug(slug)
				.orElseThrow(() -> new NotFoundException("Sayfa bulunamadı: " + slug));
		return new PageDto(page.getSlug(), page.getTitle(), page.getBody(), page.getLocale());
	}

	public record PageDto(String slug, String title, String body, String locale) {
	}
}
