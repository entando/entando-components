package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.api.model.form;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "fields")
public class KieApiFields {

    private List<KieApiFieldset> fieldset = new ArrayList<KieApiFieldset>();

    public List<KieApiFieldset> getFieldset() {
        return fieldset;
    }

    public void setFieldset(final List<KieApiFieldset> fieldset) {
        this.fieldset = fieldset;
    }

    public void addFieldset(final KieApiFieldset fieldset) {
        if (this.fieldset == null ){
            this.fieldset = new ArrayList<>();
        }
        this.fieldset.add(fieldset);
    }


}
