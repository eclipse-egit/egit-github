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
package org.eclipse.egit.github.core;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.egit.github.core.util.DateUtils;

/**
 * Pull request model class.
 */
public class PullRequest implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 7858604768525096763L;

	private boolean mergeable;

	private boolean merged;

	private Date closedAt;

	private Date mergedAt;

	private Date updatedAt;

	private Date createdAt;

	private int additions;

	private int changedFiles;

	private int comments;

	private int commits;

	private int deletions;

	private int number;

	private PullRequestMarker base;

	private PullRequestMarker head;

	private String body;

	private String diffUrl;

	private String htmlUrl;

	private String issueUrl;

	private String patchUrl;

	private String state;

	private String title;

	private String url;

	private User mergedBy;

	private User user;

	/**
	 * @return mergeable
	 */
	public boolean isMergeable() {
		return mergeable;
	}

	/**
	 * @param mergeable
	 * @return this pull request
	 */
	public PullRequest setMergeable(boolean mergeable) {
		this.mergeable = mergeable;
		return this;
	}

	/**
	 * @return merged
	 */
	public boolean isMerged() {
		return merged;
	}

	/**
	 * @param merged
	 * @return this pull request
	 */
	public PullRequest setMerged(boolean merged) {
		this.merged = merged;
		return this;
	}

	/**
	 * @return closedAt
	 */
	public Date getClosedAt() {
		return DateUtils.clone(closedAt);
	}

	/**
	 * @param closedAt
	 * @return this pull request
	 */
	public PullRequest setClosedAt(Date closedAt) {
		this.closedAt = DateUtils.clone(closedAt);
		return this;
	}

	/**
	 * @return mergedAt
	 */
	public Date getMergedAt() {
		return DateUtils.clone(mergedAt);
	}

	/**
	 * @param mergedAt
	 * @return this pull request
	 */
	public PullRequest setMergedAt(Date mergedAt) {
		this.mergedAt = DateUtils.clone(mergedAt);
		return this;
	}

	/**
	 * @return updatedAt
	 */
	public Date getUpdatedAt() {
		return DateUtils.clone(updatedAt);
	}

	/**
	 * @param updatedAt
	 * @return this pull request
	 */
	public PullRequest setUpdatedAt(Date updatedAt) {
		this.updatedAt = DateUtils.clone(updatedAt);
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
	 * @return this pull request
	 */
	public PullRequest setCreatedAt(Date createdAt) {
		this.createdAt = DateUtils.clone(createdAt);
		return this;
	}

	/**
	 * @return additions
	 */
	public int getAdditions() {
		return additions;
	}

	/**
	 * @param additions
	 * @return this pull request
	 */
	public PullRequest setAdditions(int additions) {
		this.additions = additions;
		return this;
	}

	/**
	 * @return changedFiles
	 */
	public int getChangedFiles() {
		return changedFiles;
	}

	/**
	 * @param changedFiles
	 * @return this pull request
	 */
	public PullRequest setChangedFiles(int changedFiles) {
		this.changedFiles = changedFiles;
		return this;
	}

	/**
	 * @return comments
	 */
	public int getComments() {
		return comments;
	}

	/**
	 * @param comments
	 * @return this pull request
	 */
	public PullRequest setComments(int comments) {
		this.comments = comments;
		return this;
	}

	/**
	 * @return commits
	 */
	public int getCommits() {
		return commits;
	}

	/**
	 * @param commits
	 * @return this pull request
	 */
	public PullRequest setCommits(int commits) {
		this.commits = commits;
		return this;
	}

	/**
	 * @return deletions
	 */
	public int getDeletions() {
		return deletions;
	}

	/**
	 * @param deletions
	 * @return this pull request
	 */
	public PullRequest setDeletions(int deletions) {
		this.deletions = deletions;
		return this;
	}

	/**
	 * @return number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number
	 * @return this pull request
	 */
	public PullRequest setNumber(int number) {
		this.number = number;
		return this;
	}

	/**
	 * @return base
	 */
	public PullRequestMarker getBase() {
		return base;
	}

	/**
	 * @param base
	 * @return this pull request
	 */
	public PullRequest setBase(PullRequestMarker base) {
		this.base = base;
		return this;
	}

	/**
	 * @return head
	 */
	public PullRequestMarker getHead() {
		return head;
	}

	/**
	 * @param head
	 * @return this pull request
	 */
	public PullRequest setHead(PullRequestMarker head) {
		this.head = head;
		return this;
	}

	/**
	 * @return body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 * @return this pull request
	 */
	public PullRequest setBody(String body) {
		this.body = body;
		return this;
	}

	/**
	 * @return diffUrl
	 */
	public String getDiffUrl() {
		return diffUrl;
	}

	/**
	 * @param diffUrl
	 * @return this pull request
	 */
	public PullRequest setDiffUrl(String diffUrl) {
		this.diffUrl = diffUrl;
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
	 * @return this pull request
	 */
	public PullRequest setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
		return this;
	}

	/**
	 * @return issueUrl
	 */
	public String getIssueUrl() {
		return issueUrl;
	}

	/**
	 * @param issueUrl
	 * @return this pull request
	 */
	public PullRequest setIssueUrl(String issueUrl) {
		this.issueUrl = issueUrl;
		return this;
	}

	/**
	 * @return patchUrl
	 */
	public String getPatchUrl() {
		return patchUrl;
	}

	/**
	 * @param patchUrl
	 * @return this pull request
	 */
	public PullRequest setPatchUrl(String patchUrl) {
		this.patchUrl = patchUrl;
		return this;
	}

	/**
	 * @return state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 * @return this pull request
	 */
	public PullRequest setState(String state) {
		this.state = state;
		return this;
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 * @return this pull request
	 */
	public PullRequest setTitle(String title) {
		this.title = title;
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
	 * @return this pull request
	 */
	public PullRequest setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * @return mergedBy
	 */
	public User getMergedBy() {
		return mergedBy;
	}

	/**
	 * @param mergedBy
	 * @return this pull request
	 */
	public PullRequest setMergedBy(User mergedBy) {
		this.mergedBy = mergedBy;
		return this;
	}

	/**
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 * @return this pull request
	 */
	public PullRequest setUser(User user) {
		this.user = user;
		return this;
	}

	@Override
	public String toString() {
		return "Pull Request " + number; //$NON-NLS-1$
	}
}
