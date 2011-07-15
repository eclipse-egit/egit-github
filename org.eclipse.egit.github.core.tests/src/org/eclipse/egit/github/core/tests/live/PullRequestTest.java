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
package org.eclipse.egit.github.core.tests.live;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.PullRequestMarker;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.junit.Test;

/**
 * Live pull request tests
 */
public class PullRequestTest extends LiveTest {

	private void checkMarker(PullRequestMarker marker) {
		assertNotNull(marker);
		assertNotNull(marker.getLabel());
		assertNotNull(marker.getSha());
		assertNotNull(marker.getRef());

		User user = marker.getUser();
		assertNotNull(user);
		assertNotNull(user.getLogin());

		Repository repo = marker.getRepository();
		assertNotNull(repo);
		assertNotNull(repo.getOwner());
		assertNotNull(repo.getName());
	}

	/**
	 * Test fetching a pull request
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch() throws IOException {
		PullRequestService service = new PullRequestService(client);
		PullRequest request = service.getPullRequest(new SearchRepository(
				"technoweenie", "faraday"), 15);
		assertNotNull(request);
		assertNotNull(request.getHtmlUrl());
		assertNotNull(request.getDiffUrl());
		assertNotNull(request.getPatchUrl());
		checkMarker(request.getHead());
		checkMarker(request.getBase());
	}

	/**
	 * Test fetching all pull requests
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetchAll() throws IOException {
		PullRequestService service = new PullRequestService(client);
		List<PullRequest> requests = service.getPullRequests(
				new SearchRepository("technoweenie", "faraday"),
				IssueService.STATE_CLOSED);
		assertNotNull(requests);
		assertFalse(requests.isEmpty());
		for (PullRequest request : requests) {
			assertNotNull(request);
			assertNotNull(request.getUpdatedAt());
			assertNotNull(request.getCreatedAt());
			assertNotNull(request.getHtmlUrl());
			assertNotNull(request.getDiffUrl());
			assertNotNull(request.getPatchUrl());
		}
	}
}
