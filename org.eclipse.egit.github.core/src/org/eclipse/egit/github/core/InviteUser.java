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
package org.eclipse.egit.github.core;

/**
 * GitHub model class for invitee and inviter fields.
 */
public class InviteUser extends User {

	/** serialVersionUID */
	private static final long serialVersionUID = -6889418531124809497L;

	private String nodeId;

	private String followersUrl;

	private String followingUrl;

	private String gistsUrl;

	private String starredUrl;

	private String subscriptionsUrl;

	private String organizationsUrl;

	private String reposUrl;

	private String eventsUrl;

	private String receivedEventsUrl;

	private boolean siteAdmin;

	/**
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId
	 *            the nodeId to set
	 * @return this Invitee
	 */
	public InviteUser setNodeId(String nodeId) {
		this.nodeId = nodeId;
		return this;
	}

	/**
	 * @return the followersUrl
	 */
	public String getFollowersUrl() {
		return followersUrl;
	}

	/**
	 * @param followersUrl
	 *            the followersUrl to set
	 * @return this Invitee
	 */
	public InviteUser setFollowersUrl(String followersUrl) {
		this.followersUrl = followersUrl;
		return this;
	}

	/**
	 * @return the followingUrl
	 */
	public String getFollowingUrl() {
		return followingUrl;
	}

	/**
	 * @param followingUrl
	 *            the followingUrl to set
	 * @return this invitee
	 */
	public InviteUser setFollowingUrl(String followingUrl) {
		this.followingUrl = followingUrl;
		return this;
	}

	/**
	 * @return the gistsUrl
	 */
	public String getGistsUrl() {
		return gistsUrl;
	}

	/**
	 * @param gistsUrl
	 *            the gistsUrl to set
	 * @return this Invitee
	 */
	public InviteUser setGistsUrl(String gistsUrl) {
		this.gistsUrl = gistsUrl;
		return this;
	}

	/**
	 * @return the starredUrl
	 */
	public String getStarredUrl() {
		return starredUrl;
	}

	/**
	 * @param starredUrl
	 *            the starredUrl to set
	 * @return this Invitee
	 */
	public InviteUser setStarredUrl(String starredUrl) {
		this.starredUrl = starredUrl;
		return this;
	}

	/**
	 * @return the subscriptionsUrl
	 */
	public String getSubscriptionsUrl() {
		return subscriptionsUrl;
	}

	/**
	 * @param subscriptionsUrl
	 *            the subscriptionsUrl to set
	 * @return this Invitee
	 */
	public InviteUser setSubscriptionsUrl(String subscriptionsUrl) {
		this.subscriptionsUrl = subscriptionsUrl;
		return this;
	}

	/**
	 * @return the organizationsUrl
	 */
	public String getOrganizationsUrl() {
		return organizationsUrl;
	}

	/**
	 * @param organizationsUrl
	 *            the organizationsUrl to set
	 * @return this Invitee
	 */
	public InviteUser setOrganizationsUrl(String organizationsUrl) {
		this.organizationsUrl = organizationsUrl;
		return this;
	}

	/**
	 * @return the reposUrl
	 */
	public String getReposUrl() {
		return reposUrl;
	}

	/**
	 * @param reposUrl
	 *            the reposUrl to set
	 * @return this Invitee
	 */
	public InviteUser setReposUrl(String reposUrl) {
		this.reposUrl = reposUrl;
		return this;
	}

	/**
	 * @return the eventsUrl
	 */
	public String getEventsUrl() {
		return eventsUrl;
	}

	/**
	 * @param eventsUrl
	 *            the eventsUrl to set
	 * @return this Invitee
	 */
	public InviteUser setEventsUrl(String eventsUrl) {
		this.eventsUrl = eventsUrl;
		return this;
	}

	/**
	 * @return the receivedEventsUrl
	 */
	public String getReceivedEventsUrl() {
		return receivedEventsUrl;
	}

	/**
	 * @param receivedEventsUrl
	 *            the receivedEventsUrl to set
	 * @return this Invitee
	 */
	public InviteUser setReceivedEventsUrl(String receivedEventsUrl) {
		this.receivedEventsUrl = receivedEventsUrl;
		return this;
	}

	/**
	 * @return the siteAdmin
	 */
	public boolean isSiteAdmin() {
		return siteAdmin;
	}

	/**
	 * @param siteAdmin
	 *            the siteAdmin to set
	 * @return this Invitee
	 */
	public InviteUser setSiteAdmin(boolean siteAdmin) {
		this.siteAdmin = siteAdmin;
		return this;
	}

}
