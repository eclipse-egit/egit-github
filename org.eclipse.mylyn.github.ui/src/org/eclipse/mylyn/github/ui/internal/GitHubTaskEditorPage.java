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

import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.mylyn.github.internal.GitHub;
import org.eclipse.mylyn.github.internal.GitHubTaskAttributes;
import org.eclipse.mylyn.internal.tasks.ui.editors.ToolBarButtonContribution;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Editor page for GitHub.
 */
public class GitHubTaskEditorPage extends AbstractTaskEditorPage {

	/**
	 * Constructor for the GitHubTaskEditorPage
	 * 
	 * @param editor
	 *            The task editor to create for GitHub
	 */
	public GitHubTaskEditorPage(final TaskEditor editor) {
		super(editor, GitHub.CONNECTOR_KIND);
		setNeedsPrivateSection(true);
		setNeedsSubmitButton(true);
	}

	@Override
	protected Set<TaskEditorPartDescriptor> createPartDescriptors() {
		Set<TaskEditorPartDescriptor> partDescriptors = super
				.createPartDescriptors();
		Iterator<TaskEditorPartDescriptor> descriptorIt = partDescriptors
				.iterator();
		while (descriptorIt.hasNext()) {
			TaskEditorPartDescriptor partDescriptor = descriptorIt.next();
			String id = partDescriptor.getId();
			if (id.equals(ID_PART_ATTRIBUTES) || id.equals(ID_PART_SUMMARY))
				descriptorIt.remove();
		}
		partDescriptors.add(new TaskEditorPartDescriptor(ID_PART_SUMMARY) {

			public AbstractTaskEditorPart createPart() {
				return new IssueSummaryPart(
						GitHubTaskAttributes.REPORTER_GRAVATAR.getId(),
						GitHubTaskAttributes.ASSIGNEE_GRAVATAR.getId());
			}
		}.setPath(PATH_HEADER));
		partDescriptors.add(new TaskEditorPartDescriptor(ID_PART_ATTRIBUTES) {

			public AbstractTaskEditorPart createPart() {
				return new IssueAttributePart();
			}
		}.setPath(PATH_ATTRIBUTES));
		return partDescriptors;
	}

	@Override
	public void fillToolBar(IToolBarManager toolBarManager) {
		super.fillToolBar(toolBarManager);
		ToolBarButtonContribution attachContextButtonContribution = new ToolBarButtonContribution(
				"org.eclipse.mylyn.tasks.toolbars.attachContext") { //$NON-NLS-1$
			@Override
			protected Control createButton(Composite composite) {
				Button attachContextButton = new Button(composite, SWT.FLAT);
				attachContextButton.setText(Messages.GitHubTaskEditorPage_AttachContextButton);
				attachContextButton.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event e) {
						// TODO attach context as comment
					}
				});
				return attachContextButton;
			}
		};
		attachContextButtonContribution.marginLeft = 10;
		toolBarManager.add(attachContextButtonContribution);
	}

}
