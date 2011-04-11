/*******************************************************************************
 * Copyright (c) 2011 Red Hat and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Green <david.green@tasktop.com> - initial contribution
 *     Christian Trutz <christian.trutz@gmail.com> - initial contribution
 *     Chris Aniszczyk <caniszczyk@gmail.com> - initial contribution
 *******************************************************************************/
package org.eclipse.mylyn.github.internal;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;

/**
 * GitHub connector.
 */
public class GitHubRepositoryConnector extends AbstractRepositoryConnector {

	/**
	 * GitHub kind.
	 */
	protected static final String KIND = GitHub.CONNECTOR_KIND;

	/**
	 * GitHub service which creates, lists, deletes, etc. GitHub tasks.
	 */
	private final GitHubService service = new GitHubService();

	/**
	 * GitHub specific {@link AbstractTaskDataHandler}.
	 */
	private final GitHubTaskDataHandler taskDataHandler;

	private final Map<TaskRepository, List<Label>> repositoryLabels = Collections
			.synchronizedMap(new HashMap<TaskRepository, List<Label>>());

	private final Map<TaskRepository, List<Milestone>> repositoryMilestones = Collections
			.synchronizedMap(new HashMap<TaskRepository, List<Milestone>>());

	public GitHubRepositoryConnector() {
		taskDataHandler = new GitHubTaskDataHandler(this);
	}

	/**
	 * Create client for repository
	 * 
	 * @param repository
	 * @return client
	 */
	protected GitHubClient createClient(TaskRepository repository) {
		GitHubClient client = new GitHubClient();
		GitHubCredentials credentials = GitHubCredentials.create(repository);
		client.setCredentials(credentials.getUsername(),
				credentials.getPassword());
		return client;
	}

	/**
	 * Refresh labels for repository
	 * 
	 * @param repository
	 * @return labels
	 * @throws CoreException
	 */
	public List<Label> refreshLabels(TaskRepository repository)
			throws CoreException {
		Assert.isNotNull(repository, "Repository cannot be null"); //$NON-NLS-1$
		String user = GitHub.computeTaskRepositoryProject(repository
				.getRepositoryUrl());
		String project = GitHub.computeTaskRepositoryProject(repository
				.getRepositoryUrl());
		GitHubClient client = createClient(repository);
		LabelService service = new LabelService(client);
		try {
			List<Label> labels = service.getLabels(user, project);
			this.repositoryLabels.put(repository, labels);
			return labels;
		} catch (IOException e) {
			throw new CoreException(GitHub.createErrorStatus(e));
		}
	}

	/**
	 * Get labels for task repository.
	 * 
	 * @param repository
	 * @return non-null but possibly empty list of labels
	 */
	public List<Label> getLabels(TaskRepository repository) {
		Assert.isNotNull(repository, "Repository cannot be null"); //$NON-NLS-1$
		List<Label> labels = new LinkedList<Label>();
		List<Label> cached = this.repositoryLabels.get(repository);
		if (cached != null) {
			labels.addAll(cached);
		}
		return labels;
	}

	/**
	 * Are there cached labels for the specified task repository?
	 * 
	 * @param repository
	 * @return true if contains labels, false otherwise
	 */
	public boolean hasCachedLabels(TaskRepository repository) {
		return this.repositoryLabels.containsKey(repository);
	}

	/**
	 * Refresh milestones for repository
	 * 
	 * @param repository
	 * @return milestones
	 * @throws CoreException
	 */
	public List<Milestone> refreshMilestones(TaskRepository repository)
			throws CoreException {
		Assert.isNotNull(repository, "Repository cannot be null"); //$NON-NLS-1$
		String user = GitHub.computeTaskRepositoryProject(repository
				.getRepositoryUrl());
		String project = GitHub.computeTaskRepositoryProject(repository
				.getRepositoryUrl());
		GitHubClient client = createClient(repository);
		GitHubCredentials.create(repository);
		MilestoneService service = new MilestoneService(client);
		try {
			List<Milestone> milestones = new LinkedList<Milestone>();
			milestones.addAll(service.getMilestones(user, project,
					IssueService.STATE_OPEN));
			milestones.addAll(service.getMilestones(user, project,
					IssueService.STATE_CLOSED));
			this.repositoryMilestones.put(repository, milestones);
			return milestones;
		} catch (IOException e) {
			throw new CoreException(GitHub.createErrorStatus(e));
		}
	}

	/**
	 * Get milestones for task repository.
	 * 
	 * @param repository
	 * @return non-null but possibly empty list of milestones
	 */
	public List<Milestone> getMilestones(TaskRepository repository) {
		Assert.isNotNull(repository, "Repository cannot be null"); //$NON-NLS-1$
		List<Milestone> milestones = new LinkedList<Milestone>();
		List<Milestone> cached = this.repositoryMilestones.get(repository);
		if (cached != null) {
			milestones.addAll(cached);
		}
		return milestones;
	}

