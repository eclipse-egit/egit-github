/******************************************************************************
 *  Copyright (c) 2015 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Michael Mathews - Team Membership API implementation
 *****************************************************************************/
package org.eclipse.egit.github.core;

import java.io.Serializable;

/**
 * Team Membership model class.
 */
public class TeamMembership implements Serializable {

	private static final long serialVersionUID = -8207728181588115431L;

	private TeamMembershipState state;

	private String url;

	/**
	 * @return state
	 */
	public TeamMembershipState getState() {
		return state;
	}

	/**
	 * @param state
	 */
	public void setState(TeamMembershipState state) {
		this.state = state;
	}

	/**
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public static enum TeamMembershipState {
		ACTIVE, PENDING;
	}
}
