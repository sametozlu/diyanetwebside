package gov.diyanet.portal.modules.quran;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuranSurahRepository extends JpaRepository<QuranSurah, Long> {

	List<QuranSurah> findAllByOrderByNumberAsc();

	Optional<QuranSurah> findByNumber(int number);
}
