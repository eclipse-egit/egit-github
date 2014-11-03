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

}
