/******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Singaram Subramanian (Capital One) - initial API and implementation
 *****************************************************************************/
package org.eclipse.egit.github.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.egit.github.core.Organization;
import org.junit.Test;

/**
 * Unit tests of {@link Organization}
 */
public class OrganizationTest {

	/**
	 * Test default state of organization
	 */
	@Test
	public void defaultState() {
		Organization organization = new Organization();
		assertEquals(0, organization.getId());
		assertNull(organization.getLogin());
		assertNull(organization.getDescription());
		assertNull(organization.getUrl());
	}

	/**
	 * Test updating organization fields
	 */
	@Test
	public void updateFields() {
		Organization organization = new Organization();
		assertEquals(12, organization.setId(12).getId());
		assertEquals("orgName", organization.setLogin("orgName").getLogin());
		assertEquals("description", organization.setDescription("description").getDescription());
		assertEquals("url", organization.setUrl("url").getUrl());
	}
}
