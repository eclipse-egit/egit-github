/******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *****************************************************************************/
package org.eclipse.egit.github.core.client;

import java.io.InputStream;

/**
 * Multi-part part that is a fixed size input stream
 */
public class SizedInputStreamPart {

	private InputStream stream;

	private final long size;

	/**
	 * Create new part
	 *
	 * @param stream
	 * @param size
	 */
	public SizedInputStreamPart(InputStream stream, long size) {
		this.stream = stream;
		this.size = size;
	}

	/**
	 * Get stream
	 *
	 * @return stream
	 */
	public InputStream getStream() {
		return stream;
	}

	/**
	 * Get size
	 *
	 * @return size
	 */
	public long getSize() {
		return size;
	}
}
