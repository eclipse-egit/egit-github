/*******************************************************************************
 *  Copyright (c) 2011, 2016 GitHub Inc. and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Jason Tsay (GitHub Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.egit.github.core.tests;

import static org.eclipse.egit.github.core.event.Event.TYPE_FOLLOW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.eclipse.egit.github.core.client.EventFormatter;
import org.eclipse.egit.github.core.client.GsonUtils;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.FollowPayload;
import org.junit.Test;

/**
 * Unit tests of {@link EventFormatter}
 */
public class EventFormatterTest {

	/**
	 * Follow event payload returned as {@link FollowPayload}
	 */
	@Test
	public void followPayload() {
		Event event = GsonUtils.fromJson("{\"type\":\"" + TYPE_FOLLOW
				+ "\",\"payload\":{}}", Event.class);
		assertNotNull(event);
		assertNotNull(event.getPayload());
		assertEquals(FollowPayload.class, event.getPayload().getClass());
	}

	/**
	 * Unknown event payload returned as {@link EventPayload}
	 */
	@Test
	public void unknownPayload() {
		Event event = GsonUtils.fromJson(
				"{\"type\":\"NotAnEventType\",\"payload\":{}}", Event.class);
		assertNotNull(event);
		assertNotNull(event.getPayload());
		assertEquals(EventPayload.class, event.getPayload().getClass());
	}

	/**
	 * Event with missing type has payload returned as {@link EventPayload}
	 */
	@Test
	public void missingType() {
		Event event = GsonUtils.fromJson("{\"payload\":{}}", Event.class);
		assertNotNull(event);
		assertNotNull(event.getPayload());
		assertEquals(EventPayload.class, event.getPayload().getClass());
	}

	/**
	 * Missing payload
	 */
	@Test
	public void missingPayload() {
		Event event = GsonUtils.fromJson("{}", Event.class);
		assertNotNull(event);
		assertNull(event.getPayload());
	}

	/**
	 * {@link EventFormatter#getPayloadClass(String)} should support all event types from {@link Event}
	 */
	@Test
	public void allEventTypesSupported() throws Exception {
		ArrayList<String> missing = new ArrayList<String>();
		Field[] fields = Event.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.getType() != String.class)
				continue;
			String name = field.getName();
			if (!name.startsWith("TYPE_"))
				continue;
			Class<? extends EventPayload> payloadClass = EventFormatter.getPayloadClass((String) field.get(null));
			if (payloadClass == null) {
				missing.add(name);
			}
		}
		if (!missing.isEmpty()) {
			fail("EventFormatter#getPayloadClass does not support event types: " + missing);
		}
	}
}
