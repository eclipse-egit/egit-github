/*******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *    Christian Trutz             - HttpClient 4.1
 *******************************************************************************/
package org.eclipse.egit.github.core.client;

import static org.apache.http.client.protocol.ClientContext.AUTH_CACHE;
import static org.eclipse.egit.github.core.client.IGitHubConstants.AUTH_TOKEN;
import static org.eclipse.egit.github.core.client.IGitHubConstants.CHARSET_UTF8;
import static org.eclipse.egit.github.core.client.IGitHubConstants.CONTENT_TYPE_JSON;
import static org.eclipse.egit.github.core.client.IGitHubConstants.HOST_API;
import static org.eclipse.egit.github.core.client.IGitHubConstants.HOST_DEFAULT;
import static org.eclipse.egit.github.core.client.IGitHubConstants.HOST_GISTS;
import static org.eclipse.egit.github.core.client.IGitHubConstants.PROTOCOL_HTTPS;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.ProxySelector;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * Client class for interacting with GitHub HTTP/JSON API.
 */
public class GitHubClient extends HttpClient<GitHubClient> {

	/**
	 * Create API v3 client from URL.
	 * <p>
	 * This creates an HTTPS-based client with a host that contains the host
	 * value of the given URL prefixed with 'api' if the given URL is github.com
	 * or gist.github.com
	 *
	 * @param url
	 * @return client
	 */
	public static GitHubClient createClient(String url) {
		try {
			String host = new URL(url).getHost();
			if (HOST_DEFAULT.equals(host) || HOST_GISTS.equals(host))
				host = HOST_API;
			return new GitHubClient(host);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static class SizedInputStreamBody extends InputStreamBody {

		private final long size;

		/**
		 * @param in
		 * @param size
		 */
		public SizedInputStreamBody(InputStream in, long size) {
			super(in, null);
			this.size = size;
		}

		public long getContentLength() {
			return size;
		}
	}

	private static final Header USER_AGENT = new BasicHeader(HTTP.USER_AGENT,
			"GitHubJava/1.2.0"); //$NON-NLS-1$

	private final HttpHost httpHost;

	private final HttpContext httpContext;

	private final DefaultHttpClient client = new DefaultHttpClient();

	private Header userAgent = USER_AGENT;

	private final Header accept = new BasicHeader("Accept", "application/json"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * Create default client
	 */
	public GitHubClient() {
		this(HOST_API);
	}

	/**
	 * Create client for host name
	 *
	 * @param hostname
	 */
	public GitHubClient(String hostname) {
		this(hostname, -1, PROTOCOL_HTTPS);
	}

	/**
	 * Create client for host, port, and scheme
	 *
	 * @param hostname
	 * @param port
	 * @param scheme
	 */
	public GitHubClient(String hostname, int port, String scheme) {
		super(hostname, port, scheme);
		this.httpHost = new HttpHost(hostname, port, scheme);

		// Support JVM configured proxy servers
		client.setRoutePlanner(new ProxySelectorRoutePlanner(client
				.getConnectionManager().getSchemeRegistry(), ProxySelector
				.getDefault()));

		// Preemptive authentication
		httpContext = new BasicHttpContext();
		AuthCache authCache = new BasicAuthCache();
		httpContext.setAttribute(AUTH_CACHE, authCache);
		client.addRequestInterceptor(new AuthInterceptor(), 0);
	}

	@Override
	public GitHubClient setUserAgent(String agent) {
		if (agent != null && agent.length() > 0)
			userAgent = new BasicHeader(HTTP.USER_AGENT, agent);
		else
			userAgent = USER_AGENT;
		return this;
	}

	/**
	 * Configure request with standard headers
	 *
	 * @param request
	 * @return configured request
	 */
	protected <V extends HttpMessage> V configureRequest(V request) {
		request.addHeader(userAgent);
		request.addHeader(accept);
		return request;
	}

	/**
	 * Create standard post method
	 *
	 * @param uri
	 * @return post
	 */
	protected HttpPost createPost(final String uri) {
		return configureRequest(new HttpPost(configureUri(uri)));
	}

	/**
	 * Create standard put method
	 *
	 * @param uri
	 * @return post
	 */
	protected HttpPut createPut(final String uri) {
		return configureRequest(new HttpPut(configureUri(uri)));
	}

	/**
	 * Create get method
	 *
	 * @param uri
	 * @return get method
	 */
	protected HttpGet createGet(final String uri) {
		return configureRequest(new HttpGet(configureUri(uri)));
	}

	/**
	 * Create delete method
	 *
	 * @param uri
	 * @return get method
	 */
	protected HttpDelete createDelete(String uri) {
		return configureRequest(new HttpDelete(configureUri(uri)));
	}

	/**
	 * Update credential on HTTP client credentials provider
	 *
	 * @param user
	 * @param password
	 * @return this client
	 */
	protected GitHubClient updateCredentials(String user, String password) {
		if (user != null && password != null)
			client.getCredentialsProvider().setCredentials(
					new AuthScope(httpHost.getHostName(), httpHost.getPort()),
					new UsernamePasswordCredentials(user, password));
		else
			client.getCredentialsProvider().clear();
		return this;
	}

	@Override
	public GitHubClient setCredentials(String user, String password) {
		updateCredentials(user, password);
		AuthCache authCache = (AuthCache) httpContext.getAttribute(AUTH_CACHE);
		authCache.put(httpHost, new BasicScheme());
		return this;
	}

	@Override
	public GitHubClient setOAuth2Token(String token) {
		updateCredentials(AUTH_TOKEN, token);
		AuthCache authCache = (AuthCache) httpContext.getAttribute(AUTH_CACHE);
		authCache.put(httpHost, new OAuth2Scheme());
		return this;
	}

	@Override
	public String getUser() {
		Credentials credentials = client.getCredentialsProvider()
				.getCredentials(
						new AuthScope(httpHost.getHostName(), httpHost
								.getPort()));
		return credentials != null ? credentials.getUserPrincipal().getName()
				: null;
	}

	/**
	 * Parse JSON to specified type
	 *
	 * @param <V>
	 * @param response
	 * @param type
	 * @return type
	 * @throws IOException
	 */
	protected <V> V parseJson(HttpResponse response, Type type)
			throws IOException {
		return parseJson(getStream(response), type);
	}

	/**
	 * Get {@link HttpEntity} from response
	 *
	 * @param response
	 * @return non-null entity
	 * @throws IOException
	 */
	protected HttpEntity getEntity(HttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		if (entity == null)
			throw new IOException("Response has no entity"); //$NON-NLS-1$
		return entity;
	}

	/**
	 * Get {@link InputStream} from response
	 *
	 * @param response
	 * @return non-null input stream
	 * @throws IOException
	 */
	protected InputStream getStream(HttpResponse response) throws IOException {
		InputStream stream = getEntity(response).getContent();
		if (stream == null)
			throw new IOException("Empty body"); //$NON-NLS-1$
		return stream;
	}

	/**
	 * Create error exception from response and throw it
	 *
	 * @param response
	 * @param status
	 * @return non-null newly created {@link IOException}
	 */
	protected IOException createException(HttpResponse response,
			StatusLine status) {
		final InputStream stream;
		try {
			stream = getStream(response);
		} catch (IOException e) {
			return e;
		}
		return createException(stream, status.getStatusCode(),
				status.getReasonPhrase());
	}

	/**
	 * Is the response successful?
	 *
	 * @param response
	 * @param status
	 * @return true if okay, false otherwise
	 */
	protected boolean isOk(HttpResponse response, StatusLine status) {
		return isOk(status.getStatusCode());
	}

	/**
	 * Is the response empty?
	 *
	 * @param response
	 * @param status
	 * @return true if empty, false otherwise
	 */
	protected boolean isEmpty(HttpResponse response, StatusLine status) {
		return isEmpty(status.getStatusCode());
	}

	/**
	 * Get status line from response
	 *
	 * @param response
	 * @return Non-null status line
	 * @throws IOException
	 */
	protected StatusLine getStatus(HttpResponse response) throws IOException {
		StatusLine statusLine = response.getStatusLine();
		if (statusLine == null)
			throw new IOException("Empty HTTP response status line"); //$NON-NLS-1$
		return statusLine;
	}

	@Override
	public InputStream getStream(GitHubRequest request) throws IOException {
		HttpGet method = createGet(request.generateUri());
		HttpResponse response = client.execute(httpHost, method, httpContext);
		StatusLine status = getStatus(response);
		if (isOk(response, status))
			return getStream(response);
		throw createException(response, status);
	}

	@Override
	public GitHubResponse get(GitHubRequest request) throws IOException {
		HttpGet method = createGet(request.generateUri());
		HttpResponse response = client.execute(httpHost, method, httpContext);
		StatusLine status = getStatus(response);
		if (isOk(response, status)) {
			Object body = null;
			Type type = request.getType();
			if (type != null)
				body = parseJson(response, type);
			else
				EntityUtils.consume(response.getEntity());
			return new GitHubResponse(response, body);
		}
		if (isEmpty(response, status))
			return new GitHubResponse(response, null);
		throw createException(response, status);
	}

	/**
	 * Send JSON using specified method
	 *
	 * @param <V>
	 * @param method
	 * @param params
	 * @param type
	 * @return resource
	 * @throws IOException
	 */
	protected <V> V sendJson(HttpEntityEnclosingRequestBase method,
			Object params, Type type) throws IOException {
		if (params != null)
			method.setEntity(new StringEntity(toJson(params),
					CONTENT_TYPE_JSON, CHARSET_UTF8));
		HttpResponse response = client.execute(httpHost, method, httpContext);
		StatusLine status = getStatus(response);
		if (isOk(response, status)) {
			if (type != null)
				return parseJson(response, type);
			EntityUtils.consume(response.getEntity());
			return null;
		}
		if (isEmpty(response, status))
			return null;
		throw createException(response, status);
	}

	@Override
	public <V> V post(String uri, Object params, Type type) throws IOException {
		return sendJson(createPost(uri), params, type);
	}

	@Override
	public <V> V put(String uri, Object params, Type type) throws IOException {
		return sendJson(createPut(uri), params, type);
	}

	@Override
	public void delete(String uri, Object params) throws IOException {
		HttpRequest method;
		if (params == null)
			method = createDelete(uri);
		else {
			EntityDeleteMethod delete = configureRequest(new EntityDeleteMethod(
					uri));
			delete.setEntity(new StringEntity(toJson(params),
					CONTENT_TYPE_JSON, CHARSET_UTF8));
			method = delete;
		}

		HttpResponse response = client.execute(httpHost, method, httpContext);
		StatusLine status = getStatus(response);
		if (!isEmpty(response, status))
			throw new RequestException(parseError(getStream(response)),
					status.getStatusCode());
	}

	/**
	 * Create client to use for non-API client requests
	 *
	 * @param uri
	 * @return non-null http client
	 */
	protected org.apache.http.client.HttpClient createAlternateClient(
			final String uri) {
		DefaultHttpClient client = new DefaultHttpClient();
		client.setRoutePlanner(new ProxySelectorRoutePlanner(client
				.getConnectionManager().getSchemeRegistry(), ProxySelector
				.getDefault()));
		return client;
	}

	public int postMultipart(String uri, Map<String, Object> parts)
			throws IOException {
		org.apache.http.client.HttpClient client = createAlternateClient(uri);

		HttpPost post = new HttpPost(uri);
		MultipartEntity entity = new MultipartEntity();
		for (Entry<String, Object> part : parts.entrySet()) {
			Object value = part.getValue();
			if (value instanceof SizedInputStreamPart)
				entity.addPart(part.getKey(), new SizedInputStreamBody(
						((SizedInputStreamPart) part).getStream(),
						((SizedInputStreamPart) part).getSize()));
			else
				entity.addPart(part.getKey(), new StringBody(value.toString()));
		}
		post.setEntity(entity);

		return client.execute(post).getStatusLine().getStatusCode();
	}
}
