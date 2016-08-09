/*******************************************************************************
 *  Copyright (c) 2011, 2016 GitHub Inc. and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Jason Tsay (GitHub Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.egit.github.core.client;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
import static org.eclipse.egit.github.core.event.Event.TYPE_COMMIT_COMMENT;
import static org.eclipse.egit.github.core.event.Event.TYPE_CREATE;
import static org.eclipse.egit.github.core.event.Event.TYPE_DELETE;
import static org.eclipse.egit.github.core.event.Event.TYPE_DOWNLOAD;
import static org.eclipse.egit.github.core.event.Event.TYPE_FOLLOW;
import static org.eclipse.egit.github.core.event.Event.TYPE_FORK;
import static org.eclipse.egit.github.core.event.Event.TYPE_FORK_APPLY;
import static org.eclipse.egit.github.core.event.Event.TYPE_GIST;
import static org.eclipse.egit.github.core.event.Event.TYPE_GOLLUM;
import static org.eclipse.egit.github.core.event.Event.TYPE_ISSUES;
import static org.eclipse.egit.github.core.event.Event.TYPE_ISSUE_COMMENT;
import static org.eclipse.egit.github.core.event.Event.TYPE_MEMBER;
import static org.eclipse.egit.github.core.event.Event.TYPE_PUBLIC;
import static org.eclipse.egit.github.core.event.Event.TYPE_PULL_REQUEST;
import static org.eclipse.egit.github.core.event.Event.TYPE_PULL_REQUEST_REVIEW_COMMENT;
import static org.eclipse.egit.github.core.event.Event.TYPE_PUSH;
import static org.eclipse.egit.github.core.event.Event.TYPE_RELEASE;
import static org.eclipse.egit.github.core.event.Event.TYPE_TEAM_ADD;
import static org.eclipse.egit.github.core.event.Event.TYPE_WATCH;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

import org.eclipse.egit.github.core.event.CommitCommentPayload;
import org.eclipse.egit.github.core.event.CreatePayload;
import org.eclipse.egit.github.core.event.DeletePayload;
import org.eclipse.egit.github.core.event.DownloadPayload;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.FollowPayload;
import org.eclipse.egit.github.core.event.ForkApplyPayload;
import org.eclipse.egit.github.core.event.ForkPayload;
import org.eclipse.egit.github.core.event.GistPayload;
import org.eclipse.egit.github.core.event.GollumPayload;
import org.eclipse.egit.github.core.event.IssueCommentPayload;
import org.eclipse.egit.github.core.event.IssuesPayload;
import org.eclipse.egit.github.core.event.MemberPayload;
import org.eclipse.egit.github.core.event.PublicPayload;
import org.eclipse.egit.github.core.event.PullRequestPayload;
import org.eclipse.egit.github.core.event.PullRequestReviewCommentPayload;
import org.eclipse.egit.github.core.event.PushPayload;
import org.eclipse.egit.github.core.event.ReleasePayload;
import org.eclipse.egit.github.core.event.TeamAddPayload;
import org.eclipse.egit.github.core.event.WatchPayload;

/**
 * Formats an event's payload with the appropriate class given a certain event
 * type
 */
public class EventFormatter implements JsonDeserializer<Event> {

	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(Date.class, new DateFormatter())
			.setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();

	public Event deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		final Event event = gson.fromJson(json, Event.class);
		if (event == null || !json.isJsonObject())
			return event;
		final JsonElement rawPayload = json.getAsJsonObject().get("payload");
		if (rawPayload == null || !rawPayload.isJsonObject())
			return event;
		final String type = event.getType();
		if (type == null || type.length() == 0)
			return event;

		Class<? extends EventPayload> payloadClass = getPayloadClass(type);

		if (payloadClass == null)
			return event;

		try {
			EventPayload typedPayload = context.deserialize(rawPayload,
					payloadClass);
			return event.setPayload(typedPayload);
		} catch (JsonParseException jpe) {
			// Parse exception here denotes legacy payloads with differing
			// fields than built-in payload classes provide
			return event;
		}
	}

	public static Class<? extends EventPayload> getPayloadClass(String type) {
		if (type == null)
			return null;
		if (TYPE_COMMIT_COMMENT.equals(type))
			return CommitCommentPayload.class;
		else if (TYPE_CREATE.equals(type))
			return CreatePayload.class;
		else if (TYPE_DELETE.equals(type))
			return DeletePayload.class;
		else if (TYPE_DOWNLOAD.equals(type))
			return DownloadPayload.class;
		else if (TYPE_FOLLOW.equals(type))
			return FollowPayload.class;
		else if (TYPE_FORK.equals(type))
			return ForkPayload.class;
		else if (TYPE_FORK_APPLY.equals(type))
			return ForkApplyPayload.class;
		else if (TYPE_GIST.equals(type))
			return GistPayload.class;
		else if (TYPE_GOLLUM.equals(type))
			return GollumPayload.class;
		else if (TYPE_ISSUE_COMMENT.equals(type))
			return IssueCommentPayload.class;
		else if (TYPE_ISSUES.equals(type))
			return IssuesPayload.class;
		else if (TYPE_MEMBER.equals(type))
			return MemberPayload.class;
		else if (TYPE_PUBLIC.equals(type))
			return PublicPayload.class;
		else if (TYPE_PULL_REQUEST.equals(type))
			return PullRequestPayload.class;
		else if (TYPE_PULL_REQUEST_REVIEW_COMMENT.equals(type))
			return PullRequestReviewCommentPayload.class;
		else if (TYPE_PUSH.equals(type))
			return PushPayload.class;
		else if (TYPE_RELEASE.equals(type))
			return ReleasePayload.class;
		else if (TYPE_TEAM_ADD.equals(type))
			return TeamAddPayload.class;
		else if (TYPE_WATCH.equals(type))
			return WatchPayload.class;
		else
			return null;
	}
}
