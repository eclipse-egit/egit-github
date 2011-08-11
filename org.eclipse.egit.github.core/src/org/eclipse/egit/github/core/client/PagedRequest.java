/*******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.egit.github.core.client;

import java.util.HashMap;
import java.util.Map;


/**
 * Paged request class that contains the initial page size and page number of
 * the request.
 * 
 * @param <V>
 */
public class PagedRequest<V> extends GitHubRequest {

	/**
	 * First page
	 */
	public static final int PAGE_FIRST = 1;

	/**
	 * Default page size
	 */
	public static final int PAGE_SIZE = 100;

	private final int pageSize;

	private final int page;

	/**
	 * Create paged request with default size
	 */
	public PagedRequest() {
		this(PAGE_FIRST, PAGE_SIZE);
	}

	/**
	 * Create paged request with given starting page and page size.
	 * 
	 * @param start
	 * @param size
	 */
	public PagedRequest(int start, int size) {
		page = start;
		pageSize = size;
	}

	/**
	 * @return pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return page
	 */
	public int getPage() {
		return page;
	}

	@Override
	public Map<String, String> getParams() {
		return (super.getParams() == null) ? new HashMap<String, String>() : super.getParams();
	}

	@Override
	public String generateUri() {
		final HashMap<String, String> newParams = new HashMap<String, String>();
		newParams.putAll(getParams());
		if (!newParams.containsKey(IGitHubConstants.PARAM_PAGE)
				|| !newParams.containsKey(IGitHubConstants.PARAM_PER_PAGE)) {
			newParams.put(IGitHubConstants.PARAM_PAGE, Integer.toString(page));
			newParams.put(IGitHubConstants.PARAM_PER_PAGE, Integer.toString(pageSize));
		}
		setParams(newParams);
		return super.generateUri();
	}
}
