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
package org.eclipse.egit.github.core.service;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.eclipse.egit.github.core.client.PagedRequest.PAGE_FIRST;
import static org.eclipse.egit.github.core.client.PagedRequest.PAGE_SIZE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.NoSuchPageException;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.client.PagedRequest;
import org.eclipse.egit.github.core.client.RequestException;

/**
 * Base GitHub service class.
 */
public abstract class GitHubService {

	/**
	 * Client field
	 */
	protected final GitHubClient client;

	/**
	 * Create service for client
	 *
	 * @param client
	 */
	public GitHubService(GitHubClient client) {
		if (client == null)
			throw new IllegalArgumentException("Client cannot be null"); //$NON-NLS-1$
		this.client = client;
	}

	/**
	 * Unified request creation method that all sub-classes should use so
	 * overriding classes can extend and configure the default request.
	 *
	 * @return request
	 */
	protected GitHubRequest createRequest() {
		return new GitHubRequest();
	}

	/**
	 * Unified paged request creation method that all sub-classes should use so
	 * overriding classes can extend and configure the default request.
	 *
	 * @return request
	 */
	protected <V> PagedRequest<V> createPagedRequest() {
		return createPagedRequest(PAGE_FIRST, PAGE_SIZE);
	}

	/**
	 * Unified paged request creation method that all sub-classes should use so
	 * overriding classes can extend and configure the default request.
	 *
	 * @param start
	 * @param size
	 * @return request
	 */
	protected <V> PagedRequest<V> createPagedRequest(int start, int size) {
		return new PagedRequest<V>(start, size);
	}

	/**
	 * Unified page iterator creation method that all sub-classes should use so
	 * overriding classes can extend and configure the default iterator.
	 *
	 * @param request
	 * @return iterator
	 */
	protected <V> PageIterator<V> createPageIterator(PagedRequest<V> request) {
		return new PageIterator<V>(request, client);
	}

	/**
	 * Get paged request by performing multiple requests until no more pages are
	 * available or an exception occurs.
	 *
	 * @param <V>
	 * @param request
	 * @return list of all elements
	 * @throws IOException
	 */
	protected <V> List<V> getAll(PagedRequest<V> request) throws IOException {
		return getAll(createPageIterator(request));
	}

	/**
	 * Get paged request by performing multiple requests until no more pages are
	 * available or an exception occurs.
	 *
	 * @param <V>
	 * @param iterator
	 * @return list of all elements
	 * @throws IOException
	 */
	protected <V> List<V> getAll(PageIterator<V> iterator) throws IOException {
		List<V> elements = new ArrayList<V>();
		try {
			while (iterator.hasNext())
				elements.addAll(iterator.next());
		} catch (NoSuchPageException pageException) {
			throw pageException.getCause();
		}
		return elements;
	}

	/**
	 * Check if the uri returns a non-404
	 *
	 * @param uri
	 * @return true if no exception, false if 404
	 * @throws IOException
	 */
	protected boolean check(String uri) throws IOException {
		try {
			client.get(createRequest().setUri(uri));
			return true;
		} catch (RequestException e) {
			if (e.getStatus() == SC_NOT_FOUND)
				return false;
			throw e;
		}
	}

	/**
	 * Get id for repository
	 *
	 * @param provider
	 * @return non-null id
	 */
	protected String getId(IRepositoryIdProvider provider) {
		if (provider == null)
			throw new IllegalArgumentException(
					"Repository provider cannot be null"); //$NON-NLS-1$
		final String id = provider.generateId();
		if (id == null)
			throw new IllegalArgumentException("Repository id cannot be null"); //$NON-NLS-1$
		if (id.length() == 0)
			throw new IllegalArgumentException("Repository id cannot be empty"); //$NON-NLS-1$
		return id;
	}
}
