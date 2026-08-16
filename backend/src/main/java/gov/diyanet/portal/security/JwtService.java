package gov.diyanet.portal.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private final SecretKey key;
	private final long expirationMs;

	public JwtService(
			@Value("${app.jwt.secret}") String secret,
			@Value("${app.jwt.expiration-ms}") long expirationMs) {
		this.key = signingKey(secret);
		this.expirationMs = expirationMs;
	}

	public String generateToken(UserDetails user) {
		Date now = new Date();
		return Jwts.builder()
				.subject(user.getUsername())
				.issuedAt(now)
				.expiration(new Date(now.getTime() + expirationMs))
				.signWith(key)
				.compact();
	}

	public String extractUsername(String token) {
		return parse(token).getSubject();
	}

	public boolean isValid(String token, UserDetails user) {
		String username = extractUsername(token);
		return username.equals(user.getUsername()) && parse(token).getExpiration().after(new Date());
	}

	private Claims parse(String token) {
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	private static SecretKey signingKey(String secret) {
		byte[] bytes;
		try {
			bytes = Decoders.BASE64.decode(secret);
		} catch (Exception ignored) {
			bytes = secret.getBytes(StandardCharsets.UTF_8);
		}
		if (bytes.length < 32) {
			byte[] padded = new byte[32];
			System.arraycopy(bytes, 0, padded, 0, bytes.length);
			bytes = padded;
		}
		return Keys.hmacShaKeyFor(bytes);
	}
}
