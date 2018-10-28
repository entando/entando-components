package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "options")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamFieldsOptions {

    @XmlElement
    private String text;

    @XmlElement
    private String value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
