package gov.diyanet.portal.modules.hadith;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HadithCategoryRepository extends JpaRepository<HadithCategory, Long> {

	Optional<HadithCategory> findBySlug(String slug);
}
