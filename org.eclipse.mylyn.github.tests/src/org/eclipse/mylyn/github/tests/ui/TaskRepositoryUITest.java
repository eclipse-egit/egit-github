/*******************************************************************************
 *  Copyright (c) 2011 Christian Trutz
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Christian Trutz - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.github.tests.ui;

import junit.framework.Assert;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * UI tests (SWTBot) concerning GitHub task repositories.
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class TaskRepositoryUITest {

	private static SWTWorkbenchBot bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
		bot.perspectiveByLabel("Planning").activate();
	}

	@Test
	public void githubTaskRepositories() {
		bot.menu("Window").menu("Show View").menu("Task Repositories").click();
		bot.viewByTitle("Task Repositories")
				.toolbarButton("Add Task Repository...").click();
		SWTBotShell shell = bot.shell("Add Task Repository");
		shell.activate();
		Assert.assertTrue(bot.table().indexOf("GitHub Issues") != -1);
		Assert.assertTrue(bot.table().indexOf("GitHub Gists") != -1);
		bot.button("Cancel").click();
	}

}
