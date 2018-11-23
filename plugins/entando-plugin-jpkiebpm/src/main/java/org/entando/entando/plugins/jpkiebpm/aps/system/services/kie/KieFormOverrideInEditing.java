package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.*;

import java.util.List;

/**
 * This class is used to edit the KIE form overrides from the
 * BpmFormWidgetAction in an easier way than managing directly the list of
 * KieFormOverride instances.
 */
public class KieFormOverrideInEditing {

    // A null id means that the override has not been saved into the database yet
    private Integer id;
    private boolean active;
    private String field;
    private String defaultValue;
    private String placeHolderValue;

    public KieFormOverrideInEditing() {
    }

    public KieFormOverrideInEditing(KieFormOverride kieFormOverride) {
        this.id = kieFormOverride.getId();
        this.field = kieFormOverride.getField();
        this.active = kieFormOverride.isActive();
        if (kieFormOverride.getOverrides() != null) {
            List<IBpmOverride> overrides = kieFormOverride.getOverrides().getList();
            if (overrides != null) {
                overrides.forEach((bpmOverride) -> {
                    if (bpmOverride instanceof DefaultValueOverride) {
                        this.defaultValue = ((DefaultValueOverride) bpmOverride).getDefaultValue();
                    } else if (bpmOverride instanceof PlaceHolderOverride) {
                        this.placeHolderValue = ((PlaceHolderOverride) bpmOverride).getPlaceHolder();
                    }
                });
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getPlaceHolderValue() {
        return placeHolderValue;
    }

    public void setPlaceHolderValue(String placeHolderValue) {
        this.placeHolderValue = placeHolderValue;
    }
}
