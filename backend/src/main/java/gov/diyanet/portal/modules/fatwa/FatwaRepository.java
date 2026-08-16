package gov.diyanet.portal.modules.fatwa;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FatwaRepository extends JpaRepository<Fatwa, Long> {

	@EntityGraph(attributePaths = "category")
	Optional<Fatwa> findBySlug(String slug);

	@EntityGraph(attributePaths = "category")
	Page<Fatwa> findByCategory_Slug(String slug, Pageable pageable);

	boolean existsBySlug(String slug);

	java.util.List<Fatwa> findTop5ByCategory_SlugAndSlugNotOrderByPublishedAtDesc(String categorySlug, String slug);

	@Query("""
			SELECT f FROM Fatwa f
			WHERE (:category IS NULL OR f.category.slug = :category)
			""")
	@EntityGraph(attributePaths = "category")
	Page<Fatwa> searchByCategory(@Param("category") String category, Pageable pageable);

	@Query("""
			SELECT f FROM Fatwa f
			WHERE (:category IS NULL OR f.category.slug = :category)
			  AND (LOWER(f.question) LIKE LOWER(CONCAT('%', :q, '%'))
			       OR LOWER(f.answer) LIKE LOWER(CONCAT('%', :q, '%')))
			""")
	@EntityGraph(attributePaths = "category")
	Page<Fatwa> search(@Param("category") String category, @Param("q") String q, Pageable pageable);
}
