package gov.diyanet.portal.modules.publication;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationCategoryRepository extends JpaRepository<PublicationCategory, Long> {
	Optional<PublicationCategory> findBySlug(String slug);
}
