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

package org.sofproject.topo.ui.models;

import org.sofproject.topo.ui.parts.TopoNodePart;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class TopoItemCreationModel {

	public enum Type {
		None, Node, Connection
	};

	private ObjectProperty<Type> typeProperty = new SimpleObjectProperty<TopoItemCreationModel.Type>(Type.None);
	private ObjectProperty<String> objectIdProperty = new SimpleObjectProperty<>();
	private ObjectProperty<TopoNodePart> sourceProperty = new SimpleObjectProperty<>();

	public ObjectProperty<Type> getTypeProperty() {
		return typeProperty;
	}

	public Type getType() {
		return typeProperty.getValue();
	}

	public void setType(Type type) {
		this.typeProperty.setValue(type);
	}

	public ObjectProperty<String> getObjectIdProperty() {
		return objectIdProperty;
	}

	public String getObjectId() {
		return objectIdProperty.getValue();
	}

	public void setObjectId(String objectId) {
		this.objectIdProperty.setValue(objectId);
	}

	public ObjectProperty<TopoNodePart> getSourceProperty() {
		return sourceProperty;
	}

	public TopoNodePart getSource() {
		return sourceProperty.getValue();
	}

	public void setSource(TopoNodePart source) {
		this.sourceProperty.setValue(source);
	}

}
