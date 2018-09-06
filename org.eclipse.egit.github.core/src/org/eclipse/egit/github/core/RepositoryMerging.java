package org.eclipse.egit.github.core;

import java.io.Serializable;

/**
 * Repository merging model class
 */
public class RepositoryMerging implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 6179934015396875505L;

	private String base;

	private String head;

	private String commitMessage;

	/**
	 * @return base
	 */
	public String getBase() {
		return base;
	}

	/**
	 * @param base
	 * @return this merge
	 */
	public RepositoryMerging setBase(String base) {
		this.base = base;
		return this;
	}

	/**
	 * @return head
	 */
	public String getHead() {
		return head;
	}

	/**
	 * @param head
	 * @return this merge
	 */
	public RepositoryMerging setHead(String head) {
		this.head = head;
		return this;
	}

	/**
	 * @return commitMessage
	 */
	public String getCommitMessage() {
		return commitMessage;
	}

	/**
	 * @param commitMessage
	 * @return this merge
	 */
	public RepositoryMerging setCommitMessage(String commitMessage) {
		this.commitMessage = commitMessage;
		return this;
	}

}