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

    public static final String TYPE_CLOSED = "closed";
    public static final String TYPE_REOPENED = "reopened";
    public static final String TYPE_SUBSCRIBED = "subscribed";
    public static final String TYPE_MERGED = "merged";
    public static final String TYPE_REFERENCED = "referenced";
    public static final String TYPE_MENTIONED = "mentioned";
    public static final String TYPE_ASSIGNED = "assigned";
    public static final String TYPE_UNASSIGNED = "unassigned";
    public static final String TYPE_LABELED = "labeled";
    public static final String TYPE_UNLABELED = "unlabeled";
    public static final String TYPE_MILESTONED = "milestoned";
    public static final String TYPE_DEMILESTONED = "demilestoned";
    public static final String TYPE_RENAMED = "renamed";
    public static final String TYPE_LOCKED = "locked";
    public static final String TYPE_UNLOCKED = "unlocked";
    public static final String TYPE_HEAD_REF_DELETED = "head_ref_deleted";
    public static final String TYPE_HEAD_REF_RESTORED = "head_ref_restored";

    /** serialVersionUID */
    private static final long serialVersionUID = -842754108817725707L;

    private long id;

    private String url;

    private User actor;

    private String commitId;

    private String event;

    private Date createdAt;

    private Label label;

    private User assignee;

    private Milestone milestone;

    private Issue issue;

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

    /**
     * @return actor
     */
    public User getAssignee() {
        return assignee;
    }

    /**
     * @param assignee
     * @return this issue event
     */
    public IssueEvent setAssignee(User assignee) {
        this.assignee = assignee;
        return this;
    }

    /**
     * @return milestone
     */
    public Milestone getMilestone() {
        return milestone;
    }

    /**
     * @param milestone
     * @return this issue event
     */
    public IssueEvent setMilestone(Milestone milestone) {
        this.milestone = milestone;
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
}
