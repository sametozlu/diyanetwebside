package gov.diyanet.portal.modules.province;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Province, Long> {

	Optional<Province> findBySlug(String slug);

	List<Province> findAllByOrderByPlateCodeAsc();

	boolean existsBySlug(String slug);
}
