/******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *****************************************************************************/
package org.eclipse.mylyn.internal.github.ui;

import org.eclipse.jgit.lib.Repository;

/**
 * Callback for resolving a repository
 */
public interface IRepositoryCallback {

	/**
	 * Callback when repository is resolved
	 * 
	 * @param repository
	 *            never null
	 */
	void resolved(Repository repository);

}
