package gov.diyanet.portal.integrations;

import java.net.http.HttpClient;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class IntegrationClientConfig {

	@Bean
	RestClient aladhanRestClient(
			@Value("${app.integrations.aladhan.base-url}") String baseUrl,
			@Value("${app.integrations.aladhan.timeout-ms}") int timeoutMs) {
		return restClient(baseUrl, timeoutMs);
	}

	@Bean
	RestClient alquranRestClient(
			@Value("${app.integrations.alquran.base-url}") String baseUrl,
			@Value("${app.integrations.alquran.timeout-ms}") int timeoutMs) {
		return restClient(baseUrl, timeoutMs);
	}

	@Bean
	RestClient hadithRestClient(
			@Value("${app.integrations.hadith.base-url}") String baseUrl,
			@Value("${app.integrations.hadith.timeout-ms}") int timeoutMs) {
		return restClient(baseUrl, timeoutMs);
	}

	private RestClient restClient(String baseUrl, int timeoutMs) {
		HttpClient http = HttpClient.newBuilder()
				.connectTimeout(Duration.ofMillis(timeoutMs))
				.build();
		JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(http);
		factory.setReadTimeout(Duration.ofMillis(timeoutMs));
		return RestClient.builder()
				.baseUrl(baseUrl)
				.requestFactory(factory)
				.defaultHeader("Accept", "application/json")
				.defaultHeader("User-Agent", "DijitalKapi/1.0 (conceptual public portal)")
				.build();
	}
}
