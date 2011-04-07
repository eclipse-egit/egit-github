/*******************************************************************************
 * Copyright (c) 2011 Red Hat and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Chris Aniszczyk <caniszczyk@gmail.com> - initial contribution
 *******************************************************************************/
package org.eclipse.mylyn.github.ui.internal;

import java.io.IOException;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.mylyn.github.internal.GitHub;
import org.eclipse.mylyn.github.internal.GitHubCredentials;
import org.eclipse.mylyn.github.internal.GitHubService;
import org.eclipse.mylyn.github.internal.GitHubServiceException;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;

public class CreateGistHandler extends AbstractHandler {

	@SuppressWarnings("restriction")
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if(editor != null && selection != null) {
			if(selection instanceof ITextSelection) {
				ITextSelection text = (ITextSelection) selection;
				IEditorInput input = editor.getEditorInput();
				// TODO we probably want to make this work with non IFile's too
				if(input instanceof IFileEditorInput) {
					try {
						// only use the first repository, in the future provide a selection if multiple exist
						IFileEditorInput fileInput = (IFileEditorInput) input;
						IFile file = fileInput.getFile();
						String fileExtension = file.getFileExtension();
						String title = editor.getTitle();
						Set<TaskRepository> repositories = TasksUi.getRepositoryManager().getRepositories(GitHub.CONNECTOR_KIND);
						TaskRepository repository = repositories.iterator().next();
						GitHubService service = new GitHubService();
						GitHubCredentials credentials = GitHubCredentials.create(repository);
						String url = service.createGist(title, fileExtension, text.getText(), credentials);
						Shell shell = HandlerUtil.getActiveShell(event);
						GistNotificationPopup popup = new GistNotificationPopup(shell.getDisplay(), url, title);
						popup.create();
						popup.open();
					} catch (GitHubServiceException e) {
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

}
