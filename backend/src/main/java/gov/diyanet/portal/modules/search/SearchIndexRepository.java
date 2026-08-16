package gov.diyanet.portal.modules.search;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SearchIndexRepository extends JpaRepository<SearchIndex, Long> {

	Optional<SearchIndex> findByEntityTypeAndEntityId(String entityType, Long entityId);

	@Query("""
			SELECT s FROM SearchIndex s
			WHERE (:type IS NULL OR s.entityType = :type)
			  AND (LOWER(s.title) LIKE LOWER(CONCAT('%', :q, '%'))
			       OR LOWER(COALESCE(s.summary, '')) LIKE LOWER(CONCAT('%', :q, '%'))
			       OR LOWER(COALESCE(s.body, '')) LIKE LOWER(CONCAT('%', :q, '%')))
			ORDER BY s.title
			""")
	List<SearchIndex> search(@Param("q") String q, @Param("type") String type, Pageable pageable);
}
