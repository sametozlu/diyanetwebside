package gov.diyanet.portal.modules.content;

import gov.diyanet.portal.common.api.PageParams;
import gov.diyanet.portal.common.api.PagedResponse;
import gov.diyanet.portal.common.exception.NotFoundException;
import gov.diyanet.portal.modules.content.NewsDtos.CategoryDto;
import gov.diyanet.portal.modules.content.NewsDtos.NewsDetail;
import gov.diyanet.portal.modules.content.NewsDtos.NewsSummary;
import gov.diyanet.portal.modules.content.NewsDtos.NewsWriteRequest;
import gov.diyanet.portal.modules.search.SearchService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsService {

	private final NewsRepository newsRepository;
	private final NewsCategoryRepository newsCategoryRepository;
	private final SearchService searchService;

	@Transactional(readOnly = true)
	public PagedResponse<NewsSummary> list(Integer page, Integer size, String category, Boolean featured) {
		Pageable pageable = PageParams.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
		Page<News> result;
		boolean hasCategory = category != null && !category.isBlank();
		if (hasCategory && featured != null) {
			result = newsRepository.findByStatusAndCategory_SlugAndFeatured("PUBLISHED", category, featured, pageable);
		} else if (hasCategory) {
			result = newsRepository.findByStatusAndCategory_Slug("PUBLISHED", category, pageable);
		} else if (featured != null) {
			result = newsRepository.findByStatusAndFeatured("PUBLISHED", featured, pageable);
		} else {
			result = newsRepository.findByStatus("PUBLISHED", pageable);
		}
		return PagedResponse.of(result.map(NewsMapper::toSummary));
	}

	@Transactional(readOnly = true)
	public List<NewsSummary> mostRead() {
		return newsRepository.findTop8ByStatusOrderByReadCountDesc("PUBLISHED").stream()
				.map(NewsMapper::toSummary)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<CategoryDto> categories() {
		return newsCategoryRepository.findAll().stream().map(NewsMapper::toCategory).toList();
	}

	@Transactional
	public NewsDetail getBySlug(String slug) {
		News news = newsRepository.findBySlugAndStatus(slug, "PUBLISHED")
				.orElseThrow(() -> new NotFoundException("Haber bulunamadı: " + slug));
		news.setReadCount(news.getReadCount() + 1);
		String categorySlug = news.getCategory() == null ? null : news.getCategory().getSlug();
		List<NewsSummary> related = (categorySlug == null
				? newsRepository.findTop5ByStatusAndSlugNotOrderByPublishedAtDesc("PUBLISHED", news.getSlug())
				: newsRepository.findTop5ByStatusAndCategory_SlugAndSlugNotOrderByPublishedAtDesc(
						"PUBLISHED", categorySlug, news.getSlug()))
				.stream()
				.map(NewsMapper::toSummary)
				.toList();
		return NewsMapper.toDetail(news, related);
	}

	@Transactional(readOnly = true)
	public PagedResponse<NewsSummary> adminList(Integer page, Integer size) {
		Pageable pageable = PageParams.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		return PagedResponse.of(newsRepository.findAll(pageable).map(NewsMapper::toSummary));
	}

	@Transactional
	public NewsDetail create(NewsWriteRequest request) {
		if (newsRepository.existsBySlug(request.slug())) {
			throw new IllegalArgumentException("Bu slug zaten kullanılıyor: " + request.slug());
		}
		News news = new News();
		apply(news, request);
		if (news.getPublishedAt() == null) {
			news.setPublishedAt(Instant.now());
		}
		News saved = newsRepository.save(news);
		index(saved);
		return NewsMapper.toDetail(saved);
	}

	@Transactional
	public NewsDetail update(Long id, NewsWriteRequest request) {
		News news = newsRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Haber bulunamadı: " + id));
		apply(news, request);
		News saved = newsRepository.save(news);
		index(saved);
		return NewsMapper.toDetail(saved);
	}

	@Transactional
	public void delete(Long id) {
		News news = newsRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Haber bulunamadı: " + id));
		searchService.remove("news", id);
		newsRepository.delete(news);
	}

	private void apply(News news, NewsWriteRequest request) {
		news.setTitle(request.title());
		news.setSlug(request.slug());
		news.setSummary(request.summary());
		news.setBody(request.body());
		news.setImageUrl(request.imageUrl());
		news.setFeatured(Boolean.TRUE.equals(request.featured()));
		news.setLocale(request.locale() == null || request.locale().isBlank() ? "tr" : request.locale());
		news.setStatus(request.status() == null || request.status().isBlank() ? "PUBLISHED" : request.status());
		if (request.categoryId() != null) {
			NewsCategory category = newsCategoryRepository.findById(request.categoryId())
					.orElseThrow(() -> new NotFoundException("Kategori bulunamadı: " + request.categoryId()));
			news.setCategory(category);
		}
	}

	private void index(News news) {
		searchService.index("news", news.getId(), news.getSlug(), news.getTitle(), news.getSummary(), news.getBody());
	}
}
