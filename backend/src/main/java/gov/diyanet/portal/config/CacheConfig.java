package gov.diyanet.portal.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class CacheConfig {

	public static final String PRAYER_TIMES = "prayer-times";
	public static final String PRAYER_CALENDAR = "prayer-calendar";
	public static final String PROVINCES = "provinces";
	public static final String QURAN_SURAHS = "quran-surahs";
	public static final String QURAN_SURAH = "quran-surah";
	public static final String RELIGIOUS_DAYS = "religious-days";
	public static final String HADITH_REMOTE = "hadith-remote";
	public static final String SERVICES = "services";

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager manager = new SimpleCacheManager();
		manager.setCaches(List.of(
				cache(PRAYER_TIMES, 12, TimeUnit.HOURS, 4000),
				cache(PRAYER_CALENDAR, 12, TimeUnit.HOURS, 500),
				cache(PROVINCES, 6, TimeUnit.HOURS, 2),
				cache(QURAN_SURAHS, 24, TimeUnit.HOURS, 2),
				cache(QURAN_SURAH, 24, TimeUnit.HOURS, 200),
				cache(RELIGIOUS_DAYS, 12, TimeUnit.HOURS, 4),
				cache(HADITH_REMOTE, 12, TimeUnit.HOURS, 200),
				cache(SERVICES, 6, TimeUnit.HOURS, 2)));
		return manager;
	}

	private static CaffeineCache cache(String name, long duration, TimeUnit unit, long maxSize) {
		return new CaffeineCache(name, Caffeine.newBuilder()
				.expireAfterWrite(duration, unit)
				.maximumSize(maxSize)
				.build());
	}
}
