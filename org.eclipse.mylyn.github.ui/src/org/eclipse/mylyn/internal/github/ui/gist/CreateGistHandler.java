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
package org.eclipse.mylyn.internal.github.ui.gist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.mylyn.internal.github.core.GitHub;
import org.eclipse.mylyn.internal.github.ui.GitHubUi;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Create Gist handler class.
 */
public class CreateGistHandler extends AbstractHandler {

	/**
	 * PUBLIC_GIST
	 */
	public static final String PUBLIC_GIST = "publicGist"; //$NON-NLS-1$

	/**
	 * DEFAULT_FILENAME
	 */
	private static final String DEFAULT_FILENAME = "file.txt"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.core.commands.AbstractHandler#isEnabled()
	 */
	public boolean isEnabled() {
		return !GistConnectorUi.getRepositories().isEmpty();
	}

	/**
	 * TODO replace this with HandlerUtil.getActiveEditorInput(ExecutionEvent)
	 * as soon as we don't support Eclipse 3.6 anymore copied from HandlerUtil
	 * in 3.7 to be able to run this on 3.6
	 * 
	 * Return the input of the active editor.
	 * 
	 * @param event
	 *            The execution event that contains the application context
	 * @return the input of the active editor, or <code>null</code>.
	 */
	private static IEditorInput getActiveEditorInput(ExecutionEvent event) {
		Object var = HandlerUtil.getVariable(event,
				ISources.ACTIVE_EDITOR_INPUT_NAME);
		return var instanceof IEditorInput ? (IEditorInput) var : null;
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO replace this with
		// HandlerUtil.getActiveEditorInput(ExecutionEvent) as soon
		// as we don't support Eclipse 3.6 anymore
		IEditorInput input = getActiveEditorInput(event);
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection == null || selection.isEmpty())
			selection = HandlerUtil.getActiveMenuSelection(event);
		if (selection == null || selection.isEmpty())
			return null;

		boolean isPublic = Boolean
				.parseBoolean(event.getParameter(PUBLIC_GIST));
		if (selection instanceof ITextSelection) {
			ITextSelection text = (ITextSelection) selection;
			String name = null;
			if (input instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) input).getFile();
				if (file != null)
					name = file.getName();
			} else if (input instanceof IPathEditorInput) {
				IPath path = ((IPathEditorInput) input).getPath();
				if (path != null)
					name = path.lastSegment();
			} else if (input instanceof IURIEditorInput) {
				URI uri = ((IURIEditorInput) input).getURI();
				if (uri != null) {
					String rawPath = uri.getRawPath();
					int lastSlash = rawPath.lastIndexOf('/') + 1;
					if (lastSlash > 0 && lastSlash < rawPath.length())
						name = rawPath.substring(lastSlash);
				}
			}
			if (name == null)
				name = DEFAULT_FILENAME;
			createGistJob(name, text.getText(), isPublic);
		} else if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object obj = structuredSelection.getFirstElement();
			IFile file = null;
			if (obj instanceof IFile)
				file = (IFile) obj;
			else if (obj instanceof IAdaptable)
				file = (IFile) ((IAdaptable) obj).getAdapter(IFile.class);
			if (file != null)
				createGistJob(file, isPublic);
		}
		return null;
	}

	private void createGistJob(String name, String contents, boolean isPublic) {
		Set<TaskRepository> repositories = GistConnectorUi.getRepositories();

		// only use the first repository, in the future provide a
		// selection if multiple exist
		TaskRepository repository = repositories.iterator().next();
		GitHubClient client = GitHub.configureClient(new GitHubClient());
		GitHub.addCredentials(client, repository);
		GistService service = new GistService(client);
		CreateGistJob job = new CreateGistJob(
				Messages.CreateGistHandler_CreateGistJobName, name, contents,
				service, isPublic);
		job.schedule();
	}

	private void createGistJob(IFile file, boolean isPublic) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(file.getContents()));
			String line;
			StringBuilder result = new StringBuilder();
			while ((line = br.readLine()) != null)
				result.append(line).append('\n');

			String contents = result.toString();
			createGistJob(file.getName(), contents, isPublic);
		} catch (CoreException e) {
			GitHubUi.logError(e);
		} catch (IOException e) {
			GitHubUi.logError(e);
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					GitHubUi.logError(e);
				}
		}
	}

}
