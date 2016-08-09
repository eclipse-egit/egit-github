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
package org.eclipse.egit.github.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryHook;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.event.PingWebhookPayload;
import org.junit.Test;

/**
 * Unit tests of {@link PingWebhookPayload}
 */
public class PingWebhookPayloadTest {

	/**
	 * Test default state of PingWebhookPayload
	 */
	@Test
	public void defaultState() {
		PingWebhookPayload payload = new PingWebhookPayload();
		assertNull(payload.getZen());
		assertEquals(0, payload.getHookId());
		assertNull(payload.getHook());
		assertNull(payload.getRepository());
		assertNull(payload.getSender());
	}

	/**
	 * Test updating PingWebhookPayload fields
	 */
	@Test
	public void updateFields() {
		PingWebhookPayload payload = new PingWebhookPayload();
		RepositoryHook hook = new RepositoryHook();
		Repository repo = new Repository();
		User user = new User();

		assertSame("zen", payload.setZen("zen").getZen());
		assertEquals(42, payload.setHookId(42).getHookId());
		assertSame(hook, payload.setHook(hook).getHook());
		assertSame(repo, payload.setRepository(repo).getRepository());
		assertSame(user, payload.setSender(user).getSender());
	}
}
