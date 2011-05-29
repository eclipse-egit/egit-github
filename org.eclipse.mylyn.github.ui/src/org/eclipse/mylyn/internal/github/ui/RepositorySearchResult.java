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
package org.eclipse.mylyn.internal.github.ui;

import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;

/**
 * 
 */
public class RepositorySearchResult implements ISearchResult {

	private final List<Repository> repositories;

	private final ISearchQuery query;

	public RepositorySearchResult(List<Repository> repositories,
			ISearchQuery query) {
		this.repositories = repositories;
		this.query = query;
	}

	public void addListener(ISearchResultListener l) {
		// TODO Auto-generated method stub
	}

	public void removeListener(ISearchResultListener l) {
		// TODO Auto-generated method stub
	}

	public String getLabel() {
		return query.getLabel();
	}

	public String getTooltip() {
		return query.getLabel();
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public ISearchQuery getQuery() {
		return query;
	}

	public List<Repository> getRepositories() {
		return repositories;
	}

}
