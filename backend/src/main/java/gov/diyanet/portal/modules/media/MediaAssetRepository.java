package gov.diyanet.portal.modules.media;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {

	@EntityGraph(attributePaths = "category")
	Optional<MediaAsset> findBySlug(String slug);

	@EntityGraph(attributePaths = "category")
	Page<MediaAsset> findByType(String type, Pageable pageable);

	boolean existsBySlug(String slug);
}
