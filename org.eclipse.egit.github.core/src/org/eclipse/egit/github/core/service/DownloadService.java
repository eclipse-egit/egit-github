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
package org.eclipse.egit.github.core.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.eclipse.egit.github.core.Assert;
import org.eclipse.egit.github.core.Download;
import org.eclipse.egit.github.core.DownloadResource;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.HttpResponse;
import org.eclipse.egit.github.core.client.IGitHubConstants;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.client.PagedRequest;
import org.eclipse.egit.github.core.service.GitHubService;

import com.google.gson.reflect.TypeToken;

/**
 * Service for accessing, creating, and deleting repositories downloads.
 */
public class DownloadService extends GitHubService {

	/**
	 * UPLOAD_KEY
	 */
	public static final String UPLOAD_KEY = "key"; //$NON-NLS-1$

	/**
	 * UPLOAD_ACL
	 */
	public static final String UPLOAD_ACL = "acl"; //$NON-NLS-1$

	/**
	 * UPLOAD_SUCCESS_ACTION_STATUS
	 */
	public static final String UPLOAD_SUCCESS_ACTION_STATUS = "success_action_status"; //$NON-NLS-1$

	/**
	 * UPLOAD_FILENAME
	 */
	public static final String UPLOAD_FILENAME = "Filename"; //$NON-NLS-1$

	/**
	 * UPLOAD_AWS_ACCESS_KEY_ID
	 */
	public static final String UPLOAD_AWS_ACCESS_KEY_ID = "AWSAccessKeyId"; //$NON-NLS-1$

	/**
	 * UPLOAD_POLICY
	 */
	public static final String UPLOAD_POLICY = "Policy"; //$NON-NLS-1$

	/**
	 * UPLOAD_SIGNATURE
	 */
	public static final String UPLOAD_SIGNATURE = "Signature"; //$NON-NLS-1$

	/**
	 * UPLOAD_FILE
	 */
	public static final String UPLOAD_FILE = "file"; //$NON-NLS-1$

	/**
	 * @param client
	 */
	public DownloadService(GitHubClient client) {
		super(client);
	}

	/**
	 * Get download metadata for given repository and id
	 * 
	 * @param repository
	 * @param id
	 * @return download
	 * @throws IOException
	 */
	public Download getDownload(IRepositoryIdProvider repository, int id)
			throws IOException {
		final String repoId = getId(repository);
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(IGitHubConstants.SEGMENT_DOWNLOADS);
		uri.append('/').append(id);
		GitHubRequest request = createRequest();
		request.setUri(uri);
		request.setType(Download.class);
		return (Download) client.get(request).getBody();
	}

	/**
	 * Create paged downloads request
	 * 
	 * @param repository
	 * @param start
	 * @param size
	 * @return request
	 */
	protected PagedRequest<Download> createDownloadsRequest(
			IRepositoryIdProvider repository, int start, int size) {
		final String repoId = getId(repository);
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(IGitHubConstants.SEGMENT_DOWNLOADS);
		PagedRequest<Download> request = createPagedRequest(start, size);
		request.setType(new TypeToken<List<Download>>() {
		}.getType());
		request.setUri(uri);
		return request;
	}

	/**
	 * Get metadata for all downloads for given repository
	 * 
	 * @param repository
	 * @return non-null but possibly empty list of download metadata
	 * @throws IOException
	 */
	public List<Download> getDownloads(IRepositoryIdProvider repository)
			throws IOException {
		PagedRequest<Download> request = createDownloadsRequest(repository,
				PagedRequest.PAGE_FIRST, PagedRequest.PAGE_SIZE);
		return getAll(request);
	}

	/**
	 * Page metadata for downloads for given repository
	 * 
	 * @param repository
	 * @return iterator over pages of downloads
	 */
	public PageIterator<Download> pageDownloads(IRepositoryIdProvider repository) {
		return pageDownloads(repository, PagedRequest.PAGE_SIZE);
	}

	/**
	 * Page downloads for given repository
	 * 
	 * @param repository
	 * @param size
	 * @return iterator over pages of downloads
	 */
	public PageIterator<Download> pageDownloads(
			IRepositoryIdProvider repository, int size) {
		return pageDownloads(repository, PagedRequest.PAGE_FIRST, size);
	}

	/**
	 * Page downloads for given repository
	 * 
	 * @param repository
	 * @param start
	 * @param size
	 * @return iterator over pages of downloads
	 */
	public PageIterator<Download> pageDownloads(
			IRepositoryIdProvider repository, int start, int size) {
		PagedRequest<Download> request = createDownloadsRequest(repository,
				start, size);
		return createPageIterator(request);
	}

