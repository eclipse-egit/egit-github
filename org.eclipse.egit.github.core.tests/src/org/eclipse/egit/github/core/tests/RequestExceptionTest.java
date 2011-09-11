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
package org.eclipse.egit.github.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.egit.github.core.RequestError;
import org.eclipse.egit.github.core.client.GsonUtils;
import org.eclipse.egit.github.core.client.RequestException;
import org.junit.Test;

/**
 * Unit tests of {@link RequestException}
 */
public class RequestExceptionTest {

	/**
	 * Formatted error message for invalid field
	 */
	@Test
	public void invalidField() {
		RequestError error = GsonUtils.fromJson(
				"{\"errors\":[{\"code\":\"invalid\", \"field\":\"page\"}]}",
				RequestError.class);
		RequestException e = new RequestException(error, 400);
		String formatted = e.formatErrors();
		assertNotNull(formatted);
		assertEquals("400: Invalid value for field 'page'", formatted);
	}

	/**
	 * Formatted error message with invalid field value
	 */
	@Test
	public void invalidFieldValue() {
		RequestError error = GsonUtils
				.fromJson(
						"{\"errors\":[{\"code\":\"invalid\", \"field\":\"name\", \"value\":\"100\"}]}",
						RequestError.class);
		RequestException e = new RequestException(error, 401);
		String formatted = e.formatErrors();
		assertNotNull(formatted);
		assertEquals("401: Invalid value of '100' for field 'name'", formatted);
	}

	/**
	 * Formatted error message for missing field
	 */
	@Test
	public void missingField() {
		RequestError error = GsonUtils
				.fromJson(
						"{\"errors\":[{\"code\":\"missing_field\", \"field\":\"due\"}]}",
						RequestError.class);
		RequestException e = new RequestException(error, 422);
		String formatted = e.formatErrors();
		assertNotNull(formatted);
		assertEquals("422: Missing required field 'due'", formatted);
	}

	/**
	 * Formatted error message for existing resource with field
	 */
	@Test
	public void existentField() {
		RequestError error = GsonUtils
				.fromJson(
						"{\"errors\":[{\"code\":\"already_exists\", \"field\":\"severity\",  \"resource\":\"Issue\"}]}",
						RequestError.class);
		RequestException e = new RequestException(error, 500);
		String formatted = e.formatErrors();
		assertNotNull(formatted);
		assertEquals(
				"500: Issue resource with field 'severity' already exists",
				formatted);
	}

	/**
	 * Formatted error message for error in field
	 */
	@Test
	public void errorField() {
		RequestError error = GsonUtils
				.fromJson(
						"{\"errors\":[{\"field\":\"priority\", \"resource\":\"Gist\"}]}",
						RequestError.class);
		RequestException e = new RequestException(error, 400);
		String formatted = e.formatErrors();
		assertNotNull(formatted);
		assertEquals("400: Error with field 'priority' in Gist resource",
				formatted);
	}
}
