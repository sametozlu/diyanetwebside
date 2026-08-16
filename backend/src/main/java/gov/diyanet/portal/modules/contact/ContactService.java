package gov.diyanet.portal.modules.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactService {

	private final ContactMessageRepository repository;

	@Transactional
	public ContactAck submit(ContactRequest request) {
		ContactMessage saved = repository.save(ContactMessage.builder()
				.name(request.name().trim())
				.email(request.email().trim())
				.subject(request.subject().trim())
				.message(request.message().trim())
				.status("NEW")
				.build());
		return new ContactAck(saved.getId(), "Mesajınız alındı. Bu form kavramsal portala aittir.");
	}

	public record ContactRequest(
			@NotBlank @Size(max = 160) String name,
			@NotBlank @Email @Size(max = 255) String email,
			@NotBlank @Size(max = 240) String subject,
			@NotBlank @Size(max = 4000) String message) {
	}

	public record ContactAck(Long id, String message) {
	}
}
