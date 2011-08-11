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

import java.lang.reflect.Type;
import java.util.Map;

/**
 * GitHub API request class.
 */
public class GitHubRequest {

	private String uri;

	private Map<String, String> params;

	private Type type;

	/**
	 * @return uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Generate full uri
	 * 
	 * @return uri
	 */
	public String generateUri() {
		if (uri.indexOf('?') != -1)
			return uri;
		String query = "";
		if (params != null) {
			int keyCount = params.keySet().size();
			for (int i = 0; i < keyCount; i++) {
				if (i != 0)
					query += "&";
				query += params.keySet().toArray()[i] + "=" + params.values().toArray()[i];
			}
		}
		if (query.length() > 0)
			return uri + '?' + query;
		else
			return uri;
	}

	/**
	 * @param uri
	 * @return this request
	 */
	public GitHubRequest setUri(StringBuilder uri) {
		return setUri(uri != null ? uri.toString() : null);
	}

	/**
	 * @param uri
	 * @return this request
	 */
	public GitHubRequest setUri(String uri) {
		this.uri = uri;
		return this;
	}

	/**
	 * @return params
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * @param params
	 * @return this request
	 */
	public GitHubRequest setParams(Map<String, String> params) {
		this.params = params;
		return this;
	}

	/**
	 * @return type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type
	 * @return this request
	 */
	public GitHubRequest setType(Type type) {
		this.type = type;
		return this;
	}
}
