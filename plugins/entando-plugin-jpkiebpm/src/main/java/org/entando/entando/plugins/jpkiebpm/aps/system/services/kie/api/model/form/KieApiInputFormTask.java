package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "fields" })
public class KieApiInputFormTask implements Serializable {

	@XmlElement(name = "fields", required = true)
	private List<Field> fields = new ArrayList<>();

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(propOrder = { "name", "value" })
	public static class Field {
		@XmlElement(name = "name")
		private String name;
		@XmlElement(name = "value")
		private String value;

		public Field() {
		}

		public Field(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
