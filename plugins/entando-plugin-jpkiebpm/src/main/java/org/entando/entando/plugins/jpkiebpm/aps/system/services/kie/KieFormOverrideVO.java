package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

/**
 * This class is used to edit the KIE form overrides from the
 * BpmFormWidgetAction in an easier way than managing directly the list of
 * KieFormOverride instances.
 */
public class KieFormOverrideVO {

    // A null id means that the override has not been saved into the database yet
    private Integer id;
    private boolean active;
    private String field;
    private String defaultValue;
    private String placeHolderValue;

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
