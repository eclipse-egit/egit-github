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

			/**
			 * 
			 */
			public IStatus run(IProgressMonitor monitor)
					throws OperationCanceledException {
				return GitHubUi.createStatus(IStatus.OK, "Success");
			}

			/**
			 * 
			 */
			public ISearchResult getSearchResult() {

				// TODO query should be done in run() method

				GitHubClient client = new GitHubClient(
						IGitHubConstants.HOST_API_V2, -1,
						IGitHubConstants.PROTOCOL_HTTPS);
				RepositoryService repositoryService = new RepositoryService(
						client);
				try {
					List<Repository> repositories = repositoryService
							.searchRepositories(queryString);
					return new RepositorySearchResult(repositories, this);
				} catch (IOException ioException) {
					ioException.printStackTrace();
					return null;
				}

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
