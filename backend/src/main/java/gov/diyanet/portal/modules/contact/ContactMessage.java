package gov.diyanet.portal.modules.contact;

import gov.diyanet.portal.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "contact_messages")
public class ContactMessage extends BaseEntity {

	@Column(nullable = false, length = 160)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false, length = 240)
	private String subject;

	@Column(nullable = false, columnDefinition = "text")
	private String message;

	@Column(nullable = false, length = 32)
	@Builder.Default
	private String status = "NEW";
}
