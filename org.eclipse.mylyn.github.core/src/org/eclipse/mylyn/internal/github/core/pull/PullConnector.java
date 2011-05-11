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
package org.eclipse.mylyn.internal.github.core.pull;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.protocol.Protocol;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.github.internal.GitHubClient;
import org.eclipse.mylyn.github.internal.IGitHubConstants;
import org.eclipse.mylyn.github.internal.PullRequestService;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;

public class PullConnector extends AbstractRepositoryConnector {

	/**
	 * KIND
	 */
	public static final String KIND = "githubPulls"; //$NON-NLS-1$

	/**
	 * 
	 */
	private static PullRequestService pullRequestService = null;

	/**
	 * 
	 * @param taskRepository
	 * @return
	 */
	public static PullRequestService getPullRequestService(
			TaskRepository taskRepository) {
		return getPullRequestService(taskRepository, false);
	}

	/**
	 * 
	 * @param taskRepository
	 * @param reset
	 * @return
	 */
	public static PullRequestService getPullRequestService(
			TaskRepository taskRepository, boolean reset) {
		if (pullRequestService == null || reset) {
			HostConfiguration hostConfig = new HostConfiguration();
			hostConfig.setHost(IGitHubConstants.HOST_API_V2, -1,
					Protocol.getProtocol(IGitHubConstants.PROTOCOL_HTTPS));
			GitHubClient client = new GitHubClient(hostConfig);
			AuthenticationCredentials credentials = taskRepository
					.getCredentials(AuthenticationType.REPOSITORY);
			if (credentials != null)
				client.setCredentials(credentials.getUserName(),
						credentials.getPassword());
			pullRequestService = new PullRequestService(client);
		}
		return pullRequestService;
	}

	@Override
	public boolean canCreateNewTask(TaskRepository arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canCreateTaskFromKey(TaskRepository arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getConnectorKind() {
		return KIND;
	}

	@Override
	public String getLabel() {
		return Messages.PullConnector_Label;
	}

	@Override
	public String getRepositoryUrlFromTaskUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskData getTaskData(TaskRepository arg0, String arg1,
			IProgressMonitor arg2) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTaskIdFromTaskUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTaskUrl(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasTaskChanged(TaskRepository arg0, ITask arg1, TaskData arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IStatus performQuery(TaskRepository arg0, IRepositoryQuery arg1,
			TaskDataCollector arg2, ISynchronizationSession arg3,
			IProgressMonitor arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateRepositoryConfiguration(TaskRepository arg0,
			IProgressMonitor arg1) throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTaskFromTaskData(TaskRepository arg0, ITask arg1,
			TaskData arg2) {
		// TODO Auto-generated method stub

	}

}
