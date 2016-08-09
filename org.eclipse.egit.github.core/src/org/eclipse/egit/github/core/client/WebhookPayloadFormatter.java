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
package org.eclipse.egit.github.core.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.PingWebhookPayload;
import org.eclipse.egit.github.core.event.PushWebhookPayload;

/**
 * Formats an webhook's payload with the appropriate class given a certain
 * payload type from 'X-GitHub-Event' header
 */
public class WebhookPayloadFormatter {
	/**
	 * Payload type denoting a {@link PushWebhookPayload}
	 */
	public static final String TYPE_PUSH = "push";

	/**
	 * Payload type denoting a {@link PingWebhookPayload}
	 */
	public static final String TYPE_PING = "ping";

	private static Map<String, String> ourWebhookTypeToEventTypeMap = new HashMap<String, String>();

	static {
		// Note that 'push' not mapped to Event.TYPE_PUSH and handled in special way
		ourWebhookTypeToEventTypeMap.put("commit_comment", Event.TYPE_COMMIT_COMMENT);
		ourWebhookTypeToEventTypeMap.put("create", Event.TYPE_CREATE);
		ourWebhookTypeToEventTypeMap.put("delete", Event.TYPE_DELETE);
		ourWebhookTypeToEventTypeMap.put("download", Event.TYPE_DOWNLOAD);
		ourWebhookTypeToEventTypeMap.put("follow", Event.TYPE_FOLLOW);
		ourWebhookTypeToEventTypeMap.put("fork", Event.TYPE_FORK);
		ourWebhookTypeToEventTypeMap.put("fork_apply", Event.TYPE_FORK_APPLY);
		ourWebhookTypeToEventTypeMap.put("gist", Event.TYPE_GIST);
		ourWebhookTypeToEventTypeMap.put("gollum", Event.TYPE_GOLLUM);
		ourWebhookTypeToEventTypeMap.put("issue_comment", Event.TYPE_ISSUE_COMMENT);
		ourWebhookTypeToEventTypeMap.put("issues", Event.TYPE_ISSUES);
		ourWebhookTypeToEventTypeMap.put("member", Event.TYPE_MEMBER);
		ourWebhookTypeToEventTypeMap.put("public", Event.TYPE_PUBLIC);
		ourWebhookTypeToEventTypeMap.put("pull_request", Event.TYPE_PULL_REQUEST);
		ourWebhookTypeToEventTypeMap.put("pull_request_review_comment", Event.TYPE_PULL_REQUEST_REVIEW_COMMENT);
		ourWebhookTypeToEventTypeMap.put("release", Event.TYPE_RELEASE);
		ourWebhookTypeToEventTypeMap.put("team_add", Event.TYPE_TEAM_ADD);
		ourWebhookTypeToEventTypeMap.put("watch", Event.TYPE_WATCH);
	}

	public static boolean isSupportedPayloadType(String type) {
		return TYPE_PUSH.equals(type) || TYPE_PING.equals(type) || ourWebhookTypeToEventTypeMap.containsKey(type);
	}

	public static EventPayload deserialize(JsonElement json, String type) throws JsonParseException {
		if (!json.isJsonObject())
			throw new JsonParseException("Excepted json object");
		if (!isSupportedPayloadType(type))
			throw new IllegalArgumentException("Unsupported webhook payload type: " + type);


		Class<? extends EventPayload> payloadClass;
		if (TYPE_PUSH.equals(type))
			payloadClass = PushWebhookPayload.class;
		else if (TYPE_PING.equals(type))
			payloadClass = PingWebhookPayload.class;
		else if (ourWebhookTypeToEventTypeMap.containsKey(type))
			payloadClass = EventFormatter.getPayloadClass(ourWebhookTypeToEventTypeMap.get(type));
		else
			throw new IllegalStateException();

		return GsonUtils.getGson().fromJson(json, payloadClass);
	}
}
