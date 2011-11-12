/*******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.egit.github.core;

import java.io.Serializable;
import java.util.List;

/**
 * Event payload class. Fields populated change depending on payload type.
 *
 * @see <a href="http://developer.github.com/v3/events/types">GitHub Event
 *      types API documentation</a>
 */
public class EventPayload implements Serializable{

	/** serialVersionUID */
	private static final long serialVersionUID = -67107437027566900L;

	private Comment comment;

	private String refType;

	private String ref;

	private String masterBranch;

	private String description;

	private Download download;

	private User target;

	private Repository forkee;

	private String head;

	private String before;

	private String after;

	private String action;

	private Gist gist;

	private List<Page> pages;

	private Issue issue;

	private User member;

	private int number;

	private PullRequest pullRequest;

	private int size;

	private List<Commit> commits;

	private Team team;

	private User user;

	private Repository repo;

	/**
	 * @return comment
	 */
	public Comment getComment() {
		return comment;
	}

	/**
	 * @param comment
	 * @return this eventpayload
	 */
	public EventPayload setComment(Comment comment) {
		this.comment = comment;
		return this;
	}

	/**
	 * @return refType
	 */
	public String getRefType() {
		return refType;
	}

	/**
	 * @param refType
	 * @return this eventpayload
	 */
	public EventPayload setRefType(String refType) {
		this.refType = refType;
		return this;
	}

	/**
	 * @return ref
	 */
	public String getRef() {
		return ref;
	}

	/**
	 * @param ref
	 * @return this eventpayload
	 */
	public EventPayload setRef(String ref) {
		this.ref = ref;
		return this;
	}

	/**
	 * @return masterBranch
	 */
	public String getMasterBranch() {
		return masterBranch;
	}

	/**
	 * @param masterBranch
	 * @return this eventpayload
	 */
	public EventPayload setMasterBranch(String masterBranch) {
		this.masterBranch = masterBranch;
		return this;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 * @return this eventpayload
	 */
	public EventPayload setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * @return download
	 */
	public Download getDownload() {
		return download;
	}

	/**
	 * @param download
	 * @return this eventpayload
	 */
	public EventPayload setDownload(Download download) {
		this.download = download;
		return this;
	}

	/**
	 * @return target
	 */
	public User getTarget() {
		return target;
	}

	/**
	 * @param target
	 * @return this eventpayload
	 */
	public EventPayload setTarget(User target) {
		this.target = target;
		return this;
	}

	/**
	 * @return forkee
	 */
	public Repository getForkee() {
		return forkee;
	}

	/**
	 * @param forkee
	 * @return this eventpayload
	 */
	public EventPayload setForkee(Repository forkee) {
		this.forkee = forkee;
		return this;
	}

	/**
	 * @return head
	 */
	public String getHead() {
		return head;
	}

	/**
	 * @param head
	 * @return this eventpayload
	 */
	public EventPayload setHead(String head) {
		this.head = head;
		return this;
	}

	/**
	 * @return before
	 */
	public String getBefore() {
		return before;
	}

	/**
	 * @param before
	 * @return this eventpayload
	 */
	public EventPayload setBefore(String before) {
		this.before = before;
		return this;
	}

	/**
	 * @return after
	 */
	public String getAfter() {
		return after;
	}

	/**
	 * @param after
	 * @return this eventpayload
	 */
	public EventPayload setAfter(String after) {
		this.after = after;
		return this;
	}

	/**
	 * @return action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 * @return this eventpayload
	 */
	public EventPayload setAction(String action) {
		this.action = action;
		return this;
	}

	/**
	 * @return gist
	 */
	public Gist getGist() {
		return gist;
	}

	/**
	 * @param gist
	 * @return this eventpayload
	 */
	public EventPayload setGist(Gist gist) {
		this.gist = gist;
		return this;
	}

	/**
	 * @return pages
	 */
	public List<Page> getPages() {
		return pages;
	}

	/**
	 * @param pages
	 * @return this eventpayload
	 */
	public EventPayload setPages(List<Page> pages) {
		this.pages = pages;
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
	 * @return this eventpayload
	 */
	public EventPayload setIssue(Issue issue) {
		this.issue = issue;
		return this;
	}

	/**
	 * @return member
	 */
	public User getMember() {
		return member;
	}

	/**
	 * @param member
	 * @return this eventpayload
	 */
	public EventPayload setMember(User member) {
		this.member = member;
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
	 * @return this eventpayload
	 */
	public EventPayload setNumber(int number) {
		this.number = number;
		return this;
	}

	/**
	 * @return pullRequest
	 */
	public PullRequest getPullRequest() {
		return pullRequest;
	}

	/**
	 * @param pullRequest
	 * @return this eventpayload
	 */
	public EventPayload setPullRequest(PullRequest pullRequest) {
		this.pullRequest = pullRequest;
		return this;
	}

	/**
	 * @return size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 * @return this eventpayload
	 */
	public EventPayload setSize(int size) {
		this.size = size;
		return this;
	}

	/**
	 * @return commits
	 */
	public List<Commit> getCommits() {
		return commits;
	}

	/**
	 * @param commits
	 * @return this eventpayload
	 */
	public EventPayload setCommits(List<Commit> commits) {
		this.commits = commits;
		return this;
	}

	/**
	 * @return team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * @param team
	 * @return this eventpayload
	 */
	public EventPayload setTeam(Team team) {
		this.team = team;
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
	 * @return this eventpayload
	 */
	public EventPayload setUser(User user) {
		this.user = user;
		return this;
	}

	/**
	 * @return repo
	 */
	public Repository getRepo() {
		return repo;
	}

	/**
	 * @param repo
	 * @return this eventpayload
	 */
	public EventPayload setRepo(Repository repo) {
		this.repo = repo;
		return this;
	}
}
