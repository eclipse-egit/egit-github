/*******************************************************************************
 * Copyright (c) 2004, 2015 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *     Frank Becker - indicate deprecated attachments, bug 215549
 *     Perforce - fixes for bug 318505
 *******************************************************************************/
package org.eclipse.mylyn.internal.github.ui.gist;

import java.text.DateFormat;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.mylyn.commons.ui.CommonImages;
import org.eclipse.mylyn.commons.ui.compatibility.CommonThemes;
import org.eclipse.mylyn.commons.workbench.CommonImageManger;
import org.eclipse.mylyn.internal.tasks.ui.editors.AttachmentSizeFormatter;
import org.eclipse.mylyn.internal.tasks.ui.util.AttachmentUtil;
import org.eclipse.mylyn.tasks.core.IRepositoryPerson;
import org.eclipse.mylyn.tasks.core.ITaskAttachment;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.ui.TasksUiImages;
import org.eclipse.mylyn.tasks.ui.editors.AttributeEditorToolkit;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.themes.IThemeManager;

/**
 * Copied from org.eclipse.mylyn.tasks.ui, 3.21.0.v20160913-2131,
 * org.eclipse.mylyn.internal.tasks.ui.editors.AttachmentTableLabelProvider.
 *
 * Used in GistAttachmentPart, which previously used the Mylyn-internal
 * AttachmentTableLabelProvider directly. Unfortunately, that class changed
 * in non-compatible ways with Mylyn Tasks 3.23.0.
 */
public class GistAttachmentTableLabelProvider extends ColumnLabelProvider {

	private final AttachmentSizeFormatter sizeFormatter = AttachmentSizeFormatter.getInstance();

	private final TaskDataModel model;

	private final AttributeEditorToolkit attributeEditorToolkit;

	private final CommonImageManger imageManager;

	public GistAttachmentTableLabelProvider(TaskDataModel model, AttributeEditorToolkit attributeEditorToolkit) {
		this.model = model;
		this.attributeEditorToolkit = attributeEditorToolkit;
		this.imageManager = new CommonImageManger();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		ITaskAttachment attachment = (ITaskAttachment) element;
		if (columnIndex == 0) {
			if (AttachmentUtil.isContext(attachment)) {
				return imageManager.getImage(TasksUiImages.CONTEXT_TRANSFER);
			} else if (attachment.isPatch()) {
				return imageManager.getImage(TasksUiImages.TASK_ATTACHMENT_PATCH);
			} else {
				return imageManager.getFileImage(attachment.getFileName());
			}
		} else if (columnIndex == 3 && attachment.getAuthor() != null) {
			return getAuthorImage(attachment.getAuthor(), attachment.getTaskRepository());
		}
		return null;
	}

	/**
	 * Get author image for a specified repository person and task repository
	 *
	 * @param person
	 * @param repository
	 * @return author image
	 */
	protected Image getAuthorImage(IRepositoryPerson person, TaskRepository repository) {
		if (repository != null && person != null && person.matchesUsername(repository.getUserName())) {
			return imageManager.getImage(CommonImages.PERSON_ME);
		} else {
			return imageManager.getImage(CommonImages.PERSON);
		}
	}

	public String getColumnText(Object element, int columnIndex) {
		ITaskAttachment attachment = (ITaskAttachment) element;
		switch (columnIndex) {
		case 0:
			if (AttachmentUtil.isContext(attachment)) {
				return org.eclipse.mylyn.internal.tasks.ui.editors.Messages.AttachmentTableLabelProvider_Task_Context;
			} else if (attachment.isPatch()) {
				return org.eclipse.mylyn.internal.tasks.ui.editors.Messages.AttachmentTableLabelProvider_Patch;
			} else {
				return " " + attachment.getFileName(); //$NON-NLS-1$
			}
		case 1:
			return attachment.getDescription();
		case 2:
			Long length = attachment.getLength();
			if (length < 0) {
				return "-"; //$NON-NLS-1$
			}
			return sizeFormatter.format(length);
		case 3:
			return (attachment.getAuthor() != null) ? attachment.getAuthor().toString() : ""; //$NON-NLS-1$
		case 4:
			return (attachment.getCreationDate() != null)
					? DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(attachment.getCreationDate())
					: ""; //$NON-NLS-1$
		case 5:
			// FIXME add id to ITaskAttachment
			return getAttachmentId(attachment);
		}
		return "unrecognized column"; //$NON-NLS-1$
	}

	static String getAttachmentId(ITaskAttachment attachment) {
		String a = attachment.getUrl();
		if (a != null) {
			int i = a.indexOf("?id="); //$NON-NLS-1$
			if (i != -1) {
				return a.substring(i + 4);
			}
		}
		return ""; //$NON-NLS-1$
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// ignore
	}

	@Override
	public void dispose() {
		imageManager.dispose();
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// ignore
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// ignore
	}

	@Override
	public Color getForeground(Object element) {
		ITaskAttachment att = (ITaskAttachment) element;
		if (att.isDeprecated()) {
			IThemeManager themeManager = PlatformUI.getWorkbench().getThemeManager();
			return themeManager.getCurrentTheme().getColorRegistry().get(CommonThemes.COLOR_COMPLETED);
		}
		return super.getForeground(element);
	}

	@Override
	public String getToolTipText(Object element) {
		ITaskAttachment attachment = (ITaskAttachment) element;
		StringBuilder sb = new StringBuilder();
		sb.append(org.eclipse.mylyn.internal.tasks.ui.editors.Messages.AttachmentTableLabelProvider_File_);
		sb.append(attachment.getFileName());
		if (attachment.getContentType() != null) {
			sb.append("\n"); //$NON-NLS-1$
			sb.append(org.eclipse.mylyn.internal.tasks.ui.editors.Messages.AttachmentTableLabelProvider_Type_);
			sb.append(attachment.getContentType());
		}
		return sb.toString();
		/*"\nFilename\t\t"  + attachment.getAttributeValue("filename")
			  +"ID\t\t\t"        + attachment.getAttributeValue("attachid")
		      + "\nDate\t\t\t"    + attachment.getAttributeValue("date")
		      + "\nDescription\t" + attachment.getAttributeValue("desc")
		      + "\nCreator\t\t"   + attachment.getCreator()
		      + "\nType\t\t\t"    + attachment.getAttributeValue("type")
		      + "\nURL\t\t\t"     + attachment.getAttributeValue("task.common.attachment.url");*/
	}

	@Override
	public Point getToolTipShift(Object object) {
		return new Point(5, 5);
	}

	@Override
	public int getToolTipDisplayDelayTime(Object object) {
		return 200;
	}

	@Override
	public int getToolTipTimeDisplayed(Object object) {
		return 5000;
	}

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		cell.setText(getColumnText(element, cell.getColumnIndex()));
		Image image = getColumnImage(element, cell.getColumnIndex());
		cell.setImage(image);
		cell.setBackground(getBackground(element));
		cell.setForeground(getForeground(element));
		cell.setFont(getFont(element));
	}

	@Override
	public Color getBackground(Object element) {
		if (model != null && attributeEditorToolkit != null) {
			ITaskAttachment attachment = (ITaskAttachment) element;
			if (model.hasIncomingChanges(attachment.getTaskAttribute())) {
				return attributeEditorToolkit.getColorIncoming();
			}
		}
		return null;
	}

}