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

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.eclipse.egit.github.core.client.IGitHubConstants.AUTH_TOKEN;
import static org.eclipse.egit.github.core.client.IGitHubConstants.CHARSET_UTF8;
import static org.eclipse.egit.github.core.client.IGitHubConstants.HOST_API;
import static org.eclipse.egit.github.core.client.IGitHubConstants.HOST_API_V2;
import static org.eclipse.egit.github.core.client.IGitHubConstants.PROTOCOL_HTTPS;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_V2_API;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_V3_API;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

import org.eclipse.egit.github.core.RequestError;

/**
 * Base client class for interacting with GitHub HTTP/JSON API.
 *
 * @param <C>
 */
public abstract class HttpClient<C> {

	/**
	 * 422 status code for unprocessable entity
	 */
	protected static final int HTTP_UNPROCESSABLE_ENTITY = 422;

	/**
	 * Base URI
	 */
	protected final String baseUri;

	/**
	 * Prefix to apply to base URI
	 */
	protected final String prefix;

	/**
	 * {@link Gson} instance
	 */
	protected Gson gson = GsonUtils.getGson();

	/**
	 * Create default client
	 */
	public HttpClient() {
		this(HOST_API);
	}

	/**
	 * Create client for host name
	 *
	 * @param hostname
	 */
	public HttpClient(String hostname) {
		this(hostname, -1, PROTOCOL_HTTPS);
	}

	/**
	 * Create client for host, port, and scheme
	 *
	 * @param hostname
	 * @param port
	 * @param scheme
	 */
	public HttpClient(String hostname, int port, String scheme) {
		final StringBuilder uri = new StringBuilder(scheme);
		uri.append("://"); //$NON-NLS-1$
		uri.append(hostname);
		if (port > 0)
			uri.append(port);
		baseUri = uri.toString();

		// Use URI prefix on non-standard host names
		if (HOST_API.equals(hostname) || HOST_API_V2.equals(hostname))
			prefix = null;
		else
			prefix = SEGMENT_V3_API;
	}

	/** @return this client */
	@SuppressWarnings("unchecked")
	protected final C self() {
		return (C) this;
	}

	/**
	 * Set whether or not serialized data should include fields that are null.
	 *
	 * @param serializeNulls
	 * @return this client
	 */
	public C setSerializeNulls(boolean serializeNulls) {
		gson = GsonUtils.getGson(serializeNulls);
		return self();
	}

	/**
	 * Set the value to set as the user agent header on every request created.
	 * Specifying a null or empty agent parameter will reset this client to use
	 * the default user agent header value.
	 *
	 * @param agent
	 * @return this client
	 */
	public abstract C setUserAgent(String agent);

	/**
	 * Configure request URI
	 *
	 * @param uri
	 * @return configured URI
	 */
	protected String configureUri(final String uri) {
		if (prefix == null || uri.startsWith(SEGMENT_V2_API)
				|| uri.startsWith(prefix))
			return uri;
		return prefix + uri;
	}

	/**
	 * Set credentials
	 *
	 * @param user
	 * @param password
	 * @return this client
	 */
	public abstract C setCredentials(String user, String password);

	/**
	 * Set OAuth2 token
	 *
	 * @param token
	 * @return this client
	 */
	public C setOAuth2Token(String token) {
		return setCredentials(AUTH_TOKEN, token);
	}

	/**
	 * Get the user that this client is currently authenticating as
	 *
	 * @return user or null if not authentication
	 */
	public abstract String getUser();

	/**
	 * Convert object to a JSON string
	 *
	 * @param object
	 * @return JSON string
	 * @throws IOException
	 */
	protected String toJson(Object object) throws IOException {
		try {
			return gson.toJson(object);
		} catch (JsonParseException jpe) {
			throw new IOException(jpe.getMessage());
		}
	}

	/**
	 * Parse JSON to specified type
	 *
	 * @param <V>
	 * @param stream
	 * @param type
	 * @return parsed type
	 * @throws IOException
	 */
	protected <V> V parseJson(InputStream stream, Type type) throws IOException {
		InputStreamReader reader = new InputStreamReader(stream, CHARSET_UTF8);
		try {
			return gson.fromJson(reader, type);
		} catch (JsonParseException jpe) {
			throw new IOException(jpe.getMessage());
		} finally {
			try {
				reader.close();
			} catch (IOException ignored) {
				// Ignored
			}
		}
	}

