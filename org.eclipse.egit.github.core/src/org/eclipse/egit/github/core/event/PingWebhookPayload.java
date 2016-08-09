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

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryHook;
import org.eclipse.egit.github.core.User;

/**
 * 'ping' webhook event payload model class.
 */
public class PingWebhookPayload extends EventPayload {
	private static final long serialVersionUID = 5429499953002724326L;

	private String zen;
	private int hookId;
	private RepositoryHook hook;
	private Repository repository;
	private User sender;

	public String getZen() {
		return zen;
	}

	public PingWebhookPayload setZen(String zen) {
		this.zen = zen;
		return this;
	}

	public int getHookId() {
		return hookId;
	}

	public PingWebhookPayload setHookId(int hookId) {
		this.hookId = hookId;
		return this;
	}

	public RepositoryHook getHook() {
		return hook;
	}

	public PingWebhookPayload setHook(RepositoryHook hook) {
		this.hook = hook;
		return this;
	}

	public Repository getRepository() {
		return repository;
	}

	public PingWebhookPayload setRepository(Repository repository) {
		this.repository = repository;
		return this;
	}

	public User getSender() {
		return sender;
	}

	public PingWebhookPayload setSender(User sender) {
		this.sender = sender;
		return this;
	}
}
