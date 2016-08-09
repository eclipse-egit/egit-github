/*******************************************************************************
 *  Copyright (c) 2016 JetBrains, s.r.o.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Vladislav Rassokhin (JetBrains, s.r.o.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.egit.github.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GsonUtils;
import org.eclipse.egit.github.core.client.WebhookPayloadFormatter;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.FollowPayload;
import org.eclipse.egit.github.core.event.PingWebhookPayload;
import org.eclipse.egit.github.core.event.PushWebhookPayload;
import org.junit.Test;

/**
 * Unit tests of {@link WebhookPayloadFormatter}
 */
public class WebhookPayloadFormatterTest {

	/**
	 * 'push' payload returned as {@link PushWebhookPayload}
	 */
	@Test
	public void webhookPushPayload() {
		EventPayload payload = WebhookPayloadFormatter.deserialize(new JsonObject(), WebhookPayloadFormatter.TYPE_PUSH);
		assertNotNull(payload);
		assertEquals(PushWebhookPayload.class, payload.getClass());
	}

	/**
	 * 'ping' payload returned as {@link PingWebhookPayload}
	 */
	@Test
	public void webhookPingPayload() {
		EventPayload payload = WebhookPayloadFormatter.deserialize(new JsonObject(), WebhookPayloadFormatter.TYPE_PING);
		assertNotNull(payload);
		assertEquals(PingWebhookPayload.class, payload.getClass());
	}

	/**
	 * 'follow' payload returned as {@link FollowPayload}
	 */
	@Test
	public void followPayload() {
		EventPayload payload = WebhookPayloadFormatter.deserialize(new JsonObject(), "follow");
		assertNotNull(payload);
		assertEquals(FollowPayload.class, payload.getClass());
	}

	/**
	 * Exception thrown for unsupported payload
	 */
	@Test(expected = IllegalArgumentException.class)
	public void unsupportedPayload() {
		WebhookPayloadFormatter.deserialize(new JsonObject(), "unsupported_payload_type");
	}

	/**
	 * Only JsonObjects expected as payload
	 */
	@Test(expected = JsonParseException.class)
	public void nonJsonObjectPassed() {
		WebhookPayloadFormatter.deserialize(new JsonArray(), "ping");
	}

	@Test
	public void deserializeRealPushPayload() throws Exception {

	}

	@Test
	public void deserializeRealPingPayload() throws Exception {
		InputStream stream = this.getClass().getResourceAsStream("/data/webhook-push-payload-example.json");
		assertNotNull(stream);
		JsonElement json;
		try {
			json = GsonUtils.fromJson(new InputStreamReader(stream), JsonElement.class);
		} finally {
			stream.close();
		}
		assertNotNull(json);
		EventPayload payload = WebhookPayloadFormatter.deserialize(json, "push");
		assertNotNull(payload);
		assertEquals(PushWebhookPayload.class, payload.getClass());

		PushWebhookPayload push = (PushWebhookPayload) payload;

		assertEquals("refs/heads/changes", push.getRef());

		assertNotNull(push.getCommits());
		assertEquals(1, push.getCommits().size());

		Repository repository = push.getRepository();
		assertNotNull(repository);
		assertEquals("public-repo", repository.getName());
		assertEquals(1430869212000L, repository.getCreatedAt().getTime());

	}
}
