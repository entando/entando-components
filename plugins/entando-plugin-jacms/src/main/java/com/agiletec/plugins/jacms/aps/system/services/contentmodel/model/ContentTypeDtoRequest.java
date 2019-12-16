package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.*;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import org.entando.entando.aps.system.services.entity.model.EntityTypeAttributeFullDto;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;

import java.util.List;

public class ContentTypeDtoRequest extends EntityTypeDtoRequest {
    private String defaultContentModel;

    private String defaultContentModelList;

    private String viewPage;

    public ContentTypeDtoRequest() { super();}

    public ContentTypeDtoRequest(IApsEntity entityType) {
        super(entityType);

        Content contentType = (Content)entityType;

        this.defaultContentModel = contentType.getDefaultModel();
        this.defaultContentModelList = contentType.getListModel();
        this.viewPage = contentType.getViewPage();
    }

    public ContentTypeDtoRequest(IApsEntity entityType, List<AttributeRole> roles) {
        this(entityType);
        List<AttributeInterface> entityAttributes = entityType.getAttributeList();
        for (AttributeInterface attribute : entityAttributes) {
            EntityTypeAttributeFullDto attributeDto = new EntityTypeAttributeFullDto(attribute, roles);
            this.getAttributes().add(attributeDto);
        }
    }

    public String getDefaultContentModel() {
        return defaultContentModel;
    }

    public void setDefaultContentModel(String defaultContentModel) {
        this.defaultContentModel = defaultContentModel;
    }

    public String getDefaultContentModelList() {
        return defaultContentModelList;
    }

    public void setDefaultContentModelList(String defaultContentModelList) {
        this.defaultContentModelList = defaultContentModelList;
    }

    public String getViewPage() {
        return viewPage;
    }

    public void setViewPage(String viewPage) {
        this.viewPage = viewPage;
    }
}
