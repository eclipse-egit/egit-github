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

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.GitHubResponse;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests of {@link RepositoryService}
 */
@RunWith(MockitoJUnitRunner.class)
public class RepositoryServiceTest {

	@Mock
	private GitHubClient client;

	@Mock
	private GitHubResponse response;

	private RepositoryId repo;

	private RepositoryService service;

	/**
	 * Test case set up
	 *
	 * @throws IOException
	 */
	@Before
	public void before() throws IOException {
		service = new RepositoryService(client);
		doReturn(response).when(client).get(any(GitHubRequest.class));
		repo = new RepositoryId("o", "n");
	}

	/**
	 * Create service using default constructor
	 */
	@Test
	public void constructor() {
		assertNotNull(new RepositoryService().getClient());
	}

	/**
	 * Create repository with null repository
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void createRepositoryNullRepository() throws IOException {
		service.createRepository(null);
	}

	/**
	 * Create repository
	 *
	 * @throws IOException
	 */
	@Test
	public void createRepository() throws IOException {
		Repository repo = new Repository();
		repo.setName("n");
		repo.setOwner(new User().setLogin("o"));
		service.createRepository(repo);
		verify(client).post("/user/repos", repo, Repository.class);
	}

	/**
	 * Create repository with null repository
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void editRepositoryNullRepository() throws IOException {
		service.editRepository(null);
	}

	/**
	 * Create repository with null organization name
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void createOrgRepositoryNullOrg() throws IOException {
		service.createRepository(null, new Repository());
	}

	/**
	 * Create repository with empty organizaton name
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void createOrgRepositoryEmptyOrg() throws IOException {
		service.createRepository("", new Repository());
	}

	/**
	 * Create organization repository with null repository
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void createOrgRepositoryNullRepository() throws IOException {
		service.createRepository("anorg", null);
	}

	/**
	 * Create organization repository
	 *
	 * @throws IOException
	 */
	@Test
	public void createOrgRepository() throws IOException {
		Repository repo = new Repository();
		repo.setName("n");
		repo.setOwner(new User().setLogin("o"));
		service.createRepository("abc", repo);
		verify(client).post("/orgs/abc/repos", repo, Repository.class);
	}

	/**
	 * Get repository using an repository provider
	 *
	 * @throws IOException
	 */
	@Test
	public void getRepositoryProvider() throws IOException {
		service.getRepository(repo);
		GitHubRequest request = new GitHubRequest();
		request.setUri("/repos/o/n");
		verify(client).get(request);
	}

	/**
	 * Get repository using owner owner and name
	 *
	 * @throws IOException
	 */
	@Test
	public void getRepository() throws IOException {
		service.getRepository("o", "n");
		GitHubRequest request = new GitHubRequest();
		request.setUri("/repos/o/n");
		verify(client).get(request);
	}

	/**
	 * Get forks
	 *
	 * @throws IOException
	 */
	@Test
	public void getForks() throws IOException {
		service.getForks(repo);
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils.page("/repos/o/n/forks"));
		verify(client).get(request);
	}

	/**
	 * Fork repository
	 *
	 * @throws IOException
	 */
	@Test
	public void forkRepository() throws IOException {
		service.forkRepository(repo);
		verify(client).post("/repos/o/n/forks", null, Repository.class);
	}

	/**
	 * Fork repository to organization
	 *
	 * @throws IOException
	 */
	@Test
	public void forkRepositoryToOrg() throws IOException {
		service.forkRepository(repo, "abc");
		verify(client).post("/repos/o/n/forks",
				Collections.singletonMap("org", "abc"), Repository.class);
	}

	/**
	 * Get repositories
	 *
	 * @throws IOException
	 */
	@Test
	public void getRepositories() throws IOException {
		service.getRepositories();
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils.page("/user/repos"));
		verify(client).get(request);
	}

	/**
	 * Get repositories with null user
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getRepositoriesNullUser() throws IOException {
		service.getRepositories(null);
	}

	/**
	 * Get repositories with empty user
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getRepositoriesEmptyUser() throws IOException {
		service.getRepositories("");
	}

	/**
	 * Get user repositories
	 *
	 * @throws IOException
	 */
	@Test
	public void getUserRepositories() throws IOException {
		service.getRepositories("u1");
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils.page("/users/u1/repos"));
		verify(client).get(request);
	}

	/**
	 * Get repositories with null organization
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getRepositoriesNullOrg() throws IOException {
		service.getOrgRepositories(null);
	}

	/**
	 * Get repositories with empty organization
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getRepositoriesEmptyOrg() throws IOException {
		service.getOrgRepositories("");
	}

	/**
	 * Get organization repositories
	 *
	 * @throws IOException
	 */
	@Test
	public void getOrgRepositories() throws IOException {
		service.getOrgRepositories("o1");
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils.page("/orgs/o1/repos"));
		verify(client).get(request);
	}

	/**
	 * Search repositories with null query
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void searchRepositoriesNullQuery() throws IOException {
		service.searchRepositories(null);
	}

	/**
	 * Search repository with empty query
	 *
	 * @throws IOException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void searchRepositoriesEmptyQuery() throws IOException {
		service.searchRepositories("");
	}

	/**
	 * Search repositories
	 *
	 * @throws IOException
	 */
	@Test
	public void searchRepositories() throws IOException {
		service.searchRepositories("test");
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils.page("/api/v2/json/repos/search/test"));
		verify(client).get(request);
	}

	/**
	 * Search repositories matching language
	 *
	 * @throws IOException
	 */
	@Test
	public void searchRepositoriesMatchingLanguage() throws IOException {
		service.searchRepositories("buffers", "c");
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils
				.page("/api/v2/json/repos/search/buffers?language=c"));
		verify(client).get(request);
	}
}
