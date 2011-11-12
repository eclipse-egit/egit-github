/*******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.egit.github.core.tests;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.eclipse.egit.github.core.Page;
import org.junit.Test;

/**
 * Unit tests of {@link Page}
 */
public class PageTest {

	/**
	 * Test default state of page
	 */
	@Test
	public void defaultState() {
		Page page = new Page();
		assertNull(page.getAction());
		assertNull(page.getHtmlUrl());
		assertNull(page.getPageName());
		assertNull(page.getSha());
		assertNull(page.getTitle());
	}

	/**
	 * Test updating event fields
	 */
	@Test
	public void updateFields() {
		Page page = new Page();
		assertEquals("create", page.setAction("create").getAction());
		assertEquals("url://a", page.setHtmlUrl("url://a").getHtmlUrl());
		assertEquals("page", page.setPageName("page").getPageName());
		assertEquals("000", page.setSha("000").getSha());
		assertEquals("title", page.setTitle("title").getTitle());
	}
}
