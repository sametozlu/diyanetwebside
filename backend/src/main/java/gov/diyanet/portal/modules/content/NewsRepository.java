package gov.diyanet.portal.modules.content;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {

	@EntityGraph(attributePaths = "category")
	Optional<News> findBySlugAndStatus(String slug, String status);

	@EntityGraph(attributePaths = "category")
	Optional<News> findBySlug(String slug);

	@EntityGraph(attributePaths = "category")
	Page<News> findByStatus(String status, Pageable pageable);

	@EntityGraph(attributePaths = "category")
	Page<News> findByStatusAndFeatured(String status, boolean featured, Pageable pageable);

	@EntityGraph(attributePaths = "category")
	Page<News> findByStatusAndCategory_Slug(String status, String categorySlug, Pageable pageable);

	@EntityGraph(attributePaths = "category")
	Page<News> findByStatusAndCategory_SlugAndFeatured(
			String status, String categorySlug, boolean featured, Pageable pageable);

	@EntityGraph(attributePaths = "category")
	List<News> findTop3ByStatusOrderByPublishedAtDesc(String status);

	@EntityGraph(attributePaths = "category")
	List<News> findTop8ByStatusOrderByReadCountDesc(String status);

	@EntityGraph(attributePaths = "category")
	List<News> findTop5ByStatusAndCategory_SlugAndSlugNotOrderByPublishedAtDesc(
			String status, String categorySlug, String slug);

	@EntityGraph(attributePaths = "category")
	List<News> findTop5ByStatusAndSlugNotOrderByPublishedAtDesc(String status, String slug);

	boolean existsBySlug(String slug);
}
