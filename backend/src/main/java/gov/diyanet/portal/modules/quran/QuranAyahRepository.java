package gov.diyanet.portal.modules.quran;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuranAyahRepository extends JpaRepository<QuranAyah, Long> {

	List<QuranAyah> findBySurah_IdOrderByNumberAsc(Long surahId);

	@Query("""
			SELECT a FROM QuranAyah a JOIN FETCH a.surah s
			WHERE LOWER(a.textTr) LIKE LOWER(CONCAT('%', :q, '%'))
			   OR a.textAr LIKE CONCAT('%', :q, '%')
			ORDER BY s.number, a.number
			""")
	List<QuranAyah> search(@Param("q") String q, Pageable pageable);
}
