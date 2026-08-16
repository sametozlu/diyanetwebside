package gov.diyanet.portal.modules.sermon;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SermonRepository extends JpaRepository<Sermon, Long> {

	@EntityGraph(attributePaths = "category")
	Optional<Sermon> findBySlug(String slug);

	@EntityGraph(attributePaths = "category")
	Page<Sermon> findAll(Pageable pageable);

	boolean existsBySlug(String slug);
}
