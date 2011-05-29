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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;

/**
 * 
 */
public class RepositorySearchResult implements ISearchResult {

	private List<Repository> repositories;

	private final ISearchQuery query;

	private Set<ISearchResultListener> listeners = new HashSet<ISearchResultListener>();

	public RepositorySearchResult(ISearchQuery query) {
		this.query = query;
	}

	public void addListener(ISearchResultListener searchResultListener) {
		listeners.add(searchResultListener);
	}

	public void removeListener(ISearchResultListener searchResultListener) {
		listeners.remove(searchResultListener);
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

	public void setRepositories(List<Repository> repositories) {
		this.repositories = repositories;
		for (ISearchResultListener listener : listeners) {
			listener.searchResultChanged(new SearchResultEvent(this) {
			});
		}
	}

}
