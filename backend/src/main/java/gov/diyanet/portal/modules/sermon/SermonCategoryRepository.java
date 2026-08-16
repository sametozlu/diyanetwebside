package gov.diyanet.portal.modules.sermon;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SermonCategoryRepository extends JpaRepository<SermonCategory, Long> {
	Optional<SermonCategory> findBySlug(String slug);
}
