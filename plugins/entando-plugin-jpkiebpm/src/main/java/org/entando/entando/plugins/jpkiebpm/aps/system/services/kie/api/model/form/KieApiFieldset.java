package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "fieldset")
public class KieApiFieldset {

    private String legend;


    private List<KieApiField> fields;

    public KieApiFieldset() {
    }

    public KieApiFieldset(final String legend) {
        this.legend = legend;
    }

    public void setLegend(final String legend) {
        this.legend = legend;
    }

    @XmlElement(name = "field")
    public List<KieApiField> getFields() {
        if (null == fields) {
            fields = new ArrayList<>();
        }

        return fields;
    }

    public void setField(final List<KieApiField> fields) {
        this.fields = fields;
    }


    public String getLegend() {
        return legend;
    }


}
