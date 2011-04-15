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
package org.eclipse.mylyn.internal.github.ui.gist;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.mylyn.internal.tasks.ui.editors.TaskEditorAttachmentPart;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Gist editor attachment part. Modeled after {@link TaskEditorAttachmentPart}
 * but with less columns.
 * 
 * @author Kevin Sawicki (kevin@github.com)
 */
public class GistAttachmentPart extends AbstractTaskEditorPart {

	/**
	 * Create gist editor attachment part
	 */
	public GistAttachmentPart() {
		setPartName(Messages.GistAttachmentPart_PartName);
	}

	/**
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#createControl(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	public void createControl(Composite parent, FormToolkit toolkit) {

	}

	/**
	 * @see org.eclipse.ui.forms.AbstractFormPart#dispose()
	 */
	public void dispose() {

	}

	/**
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart#fillToolBar(org.eclipse.jface.action.ToolBarManager)
	 */
	protected void fillToolBar(ToolBarManager toolBarManager) {

	}

	/**
	 * @see org.eclipse.ui.forms.AbstractFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object input) {
		return super.setFormInput(input);
	}

}
