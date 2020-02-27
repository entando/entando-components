/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.aps.system.services.content.model;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.AbstractResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.LinkAttribute;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentRestriction;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AttachResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.entity.model.EntityAttributeDto;
import org.entando.entando.aps.system.services.entity.model.EntityDto;
import org.entando.entando.plugins.jacms.web.content.validator.ContentValidator;
import org.entando.entando.web.common.json.JsonDateDeserializer;
import org.entando.entando.web.common.json.JsonDateSerializer;
import org.entando.entando.web.user.validator.UserValidator;
import org.springframework.validation.BindingResult;

public class ContentDto extends EntityDto implements Serializable {

    private String status;
    private boolean onLine;
    private String viewPage;
    private String listModel;
    private String defaultModel;

    private Date created;
    private Date lastModified;

    private String version;
    private String firstEditor;
    private String lastEditor;
    private String restriction;
    private String html;

    /**
     * The references grouped by service name.
     * <p>
     * Lists all the managers that may contain references by indicating with
     * <code>true</code> the presence of references
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Boolean> references;

    public ContentDto() {
        super();
    }

    public ContentDto(Content src) {
        super(src);
        this.setStatus(src.getStatus());
        this.setOnLine(src.isOnLine());
        this.setViewPage(src.getViewPage());
        this.setListModel(src.getListModel());
        this.setDefaultModel(src.getDefaultModel());
        this.setCreated(src.getCreated());
        this.setLastModified(src.getLastModified());
        this.setVersion(src.getVersion());
        this.setFirstEditor(src.getFirstEditor());
        this.setLastEditor(src.getLastEditor());
        this.setRestriction(src.getRestriction());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public String getViewPage() {
        return viewPage;
    }

    public void setViewPage(String viewPage) {
        this.viewPage = viewPage;
    }

    public String getListModel() {
        return listModel;
    }

    public void setListModel(String listModel) {
        this.listModel = listModel;
    }

    public String getDefaultModel() {
        return defaultModel;
    }

    public void setDefaultModel(String defaultModel) {
        this.defaultModel = defaultModel;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFirstEditor() {
        return firstEditor;
    }

    public void setFirstEditor(String firstEditor) {
        this.firstEditor = firstEditor;
    }

    public String getLastEditor() {
        return lastEditor;
    }

    public void setLastEditor(String lastEditor) {
        this.lastEditor = lastEditor;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Map<String, Boolean> getReferences() {
        return references;
    }

    public void setReferences(Map<String, Boolean> references) {
        this.references = references;
    }

    @Override
    public void fillEntity(IApsEntity prototype, ICategoryManager categoryManager, BindingResult bindingResult) {
        Content content = (Content) prototype;
        clearAttributeData(content);

        super.fillEntity(prototype, categoryManager, bindingResult);

        content.setFirstEditor(getFirstEditor() == null ? content.getFirstEditor() : getFirstEditor());
        content.setLastEditor(getLastEditor());
        content.setRestriction(ContentRestriction.getRestrictionValue(getMainGroup()));
        content.setStatus(getStatus() == null ? content.getStatus() : getStatus());

        // Load Resources from DTO ids
        fillAttributeData(bindingResult, content);
    }

    private void clearAttributeData(Content content) {
        for (AttributeInterface contentAttr : content.getAttributeList()) {
            if (AbstractResourceAttribute.class.isAssignableFrom(contentAttr.getClass())) {
                AbstractResourceAttribute resAttr = (AbstractResourceAttribute) contentAttr;
                resAttr.getTextMap().clear();
                resAttr.getResources().clear();
                resAttr.getMetadatas().clear();
            } else if (LinkAttribute.class.isAssignableFrom(contentAttr.getClass())) {
                LinkAttribute linkAttr = (LinkAttribute) contentAttr;
                linkAttr.getTextMap().clear();
                linkAttr.setSymbolicLink(null);
            } else if (BooleanAttribute.class.isAssignableFrom(contentAttr.getClass())) {
                BooleanAttribute booleanAttr = (BooleanAttribute) contentAttr;
                booleanAttr.setBooleanValue(null);
            }
        }
    }

    private void fillAttributeData(BindingResult bindingResult, Content content) {
        for (EntityAttributeDto attr : this.getAttributes()) {
            AttributeInterface contentAttr = content.getAttributeMap().get(attr.getCode());
            if (contentAttr != null) {
                if (AbstractResourceAttribute.class.isAssignableFrom(contentAttr.getClass())) {
                    for (Entry<String, Object> resEntry : attr.getValues().entrySet()) {
                        String langCode = resEntry.getKey();
                        String resourceId = (String) ((Map<String, Object>) resEntry.getValue()).get("id");
                        String name = (String) ((Map<String, Object>) resEntry.getValue()).get("name");

                        AbstractResourceAttribute resAttr = (AbstractResourceAttribute) contentAttr;
                        if (name != null) {
                            resAttr.setText(name, langCode);
                        }

                        ResourceInterface resource = new AttachResource();
                        resource.setId(resourceId);
                        resAttr.setResource(resource, langCode);

                        Map<String, Object> values = (Map<String, Object>) ((Map<String, Object>) resEntry.getValue()).get("metadata");

                        if (values != null) {
                            Map<String, String> metadata = values.entrySet().stream()
                                    .collect(Collectors.toMap(Map.Entry::getKey, e -> (String) e.getValue()));
                            resAttr.setMetadataMap(langCode, metadata);
                        }
                    }
                } else if (LinkAttribute.class.isAssignableFrom(contentAttr.getClass())) {
                    SymbolicLink link = new SymbolicLink();

                    int destType = (Integer) ((Map)attr.getValue()).get("destType");
                    switch (destType) {
                        case SymbolicLink.URL_TYPE:
                            link.setDestinationToUrl((String) ((Map)attr.getValue()).get("urlDest"));
                            break;
                        case SymbolicLink.PAGE_TYPE:
                            link.setDestinationToPage((String) ((Map)attr.getValue()).get("pageDest"));
                            break;
                        case SymbolicLink.RESOURCE_TYPE:
                            link.setDestinationToResource((String) ((Map)attr.getValue()).get("resourceDest"));
                            break;
                        case SymbolicLink.CONTENT_TYPE:
                            link.setDestinationToContent((String) ((Map)attr.getValue()).get("contentDest"));
                            break;
                        case SymbolicLink.CONTENT_ON_PAGE_TYPE:
                            link.setDestinationToContentOnPage((String) ((Map)attr.getValue()).get("contentDest"),
                                    (String) ((Map)attr.getValue()).get("pageDest"));
                            break;
                    }
                    LinkAttribute linkAttr = (LinkAttribute) contentAttr;

                    linkAttr.setSymbolicLink(link);
                }
            } else {
                bindingResult.reject(ContentValidator.ERRCODE_ATTRIBUTE_INVALID, new String[]{attr.getCode()}, "content.attribute.code.invalid");
            }
        }
    }

}
