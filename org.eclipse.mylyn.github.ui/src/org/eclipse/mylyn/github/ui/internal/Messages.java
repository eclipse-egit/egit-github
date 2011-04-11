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
package org.eclipse.mylyn.github.ui.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author Kevin Sawicki (kevin@github.com)
 */
public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.mylyn.github.ui.internal.messages"; //$NON-NLS-1$

	/** */
	public static String GitHubRepositoryQueryPage_ErrorLoading;

	/** */
	public static String GitHubRepositoryQueryPage_AssigneeLabel;

	/** */
	public static String GitHubRepositoryQueryPage_Description;

	/** */
	public static String GitHubRepositoryQueryPage_ErrorStatus;

	/** */
	public static String GitHubRepositoryQueryPage_LabelsLabel;

	/** */
	public static String GitHubRepositoryQueryPage_MentionsLabel;

	/** */
	public static String GitHubRepositoryQueryPage_MilestoneLabel;

	/** */
	public static String GitHubRepositoryQueryPage_MilestoneNone;

	/** */
	public static String GitHubRepositoryQueryPage_StatusClosed;

	/** */
	public static String GitHubRepositoryQueryPage_StatusLabel;

	/** */
	public static String GitHubRepositoryQueryPage_StatusOpen;

	/** */
	public static String GitHubRepositoryQueryPage_TaskLoadingLabels;

	/** */
	public static String GitHubRepositoryQueryPage_TaskLoadingMilestones;

	/** */
	public static String GitHubRepositoryQueryPage_TitleLabel;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
