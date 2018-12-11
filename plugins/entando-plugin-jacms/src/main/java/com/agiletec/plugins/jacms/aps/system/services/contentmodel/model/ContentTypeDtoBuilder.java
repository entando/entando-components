package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import org.entando.entando.aps.system.services.entity.model.AbstractEntityTypeDtoBuilder;
import org.entando.entando.plugins.jacms.aps.system.services.IContent;

import java.util.List;

public class ContentTypeDtoBuilder extends AbstractEntityTypeDtoBuilder<IContent, ContentTypeDto> {

    public ContentTypeDtoBuilder(List<AttributeRole> roles) {
        super(roles);
    }

    @Override
    protected ContentTypeDto toDto(IContent src) {
        return new ContentTypeDto(src, this.getRoles());
    }
}
