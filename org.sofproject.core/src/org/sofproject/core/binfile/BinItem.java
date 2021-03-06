/*
 * Copyright (c) 2018, Intel Corporation
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of the Intel Corporation nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.sofproject.core.binfile;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public abstract class BinItem {

	private String name;
	private int offset = -1;
	private BinItem parent;

	public BinItem() {
		this("");
	}

	public BinItem(String name) {
		this.name = name;
		this.parent = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BinItem getParent() {
		return parent;
	}

	public void setParent(BinItem parent) {
		this.parent = parent;
	}

	public List<BinItem> getFullPath() {
		LinkedList<BinItem> path = new LinkedList<>();
		BinItem item = this;
		do {
			path.addFirst(item);
		} while ((item = item.getParent()) != null);
		return path;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public BinItem read(ByteBuffer bb) {
		offset = bb.position();
		return this;
	}

	public abstract Object getValue();

	public abstract String getValueString();

	/**
	 * Retrieves the value as obtained from the binary, with no translation. For
	 * example, enums may override this to return integer value, not mapped to enum
	 * object.
	 *
	 * @return Raw value.
	 */
	public Object getRawValue() {
		return getValue();
	}
}
