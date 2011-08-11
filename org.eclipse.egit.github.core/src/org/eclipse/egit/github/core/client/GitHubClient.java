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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidParameterException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.egit.github.core.Assert;
import org.eclipse.egit.github.core.RequestError;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Client class for interacting with GitHub HTTP/JSON API.
 */
public class GitHubClient {

	/**
	 * Create API client from URL.
	 * 
	 * This creates an HTTPS-based client with a host that contains the host
	 * value of the given URL prefixed with 'api'.
	 * 
	 * @param url
	 * @return client
	 */
	public static GitHubClient createClient(String url) {
		try {
			String host = new URL(url).getHost();
			host = IGitHubConstants.SUBDOMAIN_API + "." + host;
			return new GitHubClient(host, -1, IGitHubConstants.PROTOCOL_HTTPS);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static final String USER_AGENT = "GitHubJava/1.1.0"; //$NON-NLS-1$

	private final URI hostUri;

	private String userAgent = USER_AGENT;

	private String user;

	private String pass;

	private boolean usingOauth = false;

	private final Gson gson = GsonUtils.getGson();

	/**
	 * Create default client 
	 */
	public GitHubClient() {
		this(IGitHubConstants.HOST_API, -1, IGitHubConstants.PROTOCOL_HTTPS);
	}

	/**
	 * Create client for host configuration
	 * 
	 * @param hostname
	 * @param port
	 * @param scheme
	 */
	public GitHubClient(String hostname, int port, String scheme) {
		try {
			hostUri = new URI(scheme, null, hostname, port, null, null, null);
		} catch (URISyntaxException e) {
			throw new InvalidParameterException();
		}
	}

	/**
	 * Create client for host configuration
	 * 
	 * @param httpHost
	 */
	public GitHubClient(URI uri) {
		Assert.notNull("URI cannot be null", uri); //$NON-NLS-1$

		hostUri = uri;
	}

	/**
	 * Set the value to set as the user agent header on every request created.
	 * Specifying a null or empty agent parameter will reset this client to use
	 * the default user agent header value.
	 * 
	 * @param agent
	 * @return this client
	 */
	public GitHubClient setUserAgent(String agent) {
		if (agent != null && agent.length() > 0)
			userAgent = agent;
		else
			userAgent = USER_AGENT;
		return this;
	}

	/**
	 * Configure a URLConnection with Authorization headers
	 *
	 * @param URLConnection instance to configure
	 * @return configured URLConnection
	 */
	protected HttpsURLConnection configureAuthorizationHeaders(HttpsURLConnection conn) {
		if (conn != null) {
			if (user != null && pass != null) {
				if (!usingOauth) {
					String authCombo = user + ":" + pass;
					byte[] encodedAuthCombo = Base64.encodeBase64(authCombo.getBytes());
					String encodedAuthString = new String(encodedAuthCombo);
					conn.setRequestProperty("Authorization", "Basic " + encodedAuthString);
				} else {
					conn.setRequestProperty("Authorization", user + " " + pass);
				}
			} else {
				conn.setRequestProperty("Authorization", null);
			}
		}
		return conn;
	}

	/**
	 * Configure request with standard headers
	 * 
	 * @param request
	 * @return configured request
	 */
	protected HttpsURLConnection configureRequest(String uri) {
		try {
			final URI requestUri = new URI(uri);
			final URI resultantUri = new URI(hostUri.getScheme(), null, hostUri.getHost(),
					hostUri.getPort(), requestUri.getPath(), requestUri.getQuery(), null);
			final HttpsURLConnection conn = (HttpsURLConnection) resultantUri.toURL()
					.openConnection();
			conn.setRequestProperty("User-Agent", userAgent);
			conn.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					// "Liberal" Hostname Verifier
					return true;
				}
			});
			return configureAuthorizationHeaders(conn);
		} catch (URISyntaxException e) {
			throw new InvalidParameterException();
		} catch (MalformedURLException e) {
			throw new InvalidParameterException();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Create standard post method
	 * 
	 * @param uri
	 * @return URLConnection instance prepped for a POST request
	 */
	protected HttpsURLConnection createPost(String uri) {
		final HttpsURLConnection conn = configureRequest(uri);
		conn.setDoOutput(true);
		try {
			conn.setRequestMethod("POST");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * Create standard put method
	 * 
	 * @param uri
	 * @return URLConnection instance prepped for a PUT request
	 */
	protected HttpsURLConnection createPut(String uri) {
		final HttpsURLConnection conn = configureRequest(uri);
		conn.setDoOutput(true);
		try {
			conn.setRequestMethod("PUT");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * Create get method
	 * 
	 * @param uri
	 * @return URLConnection instance prepped for a GET request
	 */
	protected HttpsURLConnection createGet(String uri) {
		final HttpsURLConnection conn = configureRequest(uri);
		try {
			conn.setRequestMethod("GET");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * Create delete method
	 * 
	 * @param uri
	 * @return URLConnection instance prepped for a DELETE request
	 */
	protected HttpsURLConnection createDelete(String uri) {
		final HttpsURLConnection conn = configureRequest(uri);
		try {
			conn.setRequestMethod("DELETE");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * Set credentials
	 * 
	 * @param user
	 * @param password
	 * @return this client
	 */
	public GitHubClient setCredentials(String user, String password) {
		usingOauth = false;
		this.user = user;
		pass = password;
		return this;
	}

	/**
	 * Set OAuth2 token
	 * 
	 * @param token
	 * @return this client
	 */
	public GitHubClient setOAuth2Token(String token) {
		usingOauth = true;
		user = IGitHubConstants.AUTH_TOKEN;
		pass = token;
		return this;
	}

	/**
	 * Get the user that this client is currently authenticating as
	 * 
	 * @return user or null if not authentication
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Parse json to specified type
	 * 
	 * @param <V>
	 * @param response
	 * @param type
	 * @return type
	 * @throws IOException
	 */
	protected <V> V parseJson(HttpResponse response, Type type)
			throws IOException {
		if (response.getBody().equals(""))
			throw new IOException("Empty response body");
		try {
			return gson.fromJson(response.getBody(), type);
		} catch (JsonParseException jpe) {
			throw new IOException(jpe.getMessage());
		}
	}

	/**
	 * Convert object to a JSON string
	 * 
	 * @param object
	 * @return JSON string
	 * @throws IOException
	 */
	private String toJson(Object object) throws IOException {
		try {
			return gson.toJson(object);
		} catch (JsonParseException jpe) {
			throw new IOException(jpe.getMessage());
		}
	}

	/**
	 * Parse error from response
	 * 
	 * @param response
	 * @return request error
	 * @throws IOException
	 */
	protected RequestError parseError(HttpResponse response) throws IOException {
		return parseJson(response, RequestError.class);
	}

	/**
	 * Create error exception from response and throw it
	 * 
	 * @param response
	 * @param status
	 * @return non-null newly created {@link IOException}
	 */
	protected IOException createException(HttpResponse response) {
		final int code = response.getStatusCode();
		switch (code) {
		case HttpsURLConnection.HTTP_BAD_REQUEST:
		case HttpsURLConnection.HTTP_UNAUTHORIZED:
		case HttpsURLConnection.HTTP_FORBIDDEN:
		case HttpsURLConnection.HTTP_NOT_FOUND:
		case 422:
		case HttpsURLConnection.HTTP_INTERNAL_ERROR:
			RequestError error;
			try {
				error = parseError(response);
			} catch (IOException e) {
				return e;
			}
			return new RequestException(error, code);
		default:
			return new IOException(response.getMessage());
		}
	}

	/**
	 * Is the response successful?
	 * 
	 * @param response
	 * @return true if okay, false otherwise
	 */
	protected boolean isOk(HttpResponse response) {
		switch (response.getStatusCode()) {
		case HttpsURLConnection.HTTP_OK:
		case HttpsURLConnection.HTTP_CREATED:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Is the response empty?
	 * 
	 * @param response
	 * @param status
	 * @return true if empty, false otherwise
	 */
	protected boolean isEmpty(HttpResponse response) {
		return HttpsURLConnection.HTTP_NO_CONTENT == response.getStatusCode();
	}

	public HttpResponse sendRequest(HttpsURLConnection conn, String params) throws IOException {
		conn.connect();
		if (conn.getDoOutput() && params != null) {
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(params);
			out.flush();
		}
		final HttpResponse response;
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		for (String line = in.readLine(); line != null; line = in.readLine()) {
			sb.append(line + '\n');
		}
		response = new HttpResponse(sb.toString(), conn.getResponseCode(),
				conn.getResponseMessage(), conn.getHeaderFields());
		conn.disconnect();
		return response;
	}
	/**
	 * Get response from uri and bind to specified type
	 * 
	 * @param request
	 * @return response
	 * @throws IOException
	 */
	public GitHubResponse get(GitHubRequest request) throws IOException {
		HttpResponse response = sendRequest(createGet(request.generateUri()), null);
		if (isOk(response))
			return new GitHubResponse(response, parseJson(response,
					request.getType()));
		if (isEmpty(response))
			return new GitHubResponse(response, null);
		throw createException(response);
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
	protected <V> V sendJson(HttpsURLConnection conn,
			Object params, Type type) throws IOException {
		String parameters = null;
		if (params != null) {
			parameters = toJson(params);
			conn.setRequestProperty("Content-Type", IGitHubConstants.CONTENT_TYPE_JSON);
			conn.setRequestProperty("Accept-Charset", IGitHubConstants.CHARSET_UTF8);
		}
		HttpResponse response = sendRequest(conn, parameters);
		if (isOk(response))
			if (type != null)
				return parseJson(response, type);
			else
				return null;
		if (isEmpty(response))
			return null;
		throw createException(response);
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
		return sendJson(createPost(uri), params, type);
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
		return sendJson(createPut(uri), params, type);
	}

	/**
	 * Delete resource at URI. This method will throw an {@link IOException}
	 * when the response status is not a 204 (No Content).
	 * 
	 * @param uri
	 * @throws IOException
	 */
	public void delete(String uri) throws IOException {
		HttpResponse response = sendRequest(createDelete(uri), null);
		if (!isEmpty(response))
			throw new RequestException(parseError(response),
					response.getStatusCode());
	}
}
