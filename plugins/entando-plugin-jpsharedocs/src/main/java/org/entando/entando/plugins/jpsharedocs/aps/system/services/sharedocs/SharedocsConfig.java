/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpsharedocs.aps.system.services.sharedocs;

import org.json.JSONObject;
import org.json.XML;

public class SharedocsConfig implements Cloneable {
	
	public SharedocsConfig() { }
	
	public SharedocsConfig(String path, boolean deleteOnRemove, boolean deleteResources)
	{
		this._tmpFolderPath = path;
		this._deleteOnRemove = deleteOnRemove;
		this._deleteResources = deleteResources;
	}
	
	/*
	 *	<?xml version="1.0" encoding="UTF-8"?>
	 *	<config>
	 *	   <removal>
	 *	      <attachmentDeletion>true</attachmentDeletion>
	 *	      <deletion>false</deletion>
	 *	   </removal>
	 *	   <tmpWorkingPath>/tmp/path</tmpWorkingPath>
	 *	</config>
	*/
	public SharedocsConfig(String xml) {
		JSONObject jxml = XML.toJSONObject(xml);
		JSONObject jobj = jxml.getJSONObject(XML_ROOT);
		JSONObject removal = jobj.getJSONObject(CONFIG_REMOVE);
		boolean deleteOnRemove = removal.getBoolean(CONFIG_DELETION);
		boolean deleteResources = removal.getBoolean(CONFIG_ATTACHMENT_DELETION);
		String tmpPath = jobj.getString(CONFIG_TMP_PATH);

		this.setDeleteOnRemove(deleteOnRemove);
		this.setDeleteResources(deleteResources);
		this.setTmpFolderPath(tmpPath);
	}
	
	public JSONObject toJSON() {
		JSONObject jobj = new JSONObject();
		JSONObject removal = new JSONObject();

		jobj.put(CONFIG_TMP_PATH, this.getTmpFolderPath());
		removal.put(CONFIG_DELETION, this.isDeleteOnRemove());
		removal.put(CONFIG_ATTACHMENT_DELETION, this.isDeleteResources());
		jobj.put(CONFIG_REMOVE, removal);
		return jobj;
	}
	
	public String toXML() {
		JSONObject jobj = toJSON();
		return XML.toString(jobj, XML_ROOT);
	}
	
	@Override
	public SharedocsConfig clone() {
		SharedocsConfig config = new SharedocsConfig(this.toXML());
		return config;
	}
	
	public String getTmpFolderPath() {
		return _tmpFolderPath;
	}
	public void setTmpFolderPath(String tmpFolderPath) {
		this._tmpFolderPath = tmpFolderPath;
	}
	public boolean isDeleteOnRemove() {
		return _deleteOnRemove;
	}
	public void setDeleteOnRemove(boolean deleteOnRemove) {
		this._deleteOnRemove = deleteOnRemove;
	}
	public boolean isDeleteResources() {
		return _deleteResources;
	}
	public void setDeleteResources(boolean deleteResources) {
		this._deleteResources = deleteResources;
	}
	
	private String _tmpFolderPath;
	// Delete a shared document content from the system rather than un-publishing it; default = false;
	private boolean _deleteOnRemove;
	// Toggles the deletion of attachments from a removed shared content
	private boolean _deleteResources;
	
	public final String XML_ROOT = "config";
	public final String CONFIG_TMP_PATH = "tmpWorkingPath";
	public final String CONFIG_DELETION = "deletion";
	public final String CONFIG_ATTACHMENT_DELETION = "attachmentDeletion";
	public final String CONFIG_REMOVE = "removal";
}
