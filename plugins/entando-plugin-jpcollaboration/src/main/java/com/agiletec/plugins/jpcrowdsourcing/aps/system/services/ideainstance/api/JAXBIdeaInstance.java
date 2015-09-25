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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IdeaInstance;

@XmlRootElement(name = "ideaInstance")
@XmlType(propOrder = {"code", "createdat", "groups", "children"})
public class JAXBIdeaInstance {

    public JAXBIdeaInstance() {
        super();
    }

    public JAXBIdeaInstance(IdeaInstance instance) {
		this.setCode(instance.getCode());
		this.setCreatedat(instance.getCreatedat());

		List<String> groups = new ArrayList<String>();
		if (null != instance.getGroups() && !instance.getGroups().isEmpty()) {
			groups = instance.getGroups();
		}
		this.setGroups(groups);

		List<Integer> children = new ArrayList<Integer>();
		if (null != instance.getChildren() && !instance.getChildren().isEmpty()) {
			children = instance.getChildren();
		}
		this.setChildren(children);

		this.setChildren(instance.getChildren());

    }
    
    public IdeaInstance getIdeaInstance() {
    	IdeaInstance instance = new IdeaInstance();
		instance.setCode(this.getCode());
		instance.setCreatedat(this.getCreatedat());
		instance.setChildren(this.getChildren());
		instance.setGroups(this.getGroups());
    	return instance;
    }

	@XmlElement(name = "code", required = true)
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}

	@XmlElement(name = "createdat", required = false)
	public Date getCreatedat() {
		return _createdat;
	}
	public void setCreatedat(Date createdat) {
		this._createdat = createdat;
	}

	@XmlElementWrapper(name="groups")
    @XmlElement(name="group")
	public List<String> getGroups() {
		return _groups;
	}
	public void setGroups(List<String> groups) {
		this._groups = groups;
	}

	@XmlElement(name = "idea", required = false)
	public List<Integer> getChildren() {
		return children;
	}
	public void setChildren(List<Integer> children) {
		this.children = children;
	}


	private String _code;
	private Date _createdat;
	private List<String> _groups;
	private List<Integer> children = new ArrayList<Integer>();
}
