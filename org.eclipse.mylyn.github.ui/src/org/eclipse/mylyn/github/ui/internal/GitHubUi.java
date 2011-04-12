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
package org.eclipse.mylyn.github.ui.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * GitHub UI plug-in
 */
public class GitHubUi extends AbstractUIPlugin {

	/**
	 * BUNDLE_ID
	 */
	public static final String BUNDLE_ID = "org.eclipse.mylyn.github.ui";

	/**
	 * STORE_NAME
	 */
	public static final String STORE_NAME = "avatars.ser"; //$NON-NLS-1$

	/**
	 * Create status
	 * 
	 * @param severity
	 * @param message
	 * @return status
	 */
	public static IStatus createStatus(int severity, String message) {
		return new Status(severity, BUNDLE_ID, message);
	}

	/**
	 * Create status
	 * 
	 * @param severity
	 * @param message
	 * @param e
	 * @return status
	 */
	public static IStatus createStatus(int severity, String message, Throwable e) {
		return new Status(severity, BUNDLE_ID, message, e);
	}

	/**
	 * Create error status from message
	 * 
	 * @param message
	 * @return status
	 */
	public static IStatus createErrorStatus(String message) {
		return createStatus(IStatus.ERROR, message);
	}

	/**
	 * Create error status from message and throwable
	 * 
	 * @param message
	 * @param t
	 * @return status
	 */
	public static IStatus createErrorStatus(String message, Throwable t) {
		return createStatus(IStatus.ERROR, message, t);
	}

	/**
	 * Create error status from throwable
	 * 
	 * @param e
	 * @return status
	 */
	public static IStatus createErrorStatus(Throwable e) {
		return createStatus(IStatus.ERROR,
				"Unexpected error: " + e.getMessage(), e);
	}

	/**
	 * Log message and throwable as error
	 * 
	 * @param message
	 * @param t
	 */
	public static void logError(String message, Throwable t) {
		INSTANCE.getLog().log(createErrorStatus(message, t));
	}

	/**
	 * Log throwable as error
	 * 
	 * @param t
	 */
	public static void logError(Throwable t) {
		INSTANCE.getLog().log(createErrorStatus(t.getMessage(), t));
	}

	private static GitHubUi INSTANCE;

	/**
	 * Get default activator
	 * 
	 * @return plug-in
	 */
	public static GitHubUi getDefault() {
		return INSTANCE;
	}

	private AvatarStore store = null;

	/**
	 * Create plug-in
	 */
	public GitHubUi() {

	}

	/**
	 * Get avatar store
	 * 
	 * @return avatar store
	 */
	public AvatarStore getStore() {
		return this.store;
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		loadAvatars(context);
	}

	/**
	 * Load avatars
	 * 
	 * @param context
	 */
	protected void loadAvatars(BundleContext context) {
		IPath location = Platform.getStateLocation(context.getBundle());
		File file = location.append(STORE_NAME).toFile();
		if (file.exists()) {
			ObjectInputStream stream = null;
			try {
				stream = new ObjectInputStream(new FileInputStream(file));
				this.store = (AvatarStore) stream.readObject();
			} catch (IOException e) {
				logError("Error reading avatar store", e); //$NON-NLS-1$
			} catch (ClassNotFoundException cnfe) {
				logError("Error reading avatar store", cnfe); //$NON-NLS-1$
			} finally {
				if (stream != null)
					try {
						stream.close();
					} catch (IOException ignore) {
						// Ignored
					}
			}
		}
		if (this.store == null)
			this.store = new AvatarStore();
	}

	/**
	 * Save avatars
	 * 
	 * @param context
	 */
	protected void saveAvatars(BundleContext context) {
		IPath location = Platform.getStateLocation(context.getBundle());
		File file = location.append(STORE_NAME).toFile();

		ObjectOutputStream stream = null;
		try {
			stream = new ObjectOutputStream(new FileOutputStream(file));
			stream.writeObject(this.store);
		} catch (IOException e) {
			logError("Error writing avatar store", e); //$NON-NLS-1$
		} finally {
			if (stream != null)
				try {
					stream.close();
				} catch (IOException ignore) {
					// Ignored
				}
		}
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		INSTANCE = null;
		saveAvatars(context);
	}

}
