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
package org.eclipse.mylyn.internal.github.ui.pull;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.mylyn.github.internal.GitHub;
import org.eclipse.mylyn.github.internal.PullRequestService;
import org.eclipse.mylyn.github.internal.Repository;
import org.eclipse.mylyn.github.ui.internal.GitHubUi;
import org.eclipse.mylyn.internal.github.core.pull.PullConnector;
import org.eclipse.mylyn.internal.tasks.core.IRepositoryConstants;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage;
import org.eclipse.swt.widgets.Composite;

/**
 * Pull repository settings page class.
 */
public class PullRepositorySettingsPage extends AbstractRepositorySettingsPage {

	private static final String HTTP_URL = "http://github.com/"; //$NON-NLS-1$

	/**
	 * @param taskRepository
	 */
	public PullRepositorySettingsPage(TaskRepository taskRepository) {
		super(Messages.PullRepositorySettingsPage_Title,
				Messages.PullRepositorySettingsPage_Description, taskRepository);
	}

	/**
	 * @see AbstractRepositorySettingsPage#createAdditionalControls(Composite)
	 */
	@Override
	protected void createAdditionalControls(Composite composite) {
		if (repository == null) {
			setUrl(HTTP_URL + "user/repository"); //$NON-NLS-1$
			setUserId("user"); //$NON-NLS-1$
			repositoryLabelEditor.setStringValue(Messages.PullRepositorySettingsPage_RepositoryName);
		}
	}

	/**
	 * AbstractRepositorySettingsPage#getConnectorKind
	 */
	@Override
	public String getConnectorKind() {
		return PullConnector.KIND;
	}

	/**
	 * @see AbstractRepositorySettingsPage#getValidator(TaskRepository)
	 */
	@Override
	protected Validator getValidator(final TaskRepository taskRepository) {
		return new Validator() {

			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				try {
					monitor.beginTask(
							Messages.PullRepositorySettingsPage_TaskValidatingSettings,
							100);
					PullRequestService pullRequestService = PullConnector
							.getPullRequestService(taskRepository, true);
					Repository repo = GitHub.getRepository(taskRepository
							.getRepositoryUrl());
					try {
						monitor.subTask(Messages.PullRepositorySettingsPage_TaskContactingGitHub);
						pullRequestService.getPullRequests(repo, ""); //$NON-NLS-1$
					} catch (IOException ioException) {
						String message = MessageFormat
								.format(Messages.PullRepositorySettingsPage_StatusError,
										ioException.getLocalizedMessage());
						setStatus(GitHubUi.createErrorStatus(message));
						return;
					} finally {
						monitor.done();
					}
					setStatus(new Status(IStatus.OK, GitHubUi.BUNDLE_ID,
							Messages.PullRepositorySettingsPage_StatusSuccess));
				} finally {
					monitor.done();
				}
			}
		};
	}

	/**
	 * @see AbstractRepositorySettingsPage#isValidUrl(String)
	 */
	@Override
	protected boolean isValidUrl(String url) {
		if (url.startsWith(HTTP_URL))
			try {
				new URL(url);
				return true;
			} catch (IOException ioException) {
				return false;
			}
		return false;
	}

	/**
	 * @see AbstractRepositorySettingsPage#applyTo(TaskRepository)
	 */
	public void applyTo(TaskRepository repository) {
		repository.setProperty(IRepositoryConstants.PROPERTY_CATEGORY,
				IRepositoryConstants.CATEGORY_REVIEW);
		super.applyTo(repository);
	}

	/**
	 * @see AbstractRepositorySettingsPage#canValidate()
	 */
	public boolean canValidate() {
		return isPageComplete()
				&& (getMessage() == null || getMessageType() != IMessageProvider.ERROR);
	}

}
