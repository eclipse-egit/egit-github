/*******************************************************************************
 *  Copyright (c) 2016 JetBrains, s.r.o.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Vladislav Rassokhin (JetBrains, s.r.o.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.egit.github.core.event;

import java.util.List;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;

/**
 * 'push' webhook event payload model class.
 */
public class PushWebhookPayload extends EventPayload {

    private static final long serialVersionUID = 4018755847566805828L;

    private String ref;

    private String before;
    private String after;

    // Either head or headCommit
    private String head;
    private Commit headCommit;

    private boolean created;
    private boolean deleted;
    private boolean forced;

    private List<Commit> commits;
    private Repository repository;

    private User pusher;
    private User sender;

    public String getRef() {
        return ref;
    }

    public String getBefore() {
        return before;
    }

    public String getAfter() {
        return after;
    }

    public String getHead() {
        return head;
    }

    public Commit getHeadCommit() {
        return headCommit;
    }

    public boolean isCreated() {
        return created;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isForced() {
        return forced;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public Repository getRepository() {
        return repository;
    }

    public User getPusher() {
        return pusher;
    }

    public User getSender() {
        return sender;
    }

    public PushWebhookPayload setRef(String ref) {
        this.ref = ref;
        return this;
    }

    public PushWebhookPayload setBefore(String before) {
        this.before = before;
        return this;
    }

    public PushWebhookPayload setAfter(String after) {
        this.after = after;
        return this;
    }

    public PushWebhookPayload setHead(String head) {
        this.head = head;
        return this;
    }

    public PushWebhookPayload setHeadCommit(Commit head_commit) {
        this.headCommit = head_commit;
        return this;
    }

    public PushWebhookPayload setCreated(boolean created) {
        this.created = created;
        return this;
    }

    public PushWebhookPayload setDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public PushWebhookPayload setForced(boolean forced) {
        this.forced = forced;
        return this;
    }

    public PushWebhookPayload setCommits(List<Commit> commits) {
        this.commits = commits;
        return this;
    }

    public PushWebhookPayload setRepository(Repository repository) {
        this.repository = repository;
        return this;
    }

    public PushWebhookPayload setPusher(User pusher) {
        this.pusher = pusher;
        return this;
    }

    public PushWebhookPayload setSender(User sender) {
        this.sender = sender;
        return this;
    }
}
