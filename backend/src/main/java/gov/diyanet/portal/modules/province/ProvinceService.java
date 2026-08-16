package gov.diyanet.portal.modules.province;

import gov.diyanet.portal.config.CacheConfig;
import gov.diyanet.portal.common.exception.NotFoundException;
import gov.diyanet.portal.modules.content.NewsMapper;
import gov.diyanet.portal.modules.content.NewsRepository;
import gov.diyanet.portal.modules.prayer.PrayerTimesService;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProvinceService {

	private final ProvinceRepository provinceRepository;
	private final DistrictRepository districtRepository;
	private final NewsRepository newsRepository;
	private final PrayerTimesService prayerTimesService;

	@Cacheable(CacheConfig.PROVINCES)
	@Transactional(readOnly = true)
	public List<ProvinceSummary> list() {
		return provinceRepository.findAllByOrderByPlateCodeAsc().stream()
				.map(p -> new ProvinceSummary(p.getId(), p.getName(), p.getSlug(), p.getPlateCode(), p.getLat(), p.getLng()))
				.toList();
	}

	@Transactional(readOnly = true)
	public ProvinceDetail getBySlug(String slug) {
		Province province = provinceRepository.findBySlug(slug)
				.orElseThrow(() -> new NotFoundException("İl bulunamadı: " + slug));
		List<DistrictDto> districts = districtRepository.findByProvince_IdOrderByNameAsc(province.getId()).stream()
				.map(d -> new DistrictDto(d.getName(), d.getSlug()))
				.toList();
		var latestNews = newsRepository.findTop3ByStatusOrderByPublishedAtDesc("PUBLISHED").stream()
				.map(NewsMapper::toSummary)
				.toList();
		var prayer = prayerTimesService.getTimes(slug, LocalDate.now());
		return new ProvinceDetail(
				province.getId(),
				province.getName(),
				province.getSlug(),
				province.getPlateCode(),
				province.getLat(),
				province.getLng(),
				province.getAddress(),
				province.getPhone(),
				province.getEmail(),
				province.getWebsite(),
				province.getAbout(),
				districts,
				latestNews,
				prayer);
	}

	@Transactional
	public ProvinceSummary create(ProvinceWriteRequest request) {
		if (provinceRepository.existsBySlug(request.slug())) {
			throw new IllegalArgumentException("Bu slug zaten kullanılıyor");
		}
		Province p = apply(new Province(), request);
		Province saved = provinceRepository.save(p);
		return toSummary(saved);
	}

	@Transactional
	public ProvinceSummary update(Long id, ProvinceWriteRequest request) {
		Province p = provinceRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("İl bulunamadı: " + id));
		apply(p, request);
		return toSummary(provinceRepository.save(p));
	}

	@Transactional
	public void delete(Long id) {
		if (!provinceRepository.existsById(id)) {
			throw new NotFoundException("İl bulunamadı: " + id);
		}
		provinceRepository.deleteById(id);
	}

	private Province apply(Province p, ProvinceWriteRequest request) {
		p.setName(request.name());
		p.setSlug(request.slug());
		p.setPlateCode(request.plateCode());
		p.setLat(request.lat());
		p.setLng(request.lng());
		p.setAddress(request.address());
		p.setPhone(request.phone());
		p.setEmail(request.email());
		p.setWebsite(request.website());
		p.setAbout(request.about());
		return p;
	}

	private ProvinceSummary toSummary(Province p) {
		return new ProvinceSummary(p.getId(), p.getName(), p.getSlug(), p.getPlateCode(), p.getLat(), p.getLng());
	}

	public record ProvinceSummary(Long id, String name, String slug, int plateCode, Double lat, Double lng) {
	}

	public record DistrictDto(String name, String slug) {
	}

	public record ProvinceDetail(
			Long id,
			String name,
			String slug,
			int plateCode,
			Double lat,
			Double lng,
			String address,
			String phone,
			String email,
			String website,
			String about,
			List<DistrictDto> districts,
			List<gov.diyanet.portal.modules.content.NewsDtos.NewsSummary> latestNews,
			gov.diyanet.portal.modules.prayer.PrayerTimesResponse prayerTimes) {
	}

	public record ProvinceWriteRequest(
			@NotBlank String name,
			@NotBlank String slug,
			int plateCode,
			Double lat,
			Double lng,
			String address,
			String phone,
			String email,
			String website,
			String about) {
	}
}
