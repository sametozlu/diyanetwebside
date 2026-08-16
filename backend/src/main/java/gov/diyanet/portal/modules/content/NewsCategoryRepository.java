package gov.diyanet.portal.modules.content;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {

	Optional<NewsCategory> findBySlug(String slug);
}
