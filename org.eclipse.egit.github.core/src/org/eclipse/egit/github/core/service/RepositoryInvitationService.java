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
package org.eclipse.egit.github.core.service;

import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_INVITATIONS;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_REPOS;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_REPOSITORY_INVITATIONS;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_USER;
import static org.eclipse.egit.github.core.client.PagedRequest.PAGE_FIRST;
import static org.eclipse.egit.github.core.client.PagedRequest.PAGE_SIZE;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.RepositoryInvitation;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.client.PagedRequest;

/**
 * Repository Invitation service class for listing, adding and removing
 * invitations{@link RepositoryInvitation} objects using a {@link GitHubClient}.
 *
 * @see <a href="http://developer.github.com/v3/repos/invitations">GitHub
 *      Repository Invitation API documentation</a>
 */
public class RepositoryInvitationService extends GitHubService {

	/**
	 * Create repository service
	 */
	public RepositoryInvitationService() {
		super();
	}

	/**
	 * Create repository invitation service
	 *
	 * @param client
	 *            cannot be null
	 */
	public RepositoryInvitationService(GitHubClient client) {
		super(client);
	}

	/**
	 * Delete the repository invitation with the given id
	 *
	 * @param owner
	 * @param repository
	 * @param invitationId
	 * @throws IOException
	 */
	public void deleteRepositoryInvitation(String owner, String repository,
			long invitationId) throws IOException {
		deleteRepositoryInvitation(owner, repository,
				Long.toString(invitationId));
	}

	/**
	 * Delete the repository invitation with the given id
	 *
	 * @param owner
	 * @param repository
	 * @param invitationId
	 * @throws IOException
	 */
	public void deleteRepositoryInvitation(String owner, String repository,
			String invitationId) throws IOException {
		verifyRepository(owner, repository);

		String repoId = owner + '/' + repository;
		deleteRepositoryInvitation(repoId, invitationId);
	}

	/**
	 * Delete the repository invitation wtih the given id
	 *
	 * @param repoId
	 * @param invitationId
	 * @throws IOException
	 */
	private void deleteRepositoryInvitation(String repoId, String invitationId)
			throws IOException {
		if (invitationId == null)
			throw new IllegalArgumentException("invitationId cannot be null"); //$NON-NLS-1$
		if (invitationId.length() == 0)
			throw new IllegalArgumentException("invitationId cannot be empty"); //$NON-NLS-1$

		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(SEGMENT_INVITATIONS);
		uri.append('/').append(invitationId);
		client.delete(uri.toString());
	}

	/**
	 * Accept a repository invitation
	 *
	 * @param invitationId
	 * @throws IOException
	 */
	public void acceptRepositoryInvitation(long invitationId)
			throws IOException {
		acceptRepositoryInvitation(Long.toString(invitationId));
	}

	/**
	 * Accept a repository invitation
	 *
	 * @param invitationId
	 * @throws IOException
	 */
	public void acceptRepositoryInvitation(String invitationId)
			throws IOException {
		if (invitationId == null)
			throw new IllegalArgumentException("invitiationId cannot be null"); //$NON-NLS-1$

		StringBuilder uri = new StringBuilder(
				SEGMENT_USER + SEGMENT_REPOSITORY_INVITATIONS);
		uri.append('/').append(invitationId);
		client.patch(uri.toString());
	}

	/**
	 * Decline a repository invitation
	 *
	 * @param invitationId
	 * @throws IOException
	 */
	public void declineRepositoryInvitation(long invitationId)
			throws IOException {
		declineRepositoryInvitation(Long.toString(invitationId));
	}

	/**
	 * Decline a repository invitation
	 *
	 * @param invitationId
	 * @throws IOException
	 */
	public void declineRepositoryInvitation(String invitationId)
			throws IOException {
		if (invitationId == null)
			throw new IllegalArgumentException("invitiationId cannot be null"); //$NON-NLS-1$

		StringBuilder uri = new StringBuilder(
				SEGMENT_USER + SEGMENT_REPOSITORY_INVITATIONS);
		uri.append('/').append(invitationId);
		client.delete(uri.toString());
	}

