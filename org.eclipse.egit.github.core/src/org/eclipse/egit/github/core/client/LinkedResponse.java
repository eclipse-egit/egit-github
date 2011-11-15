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
package org.eclipse.egit.github.core.client;

/**
 * Base HTTP client response with links to other resources
 */
public abstract class LinkedResponse {

	/**
	 * Links to other pages
	 */
	protected PageLinks links;

	private final Object body;

	/**
	 * Create response
	 *
	 * @param body
	 */
	public LinkedResponse(Object body) {
		this.body = body;
	}

	/**
	 * Get page links
	 *
	 * @return links
	 */
	protected PageLinks getLinks() {
		if (links == null)
			links = new PageLinks(this);
		return links;
	}

	/**
	 * Get link uri to first page
	 *
	 * @return possibly null uri
	 */
	public String getFirst() {
		return getLinks().getFirst();
	}

	/**
	 * Get link uri to previous page
	 *
	 * @return possibly null uri
	 */
	public String getPrevious() {
		return getLinks().getPrev();
	}

	/**
	 * Get link uri to next page
	 *
	 * @return possibly null uri
	 */
	public String getNext() {
		return getLinks().getNext();
	}

	/**
	 * Get link uri to last page
	 *
	 * @return possibly null uri
	 */
	public String getLast() {
		if (links == null)
			links = new PageLinks(this);
		return links.getLast();
	}

	/**
	 * Parsed response body
	 *
	 * @return body
	 */
	public Object getBody() {
		return body;
	}

	/**
	 * Get first header with given name
	 *
	 * @param name
	 * @return header value, possibly null
	 */
	public abstract String getHeader(String name);
}
