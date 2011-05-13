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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.eclipse.egit.github.core.Assert;
import org.eclipse.egit.github.core.RequestError;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

/**
 * Client class for interacting with GitHub HTTP/JSON API.
 */
public class GitHubClient {

	private static final int PER_PAGE = 100;

	private final HttpHost httpHost;

	private final HttpContext httpContext;

	private final DefaultHttpClient client = new DefaultHttpClient();

	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(Date.class, new DateFormatter())
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.serializeNulls().create();

	/**
	 * Create default client
	 */
	public GitHubClient() {
		this(new HttpHost(IGitHubConstants.HOST_API, -1,
				IGitHubConstants.PROTOCOL_HTTPS));
	}

	/**
	 * Create client for configuration
	 * 
	 * @param configuration
	 */
	public GitHubClient(HttpHost configuration) {
		Assert.notNull("Configuration cannot be null", configuration); //$NON-NLS-1$
		this.httpHost = configuration;

		// Preemptive authentication
		httpContext = new BasicHttpContext();
		AuthCache authCache = new BasicAuthCache();
		authCache.put(httpHost, new BasicScheme());
		httpContext.setAttribute(ClientContext.AUTH_CACHE, authCache);
	}

	/**
	 * Create standard post method
	 * 
	 * @param uri
	 * @return post
	 */
	protected HttpPost createPost(String uri) {
		HttpPost method = new HttpPost(uri);
		return method;
	}

	/**
	 * Create standard post method
	 * 
	 * @param uri
	 * @return post
	 */
	protected HttpPut createPut(String uri) {
		HttpPut method = new HttpPut(uri);
		return method;
	}

	/**
	 * Create get method
	 * 
	 * @param uri
	 * @return get method
	 */
	protected HttpGet createGet(String uri) {
		HttpGet method = new HttpGet(uri);
		return method;
	}

	/**
	 * Set credentials
	 * 
	 * @param user
	 * @param password
	 */
	public void setCredentials(String user, String password) {
		if (user != null && password != null) {
			Credentials credentials = new UsernamePasswordCredentials(user,
					password);
			this.client.getCredentialsProvider().setCredentials(
					new AuthScope(httpHost.getHostName(), httpHost.getPort()),
					credentials);
		}
	}

	/**
	 * Parse json to specified type
	 * 
	 * @param <V>
	 * @param method
	 * @param type
	 * @return type
	 * @throws IOException
	 */
	protected <V> V parseJson(HttpResponse response, Type type)
			throws IOException {
		InputStream stream = response.getEntity().getContent();
		if (stream == null)
			throw new JsonParseException("Empty body"); //$NON-NLS-1$
		InputStreamReader reader = new InputStreamReader(stream);
		try {
			return this.gson.fromJson(reader, type);
		} catch (JsonParseException jpe) {
			jpe.printStackTrace();
			throw new IOException(jpe);
		}
	}

	/**
	 * Get name value pairs for data map.
	 * 
	 * @param data
	 * @param page
	 * @return name value pair array
	 */
	protected String getPairs(Map<String, String> data, int page) {
		StringBuilder pairs = new StringBuilder();
		if (data == null || data.isEmpty()) {
			pairs.append(IGitHubConstants.PARAM_PAGE).append("=").append(page);
			pairs.append("&");
			pairs.append(IGitHubConstants.PARAM_PER_PAGE).append("=")
					.append(PER_PAGE);
		} else {
			int i = 1;
			for (Entry<String, String> entry : data.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				pairs.append(key).append("=").append(value);
				if (i < data.size()) {
					pairs.append("&");
				}
				i++;
			}
		}
		return pairs.toString();
	}

	/**
	 * Get response stream from uri. It is the responsibility of the calling
	 * method to close the returned stream.
	 * 
	 * @param request
	 * @return stream
	 * @throws IOException
	 */
	public InputStream getStream(GitHubRequest request) throws IOException {

		String uri = request.getUri();
		if (!uri.contains("?")) {
			uri = uri + "?" + getPairs(request.getParams(), request.getPage());
		}
		HttpGet method = createGet(uri);

		try {
			HttpResponse response = this.client.execute(this.httpHost, method,
					this.httpContext);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine == null) {
				throw new IllegalStateException(
						"HTTP response status line should not be null."); //$NON-NLS-1$
			}
			int status = statusLine.getStatusCode();
			switch (status) {
			case 200:
				return response.getEntity().getContent();
			case 400:
			case 401:
			case 403:
			case 404:
			case 422:
			case 500:
				RequestError error = parseJson(response, RequestError.class);
				throw new RequestException(error, status);
			default:
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (JsonParseException jpe) {
			throw new IOException(jpe.getMessage());
		}
	}

	/**
	 * Get response from uri and bind to specified type
	 * 
	 * @param request
	 * @return response
	 * @throws IOException
	 */
	public GitHubResponse get(GitHubRequest request) throws IOException {
		String uri = request.getUri();
		if (!uri.contains("?")) {
			uri = uri + "?" + getPairs(request.getParams(), request.getPage());
		}
		HttpGet method = createGet(uri);
		try {
			HttpResponse response = this.client.execute(this.httpHost, method,
					this.httpContext);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine == null) {
				throw new IllegalStateException(
						"HTTP response status line should not be null."); //$NON-NLS-1$
			}
			int status = statusLine.getStatusCode();
			switch (status) {
			case 200:
				return new GitHubResponse(response, parseJson(response,
						request.getType()));
			case 400:
			case 401:
			case 403:
			case 404:
			case 422:
			case 500:
				RequestError error = parseJson(response, RequestError.class);
				throw new RequestException(error, status);
			default:
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (JsonParseException jpe) {
			throw new IOException(jpe.getMessage());
		}
	}

	/**
	 * Send json using specified method
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
		if (params != null) {
			StringBuilder payload = new StringBuilder();
			this.gson.toJson(params, payload);
			method.setEntity(new StringEntity(payload.toString(),
					IGitHubConstants.CONTENT_TYPE_JSON,
					IGitHubConstants.CHARSET_UTF8));
		}
		HttpResponse response = this.client.execute(this.httpHost, method,
				this.httpContext);
		StatusLine statusLine = response.getStatusLine();
		if (statusLine == null) {
			throw new IllegalStateException(
					"HTTP response status line should not be null."); //$NON-NLS-1$
		}
		int status = statusLine.getStatusCode();
		switch (status) {
		case 200:
		case 201:
			if (type != null)
				return parseJson(response, type);
		case 204:
			break;
		case 400:
		case 401:
		case 403:
		case 404:
		case 422:
		case 500:
			RequestError error = parseJson(response, RequestError.class);
			throw new RequestException(error, status);
		default:
			throw new IOException(statusLine.getReasonPhrase());
		}
		return null;
	}

	/**
	 * Post data to uri
	 * 
	 * @param <V>
	 * @param uri
	 * @param params
	 * @param type
	 * @return response
	 * @throws IOException
	 */
	public <V> V post(String uri, Object params, Type type) throws IOException {
		HttpPost method = createPost(uri);
		return sendJson(method, params, type);
	}

	/**
	 * Put data to uri
	 * 
	 * @param <V>
	 * @param uri
	 * @param params
	 * @param type
	 * @return response
	 * @throws IOException
	 */
	public <V> V put(String uri, Object params, Type type) throws IOException {
		HttpPut method = createPut(uri);
		return sendJson(method, params, type);
	}

}
