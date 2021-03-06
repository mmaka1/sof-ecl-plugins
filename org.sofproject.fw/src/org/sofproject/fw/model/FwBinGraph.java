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

package org.sofproject.fw.model;

import java.util.ArrayList;
import java.util.List;

import org.sofproject.core.binfile.BinArray;
import org.sofproject.core.binfile.BinFile;
import org.sofproject.core.binfile.BinInteger;
import org.sofproject.core.binfile.BinItem;
import org.sofproject.core.binfile.BinStruct;
import org.sofproject.fw.binfile.BinStructCseManifestHeader;
import org.sofproject.fw.binfile.BinStructCssManifestHeader;
import org.sofproject.fw.binfile.BinStructFwBinaryHeader;
import org.sofproject.fw.binfile.BinStructFwModuleEntry;
import org.sofproject.fw.binfile.BinStructFwVersion;
import org.sofproject.fw.binfile.BinStructSegmentDesc;
import org.sofproject.fw.keys.SimpleFwKeyResolver;
import org.sofproject.fw.memmap.DspMemoryMap;

public class FwBinGraph {

	// fw memory map node is standalone and located in [0,0]
	public static final int FW_MEM_MAP_ROW = 0;
	public static final int FW_MEM_MAP_COLUMN = 0;

	// bin tree begins from [0,1]
	private static final int FIRST_GRAPH_ROW = 1;
	private static final int FIRST_GRAPH_COLUMN = 0;

	private List<FwBinItem> childElements = new ArrayList<>();

	private BinFile fwBin;
	private DspMemoryMap memMap;

	public FwBinGraph(BinFile fwBin, DspMemoryMap memMap) {
		this.fwBin = fwBin;
		this.memMap = memMap;
		if (memMap != null) {
			createFwMemoryMap();
		}
		populateGraph();
	}

	public BinFile getBinFile() {
		return fwBin;
	}

	public DspMemoryMap getMemoryMap() {
		return memMap;
	}

	public void addChildElement(FwBinBlock parentNode, FwBinBlock node) {
		childElements.add(node);
		if (parentNode != null) {
			childElements.add(new FwBinConnection(parentNode, node));
		}
	}

	public void addChildElement(FwBinItem node, int idx) {
		childElements.add(idx, node);
	}

	public List<FwBinItem> getChildElements() {
		return childElements;
	}

	private void createFwMemoryMap() {
		// TODO:
		FwMemoryMap fwMemMap = new FwMemoryMap(memMap, FW_MEM_MAP_ROW, FW_MEM_MAP_COLUMN);
		childElements.add(fwMemMap);
	}

	private void populateGraph() {

		FwBinBlock rootNode = new FwBinBlock(fwBin.getName(), new BinStruct(fwBin.getName()), FIRST_GRAPH_ROW,
				FIRST_GRAPH_COLUMN);

		addChildElement(null, rootNode);

		// add children starting from row 0 and column 1
		addElementChildren(rootNode, fwBin.getChildItems(), false, FIRST_GRAPH_ROW, FIRST_GRAPH_COLUMN + 1);
	}

	private static FwBinBlock createBinBlock(BinStruct binFileItem, int row, int column) {
		FwBinBlock binBlock = new FwBinBlock(binFileItem.getName(), binFileItem, row, column);
		// TODO: split the code by types derived from FwBinBlock
		if (binFileItem instanceof BinStructCseManifestHeader) {
			addCseManifestHeaderAttribs(binBlock, (BinStructCseManifestHeader) binFileItem);
		} else if (binFileItem instanceof BinStructCssManifestHeader) {
			addCssManifestHeaderAttribs(binBlock, (BinStructCssManifestHeader) binFileItem);
		} else if (binFileItem instanceof BinStructFwVersion) {
			addFwVersionAttribs(binBlock, (BinStructFwVersion) binFileItem);
		} else if (binFileItem instanceof BinStructFwBinaryHeader) {
			addFwBinaryHeaderAttribs(binBlock, (BinStructFwBinaryHeader) binFileItem);
		} else if (binFileItem instanceof BinStructFwModuleEntry) {
			addFwModuleEntryAttribs(binBlock, (BinStructFwModuleEntry) binFileItem);
		}
		return binBlock;
	}

