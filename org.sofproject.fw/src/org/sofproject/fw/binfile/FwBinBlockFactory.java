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

package org.sofproject.fw.binfile;

import java.nio.ByteBuffer;

import org.sofproject.core.binfile.BinItem;
import org.sofproject.core.binfile.IBinItemFactory;

public class FwBinBlockFactory implements IBinItemFactory {

	private BinStructExtManifest extManifest = null;
	private BinStructCseManifest cseManifest = null;
	private BinStructFwManifest fwManifest = null;
	
	int extManifestSize = 0;
	int cseManifestSize = 0; 
	
	@Override
	public BinItem create(ByteBuffer bb) {
		if (extManifest == null) {
			extManifest = new BinStructExtManifest();
			try {
				extManifest.read(bb);
				extManifestSize = extManifest.getSize();
				return extManifest;
			} catch (RuntimeException e) {
				// no extended manifest. keep the object allocated
				// to avoid reading it again and fallback to the Cse Manifest
			}
		}
		if (cseManifest == null) {
			cseManifest = new BinStructCseManifest();
			cseManifest.read(bb);
			return cseManifest;
		}
		if (fwManifest == null) {
			fwManifest = new BinStructFwManifest();
			bb.position(extManifestSize + 0x2000);
			fwManifest.read(bb);
			return fwManifest;
		}
		return null;
	}

	public void dispose() {
		extManifest = null;
		cseManifest = null;
		fwManifest = null;
	}
	
}
