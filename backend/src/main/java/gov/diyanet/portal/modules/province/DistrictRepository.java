package gov.diyanet.portal.modules.province;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, Long> {

	List<District> findByProvince_IdOrderByNameAsc(Long provinceId);

	Optional<District> findByProvince_IdAndSlug(Long provinceId, String slug);
}
