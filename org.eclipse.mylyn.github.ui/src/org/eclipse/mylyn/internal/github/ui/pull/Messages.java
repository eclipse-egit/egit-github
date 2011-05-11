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

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.mylyn.internal.github.ui.pull.messages"; //$NON-NLS-1$
	public static String PullRepositorySettingsPage_Description;
	public static String PullRepositorySettingsPage_RepositoryName;
	public static String PullRepositorySettingsPage_StatusError;
	public static String PullRepositorySettingsPage_StatusSuccess;
	public static String PullRepositorySettingsPage_TaskContactingGitHub;
	public static String PullRepositorySettingsPage_TaskValidatingSettings;
	public static String PullRepositorySettingsPage_Title;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