	/**
	 * Does status code denote an error
	 *
	 * @param code
	 * @return true if error, false otherwise
	 */
	protected boolean isError(final int code) {
		switch (code) {
		case HTTP_BAD_REQUEST:
		case HTTP_UNAUTHORIZED:
		case HTTP_FORBIDDEN:
		case HTTP_NOT_FOUND:
		case HTTP_UNPROCESSABLE_ENTITY:
		case HTTP_INTERNAL_ERROR:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Does status code denote a non-error response?
	 *
	 * @param code
	 * @return true if okay, false otherwise
	 */
	protected boolean isOk(final int code) {
		switch (code) {
		case HTTP_OK:
		case HTTP_CREATED:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Is the response empty?
	 *
	 * @param code
	 * @return true if empty, false otherwise
	 */
	protected boolean isEmpty(final int code) {
		return HTTP_NO_CONTENT == code;
	}

	/**
	 * Parse error from response
	 *
	 * @param response
	 * @return request error
	 * @throws IOException
	 */
	protected RequestError parseError(InputStream response) throws IOException {
		return parseJson(response, RequestError.class);
	}

	/**
	 * Get body from response inputs stream
	 *
	 * @param request
	 * @param stream
	 * @return parsed body
	 * @throws IOException
	 */
	protected Object getBody(GitHubRequest request, InputStream stream)
			throws IOException {
		Type type = request.getType();
		if (type != null)
			return parseJson(stream, type);
		else
			return null;
	}

	/**
	 * Create error exception from response and throw it
	 *
	 * @param response
	 * @param code
	 * @param status
	 * @return non-null newly created {@link IOException}
	 */
	protected IOException createException(InputStream response, int code,
			String status) {
		if (isError(code)) {
			final RequestError error;
			try {
				error = parseError(response);
			} catch (IOException e) {
				return e;
			}
			return new RequestException(error, code);
		} else
			return new IOException(status);
	}

	/**
	 * Get response stream from URI. It is the responsibility of the calling
	 * method to close the returned stream.
	 *
	 * @param request
	 * @return stream
	 * @throws IOException
	 */
	public abstract InputStream getStream(GitHubRequest request)
			throws IOException;

	/**
	 * Get response from URI and bind to specified type
	 *
	 * @param request
	 * @return response
	 * @throws IOException
	 */
	public abstract LinkedResponse get(GitHubRequest request)
			throws IOException;

	/**
	 * Post data to URI
	 *
	 * @param uri
	 * @param params
	 * @param type
	 * @return response
	 * @throws IOException
	 */
	public abstract <V> V post(String uri, Object params, Type type)
			throws IOException;

	/**
	 * Post to URI
	 *
	 * @param uri
	 * @throws IOException
	 */
	public void post(String uri) throws IOException {
		post(uri, null, null);
	}

	/**
	 * Post parts to URI
	 *
	 * @param uri
	 * @param parts
	 * @return status code
	 * @throws IOException
	 */
	public abstract int postMultipart(String uri, Map<String, Object> parts)
			throws IOException;

	/**
	 * Put data to URI
	 *
	 * @param <V>
	 * @param uri
	 * @param params
	 * @param type
	 * @return response
	 * @throws IOException
	 */
	public abstract <V> V put(String uri, Object params, Type type)
			throws IOException;

	/**
	 * Put to URI
	 *
	 * @param uri
	 * @throws IOException
	 */
	public void put(String uri) throws IOException {
		put(uri, null, null);
	}

	/**
	 * Delete resource at URI. This method will throw an {@link IOException}
	 * when the response status is not a 204 (No Content).
	 *
	 * @param uri
	 * @param params
	 * @throws IOException
	 */
	public abstract void delete(String uri, Object params) throws IOException;

	/**
	 * Delete resource at URI. This method will throw an {@link IOException}
	 * when the response status is not a 204 (No Content).
	 *
	 * @param uri
	 * @throws IOException
	 */
	public void delete(String uri) throws IOException {
		delete(uri, null);
	}
}
