package gov.diyanet.portal.modules.fatwa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FatwaCategoryRepository extends JpaRepository<FatwaCategory, Long> {
	Optional<FatwaCategory> findBySlug(String slug);
}
