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

import org.apache.http.Header;
import org.apache.http.HttpResponse;

/**
 * GitHub API response class that provides the parsed response body as well as
 * any links to the first, previous, next, and last responses.
 */
public class GitHubResponse extends LinkedResponse {

	/**
	 * HTTP response
	 */
	protected final HttpResponse response;

	/**
	 * Create response
	 *
	 * @param response
	 * @param body
	 */
	public GitHubResponse(HttpResponse response, Object body) {
		super(body);
		this.response = response;
	}

	@Override
	public String getHeader(String name) {
		Header header = response.getFirstHeader(name);
		return header != null ? header.getValue() : null;
	}
}
