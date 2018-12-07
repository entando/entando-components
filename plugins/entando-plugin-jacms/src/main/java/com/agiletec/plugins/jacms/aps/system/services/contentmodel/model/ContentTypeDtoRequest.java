package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.*;
import org.entando.entando.aps.system.services.entity.model.EntityTypeAttributeFullDto;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;

import java.util.List;

public class ContentTypeDtoRequest extends EntityTypeDtoRequest {

    public ContentTypeDtoRequest() { super();}

    public ContentTypeDtoRequest(IApsEntity entityType) { super(entityType); }

    public ContentTypeDtoRequest(IApsEntity entityType, List<AttributeRole> roles) {
        this(entityType);
        List<AttributeInterface> entityAttributes = entityType.getAttributeList();
        for (AttributeInterface attribute : entityAttributes) {
            EntityTypeAttributeFullDto attributeDto = new EntityTypeAttributeFullDto(attribute, roles);
            this.getAttributes().add(attributeDto);
        }
    }
}
