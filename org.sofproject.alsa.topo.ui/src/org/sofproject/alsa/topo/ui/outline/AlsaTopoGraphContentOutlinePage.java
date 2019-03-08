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

package org.sofproject.alsa.topo.ui.outline;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.sofproject.alsa.topo.model.AlsaTopoConnection;
import org.sofproject.alsa.topo.model.AlsaTopoGraph;
import org.sofproject.alsa.topo.model.AlsaTopoNode;
import org.sofproject.alsa.topo.ui.editor.AlsaTopoEditor;

public class AlsaTopoGraphContentOutlinePage extends ContentOutlinePage {
	AlsaTopoGraph topoModel;

	public AlsaTopoGraphContentOutlinePage(AlsaTopoEditor editor) {
		topoModel = editor.getTopoModel();
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new AlsaTopoGraphContentProvider());
		viewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new AlsaTopoGraphLabelProvider()));
		viewer.addSelectionChangedListener(this);
		viewer.setInput(topoModel);
	}

	class AlsaTopoGraphLabelProvider extends LabelProvider implements IStyledLabelProvider {

		@Override
		public StyledString getStyledText(Object element) {
			if (element instanceof AlsaTopoNode) {
				AlsaTopoNode node = (AlsaTopoNode) element;
				StyledString s = new StyledString(node.getName());
				s.append(" (" + node.getTypeName() + ")", StyledString.DECORATIONS_STYLER);
				return s;
			} else if (element instanceof AlsaTopoConnection) {
				AlsaTopoConnection conn = (AlsaTopoConnection) element;
				return new StyledString(conn.toString(), StyledString.COUNTER_STYLER);
			}
			// TODO Auto-generated method stub
			return new StyledString("?");
		}

	}
}