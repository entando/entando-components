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
package org.entando.entando.plugins.jprssaggregator.aps.system.services.converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;

/**
 * This class stores the plugin's xml configuration 
 */
public class AggregatorConfig {

	public AggregatorConfig(Element element) {
		this.setContentType(element.getAttributeValue("contentType"));
		this.parseInsertOnline(element);
		this.setAttributes(element.getChild("attributes").getChildren("element"));
		this.setDescr(element.getChild("descr"));
		Element groups = element.getChild("groups");
		if (null != groups) {
			this.setMainGroup(groups.getAttributeValue("main"));
			List<Element> groupsElement = groups.getChildren();
			if (null != groupsElement && groupsElement.size() > 0) {
				Iterator<Element> it = groupsElement.iterator();
				while (it.hasNext()) {
					Element groupEl = it.next();
					this.getGroups().add(groupEl.getAttributeValue("name"));
				}
			}
		}
		Element apsAggregatorItem = element.getChild("apsAggregatorItem");
		if (null != apsAggregatorItem) {
			this.setApsAggregatorItems(apsAggregatorItem.getChildren());
		}
	}
	
	private void parseInsertOnline(Element element) {
		this.setInsertOnline(true);
		//TODO In attesa della ricerca sui contenuti offline.
		//Attribute attribute = element.getAttribute("insertOnLine");
		//if (null != attribute && attribute.getValue().equalsIgnoreCase("true")) {
			//this.setInsertOnline(true);
		//}
	}

	public String getLinkAttributeName() {
		String linkName = null;
		Iterator<Element> it = this.getAttributes().iterator();
		while (it.hasNext()) {
			Element element = it.next();
			if (element.getAttributeValue("source").equals("link")) {
				linkName = element.getAttributeValue("dest");
				break;
			}
		}
		return linkName;
	}
	
	/**
	 * @return the contentType that will contain the feed
	 */
	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	/**
	 * @return the list of attributes configuration for the congersion
	 */
	public List<Element> getAttributes() {
		return _attributes;
	}
	public void setAttributes(List<Element> attributes) {
		this._attributes = attributes;
	}
	
	/**
	 * Get the value for the description
	 * @return the mapping configuration for the content description
	 */
	public Element getDescr() {
		return _descr;
	}
	public void setDescr(Element descr) {
		this._descr = descr;
	}
	
	/**
	 * Get the code of the group to set as the content main group
	 * @return the code of the Group that will be the main group of the content
	 */
	public String getMainGroup() {
		return _mainGroup;
	}
	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}
	
	/**
	 * @return return a list of alternative groups for the content
	 */
	public List<String> getGroups() {
		return _groups;
	}
	public void setGroups(List<String> groups) {
		this._groups = groups;
	}
	
	/**
	 * When true insert online the content.
	 * @return
	 */
	public boolean isInsertOnline() {
		return _insertOnline;
	}
	public void setInsertOnline(boolean insertOnline) {
		this._insertOnline = insertOnline;
	}
	
	/**
	 * Mapping elements are used to assign values from the ApsAggregatorItem to the content
	 * @return return the list of mapping elements
	 */
	public List<Element> getApsAggregatorItems() {
		return _apsAggregatorItems;
	}
	public void setApsAggregatorItems(List<Element> apsAggregatorItems) {
		this._apsAggregatorItems = apsAggregatorItems;
	}

	private String _contentType;
	private List<Element> _attributes = new ArrayList<Element>();
	private Element _descr;
	private String _mainGroup;
	private boolean _insertOnline;
	private List<String> _groups = new ArrayList<String>();
	private List<Element> _apsAggregatorItems = new ArrayList<Element>();
}
