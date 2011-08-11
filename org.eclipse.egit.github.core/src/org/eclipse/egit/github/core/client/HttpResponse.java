/*******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.egit.github.core.client;

import java.util.List;
import java.util.Map;

/**
 * Http response class
 */
public class HttpResponse {

	private String body;

	private String message;

	private int statusCode;

	private Map<String, List<String>> headers;

	public HttpResponse(String body, int statusCode, String message, Map<String, List<String>> headers) {
		this.body = body;
		this.statusCode = statusCode;
		this.message = message;
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public String getMessage() {
		return message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}
}