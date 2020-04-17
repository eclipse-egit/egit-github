/*******************************************************************************
 *  Copyright (c) 2020 Liferay, Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *    Gregory Amerson (Liferay, Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.egit.github.core.tests.live;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import java.io.IOException;
import java.util.List;

import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.RepositoryInvitation;
import org.eclipse.egit.github.core.service.CollaboratorService;
import org.eclipse.egit.github.core.service.RepositoryInvitationService;
import org.junit.Test;

/**
 * Live repository invitations test
 */
public class RepositoryInvitationTest extends LiveTest {

	/**
	 * Test listing repository invitations for the currently logged in user
	 *
	 * @throws IOException
	 */
	@Test
	public void getUserRepositoryInvitations() throws IOException {
		RepositoryInvitationService service = new RepositoryInvitationService(client);
		List<RepositoryInvitation> repoInvitations = service.getUserRepositoryInvitations();
		assertNotNull(repoInvitations);
		assertFalse(repoInvitations.isEmpty());
		for (RepositoryInvitation repoInvitation : repoInvitations) {
			assertRepoInvite(repoInvitation);

		}
	}

	private void assertRepoInvite(RepositoryInvitation repoInvite) {
		assertNotNull(repoInvite);
		assertNotNull(repoInvite.getCreatedAt());
		assertNotNull(repoInvite.getHtmlUrl());
		assertTrue(repoInvite.getId() > 0);
		assertNotNull(repoInvite.getInvitee().getAvatarUrl());
		assertNotNull(repoInvite.getInvitee().getEventsUrl());
		assertNotNull(repoInvite.getInvitee().getFollowersUrl());
		assertNotNull(repoInvite.getInvitee().getFollowingUrl());
		assertNotNull(repoInvite.getInvitee().getGistsUrl());
		assertNotNull(repoInvite.getInvitee().getHtmlUrl());
		assertTrue(repoInvite.getInvitee().getId() > 0);
		assertNotNull(repoInvite.getInvitee().getLogin());
		assertNotNull(repoInvite.getInvitee().getNodeId());
		assertNotNull(repoInvite.getInvitee().getOrganizationsUrl());
		assertNotNull(repoInvite.getInvitee().getReceivedEventsUrl());
		assertNotNull(repoInvite.getInvitee().getReposUrl());
		assertNotNull(repoInvite.getInvitee().getStarredUrl());
		assertNotNull(repoInvite.getInvitee().getSubscriptionsUrl());
		assertNotNull(repoInvite.getInvitee().getType());
		assertNotNull(repoInvite.getInvitee().getUrl());
		assertFalse(repoInvite.getInvitee().isSiteAdmin());
		assertNotNull(repoInvite.getInviter());
		assertNotNull(repoInvite.getPermissions());
		assertNotNull(repoInvite.getRepository());
		assertNotNull(repoInvite.getUrl());
	}

	/**
	 * Get invitations for a specific user
	 *
	 * @throws IOException
	 */
	@Test
	public void getRepositoryInvitations() throws IOException {
		RepositoryInvitationService service = new RepositoryInvitationService(client);
		String owner = System.getProperty("github.test.user");
		String repository = System.getProperty("github.test.repository");
		assumeNotNull(owner);
		assumeNotNull(repository);
		List<RepositoryInvitation> repoInvites = service.getRepositoryInvitations(owner, repository);
		assertNotNull(repoInvites);
		assertFalse(repoInvites.isEmpty());
		for (RepositoryInvitation repoInvite : repoInvites) {
			assertRepoInvite(repoInvite);
		}
	}

