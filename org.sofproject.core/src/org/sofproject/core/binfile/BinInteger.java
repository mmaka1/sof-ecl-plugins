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

public class BinInteger extends BinContainer {

	private int value;
	private boolean hexDispOnly;

	public BinInteger(String name) {
		this(name, false);
	}

	public BinInteger(String name, boolean hexDispOnly) {
		super(name);
		this.hexDispOnly = hexDispOnly;
	}

	@Override
	public BinItem read(ByteBuffer bb) {
		super.read(bb);
		value = bb.getInt();
		// pass value (and file offset) to the child bin fields if any
		for (BinItem item : children) {
			item.setOffset(getOffset());
			if (item instanceof BinBitField) {
				((BinBitField) item).setValue(value);
			}
		}
		return this;
	}

	public Integer getValue() {
		return new Integer(value);
	}

	@Override
	public String getValueString() {
		if (hexDispOnly)
			return String.format("0x%08x", value);
		else
			return String.format("%d (0x%x)", value, value);
	}

	@Override
	public String toString() {
		return String.format("int %s : %d (0x%x)", getName(), value, value);
	}

}