	private static void addCseManifestHeaderAttribs(FwBinBlock binBlock, BinStructCseManifestHeader cseManifestHeader) {
		binBlock.setAttribute(FwBinBlock.AG_GRAPH, "# modules", cseManifestHeader.getChildValue("number_of_modules"));
		binBlock.setAttribute(FwBinBlock.AG_GRAPH, "version", cseManifestHeader.getChildItem("ver").getValueString());
	}

	@SuppressWarnings("unchecked")
	private static void addCssManifestHeaderAttribs(FwBinBlock binBlock, BinStructCssManifestHeader hdr) {
		binBlock.setAttribute(FwBinBlock.AG_GRAPH, "type", hdr.getChildValue("type"));
		binBlock.setAttribute(FwBinBlock.AG_GRAPH, "version", hdr.getChildItem("ver").getValueString());
		binBlock.setAttribute(FwBinBlock.AG_GRAPH, "date", hdr.getChildItem("date").getValueString());
		binBlock.setAttribute(FwBinBlock.AG_GRAPH, "signing key",
				SimpleFwKeyResolver.searchModulusName((byte[]) hdr.getChildValue("modulus")));
	}

	private static void addFwVersionAttribs(FwBinBlock binBlock, BinStructFwVersion fwVer) {
		binBlock.setAttribute(FwBinBlock.AG_GRAPH, "ver", String.format("%d.%d.%d.%d", fwVer.getChildValue("major"),
				fwVer.getChildValue("minor"), fwVer.getChildValue("hotfix"), fwVer.getChildValue("build")));
	}

	private static void addFwBinaryHeaderAttribs(FwBinBlock binBlock, BinStructFwBinaryHeader hdr) {
		binBlock.setAttribute(FwBinBlock.AG_GRAPH, "# preload pages", hdr.getChildValue("preload_page_count"));
		binBlock.setAttribute(FwBinBlock.AG_GRAPH, "ver",
				String.format("%d.%d.%d.%d", hdr.getChildValue("major_version"), hdr.getChildValue("minor_version"),
						hdr.getChildValue("hotfix_version"), hdr.getChildValue("build_version")));
		binBlock.setAttribute(FwBinBlock.AG_GRAPH, "load offset", hdr.getChildItem("load_offset").getValueString());
	}

	private static void addFwModuleEntryAttribs(FwBinBlock binBlock, BinStructFwModuleEntry modEntry) {
		binBlock.setAttribute(FwBinBlock.AG_GRAPH, "name", modEntry.getChildValue("name"));
		binBlock.setAttribute(FwBinBlock.AG_GRAPH, "entry point",
				modEntry.getChildItem("entry_point").getValueString());
		BinArray<BinStructSegmentDesc> segments = (BinArray<BinStructSegmentDesc>) modEntry.getChildItem("segments");
		for (int i = 0; i < segments.getItems().size(); i++) {
			binBlock.setAttribute(FwBinBlock.AG_GRAPH, segmentIndexToString(i), segmentToString(segments.getItem(i)));
		}
	}

	public static String segmentIndexToString(int index) {
		switch (index) {
		case 0:
			return ".text";
		case 1:
			return ".rodata";
		case 2:
			return ".bss";
		default:
			return "unknown";
		}
	}

	private static String segmentToString(BinStructSegmentDesc seg) {
		int pages = (Integer) ((BinInteger) seg.getChildItem("flags")).getChildValue("length");
		String s = String.format("%d pag%s at %s", pages, pages != 1 ? "es" : "e",
				seg.getChildItem("v_base_addr").getValueString());
		return s;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int addElementChildren(FwBinBlock parentNode, List<BinItem> children, boolean oneRow, int row, int column) {
		int begRow = row;
		boolean showConnection = true;
		for (BinItem child : children) {
			if (child instanceof BinStruct && !child.getName().equals("extension_header")) {
				FwBinBlock childNode = createBinBlock((BinStruct) child, row, column);
				addChildElement(showConnection ? parentNode : null, childNode);
				if (oneRow) {
					++column;
					showConnection = false; // do not show more connections to the same row
				} else {
					row = addElementChildren(childNode, ((BinStruct) child).getChildItems(), false, row, column + 1);
				}
			} else if (!oneRow && child instanceof BinArray) {
				row = addElementChildren(parentNode, ((BinArray) child).getItems(), true, row, column);
			}
		}
		if (begRow == row)
			++row;
		return row;
	}
}
