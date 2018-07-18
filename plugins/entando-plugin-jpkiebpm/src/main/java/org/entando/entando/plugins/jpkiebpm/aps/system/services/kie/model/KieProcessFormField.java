/*
 * The MIT License
 *
 * Copyright 2017 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Entando
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class KieProcessFormField {

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public KieProcessProperty getProperty(String name) {
		if (null == this.properties) {
			return null;
		}
		for (int i = 0; i < properties.size(); i++) {
			KieProcessProperty property = properties.get(i);
			if (property.getName().equalsIgnoreCase(name)) {
				return property;
			}
		}
		return null;
	}

	public void addProperty(String name, Object value) {

		if(this.properties == null) {
			this.properties = new ArrayList<>();
		}

		KieProcessProperty prop = new KieProcessProperty();
		prop.setName(name);
		prop.setValue(value.toString());

		this.properties.add(prop);
	}

	public List<KieProcessProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<KieProcessProperty> properties) {
		this.properties = properties;
	}

	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private Integer position;
	@XmlAttribute
	private String type;
	@XmlElement(name = "property")
	private List<KieProcessProperty> properties;
	
	@Override
	public String toString() {
		return "KieProcessFormField [id=" + id + ", name=" + name + ", position=" + position + ", type=" + type
				+ ", properties=" + properties + "]";
	}
	
	
}
