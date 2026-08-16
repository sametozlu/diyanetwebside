package gov.diyanet.portal.modules.event;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
	Optional<EventCategory> findBySlug(String slug);
}