	/**
	 * Creates and then deletes a repository invitation
	 *
	 * @throws IOException
	 */
	@Test
	public void createAndDeleteRepositoryInvitation() throws IOException {
		String user = System.getProperty("github.test.user");
		String collabUser = System.getProperty("github.test.collab.user");
		String collabPassword = System.getProperty("github.test.collab.password");
		String collabRepository = System.getProperty("github.test.collab.repository");
		assumeNotNull(user);
		assumeNotNull(collabUser);
		assumeNotNull(collabPassword);
		assumeNotNull(collabRepository);

		client.setCredentials(collabUser, collabPassword);

		RepositoryInvitationService repositoryInvitationService =
			new RepositoryInvitationService(client);

		List<RepositoryInvitation> priorInvitations =
			repositoryInvitationService.getRepositoryInvitations(collabUser, collabRepository);

		CollaboratorService cs = new CollaboratorService(client);

		cs.addCollaborator(RepositoryId.create(collabUser, collabRepository), user);

		List<RepositoryInvitation> currentInvitations =
			repositoryInvitationService.getRepositoryInvitations(collabUser, collabRepository);

		assertEquals(priorInvitations.size() + 1, currentInvitations.size());

		RepositoryInvitation newInvitation =
			currentInvitations.get(currentInvitations.size() - 1);

		repositoryInvitationService.deleteRepositoryInvitation(
			collabUser, collabRepository, newInvitation.getId());

		currentInvitations =
			repositoryInvitationService.getRepositoryInvitations(collabUser, collabRepository);

		assertEquals(priorInvitations.size(), currentInvitations.size());
	}

	/**
	 * Creates and sends a invitation from collab user and then accepts it with test user
	 *
	 * @throws IOException
	 */
	@Test
	public void acceptRepositoryInvitations() throws IOException {
		String user = System.getProperty("github.test.user");
		String password = System.getProperty("github.test.password");
		String collabUser = System.getProperty("github.test.collab.user");
		String collabPassword = System.getProperty("github.test.collab.password");
		String collabRepository = System.getProperty("github.test.collab.repository");
		assumeNotNull(user);
		assumeNotNull(password);
		assumeNotNull(collabUser);
		assumeNotNull(collabPassword);
		assumeNotNull(collabRepository);

		client.setCredentials(collabUser, collabPassword);

		CollaboratorService cs = new CollaboratorService(client);

		cs.addCollaborator(RepositoryId.create(collabUser, collabRepository), user);

		RepositoryInvitationService repositoryInvitationService =
			new RepositoryInvitationService(client);

		List<RepositoryInvitation> originalInvitations =
			repositoryInvitationService.getRepositoryInvitations(collabUser, collabRepository);

		RepositoryInvitation newInvitation =
			originalInvitations.get(originalInvitations.size() - 1);

		client.setCredentials(user, password);

		repositoryInvitationService.acceptRepositoryInvitation(newInvitation.getId());

		client.setCredentials(collabUser, collabPassword);

		List<RepositoryInvitation> currentInvitations =
			repositoryInvitationService.getUserRepositoryInvitations();

		assertEquals(originalInvitations.size() - 1, currentInvitations.size());

		client.setCredentials(collabUser, collabPassword);

		cs.removeCollaborator(RepositoryId.create(collabUser, collabRepository), user);
	}

	/**
	 * Creates and sends a invitation from collab user and then declines it with test user
	 *
	 * @throws IOException
	 */
	@Test
	public void declineRepositoryInvitation() throws IOException {
		String user = System.getProperty("github.test.user");
		String password = System.getProperty("github.test.password");
		String collabUser = System.getProperty("github.test.collab.user");
		String collabPassword = System.getProperty("github.test.collab.password");
		String collabRepository = System.getProperty("github.test.collab.repository");
		assumeNotNull(user);
		assumeNotNull(password);
		assumeNotNull(collabUser);
		assumeNotNull(collabPassword);
		assumeNotNull(collabRepository);

		RepositoryInvitationService repositoryInvitationService =
			new RepositoryInvitationService(client);

		List<RepositoryInvitation> originalRepositoryInvitations =
			repositoryInvitationService.getUserRepositoryInvitations();

		client.setCredentials(collabUser, collabPassword);

		CollaboratorService cs = new CollaboratorService(client);

		cs.addCollaborator(RepositoryId.create(collabUser, collabRepository), user);

		client.setCredentials(user, password);

		List<RepositoryInvitation> newRepositoryInvitations =
			repositoryInvitationService.getUserRepositoryInvitations();

		assertEquals(originalRepositoryInvitations.size() + 1, newRepositoryInvitations.size());

		RepositoryInvitation repositoryInvitation =
			newRepositoryInvitations.get(newRepositoryInvitations.size() - 1);

		repositoryInvitationService.declineRepositoryInvitation(repositoryInvitation.getId());

		List<RepositoryInvitation> currentRepositoryInvitations =
			repositoryInvitationService.getUserRepositoryInvitations();

		assertEquals(originalRepositoryInvitations.size(), currentRepositoryInvitations.size());
	}


}
