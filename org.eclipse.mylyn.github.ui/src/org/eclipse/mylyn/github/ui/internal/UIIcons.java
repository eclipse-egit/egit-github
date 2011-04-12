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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Icons for the Mylyn GitHub connector.
 * 
 * @author Kevin Sawicki (kevin@github.com)
 */
public class UIIcons {

	/** Icon for an issue label */
	public final static ImageDescriptor OBJ_LABEL;

	/** base URL */
	public final static URL base;

	static {
		base = init();
		OBJ_LABEL = map("obj16/issue_label.png"); //$NON-NLS-1$
	}

	private static ImageDescriptor map(final String icon) {
		if (base != null) {
			try {
				return ImageDescriptor.createFromURL(new URL(base, icon));
			} catch (MalformedURLException mux) {
				GitHubUi.logError("Error loading image", mux); //$NON-NLS-1$
			}
		}
		return ImageDescriptor.getMissingImageDescriptor();
	}

	private static URL init() {
		try {
			return new URL(GitHubUi.getDefault().getBundle().getEntry("/"), //$NON-NLS-1$
					"icons/"); //$NON-NLS-1$
		} catch (MalformedURLException mux) {
			GitHubUi.logError("Error determining icon base", mux); //$NON-NLS-1$
			return null;
		}
	}
}