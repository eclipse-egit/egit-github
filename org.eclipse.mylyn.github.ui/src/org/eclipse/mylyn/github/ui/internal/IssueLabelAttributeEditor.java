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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.ui.editors.AbstractAttributeEditor;
import org.eclipse.mylyn.tasks.ui.editors.LayoutHint;
import org.eclipse.mylyn.tasks.ui.editors.LayoutHint.ColumnSpan;
import org.eclipse.mylyn.tasks.ui.editors.LayoutHint.RowSpan;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Editor part for viewing an issue's labels.
 */
public class IssueLabelAttributeEditor extends AbstractAttributeEditor {

	private class NewLabelAction extends Action {

		public NewLabelAction() {
			super(Messages.IssueLabelAttributeEditor_ActionNewLabel,
					IAction.AS_PUSH_BUTTON);
		}

		/**
		 * @see org.eclipse.jface.action.Action#run()
		 */
		public void run() {
			TaskAttribute labels = getTaskAttribute();
			InputDialog dialog = new InputDialog(getControl().getShell(),
					Messages.IssueLabelAttributeEditor_TitleNewLabel,
					Messages.IssueLabelAttributeEditor_DescriptionNewLabel,
					"", new IInputValidator() { //$NON-NLS-3$

						public String isValid(String newText) {
							String message = null;
							if (newText == null || newText.trim().length() == 0) {
								message = Messages.IssueLabelAttributeEditor_MessageEnterName;
							}
							return message;
						}
					});
			if (Window.OK == dialog.open()) {
				String label = dialog.getValue();
				if (!labels.getValues().contains(label)) {
					labels.addValue(label);
					markLabelsChanged();
					refreshLabels();
				}
			}
		}

	}

	private class LabelAction extends Action {

		public LabelAction(String label) {
			super(label, IAction.AS_CHECK_BOX);
			setImageDescriptor(GitHubImages.DESC_GITHUB_ISSUE_LABEL);
		}

		/**
		 * @see org.eclipse.jface.action.Action#run()
		 */
		public void run() {
			TaskAttribute labels = getTaskAttribute();
			String label = getText();
			boolean changed = false;
			if (isChecked() && !labels.getValues().contains(label)) {
				labels.addValue(label);
				changed = true;
			} else {
				labels.removeValue(getText());
				changed = true;
			}
			if (changed) {
				markLabelsChanged();
				refreshLabels();
			}
		}
	}

	private Text labelsText;

	/**
	 * @param manager
	 * @param taskAttribute
	 */
	public IssueLabelAttributeEditor(TaskDataModel manager,
			TaskAttribute taskAttribute) {
		super(manager, taskAttribute);
		setLayoutHint(new LayoutHint(RowSpan.SINGLE, ColumnSpan.MULTIPLE));
	}

	private void markLabelsChanged() {
		getModel().attributeChanged(getTaskAttribute());
	}

	private void refreshLabels() {
		List<String> values = new ArrayList<String>(getTaskAttribute()
				.getValues());
		Collections.sort(values, new Comparator<String>() {

			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});

		StringBuilder labelText = new StringBuilder();
		if (!values.isEmpty()) {
			for (String label : values)
				labelText.append(label).append(", "); //$NON-NLS-1$
			labelText.deleteCharAt(labelText.length() - 1);
			labelText.deleteCharAt(labelText.length() - 1);
		}
		labelsText.setText(labelText.toString());
		labelsText.getParent().layout();
	}

	/**
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractAttributeEditor#createControl(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	public void createControl(Composite parent, FormToolkit toolkit) {
		TaskAttribute labels = getTaskAttribute();
		StringBuilder labelsValue = new StringBuilder();
		for (String value : labels.getValues())
			labelsValue.append(' ').append(value).append(',');
		if (labelsValue.length() > 0)
			labelsValue.deleteCharAt(labelsValue.length() - 1);

		labelsText = toolkit.createText(parent, labelsValue.toString(),
				SWT.WRAP | SWT.READ_ONLY);
		GridDataFactory.swtDefaults().indent(5, 0).grab(true, false)
				.applyTo(labelsText);

		MenuManager manager = new MenuManager();
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				manager.add(new NewLabelAction());
				manager.add(new Separator());
				TaskAttribute labels = getTaskAttribute();
				List<String> values = labels.getValues();
				for (Entry<String, String> label : labels.getOptions()
						.entrySet()) {
					String value = label.getValue();
					LabelAction action = new LabelAction(value);
					action.setChecked(values.contains(value));
					manager.add(action);
				}
				manager.update();
			}
		});
		Menu menu = manager.createContextMenu(labelsText);
		labelsText.setMenu(menu);

		setControl(labelsText);
	}
}