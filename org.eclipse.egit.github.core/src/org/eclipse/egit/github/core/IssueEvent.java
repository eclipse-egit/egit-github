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
package org.eclipse.egit.github.core;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.egit.github.core.util.DateUtils;

/**
 * Issue event model class
 */
public class IssueEvent implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -842754108817725707L;

	/**
	 * Issue closed event
	 */
	public static final String EVENT_CLOSED = "closed"; //$NON-NLS-1$

	/**
	 * Issue reopened event
	 */
	public static final String EVENT_REOPENED = "reopened"; //$NON-NLS-1$

	/**
	 * User subscribed to issue event
	 */
	public static final String EVENT_SUBSCRIBED = "subscribed"; //$NON-NLS-1$

	/**
	 * Issue merged event
	 */
	public static final String EVENT_MERGED = "merged"; //$NON-NLS-1$

	/**
	 * Issue referenced event
	 */
	public static final String EVENT_REFERENCED = "referenced"; //$NON-NLS-1$

	/**
	 * User mentioned in issue event
	 */
	public static final String EVENT_MENTIONED = "mentioned"; //$NON-NLS-1$

	/**
	 * Issue assigned event
	 */
	public static final String EVENT_ASSIGNED = "assigned"; //$NON-NLS-1$

	/**
	 * Issue unassigned event
	 */
	public static final String EVENT_UNASSIGNED = "unassigned"; //$NON-NLS-1$

	/**
	 * Issue labeled event
	 */
	public static final String EVENT_LABELED = "labeled"; //$NON-NLS-1$

	/**
	 * Issue unlabeled event
	 */
	public static final String EVENT_UNLABELED = "unlabeled"; //$NON-NLS-1$

	/**
	 * Issue added to milestone event
	 */
	public static final String EVENT_MILESTONED = "milestoned"; //$NON-NLS-1$

	/**
	 * Issue removed from milestone event
	 */
	public static final String EVENT_DEMILESTONED = "demilestoned"; //$NON-NLS-1$

	/**
	 * Issue renamed event
	 */
	public static final String EVENT_RENAMED = "renamed"; //$NON-NLS-1$

	/**
	 * Issue locked event
	 */
	public static final String EVENT_LOCKED = "locked"; //$NON-NLS-1$

	/**
	 * Issue unlocked event
	 */
	public static final String EVENT_UNLOCKED = "unlocked"; //$NON-NLS-1$

	/**
	 * Pull request branch deleted event
	 */
	public static final String EVENT_HEAD_REF_DELETED = "head_ref_deleted"; //$NON-NLS-1$

	/**
	 * Pull request branch restored event
	 */
	public static final String EVENT_HEAD_REF_RESTORED = "head_ref_restored"; //$NON-NLS-1$

	private Date createdAt;

	private Issue issue;

	private long id;

	private String commitId;

	private String event;

	private String url;

	private User actor;

	private Label label;

	/**
	 * @return createdAt
	 */
	public Date getCreatedAt() {
		return DateUtils.clone(createdAt);
	}

	/**
	 * @param createdAt
	 * @return this issue event
	 */
	public IssueEvent setCreatedAt(Date createdAt) {
		this.createdAt = DateUtils.clone(createdAt);
		return this;
	}

	/**
	 * @return issue
	 */
	public Issue getIssue() {
		return issue;
	}

	/**
	 * @param issue
	 * @return this issue event
	 */
	public IssueEvent setIssue(Issue issue) {
		this.issue = issue;
		return this;
	}

	/**
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 * @return this issue event
	 */
	public IssueEvent setId(long id) {
		this.id = id;
		return this;
	}

	/**
	 * @return commitId
	 */
	public String getCommitId() {
		return commitId;
	}

	/**
	 * @param commitId
	 * @return this issue event
	 */
	public IssueEvent setCommitId(String commitId) {
		this.commitId = commitId;
		return this;
	}

	/**
	 * @return event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @param event
	 * @return this issue event
	 */
	public IssueEvent setEvent(String event) {
		this.event = event;
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
	 * @return this issue event
	 */
	public IssueEvent setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * @return actor
	 */
	public User getActor() {
		return actor;
	}

	/**
	 * @param actor
	 * @return this issue event
	 */
	public IssueEvent setActor(User actor) {
		this.actor = actor;
		return this;
	}

	/**
	 * @return label
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * @param label
	 * @return this issue event
	 */
	public IssueEvent setLabel(Label label) {
		this.label = label;
		return this;
	}
}
