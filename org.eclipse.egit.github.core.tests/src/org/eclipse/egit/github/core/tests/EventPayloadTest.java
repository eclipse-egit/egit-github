/*******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.egit.github.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Download;
import org.eclipse.egit.github.core.EventPayload;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Page;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.Team;
import org.eclipse.egit.github.core.User;
import org.junit.Test;

/**
 * Unit tests of {@link EventPayload}
 */
public class EventPayloadTest {

	/**
	 * Test default state of event payload
	 */
	@Test
	public void defaultState() {
		EventPayload payload = new EventPayload();
		assertNull(payload.getAction());
		assertNull(payload.getAfter());
		assertNull(payload.getBefore());
		assertNull(payload.getComment());
		assertNull(payload.getCommits());
		assertNull(payload.getDescription());
		assertNull(payload.getDownload());
		assertNull(payload.getForkee());
		assertNull(payload.getGist());
		assertNull(payload.getHead());
		assertNull(payload.getIssue());
		assertNull(payload.getMasterBranch());
		assertNull(payload.getMember());
		assertEquals(0, payload.getNumber());
		assertNull(payload.getPages());
		assertNull(payload.getPullRequest());
		assertNull(payload.getRef());
		assertNull(payload.getRefType());
		assertNull(payload.getRepo());
		assertEquals(0, payload.getSize());
		assertNull(payload.getTarget());
		assertNull(payload.getTeam());
		assertNull(payload.getUser());
	}

	/**
	 * Test updating event payload fields
	 */
	@Test
	public void updateFields() {
		EventPayload payload = new EventPayload();
		assertEquals("create", payload.setAction("create").getAction());
		assertEquals("000", payload.setAfter("000").getAfter());
		assertEquals("000", payload.setBefore("000").getBefore());
		Comment comment = new Comment().setBody("comment");
		assertEquals(comment, payload.setComment(comment).getComment());
		assertEquals(new ArrayList<Commit>(),
				payload.setCommits(new ArrayList<Commit>()).getCommits());
		assertEquals("description",
				payload.setDescription("description").getDescription());
		Download download = new Download().setName("download");
		assertEquals(download, payload.setDownload(download).getDownload());
		Repository forkee = new Repository().setName("forkee");
		assertEquals(forkee, payload.setForkee(forkee).getForkee());
		Gist gist = new Gist().setId("gist");
		assertEquals(gist, payload.setGist(gist).getGist());
		assertEquals("head", payload.setHead("head").getHead());
		Issue issue = new Issue().setTitle("issue");
		assertEquals(issue, payload.setIssue(issue).getIssue());
		assertEquals("master", payload.setMasterBranch("master").getMasterBranch());
		User member = new User().setName("member");
		assertEquals(member, payload.setMember(member).getMember());
		assertEquals(9000, payload.setNumber(9000).getNumber());
		assertEquals(new ArrayList<Page>(),
				payload.setPages(new ArrayList<Page>()).getPages());
		PullRequest pullRequest = new PullRequest().setTitle("pull request");
		assertEquals(pullRequest, payload.setPullRequest(pullRequest).getPullRequest());
		assertEquals("master", payload.setRef("master").getRef());
		assertEquals("repository", payload.setRefType("repository").getRefType());
		Repository repo = new Repository().setName("repo");
		assertEquals(repo, payload.setRepo(repo).getRepo());
		assertEquals(100, payload.setSize(100).getSize());
		User target = new User().setName("target");
		assertEquals(target, payload.setTarget(target).getTarget());
		Team team = new Team().setName("team");
		assertEquals(team, payload.setTeam(team).getTeam());
		User user = new User().setName("user");
		assertEquals(user, payload.setUser(user).getUser());
	}
}
