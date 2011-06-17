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
import java.util.Collections;
import java.util.List;

import org.eclipse.egit.github.core.Assert;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.IGitHubConstants;
import org.eclipse.egit.github.core.client.PagedRequest;

/**
 * Service class getting and listing pull requests.
 */
public class PullRequestService extends GitHubService {

	/**
	 * @param client
	 */
	public PullRequestService(GitHubClient client) {
		super(client);
	}

	/**
	 * Get pull request from repository with id
	 * 
	 * @param repository
	 * @param id
	 * @return pull request
	 * @throws IOException
	 */
	public PullRequest getPullRequest(IRepositoryIdProvider repository,
			String id) throws IOException {
		final String repoId = getId(repository);
		Assert.notNull("Id cannot be null", id); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(IGitHubConstants.SEGMENT_PULLS);
		uri.append('/').append(id);
		GitHubRequest request = new GitHubRequest();
		request.setUri(uri);
		request.setType(PullRequest.class);
		return (PullRequest) client.get(request).getBody();
	}

	/**
	 * Get pull requests from repository matching state
	 * 
	 * @param repository
	 * @param state
	 * @return list of pull requests
	 * @throws IOException
	 */
	public List<PullRequest> getPullRequests(IRepositoryIdProvider repository,
			String state) throws IOException {
		final String id = getId(repository);
		Assert.notNull("State cannot be null", state); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(IGitHubConstants.SEGMENT_PULLS);
		PagedRequest<PullRequest> request = createPagedRequest();
		request.setUri(uri);
		request.setParams(Collections.singletonMap(IssueService.FILTER_STATE,
				state));
		request.setType(new TypeToken<List<PullRequest>>() {
		}.getType());
		return getAll(request);
	}
}
