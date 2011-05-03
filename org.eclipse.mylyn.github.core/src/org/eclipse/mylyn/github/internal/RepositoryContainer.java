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
package org.eclipse.mylyn.github.internal;

import java.util.Collection;
import java.util.List;

/**
 * Repository container class.
 * 
 * @author Kevin Sawicki (kevin@github.com)
 */
public class RepositoryContainer implements IResourceProvider<Repository> {

	private List<Repository> repositories;

	/**
	 * @see org.eclipse.mylyn.github.internal.IResourceProvider#getResources()
	 */
	public Collection<Repository> getResources() {
		return this.repositories;
	}

}
