/*******************************************************************************
 * Copyright (c) 2011 Red Hat and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Christian Trutz <christian.trutz@gmail.com> - initial contribution
 *******************************************************************************/
package org.eclipse.mylyn.github.internal;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for {@link GitHubService#searchIssues(String, String, String, String)}.
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class GitHubService_searchIssues_Test {

	@Mock
	private HttpClient httpClient;

	@Mock
	private GetMethod httpGetMethod;

	@InjectMocks
	@SuppressWarnings("restriction")
	GitHubService gitHubService = new GitHubService();

	@Before
	public void before() throws IOException {
		when(httpClient.executeMethod(any(GetMethod.class))).thenReturn(
				HttpStatus.SC_OK);
	}

	@Test
	@SuppressWarnings("restriction")
	public void valid_NoIssues() throws GitHubServiceException, IOException {
		when(httpGetMethod.getResponseBody()).thenReturn(
				"{\"issues\":[]}".getBytes());
		GitHubIssues issues = gitHubService.searchIssues("testuser",
				"testrepo", "open", "", new GitHubCredentials("testuser", "testpw"));
		Assert.assertEquals(0, issues.getIssues().length);
	}

}
