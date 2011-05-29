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
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.IPageSite;

/**
 * 
 */
public class RepositorySearchResultPage implements ISearchResultPage {

	private IPageSite site = null;

	private TableViewer repositories = null;

	public IPageSite getSite() {
		return site;
	}

	public void init(IPageSite site) throws PartInitException {
		this.site = site;
	}

	public void createControl(Composite parent) {
		repositories = new TableViewer(parent);
		repositories.setContentProvider(new ArrayContentProvider());
		repositories.setLabelProvider(new LabelProvider());
	}

	public void dispose() {
	}

	public Control getControl() {
		return repositories.getTable();
	}

	public void setActionBars(IActionBars actionBars) {
		// TODO Auto-generated method stub
	}

	public void setFocus() {
		repositories.getTable().setFocus();
	}

	public Object getUIState() {
		return null;
	}

	public void setInput(ISearchResult searchResult, Object uiState) {
		System.out.println("SET RESULT");
		if (searchResult != null) {
			RepositorySearchResult repositorySearchResult = (RepositorySearchResult) searchResult;
			List<Repository> reposList = repositorySearchResult
					.getRepositories();
			if (reposList != null) {
				Repository[] reposArray = new Repository[reposList.size()];
				repositories.setInput(reposList.toArray(reposArray));
			}
		}
	}

	public void setViewPart(ISearchResultViewPart part) {
		// TODO Auto-generated method stub
	}

	public void restoreState(IMemento memento) {
		// TODO Auto-generated method stub
	}

	public void saveState(IMemento memento) {
		// TODO Auto-generated method stub
	}

	public void setID(String id) {
		// TODO Auto-generated method stub

	}

	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLabel() {
		return "LABEL";
	}

}
