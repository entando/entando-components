package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name="layoutTemplate")
@XmlAccessorType(XmlAccessType.FIELD)
public class PamLayoutTemplate {



    @XmlElement(name = "rows")
    public List<PamLayoutTemplateRow> rows;

    public List<PamLayoutTemplateRow> getRows() {
        return rows;
    }

    public void setRows(List<PamLayoutTemplateRow> rows) {
        this.rows = rows;
    }
}
