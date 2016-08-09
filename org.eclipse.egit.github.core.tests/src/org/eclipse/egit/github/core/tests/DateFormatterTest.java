/******************************************************************************
 *  Copyright (c) 2011, 2016 GitHub Inc. and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *    Vladislav Rassokhin (JetBrains, s.r.o.) - support timestamp in seconds
 *****************************************************************************/
package org.eclipse.egit.github.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.util.Date;

import org.eclipse.egit.github.core.client.DateFormatter;
import org.junit.Test;

/**
 * Unit tests of {@link DateFormatter}
 */
public class DateFormatterTest {

	/**
	 * Verify serialized date returns value deserilized
	 */
	@Test
	public void serializeDeserialize() {
		DateFormatter formatter = new DateFormatter();
		Date date = new Date(10000);
		JsonElement element = formatter.serialize(date, null, null);
		assertNotNull(element);
		String value = element.getAsString();
		assertNotNull(value);
		assertTrue(value.length() > 0);
		Date out = formatter.deserialize(element, null, null);
		assertNotNull(out);
		assertEquals(date.getTime(), out.getTime());
	}

	/**
	 * Deserialize empty string
	 */
	@Test(expected = JsonParseException.class)
	public void emptyInput() {
		new DateFormatter().deserialize(new JsonPrimitive(""), null, null);
	}

	@Test
	public void deserializeTimestamp() throws Exception {
		DateFormatter formatter = new DateFormatter();
		int timestamp = 1470680416;
		Date date = new Date(timestamp * 1000L);
		Date out1 = formatter.deserialize(new JsonPrimitive(timestamp), null, null);
		Date out2 = formatter.deserialize(new JsonPrimitive(String.valueOf(timestamp)), null, null);
		assertNotNull(out1);
		assertNotNull(out2);
		assertEquals(date.getTime(), out1.getTime());
		assertEquals(date.getTime(), out2.getTime());
	}

	@Test
	public void deserializeFormat_V2_2() throws Exception {
		DateFormatter formatter = new DateFormatter();
		Date date = new Date(1447096561000L);
		Date out = formatter.deserialize(new JsonPrimitive("2015-11-09T19:16:01Z"), null, null);
		assertNotNull(out);
		assertEquals(date.getTime(), out.getTime());
	}
}
