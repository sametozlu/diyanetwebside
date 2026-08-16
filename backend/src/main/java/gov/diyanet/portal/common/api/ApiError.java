package gov.diyanet.portal.common.api;

import java.time.Instant;
import java.util.List;

public record ApiError(
		int status,
		String message,
		Instant timestamp,
		List<String> details) {

	public static ApiError of(int status, String message) {
		return new ApiError(status, message, Instant.now(), List.of());
	}

	public static ApiError of(int status, String message, List<String> details) {
		return new ApiError(status, message, Instant.now(), details);
	}
}
