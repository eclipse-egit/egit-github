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
package org.eclipse.mylyn.github.internal;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.Assert;

/**
 * Label service class for listing {@link Label} objects in use for a given user
 * and repository.
 * 
 * @author Kevin Sawicki (kevin@github.com)
 */
public class LabelService {

	private GitHubClient client;

	/**
	 * Create label service for client
	 * 
	 * @param client
	 */
	public LabelService(GitHubClient client) {
		Assert.isNotNull(client, "Client cannot be null"); //$NON-NLS-1$
		this.client = client;
	}

	/**
	 * Get labels
	 * 
	 * @param user
	 * @param repository
	 * @return list of labels
	 * @throws IOException
	 */
	public List<Label> getLabels(String user, String repository)
			throws IOException {
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(user).append('/').append(repository);
		uri.append(IGitHubConstants.SEGMENT_LABELS).append(
				IGitHubConstants.SUFFIX_JSON);
		TypeToken<List<Label>> labelToken = new TypeToken<List<Label>>() {
		};
		return this.client.get(uri.toString(), labelToken.getType());
	}

	/**
	 * Set the labels for an issue
	 * 
	 * @param user
	 * @param repository
	 * @param issueId
	 * @param labels
	 * @return list of labels
	 * @throws IOException
	 */
	public List<Label> setLabels(String user, String repository,
			String issueId, List<Label> labels) throws IOException {
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(user).append('/').append(repository);
		uri.append(IGitHubConstants.SEGMENT_ISSUES).append('/').append(issueId);
		uri.append(IGitHubConstants.SEGMENT_LABELS).append(
				IGitHubConstants.SUFFIX_JSON);

		TypeToken<List<Label>> labelToken = new TypeToken<List<Label>>() {
		};
		return this.client.put(uri.toString(), labels, labelToken.getType());
	}

	/**
	 * Create label
	 * 
	 * @param user
	 * @param repository
	 * @param label
	 * @return created label
	 * @throws IOException
	 */
	public Label createLabel(String user, String repository, Label label)
			throws IOException {
		StringBuilder uri = new StringBuilder(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(user).append('/').append(repository);
		uri.append(IGitHubConstants.SEGMENT_LABELS).append(
				IGitHubConstants.SUFFIX_JSON);
		return this.client.post(uri.toString(), label, Label.class);
	}

}
