/*******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Jason Tsay (GitHub Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.egit.github.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.eclipse.egit.github.core.Event;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.event.EventPayload;
import org.junit.Test;

public class EventTest {

	/**
	 * Test default state of event
	 */
	@Test
	public void defaultState() {
		Event event = new Event();
		assertNull(event.getType());
		assertNull(event.getPayload());
		assertNull(event.getRepo());
		assertNull(event.getActor());
		assertNull(event.getOrg());
		assertNull(event.getCreatedAt());
		assertFalse(event.isPublic());
	}

	/**
	 * Test updating event fields
	 */
	@Test
	public void updateFields() {
		Event event = new Event();
		assertEquals("PushEvent", event.setType("PushEvent").getType());
		EventPayload payload = new EventPayload();
		assertEquals(payload, event.setPayload(payload).getPayload());
		Repository repo = new Repository().setName("repo");
		assertEquals(repo, event.setRepo(repo).getRepo());
		User actor = new User().setLogin("actor");
		assertEquals(actor, event.setActor(actor).getActor());
		User org = new User().setLogin("org");
		assertEquals(org, event.setOrg(org).getOrg());
		assertEquals(new Date(5000), event.setCreatedAt(new Date(5000)).getCreatedAt());
		assertTrue(event.setPublic(true).isPublic());
	}

	/**
	 * Test non-mutable created at date
	 */
	@Test
	public void getCreatedAtReferenceMutableObject() {
		Event event = new Event();
		event.setCreatedAt(new Date(11111));
		event.getCreatedAt().setTime(0);
		assertTrue(event.getCreatedAt().getTime() != 0);
	}
}