	/**
	 * Are there cached milestones for the specified task repository?
	 * 
	 * @param repository
	 * @return true if contains milestones, false otherwise
	 */
	public boolean hasCachedMilestones(TaskRepository repository) {
		return this.repositoryMilestones.containsKey(repository);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return always {@code true}
	 */
	@Override
	public boolean canCreateNewTask(TaskRepository repository) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return always {@code true}
	 */
	@Override
	public boolean canCreateTaskFromKey(TaskRepository repository) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see #KIND
	 */
	@Override
	public String getConnectorKind() {
		return KIND;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return "GitHub";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractTaskDataHandler getTaskDataHandler() {
		return this.taskDataHandler;
	}

	@Override
	public IStatus performQuery(TaskRepository repository,
			IRepositoryQuery query, TaskDataCollector collector,
			ISynchronizationSession session, IProgressMonitor monitor) {

		IStatus result = Status.OK_STATUS;
		String queryStatus = query.getAttribute("status");

		String[] statuses;
		if (queryStatus.equals("all")) {
			statuses = new String[] { "open", "closed" };
		} else {
			statuses = new String[] { queryStatus };
		}

		monitor.beginTask("Querying repository ...", statuses.length);
		try {
			String user = GitHub.computeTaskRepositoryUser(repository.getUrl());
			String project = GitHub.computeTaskRepositoryProject(repository
					.getUrl());

			GitHubClient client = createClient(repository);
			IssueService service = new IssueService(client);

			// perform query

			for (String status : statuses) {
				Map<String, String> filterData = new HashMap<String, String>();
				filterData.put(IssueService.FILTER_STATE, status);

				List<Issue> issues = service.getIssues(user, project,
						filterData);

				// collect task data
				for (Issue issue : issues) {
					TaskData taskData = taskDataHandler.createPartialTaskData(
							repository, monitor, user, project, issue);
					collector.accept(taskData);
				}
				monitor.worked(1);
			}

			result = Status.OK_STATUS;
		} catch (IOException e) {
			result = GitHub.createErrorStatus(e);
		}

		monitor.done();
		return result;
	}

	@Override
	public TaskData getTaskData(TaskRepository repository, String taskId,
			IProgressMonitor monitor) throws CoreException {

		String user = GitHub.computeTaskRepositoryUser(repository.getUrl());
		String project = GitHub.computeTaskRepositoryProject(repository
				.getUrl());

		try {
			GitHubClient client = createClient(repository);
			IssueService service = new IssueService(client);
			Issue issue = service.getIssue(user, project, taskId);
			List<Comment> comments = null;
			if (issue.getComments() > 0) {
				comments = service.getComments(user, project, taskId);
			}
			TaskData taskData = taskDataHandler.createTaskData(repository,
					monitor, user, project, issue, comments);

			return taskData;
		} catch (IOException e) {
			throw new CoreException(GitHub.createErrorStatus(e));
		}
	}

	@Override
	public String getRepositoryUrlFromTaskUrl(String taskFullUrl) {
		if (taskFullUrl != null) {
			Matcher matcher = Pattern.compile("(http://.+?)/issues/issue/([^/]+)").matcher(taskFullUrl);
			if (matcher.matches()) {
				return matcher.group(1);
			}
		}
		return null;
	}

	@Override
	public String getTaskIdFromTaskUrl(String taskFullUrl) {
		if (taskFullUrl != null) {
			Matcher matcher = Pattern.compile(".+?/issues/issue/([^/]+)").matcher(taskFullUrl);
			if (matcher.matches()) {
				return matcher.group(1);
			}
		}
		return null;
	}

	@Override
	public String getTaskUrl(String repositoryUrl, String taskId) {
		return repositoryUrl+"/issues/issue/"+taskId;
	}

	@Override
	public void updateRepositoryConfiguration(TaskRepository taskRepository,
			IProgressMonitor monitor) throws CoreException {
	}

	@Override
	public boolean hasTaskChanged(TaskRepository repository, ITask task,
			TaskData taskData) {
		return new TaskMapper(taskData).hasChanges(task);
	}

	@Override
	public void updateTaskFromTaskData(TaskRepository taskRepository,
			ITask task, TaskData taskData) {
		if (!taskData.isNew()) {
			task.setUrl(getTaskUrl(taskRepository.getUrl(), taskData.getTaskId()));
		}
		new TaskMapper(taskData).applyTo(task);
	}

	public GitHubService getService() {
		return service;
	}
}
