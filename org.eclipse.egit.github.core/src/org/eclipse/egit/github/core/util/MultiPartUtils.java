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
package org.eclipse.egit.github.core.util;

import static org.eclipse.egit.github.core.client.IGitHubConstants.CHARSET_UTF8;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Utilities for writing multiple HTTP requests
 */
public class MultiPartUtils {

	/**
	 * Post parts to URL
	 *
	 * @param url
	 * @param parts
	 * @return connection that was posted to
	 * @throws IOException
	 */
	public static HttpURLConnection post(String url, Map<String, Object> parts)
			throws IOException {
		HttpURLConnection post = (HttpURLConnection) new URL(url)
				.openConnection();
		post.setRequestMethod("POST");
		return post(post, parts);
	}

	/**
	 * Post parts to connection
	 *
	 * @param post
	 * @param parts
	 * @return connection that was posted to
	 * @throws IOException
	 */
	public static HttpURLConnection post(HttpURLConnection post,
			Map<String, Object> parts) throws IOException {
		String boundary = "00content0boundary00";
		post.setDoOutput(true);
		post.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
		BufferedOutputStream output = new BufferedOutputStream(
				post.getOutputStream());
		byte[] buffer = new byte[8192];
		byte[] boundarySeparator = ("--" + boundary + "\r\n")
				.getBytes(CHARSET_UTF8);
		byte[] newline = "\r\n".getBytes(CHARSET_UTF8);
		try {
			for (Entry<String, Object> part : parts.entrySet()) {
				output.write(boundarySeparator);
				StringBuilder partBuffer = new StringBuilder(
						"Content-Disposition: ");
				partBuffer.append("form-data; name=\"");
				partBuffer.append(part.getKey());
				partBuffer.append('"');
				partBuffer.append("\r\n\r\n");
				output.write(partBuffer.toString().getBytes(CHARSET_UTF8));
				final Object value = part.getValue();
				if (value instanceof InputStream) {
					InputStream input = (InputStream) value;
					int read;
					while ((read = input.read(buffer)) != -1)
						output.write(buffer, 0, read);
					input.close();
				} else
					output.write(part.getValue().toString()
							.getBytes(CHARSET_UTF8));
				output.write(newline);
			}
			output.write(("--" + boundary + "--\r\n").getBytes(CHARSET_UTF8));
		} finally {
			output.close();
		}
		return post;
	}
}
