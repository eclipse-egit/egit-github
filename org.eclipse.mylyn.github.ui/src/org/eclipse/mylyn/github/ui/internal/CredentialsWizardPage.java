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

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Credentials wizard page class.
 * 
 * @author Kevin Sawicki (kevin@github.com)
 */
public class CredentialsWizardPage extends WizardPage {

	private Text userText;

	private Text passwordText;

	/**
	 * Create credentials wizard page
	 */
	protected CredentialsWizardPage() {
		super("credentialsPage", "GitHub Credentials", null);
		setDescription("Enter GitHub account login and password");
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite displayArea = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(true)
				.applyTo(displayArea);
		
		

		setControl(displayArea);
	}

	public String getUserName() {
		return this.userText.getText();
	}

	public String getPassword() {
		return this.passwordText.getText();
	}

}
