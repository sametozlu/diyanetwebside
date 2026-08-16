package gov.diyanet.portal.modules.content;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<PageEntity, Long> {

	Optional<PageEntity> findBySlug(String slug);
}
