package gov.diyanet.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PortalBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalBackendApplication.class, args);
	}

}
