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

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

/**
 * {@link ISearchPage} for GitHub repositories.
 */
public class RepositorySearchPage extends DialogPage implements ISearchPage {
	public RepositorySearchPage() {
	}

	/**
	 * @param parent
	 */
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		// query string
		Label labelQuery = new Label(composite, SWT.NONE);
		labelQuery.setText("Repositories that have to do with:");
		Text textQuery = new Text(composite, SWT.BORDER);
		GridData gd_textQuery = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_textQuery.widthHint = 260;
		textQuery.setLayoutData(gd_textQuery);
		textQuery.setText("mylyn+github");

		setControl(composite);
	}

	/**
	 * 
	 */
	public boolean performAction() {
		return true;
	}

	/**
	 * 
	 */
	public void setContainer(ISearchPageContainer container) {
	}

}
