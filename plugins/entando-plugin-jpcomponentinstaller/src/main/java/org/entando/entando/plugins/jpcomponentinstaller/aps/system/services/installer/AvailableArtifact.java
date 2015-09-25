/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpcomponentinstaller.aps.system.services.installer;

import java.io.Serializable;

/**
 * @author E.Santoboni
 */
public class AvailableArtifact implements Serializable {
	
	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
		this._id = id;
	}
	
	public String getLabel() {
		return _label;
	}
	public void setLabel(String label) {
		this._label = label;
	}
	
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	
	public String getGroupId() {
		return _groupId;
	}
	public void setGroupId(String groupId) {
		this._groupId = groupId;
	}
	
	public String getArtifactId() {
		return _artifactId;
	}
	public void setArtifactId(String artifactId) {
		this._artifactId = artifactId;
	}
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	@Override
	protected Object clone() {
		AvailableArtifact clone = new AvailableArtifact();
		clone.setArtifactId(this.getArtifactId());
		clone.setDescription(this.getDescription());
		clone.setGroupId(this.getGroupId());
		clone.setId(this.getId());
		clone.setLabel(this.getLabel());
		clone.setType(this.getType());
		return clone;
	}
	
	private Integer _id;
	private String _label;
	private String _description;
	private String _groupId;
	private String _artifactId;
	private Type type;
	
	public enum Type {PLUGIN, BUNDLE, APP};
	
}