	/**
	 * Delete download with given id from given repository
	 * 
	 * @param repository
	 * @param id
	 * @throws IOException
	 */
	public void deleteDownload(IRepositoryIdProvider repository, int id)
			throws IOException {
		final String repoId = getId(repository);
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(IGitHubConstants.SEGMENT_DOWNLOADS);
		uri.append('/').append(id);
		client.delete(uri.toString());
	}

	/**
	 * Create a new resource for download associated with the given repository
	 * 
	 * @param repository
	 * @param download
	 * @return download resource
	 * @throws IOException
	 */
	public DownloadResource createResource(IRepositoryIdProvider repository,
			Download download) throws IOException {
		final String repoId = getId(repository);
		Assert.notNull("Download cannot be null", download);
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(repoId);
		uri.append(IGitHubConstants.SEGMENT_DOWNLOADS);
		return (DownloadResource) client.post(uri.toString(), download,
				DownloadResource.class);
	}

	/**
	 * Upload a resource to be available as the download described by the given
	 * resource.
	 * 
	 * @param resource
	 * @param content
	 * @param size
	 * @throws IOException
	 */
	public void uploadResource(DownloadResource resource, InputStream content,
			long size) throws IOException {
		Assert.notNull("Download resource cannot be null", resource);

		HttpsURLConnection conn = (HttpsURLConnection)new URL(resource.getS3Url()).openConnection();

		conn.setHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				// "Liberal" Hostname Verifier
				return true;
			}
		});

		conn.setRequestMethod("POST");
		conn.setDoOutput(true);

		String params = UPLOAD_KEY + "=" + resource.getPath()
				+ "&" + UPLOAD_ACL + "=" + resource.getAcl()
				+ "&" + UPLOAD_SUCCESS_ACTION_STATUS + "=" + HttpsURLConnection.HTTP_CREATED
				+ "&" + UPLOAD_FILENAME + "=" + resource.getName()
				+ "&" + UPLOAD_AWS_ACCESS_KEY_ID + "=" + resource.getAccesskeyid()
				+ "&" + UPLOAD_POLICY + "=" + resource.getPolicy()
				+ "&" + UPLOAD_SIGNATURE + "=" + resource.getSignature()
				+ "&" + "Content-Type" + "=" + resource.getMimeType()
				+ "&" + UPLOAD_FILE + "=";

		final BufferedReader fileReader = new BufferedReader(new InputStreamReader(content));
		char c;
		while ((c = (char)fileReader.read()) != -1) {
			params += c;
		}

		conn.connect();

		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		out.write(params);
		out.flush();

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		for (String line = in.readLine(); line != null; line = in.readLine()) {
			sb.append(line + '\n');
		}
		final HttpResponse response = new HttpResponse(sb.toString(), conn.getResponseCode(),
				conn.getResponseMessage(), conn.getHeaderFields());
		conn.disconnect();
		if (response.getStatusCode() != HttpsURLConnection.HTTP_CREATED)
			throw new IOException("Unexpected response status of "
					+ response.getStatusCode()); //$NON-NLS-1$
	}

	/**
	 * Create download and set the content to be the content of given input
	 * stream. This is a convenience method that performs a
	 * {@link #createResource(IRepositoryIdProvider, Download)} followed by a
	 * {@link #uploadResource(DownloadResource, InputStream, long)} with the
	 * results.
	 * 
	 * @param repository
	 * @param download
	 *            metadata about the download
	 * @param content
	 *            raw content of the download
	 * @param size
	 *            size of content in the input stream
	 * @throws IOException
	 */
	public void createDownload(IRepositoryIdProvider repository,
			Download download, InputStream content, long size)
			throws IOException {
		DownloadResource resource = createResource(repository, download);
		uploadResource(resource, content, size);
	}

	/**
	 * Create download from content of given file.
	 * 
	 * @see #createDownload(IRepositoryIdProvider, Download, InputStream, long)
	 * @param repository
	 * @param download
	 *            metadata about the download
	 * @param file
	 *            must be non-null
	 * @throws IOException
	 */
	public void createDownload(IRepositoryIdProvider repository,
			Download download, File file) throws IOException {
		Assert.notNull("File cannot be null", file);
		createDownload(repository, download, new FileInputStream(file),
				file.length());
	}
}
