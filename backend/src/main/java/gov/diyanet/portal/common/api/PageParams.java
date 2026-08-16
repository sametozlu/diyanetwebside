package gov.diyanet.portal.common.api;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public final class PageParams {

	public static final int DEFAULT_SIZE = 12;
	public static final int MAX_SIZE = 50;

	private PageParams() {
	}

	public static PageRequest of(Integer page, Integer size) {
		return of(page, size, Sort.unsorted());
	}

	public static PageRequest of(Integer page, Integer size, Sort sort) {
		int p = page == null || page < 0 ? 0 : page;
		int s = size == null ? DEFAULT_SIZE : Math.min(Math.max(size, 1), MAX_SIZE);
		return PageRequest.of(p, s, sort);
	}
}