	/**
	 * Get all repository invitations for a specific owner and repository
	 *
	 * @param owner
	 * @param repository
	 * @return a list of the repository invitations
	 * @throws IOException
	 */
	public List<RepositoryInvitation> getRepositoryInvitations(String owner,
			String repository) throws IOException {
		return getAll(pageRepositoryInvitations(owner, repository));
	}

	/**
	 * Page repository invitations for a given owner and repository
	 *
	 * @param owner
	 * @param repository
	 * @return pageIterator for repository invitations
	 */
	public PageIterator<RepositoryInvitation> pageRepositoryInvitations(
			String owner, String repository) {
		return pageRepositoryInvitations(owner, repository, PAGE_FIRST,
				PAGE_SIZE);
	}

	/**
	 * Page repository invitations for a specifc owner and repository
	 *
	 * @param owner
	 * @param repository
	 * @param start
	 * @param size
	 * @return stuff
	 */
	public PageIterator<RepositoryInvitation> pageRepositoryInvitations(
			String owner, String repository, int start, int size) {
		if (owner == null)
			throw new IllegalArgumentException("owner cannot be null"); //$NON-NLS-1$
		if (repository == null)
			throw new IllegalArgumentException("repositor5y cannot be null"); //$NON-NLS-1$

		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(owner);
		uri.append('/').append(repository);
		uri.append(SEGMENT_INVITATIONS);
		PagedRequest<RepositoryInvitation> request = createPagedRequest(start,
				size);
		request.setUri(uri);
		request.setType(new TypeToken<List<RepositoryInvitation>>() {
			// make protected type visible
		}.getType());
		return createPageIterator(request);
	}

	/**
	 * Return all repository invitations for the currently authenticated user
	 *
	 * @return a list of repository invitations
	 * @throws IOException
	 */
	public List<RepositoryInvitation> getUserRepositoryInvitations()
			throws IOException {
		return getUserRepositoryInvitations(null);
	}

	/**
	 * Return a filtered list of repository invitations for the currently
	 * authenticated user
	 *
	 * @param filterData
	 * @return a list of repository invitations
	 * @throws IOException
	 */
	public List<RepositoryInvitation> getUserRepositoryInvitations(
			Map<String, String> filterData) throws IOException {
		return getAll(pageUserRepositoryInvitations(filterData));
	}

	/**
	 * Page repository invitations for the currently authenticated user
	 *
	 * @param filterData
	 * @return iterator
	 */
	public PageIterator<RepositoryInvitation> pageUserRepositoryInvitations(
			Map<String, String> filterData) {
		return pageUserRepositoryInvitations(filterData, PAGE_SIZE);
	}

	/**
	 * Page repository invitations for the currently authenticated user
	 *
	 * @param filterData
	 * @param size
	 * @return iterator
	 */
	public PageIterator<RepositoryInvitation> pageUserRepositoryInvitations(
			Map<String, String> filterData, int size) {
		return pageUserRepositoryInvitations(filterData, PAGE_FIRST, size);
	}

	/**
	 * Page repository invitations for the currently authenticated user
	 *
	 * @param filterData
	 * @param start
	 * @param size
	 * @return iterator
	 */
	public PageIterator<RepositoryInvitation> pageUserRepositoryInvitations(
			Map<String, String> filterData, int start, int size) {
		PagedRequest<RepositoryInvitation> request = createPagedRequest(start,
				size);
		request.setParams(filterData);
		StringBuilder uri = new StringBuilder(
				SEGMENT_USER + SEGMENT_REPOSITORY_INVITATIONS);
		request.setUri(uri);
		request.setType(new TypeToken<List<RepositoryInvitation>>() {
			// make protected type visible
		}.getType());
		return createPageIterator(request);
	}
}