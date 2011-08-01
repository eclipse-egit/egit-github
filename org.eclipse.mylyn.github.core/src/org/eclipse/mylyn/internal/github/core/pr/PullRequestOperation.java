/******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *****************************************************************************/
package org.eclipse.mylyn.internal.github.core.pr;

/**
 * Enumeration of task operations
 */
public enum PullRequestOperation {

	/**
	 * LEAD
	 */
	LEAVE("Leave "),

	/**
	 * REOPEN
	 */
	REOPEN("Reopen"),

	/**
	 * CLOSE
	 */
	CLOSE("Close");

	private final String label;

	private PullRequestOperation(String label) {
		this.label = label;
	}

	/**
	 * Get label
	 * 
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Get id
	 * 
	 * @return id
	 */
	public String getId() {
		return name();
	}

	/**
	 * get the operation by its id
	 * 
	 * @param opId
	 *            the id, or null
	 * @return the operation, or null if the id was null or did not match any
	 *         operation
	 */
	public static PullRequestOperation fromId(final String opId) {
		for (PullRequestOperation op : values())
			if (op.getId().equals(opId))
				return op;
		return null;
	}

}
