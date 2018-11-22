package org.entando.entando.plugins.jacms.web.contentmodel.util;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.enums.DefaultContentModel;

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

    public ContentTypeDtoBuilder withName(String name) {
        contentTypeDto.setName(name);
        return this;
    }

    public ContentTypeDtoBuilder withDefaultContentModel(DefaultContentModel contentModel) {
        contentTypeDto.setDefaultContentModel(contentModel);
        return this;
    }

    public ContentTypeDtoBuilder withDefaultContentModelList(DefaultContentModel contentModel) {
        contentTypeDto.setDefaultContentModelList(contentModel);
        return this;
    }

}
