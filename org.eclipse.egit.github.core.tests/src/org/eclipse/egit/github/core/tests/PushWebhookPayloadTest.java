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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.event.PushWebhookPayload;
import org.junit.Test;

/**
 * Unit tests of {@link PushWebhookPayload}
 */
public class PushWebhookPayloadTest {

	/**
	 * Test default state of PushWebhookPayload
	 */
	@Test
	public void defaultState() {
		PushWebhookPayload payload = new PushWebhookPayload();
		assertNull(payload.getRef());
		assertNull(payload.getBefore());
		assertNull(payload.getAfter());
		assertNull(payload.getHead());
		assertNull(payload.getHeadCommit());
		assertFalse(payload.isCreated());
		assertFalse(payload.isDeleted());
		assertFalse(payload.isForced());
		assertNull(payload.getCommits());
		assertNull(payload.getRepository());
		assertNull(payload.getPusher());
		assertNull(payload.getSender());
	}

	/**
	 * Test updating PushWebhookPayload fields
	 */
	@Test
	public void updateFields() {
		PushWebhookPayload payload = new PushWebhookPayload();
		Commit commit = new Commit();
		ArrayList<Commit> commits = new ArrayList<Commit>();
		commits.add(commit);
		Repository repo = new Repository();
		User sender = new User().setName("sender");
		User pusher = new User().setName("pusher");

		assertSame("HEAD", payload.setRef("HEAD").getRef());
		assertSame("COMMIT-SHA-BEFORE", payload.setBefore("COMMIT-SHA-BEFORE").getBefore());
		assertSame("COMMIT-SHA-AFTER", payload.setAfter("COMMIT-SHA-AFTER").getAfter());
		assertSame("COMMIT-SHA-HEAD", payload.setHead("COMMIT-SHA-HEAD").getHead());
		assertSame(commit, payload.setHeadCommit(commit).getHeadCommit());
		assertEquals(true, payload.setCreated(true).isCreated());
		assertEquals(true, payload.setDeleted(true).isDeleted());
		assertEquals(true, payload.setForced(true).isForced());
		assertSame(commits, payload.setCommits(commits).getCommits());
		assertSame(repo, payload.setRepository(repo).getRepository());
		assertSame(sender, payload.setSender(sender).getSender());
		assertSame(pusher, payload.setPusher(pusher).getPusher());
	}
}
