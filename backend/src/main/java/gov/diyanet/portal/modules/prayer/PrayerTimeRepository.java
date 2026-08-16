package gov.diyanet.portal.modules.prayer;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrayerTimeRepository extends JpaRepository<PrayerTime, Long> {

	Optional<PrayerTime> findByProvince_IdAndDate(Long provinceId, LocalDate date);
}
