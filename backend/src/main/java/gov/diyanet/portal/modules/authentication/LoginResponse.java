package gov.diyanet.portal.modules.authentication;

import java.util.List;

public record LoginResponse(String token, String name, List<String> roles) {
}
