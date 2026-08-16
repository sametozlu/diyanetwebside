package gov.diyanet.portal.modules.hadith;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HadithRepository extends JpaRepository<Hadith, Long> {

	@EntityGraph(attributePaths = "category")
	Optional<Hadith> findBySlug(String slug);

	@EntityGraph(attributePaths = "category")
	Page<Hadith> findAll(Pageable pageable);

	@EntityGraph(attributePaths = "category")
	Page<Hadith> findByCategory_Slug(String slug, Pageable pageable);

	@Query("""
			SELECT h FROM Hadith h
			WHERE (:category IS NULL OR h.category.slug = :category)
			  AND (:q IS NULL OR LOWER(h.title) LIKE LOWER(CONCAT('%', :q, '%'))
			       OR LOWER(h.textTr) LIKE LOWER(CONCAT('%', :q, '%'))
			       OR LOWER(COALESCE(h.narrator, '')) LIKE LOWER(CONCAT('%', :q, '%'))
			       OR LOWER(COALESCE(h.source, '')) LIKE LOWER(CONCAT('%', :q, '%')))
			""")
	@EntityGraph(attributePaths = "category")
	Page<Hadith> search(@Param("category") String category, @Param("q") String q, Pageable pageable);
}
