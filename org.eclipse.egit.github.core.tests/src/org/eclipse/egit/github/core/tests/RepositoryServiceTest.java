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

	@Test(expected = IllegalArgumentException.class)
	public void createRepositoryNullRepository() throws IOException {
		service.createRepository(null);
	}

	@Test
	public void createRepository() throws IOException {
		Repository repo = new Repository();
		repo.setName("n");
		repo.setOwner(new User().setLogin("o"));
		service.createRepository(repo);
		verify(client).post("/user/repos", repo, Repository.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createOrgRepositoryNullOrg() throws IOException {
		service.createRepository(null, new Repository());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createOrgRepositoryEmptyOrg() throws IOException {
		service.createRepository("", new Repository());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createOrgRepositoryNullRepository() throws IOException {
		service.createRepository("anorg", null);
	}

	@Test
	public void createOrRepository() throws IOException {
		Repository repo = new Repository();
		repo.setName("n");
		repo.setOwner(new User().setLogin("o"));
		service.createRepository("abc", repo);
		verify(client).post("/orgs/abc/repos", repo, Repository.class);
	}

	@Test
	public void getRepositoryProvider() throws IOException {
		service.getRepository(repo);
		GitHubRequest request = new GitHubRequest();
		request.setUri("/repos/o/n");
		verify(client).get(request);
	}

	@Test
	public void getRepository() throws IOException {
		service.getRepository("o", "n");
		GitHubRequest request = new GitHubRequest();
		request.setUri("/repos/o/n");
		verify(client).get(request);
	}

	@Test
	public void getForks() throws IOException {
		service.getForks(repo);
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils.page("/repos/o/n/forks"));
		verify(client).get(request);
	}

	@Test
	public void forkRepository() throws IOException {
		service.forkRepository(repo);
		verify(client).post("/repos/o/n/forks", null, Repository.class);
	}

	@Test
	public void forkRepositoryToOrg() throws IOException {
		service.forkRepository(repo, "abc");
		verify(client).post("/repos/o/n/forks",
				Collections.singletonMap("org", "abc"), Repository.class);
	}

	@Test
	public void getRepositories() throws IOException {
		service.getRepositories();
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils.page("/user/repos"));
		verify(client).get(request);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRepositoriesNullUser() throws IOException {
		service.getRepositories(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRepositoriesEmptyUser() throws IOException {
		service.getRepositories("");
	}

	@Test
	public void getUserRepositories() throws IOException {
		service.getRepositories("u1");
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils.page("/users/u1/repos"));
		verify(client).get(request);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRepositoriesNullOrg() throws IOException {
		service.getOrgRepositories(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRepositoriesEmptyOrg() throws IOException {
		service.getOrgRepositories("");
	}

	@Test
	public void getOrgRepositories() throws IOException {
		service.getOrgRepositories("o1");
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils.page("/orgs/o1/repos"));
		verify(client).get(request);
	}

	@Test(expected = IllegalArgumentException.class)
	public void searchRepositoriesNullQuery() throws IOException {
		service.searchRepositories(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void searchRepositoriesEmptyQuery() throws IOException {
		service.searchRepositories("");
	}

	@Test
	public void searchRepositories() throws IOException {
		service.searchRepositories("test");
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils.page("/api/v2/json/repos/search/test"));
		verify(client).get(request);
	}

	@Test
	public void searchRepositoriesMatchingLanguage() throws IOException {
		service.searchRepositories("buffers", "c");
		GitHubRequest request = new GitHubRequest();
		request.setUri(Utils
				.page("/api/v2/json/repos/search/buffers?language=c"));
		verify(client).get(request);
	}
}
