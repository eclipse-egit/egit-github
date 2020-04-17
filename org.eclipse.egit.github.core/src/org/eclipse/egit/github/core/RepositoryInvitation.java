/*******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
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
package org.eclipse.egit.github.core;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.egit.github.core.util.DateUtils;

/**
 * GitHub repository invitation model class.
 */
public class RepositoryInvitation implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -6236047450596842076L;

	private long id;

	private Repository repository;

	private InviteUser invitee;

	private InviteUser inviter;

	private String permissions;

	private Date createdAt;

	private String url;

	private String htmlUrl;

	/**
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 * @return this RepositoryInvitation
	 */
	public RepositoryInvitation setId(long id) {
		this.id = id;
		return this;
	}

	/**
	 * @return repository
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * @param repository
	 * @return this RepositoryInvitation
	 */
	public RepositoryInvitation setRepository(Repository repository) {
		this.repository = repository;
		return this;
	}

	/**
	 * @return the invitee
	 */
	public InviteUser getInvitee() {
		return invitee;
	}

	/**
	 * @param invitee
	 *            the invitee to set
	 * @return this RepositoryInvitation
	 */
	public RepositoryInvitation setInvitee(InviteUser invitee) {
		this.invitee = invitee;
		return this;
	}

	/**
	 * @return the inviter
	 */
	public InviteUser getInviter() {
		return inviter;
	}

	/**
	 * @param inviter
	 *            the inviter to set
	 * @return this RepositoryInvitation
	 */
	public RepositoryInvitation setInviter(InviteUser inviter) {
		this.inviter = inviter;
		return this;
	}

	/**
	 * @return the permissions
	 */
	public String getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 * @return this
	 */
	public RepositoryInvitation setPermissions(String permissions) {
		this.permissions = permissions;
		return this;
	}

	/**
	 * @return createdAt
	 */
	public Date getCreatedAt() {
		return DateUtils.clone(createdAt);
	}

	/**
	 * @param createdAt
	 * @return this comment
	 */
	public RepositoryInvitation setCreatedAt(Date createdAt) {
		this.createdAt = DateUtils.clone(createdAt);
		return this;
	}

	/**
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 * @return this RepositoryInvitation
	 */
	public RepositoryInvitation setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * @return htmlUrl
	 */
	public String getHtmlUrl() {
		return htmlUrl;
	}

	/**
	 * @param htmlUrl
	 * @return this repository invitation
	 */
	public RepositoryInvitation setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
		return this;
	}

}
