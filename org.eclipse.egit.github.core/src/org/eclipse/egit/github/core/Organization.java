package org.eclipse.egit.github.core;

import java.io.Serializable;


public class Organization implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -747906610305335107L;

	private int id;

	private String login;

	private String description;

	private String url;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public Organization setId(int id) {
		this.id = id;
		return this;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public Organization setLogin(String login) {
		this.login = login;
		return this;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public Organization setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public Organization setUrl(String url) {
		this.url = url;
		return this;
	}

}
