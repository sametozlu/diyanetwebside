package gov.diyanet.portal.modules.catalog;

import gov.diyanet.portal.modules.catalog.DigitalServiceService.ServiceDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class DigitalServiceController {

	private final DigitalServiceService digitalServiceService;

	@GetMapping
	public List<ServiceDto> list() {
		return digitalServiceService.list();
	}
}
