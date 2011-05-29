/*******************************************************************************
 *  Copyright (c) 2011 Christian Trutz
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Christian Trutz - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.internal.github.ui;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.IGitHubConstants;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * {@link ISearchPage} for GitHub repositories.
 */
public class RepositorySearchPage extends DialogPage implements ISearchPage {

	/**
	 * 
	 */
	private Text query = null;

	/**
	 * @param parent
	 */
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		// query string
		Label labelQuery = new Label(composite, SWT.NONE);
		labelQuery.setText("Repositories that have to do with:");
		query = new Text(composite, SWT.BORDER);
		GridData gd_textQuery = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_textQuery.widthHint = 260;
		query.setLayoutData(gd_textQuery);
		query.setText("mylyn+github");

		setControl(composite);
	}

	/**
	 * 
	 */
	public boolean performAction() {

		final String queryString = query.getText();

		NewSearchUI.runQueryInBackground(new ISearchQuery() {

			private RepositorySearchResult searchResult = new RepositorySearchResult(
					this);

			/**
			 * 
			 */
			public IStatus run(IProgressMonitor monitor)
					throws OperationCanceledException {
				GitHubClient client = new GitHubClient(
						IGitHubConstants.HOST_API_V2, -1,
						IGitHubConstants.PROTOCOL_HTTPS);
				RepositoryService repositoryService = new RepositoryService(
						client);
				try {
					searchResult.setRepositories(repositoryService
							.searchRepositories(queryString));
					return GitHubUi.createStatus(IStatus.OK, "Success");
				} catch (IOException ioException) {
					return GitHubUi.createErrorStatus(ioException);
				}
			}

			/**
			 * 
			 */
			public ISearchResult getSearchResult() {
				return searchResult;
			}

			/**
			 * 
			 */
			public String getLabel() {
				return queryString;
			}

			/**
			 * 
			 */
			public boolean canRunInBackground() {
				return true;
			}

			/**
			 * 
			 */
			public boolean canRerun() {
				return true;
			}

		});
		return true;
	}

	/**
	 * 
	 */
	public void setContainer(ISearchPageContainer container) {
	}

}
