package gov.diyanet.portal.modules.event;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

	@EntityGraph(attributePaths = {"category", "province"})
	Optional<Event> findBySlug(String slug);

	boolean existsBySlug(String slug);

	@Query("""
			SELECT e FROM Event e
			WHERE (:province IS NULL OR e.province.slug = :province)
			  AND (:category IS NULL OR e.category.slug = :category)
			""")
	Page<Event> search(@Param("province") String province, @Param("category") String category, Pageable pageable);
}
