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

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Assert;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.ListResourceCollector;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.IGitHubConstants;
import org.eclipse.egit.github.core.client.PagedRequest;

/**
 * Service class for getting and list gists.
 */
public class GistService extends GitHubService {

	/**
	 * Create gist service
	 * 
	 * @param client
	 */
	public GistService(GitHubClient client) {
		super(client);
	}

	/**
	 * Get gist
	 * 
	 * @param id
	 * @return gist
	 * @throws IOException
	 */
	public Gist getGist(String id) throws IOException {
		Assert.notNull("Gist id cannot be null", id); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_GISTS);
		uri.append('/').append(id);
		GitHubRequest request = new GitHubRequest();
		request.setUri(uri);
		request.setType(Gist.class);
		return (Gist) this.client.get(request).getBody();
	}

	/**
	 * Get gists for specified user
	 * 
	 * @param user
	 * @return list of gists
	 * @throws IOException
	 */
	public List<Gist> getGists(String user) throws IOException {
		Assert.notNull("User cannot be null", user); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_USERS);
		uri.append('/').append(user);
		uri.append(IGitHubConstants.SEGMENT_GISTS);
		ListResourceCollector<Gist> collector = new ListResourceCollector<Gist>();
		PagedRequest<Gist> request = new PagedRequest<Gist>(collector);
		request.setUri(uri).setType(new TypeToken<List<Gist>>() {
		}.getType());
		getAll(request);
		return collector.getResources();
	}

	/**
	 * Create a gist
	 * 
	 * @param gist
	 * @return created gist
	 * @throws IOException
	 */
	public Gist createGist(Gist gist) throws IOException {
		Assert.notNull("Gist cannot be null", gist);
		StringBuilder uri = new StringBuilder();
		String user = client.getUser();
		if (user != null)
			uri.append(IGitHubConstants.SEGMENT_USERS).append('/').append(user);
		uri.append(IGitHubConstants.SEGMENT_GISTS);
		return this.client.post(uri.toString(), gist, Gist.class);
	}

	/**
	 * Update a gist
	 * 
	 * @param gist
	 * @return updated gist
	 * @throws IOException
	 */
	public Gist updateGist(Gist gist) throws IOException {
		Assert.notNull("Gist cannot be null", gist); //$NON-NLS-1$
		String id = gist.getId();
		Assert.notNull("Gist id cannot be null", id); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_GISTS);
		uri.append('/').append(id);
		return this.client.post(uri.toString(), gist, Gist.class);
	}

	/**
	 * Create comment on specified gist id
	 * 
	 * @param gistId
	 * @param comment
	 * @return created issue
	 * @throws IOException
	 */
	public Comment createComment(String gistId, String comment)
			throws IOException {
		Assert.notNull("Gist id cannot be null", gistId); //$NON-NLS-1$
		Assert.notNull("Gist comment cannot be null", comment);
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_GISTS);
		uri.append('/').append(gistId);
		uri.append(IGitHubConstants.SEGMENT_COMMENTS);

		Map<String, String> params = new HashMap<String, String>(1, 1);
		params.put(IssueService.FIELD_BODY, comment);
		return this.client.post(uri.toString(), params, Comment.class);
	}

	/**
	 * Get comments for specified gist id
	 * 
	 * @param gistId
	 * @return list of comments
	 * @throws IOException
	 */
	public List<Comment> getComments(String gistId) throws IOException {
		Assert.notNull("Gist id cannot be null", gistId); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_GISTS);
		uri.append('/').append(gistId);
		uri.append(IGitHubConstants.SEGMENT_COMMENTS);
		ListResourceCollector<Comment> collector = new ListResourceCollector<Comment>();
		PagedRequest<Comment> request = new PagedRequest<Comment>(collector);
		request.setUri(uri).setType(new TypeToken<List<Comment>>() {
		}.getType());
		getAll(request);
		return collector.getResources();
	}

	/**
	 * Delete the Gist with the given id
	 * 
	 * @param gistId
	 * @throws IOException
	 */
	public void deleteGist(String gistId) throws IOException {
		Assert.notNull("Gist id cannot be null", gistId); //$NON-NLS-1$	
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_GISTS);
		uri.append('/').append(gistId);
		client.delete(uri.toString());
	}

	/**
	 * Delete the Gist comment with the given id
	 * 
	 * @param commentId
	 * @throws IOException
	 */
	public void deleteComment(String commentId) throws IOException {
		Assert.notNull("Gist comment id cannot be null", commentId); //$NON-NLS-1$	
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_GISTS);
		uri.append(IGitHubConstants.SEGMENT_COMMENTS);
		uri.append('/').append(commentId);
		client.delete(uri.toString());
	}

}
