package gov.diyanet.portal.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

	private static final long WINDOW_MS = 60_000L;
	private static final int LOGIN_LIMIT = 10;
	private static final int PUBLIC_LIMIT = 120;

	private final Map<String, Deque<Long>> hits = new ConcurrentHashMap<>();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String path = request.getRequestURI();
		if (!path.startsWith("/api/") || path.startsWith("/api/admin")) {
			filterChain.doFilter(request, response);
			return;
		}
		int limit = "/api/auth/login".equals(path) ? LOGIN_LIMIT : PUBLIC_LIMIT;
		String key = clientIp(request) + "|" + ("/api/auth/login".equals(path) ? "login" : "public");
		long now = Instant.now().toEpochMilli();
		Deque<Long> timestamps = hits.computeIfAbsent(key, k -> new ArrayDeque<>());
		synchronized (timestamps) {
			while (!timestamps.isEmpty() && now - timestamps.peekFirst() > WINDOW_MS) {
				timestamps.pollFirst();
			}
			if (timestamps.size() >= limit) {
				response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
				response.setContentType("application/json");
				response.getWriter().write("{\"status\":429,\"message\":\"İstek limiti aşıldı\"}");
				return;
			}
			timestamps.addLast(now);
		}
		filterChain.doFilter(request, response);
	}

	private String clientIp(HttpServletRequest request) {
		String forwarded = request.getHeader("X-Forwarded-For");
		if (forwarded != null && !forwarded.isBlank()) {
			return forwarded.split(",")[0].trim();
		}
		return request.getRemoteAddr() == null ? "unknown" : request.getRemoteAddr();
	}
}
