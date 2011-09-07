/******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *****************************************************************************/
package org.eclipse.egit.github.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.GitHubResponse;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.CommitService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests of {@link CommitService}
 */
@RunWith(MockitoJUnitRunner.class)
public class CommitServiceTest {

	@Mock
	private GitHubClient client;

	@Mock
	private GitHubResponse response;

	private CommitService service;

	/**
	 * Test case set up
	 *
	 * @throws IOException
	 */
	@Before
	public void before() throws IOException {
		doReturn(response).when(client).get(any(GitHubRequest.class));
		service = new CommitService(client);
	}

	/**
	 * Create service using default constructor
	 */
	@Test
	public void constructor() {
		assertNotNull(new CommitService().getClient());
	}

	/**
	 * Get commits
	 *
	 * @throws IOException
	 */
	@Test
	public void getCommits() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		service.getCommits(repo);
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils.page("/repos/o/n/commits"));
		verify(client).get(request);
	}

	/**
	 * Page commits
	 */
	@Test
	public void pageCommits() {
		RepositoryId repo = new RepositoryId("o", "n");
		PageIterator<RepositoryCommit> iterator = service.pageCommits(repo);
		assertNotNull(iterator);
		assertEquals(Utils.page("/repos/o/n/commits"), iterator.getRequest()
				.generateUri());
		assertTrue(iterator.hasNext());
	}

	/**
	 * Get commit with null sha
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getCommitNullSha() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		service.getCommit(repo, null);
	}

	/**
	 * Get commit with empty sha
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getCommitEmptySha() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		service.getCommit(repo, "");
	}

	/**
	 * Get commit
	 *
	 * @throws IOException
	 */
	@Test
	public void getCommit() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		service.getCommit(repo, "abc");
		GitHubRequest request = new GitHubRequest();
		request.setUri("/repos/o/n/commits/abc");
		verify(client).get(request);
	}

	/**
	 * Get comments with null sha
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getCommentsNullSha() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		service.getComments(repo, null);
	}

	/**
	 * Get comments with empty sha
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getCommentsEmptySha() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		service.getComments(repo, "");
	}

	/**
	 * Get commit comments
	 *
	 * @throws IOException
	 */
	@Test
	public void getComments() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		service.getComments(repo, "abc");
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils.page("/repos/o/n/commits/abc/comments"));
		verify(client).get(request);
	}

	/**
	 * Get comment comment
	 *
	 * @throws IOException
	 */
	@Test
	public void getComment() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		service.getComment(repo, 33);
		GitHubRequest request = new GitHubRequest();
		request.setUri("/repos/o/n/comments/33");
		verify(client).get(request);
	}

	/**
	 * Add comment with null sha
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addCommentNullSha() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		CommitComment comment = new CommitComment();
		service.addComment(repo, null, comment);
	}

	/**
	 * Add comment with empty sha
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addCommentEmptySha() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		CommitComment comment = new CommitComment();
		service.addComment(repo, "", comment);
	}

	/**
	 * Add commit comment
	 *
	 * @throws IOException
	 */
	@Test
	public void addComment() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		CommitComment comment = new CommitComment();
		service.addComment(repo, "abcd", comment);
		verify(client).post("/repos/o/n/commits/abcd/comments", comment,
				CommitComment.class);
	}

	/**
	 * Edit with null comment
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void editCommentNullComment() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		service.editComment(repo, null);
	}

	/**
	 * Edit commit comment
	 *
	 * @throws IOException
	 */
	@Test
	public void editComment() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		CommitComment comment = new CommitComment();
		comment.setId(56);
		service.editComment(repo, comment);
		verify(client).post("/repos/o/n/comments/56", comment,
				CommitComment.class);
	}

	/**
	 * Delete commit comment
	 *
	 * @throws IOException
	 */
	@Test
	public void deleteComment() throws IOException {
		RepositoryId repo = new RepositoryId("o", "n");
		service.deleteComment(repo, 75);
		verify(client).delete("/repos/o/n/comments/75");
	}
}
