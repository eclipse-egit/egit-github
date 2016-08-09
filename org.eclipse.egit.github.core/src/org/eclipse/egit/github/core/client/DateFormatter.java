/*******************************************************************************
 *  Copyright (c) 2011 GitHub Inc. and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *    Vladislav Rassokhin (JetBrains, s.r.o.) - support timestamp in seconds
 *******************************************************************************/
package org.eclipse.egit.github.core.client;

import static org.eclipse.egit.github.core.client.IGitHubConstants.DATE_FORMAT;
import static org.eclipse.egit.github.core.client.IGitHubConstants.DATE_FORMAT_V2_1;
import static org.eclipse.egit.github.core.client.IGitHubConstants.DATE_FORMAT_V2_2;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Formatter for date formats present in the GitHub v2 and v3 API.
 */
public class DateFormatter implements JsonDeserializer<Date>,
		JsonSerializer<Date> {

	private final DateFormat[] formats;

	/**
	 * Create date formatter
	 */
	public DateFormatter() {
		formats = new DateFormat[3];
		formats[0] = new SimpleDateFormat(DATE_FORMAT);
		formats[1] = new SimpleDateFormat(DATE_FORMAT_V2_1);
		formats[2] = new SimpleDateFormat(DATE_FORMAT_V2_2);
		final TimeZone timeZone = TimeZone.getTimeZone("Zulu"); //$NON-NLS-1$
		for (DateFormat format : formats)
			format.setTimeZone(timeZone);
	}

	public Date deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		if (json instanceof JsonPrimitive) {
			final JsonPrimitive primitive = (JsonPrimitive) json;
			if (primitive.isNumber()) {
				return getDateFromTimestamp(json.getAsLong());
			} else if (primitive.isString()) {
				JsonParseException exception = null;
				final String value = json.getAsString();
				for (DateFormat format : formats)
					try {
						synchronized (format) {
							return format.parse(value);
						}
					} catch (ParseException e) {
						exception = new JsonParseException(e);
					}
				try {
					return getDateFromTimestamp(Long.parseLong(value));
				} catch (NumberFormatException e) {
					if (exception != null) {
						throw exception;
					}
					throw e;
				}
			}
		}
		throw new JsonParseException("Expected either string or number primitive");
	}

	public JsonElement serialize(Date date, Type type,
			JsonSerializationContext context) {
		final DateFormat primary = formats[0];
		String formatted;
		synchronized (primary) {
			formatted = primary.format(date);
		}
		return new JsonPrimitive(formatted);
	}

	private static Date getDateFromTimestamp(long timestamp) {
		if (timestamp > 10000000000L) {
			return new Date(timestamp);
		}
		return new Date(timestamp * 1000);
	}
}
