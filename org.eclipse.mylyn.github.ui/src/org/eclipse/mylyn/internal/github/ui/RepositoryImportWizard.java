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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.RepositoryUtil;
import org.eclipse.egit.core.op.CloneOperation;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.ui.UIPreferences;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

/**
 * {@link IImportWizard} for cloning GitHub repositories.
 */
public class RepositoryImportWizard extends Wizard implements IImportWizard {

	private RepositorySearchWizardPage repositorySearchWizardPage = null;

	/**
	 * {@inheritDoc}
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPages() {
		addPage(repositorySearchWizardPage = new RepositorySearchWizardPage());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean performFinish() {
		try {
			Repository repository = repositorySearchWizardPage.getRepository();
			String repoName = repository.toString();
			URIish uri = new URIish("git://github.com/" + repoName + ".git");
			boolean allSelected = true;
			Collection<Ref> selectedBranches = Collections.emptyList();
			IPreferenceStore preferenceStore = org.eclipse.egit.ui.Activator
					.getDefault().getPreferenceStore();
			int timeout = preferenceStore
					.getInt(UIPreferences.REMOTE_CONNECTION_TIMEOUT);
			String defaultRepoDir = preferenceStore
					.getString(UIPreferences.DEFAULT_REPO_DIR);
			final CloneOperation cloneOperation = new CloneOperation(uri,
					allSelected, selectedBranches, new File(defaultRepoDir
							+ File.separator + repoName), "master", "origin",
					timeout);
			getContainer().run(false, false, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					cloneOperation.run(monitor);
					RepositoryUtil repositoryUtil = Activator.getDefault()
							.getRepositoryUtil();
					repositoryUtil.addConfiguredRepository(cloneOperation
							.getGitDir());
				}
			});
		} catch (URISyntaxException uriSyntaxException) {
			GitHubUi.logError(uriSyntaxException);
			return false;
		} catch (InvocationTargetException invocationTargetException) {
			GitHubUi.logError(invocationTargetException);
			return false;
		} catch (InterruptedException interruptedException) {
			GitHubUi.logError(interruptedException);
			return false;
		}
		return true;
	}

}
