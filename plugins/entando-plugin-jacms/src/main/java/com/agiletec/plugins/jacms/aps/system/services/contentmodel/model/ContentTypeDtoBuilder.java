package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import org.entando.entando.aps.system.services.entity.model.AbstractEntityTypeDtoBuilder;
import org.entando.entando.plugins.jacms.aps.system.services.IContentType;

import java.util.List;

public class ContentTypeDtoBuilder extends AbstractEntityTypeDtoBuilder<IContentType, ContentTypeDto> {

    public ContentTypeDtoBuilder(List<AttributeRole> roles) {
        super(roles);
    }

    @Override
    protected ContentTypeDto toDto(IContentType src) {
        return new ContentTypeDto(src, this.getRoles());
    }
}
