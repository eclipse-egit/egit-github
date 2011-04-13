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
package org.eclipse.mylyn.github.internal;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.Assert;

/**
 * Service class for getting and list gists.
 * 
 * @author Kevin Sawicki (kevin@github.com)
 */
public class GistService {

	private GitHubClient client;

	/**
	 * Create gist service
	 * 
	 * @param client
	 */
	public GistService(GitHubClient client) {
		Assert.isNotNull(client, "Client cannot be null");
		this.client = client;
	}

	/**
	 * Get gist
	 * 
	 * @param id
	 * @return gist
	 * @throws IOException
	 */
	public Gist getGist(String id) throws IOException {
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_GISTS);
		uri.append('/').append(id).append(IGitHubConstants.SUFFIX_JSON);
		return this.client.get(uri.toString(), Gist.class);
	}

	/**
	 * Get gists for specified user
	 * 
	 * @param user
	 * @return list of gists
	 * @throws IOException
	 */
	public List<Gist> getGists(String user) throws IOException {
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_USERS);
		uri.append('/').append(user);
		uri.append(IGitHubConstants.SEGMENT_GISTS).append(
				IGitHubConstants.SUFFIX_JSON);
		TypeToken<List<Gist>> gistToken = new TypeToken<List<Gist>>() {
		};
		return this.client.get(uri.toString(), gistToken.getType());
	}

	/**
	 * Create gist
	 * 
	 * @param gist
	 * @return created gist
	 * @throws IOException
	 */
	public Gist createGist(Gist gist) throws IOException {
		StringBuilder uri = new StringBuilder();
		User user = gist.getAuthor();
		if (user != null) {
			uri.append(IGitHubConstants.SEGMENT_USERS);
			uri.append('/').append(user.getLogin());
		}
		uri.append(IGitHubConstants.SEGMENT_GISTS).append(
				IGitHubConstants.SUFFIX_JSON);
		return this.client.post(uri.toString(), gist, Gist.class);
	}

}
