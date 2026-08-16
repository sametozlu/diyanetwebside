package gov.diyanet.portal.modules.publication;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

	@EntityGraph(attributePaths = "category")
	Optional<Publication> findBySlug(String slug);

	boolean existsBySlug(String slug);

	@Query("""
			SELECT p FROM Publication p
			WHERE (:type IS NULL OR p.type = :type)
			""")
	Page<Publication> searchByType(@Param("type") String type, Pageable pageable);

	@Query("""
			SELECT p FROM Publication p
			WHERE (:type IS NULL OR p.type = :type)
			  AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%'))
			       OR LOWER(COALESCE(p.summary, '')) LIKE LOWER(CONCAT('%', :q, '%')))
			""")
	Page<Publication> search(@Param("type") String type, @Param("q") String q, Pageable pageable);
}
