package gov.diyanet.portal.modules.media;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaCategoryRepository extends JpaRepository<MediaCategory, Long> {
	Optional<MediaCategory> findBySlug(String slug);
}
