/*
 * Copyright (c) 2019, Intel Corporation
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

package org.sofproject.topo.ui.graph;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.gef.graph.Node;
import org.sofproject.core.binfile.BinStruct;
import org.sofproject.ui.editor.IBinStructHolder;

/**
 * Connects Gef graph nodes domain with the ITopoNode interface. Visuals may use
 * the ITopoNode interface, obtained from getTopoModelNode(), directly to query
 * visual attributes.
 */
public class GefTopoNode extends Node implements IBinStructHolder {

	private ITopoNode topoModelNode;

	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	public static final String PROP_NODE_NAME = "node-name";

	public GefTopoNode(ITopoNode topoModelNode) {
		this.topoModelNode = topoModelNode;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	public ITopoNode getTopoModelNode() {
		return topoModelNode;
	}

	@Override
	public BinStruct getBinStruct() {
		return topoModelNode.getBinStruct();
	}

	public void setName(String newName) {
		String oldName = topoModelNode.getName();
		topoModelNode.setName(newName);
		pcs.firePropertyChange(PROP_NODE_NAME, oldName, newName);
	}
}
