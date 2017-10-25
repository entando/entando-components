package org.entando.entando.plugins.jpkiebpm.aps.system.services.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "datatable-field-definition")
@XmlAccessorType(XmlAccessType.FIELD)
public class DatatableFieldDefinition {

    private List<Field> fields = new ArrayList<>();

    public DatatableFieldDefinition() {
    }

    public List<Field> getFields() {
        return fields;
    }

    public void addField(final Field field) {
        this.fields.add(field);
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @XmlRootElement(name = "field")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Field {
        @XmlElement
        private String title;
        @XmlElement
        private String data;
        @XmlElement
        private Boolean visible;

        @XmlElement
        private Byte position;


        public Field() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(final String title) {
            this.title = title;

        }

        public void setData(final String data) {
            this.data = data;
        }

        public Boolean getVisible() {
            return visible;
        }

        public void setVisible(Boolean visible) {
            this.visible = visible;
        }

        public String getData() {
            return data;
        }

        public Byte getPosition() {
            return position;
        }

        public void setPosition(Byte position) {
            this.position = position;
        }

    }
}
