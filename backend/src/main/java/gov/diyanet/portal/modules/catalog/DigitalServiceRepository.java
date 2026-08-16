package gov.diyanet.portal.modules.catalog;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigitalServiceRepository extends JpaRepository<DigitalService, Long> {

	List<DigitalService> findAllByOrderBySortOrderAsc();

	boolean existsBySlug(String slug);
}
