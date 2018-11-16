package org.entando.entando.plugins.jacms.web.contentmodel.util;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;

public class ContentTypeDtoBuilder {

    private ContentTypeDto contentTypeDto = new ContentTypeDto();

    public ContentTypeDto build() {
        return contentTypeDto;
    }

    public ContentTypeDtoBuilder withId(Long id) {
        contentTypeDto.setId(id);
        return this;
    }

    public ContentTypeDtoBuilder withCode(String code) {
        contentTypeDto.setCode(code);
        return this;
    }
}
