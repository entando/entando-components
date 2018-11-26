package org.entando.entando.plugins.jacms.web.contentmodel.util;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeDto;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.enums.AttributeType;

public class AttributeDtoBuilder {

    private AttributeDto attribute = new AttributeDto();

    public AttributeDtoBuilder withId(Long id) {
        attribute.setId(id);
        return this;
    }

    public AttributeDtoBuilder withType(AttributeType type) {
        attribute.setType(type);
        return this;
    }

    public AttributeDtoBuilder withCode(String code) {
        attribute.setCode(code);
        return this;
    }

    public AttributeDtoBuilder withName(String name) {
        attribute.setName(name);
        return this;
    }

    public AttributeDtoBuilder withMandatory(Boolean mandatory) {
        attribute.setMandatory(mandatory);
        return this;
    }

    public AttributeDtoBuilder withSearcheable(Boolean searcheable) {
        attribute.setSearchable(searcheable);
        return this;
    }

    public AttributeDtoBuilder withFilterable(Boolean filterable) {
        attribute.setFilterable(filterable);
        return this;
    }

    public AttributeDto build() {
        return attribute;
    }


}
