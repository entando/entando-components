/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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