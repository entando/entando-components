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
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.util.CheckFormatUtil;
import com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.AbstractResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.LinkAttribute;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentRestriction;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AttachResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.entity.model.EntityAttributeDto;
import org.entando.entando.aps.system.services.entity.model.EntityDto;
import org.entando.entando.plugins.jacms.web.content.validator.ContentValidator;
import org.entando.entando.web.common.json.JsonDateDeserializer;
import org.entando.entando.web.common.json.JsonDateSerializer;
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
        for (AttributeInterface attribute : content.getAttributeList()) {
            clearAttribute(attribute);
        }
    }

    private void clearAttribute(AttributeInterface attribute) {
        clearAbstractResourceAttribute(attribute);
        clearLinkAttribute(attribute);
        clearBooleanAttribute(attribute);
        clearDateAttribute(attribute);
        clearListAttribute(attribute);
        clearMonolistAttribute(attribute);
        clearCompositeAttribute(attribute);
    }

    private void clearAbstractResourceAttribute(AttributeInterface attribute) {
        if (AbstractResourceAttribute.class.isAssignableFrom(attribute.getClass())) {
            AbstractResourceAttribute resourceAttribute = (AbstractResourceAttribute) attribute;
            resourceAttribute.getTextMap().clear();
            resourceAttribute.getResources().clear();
            resourceAttribute.getMetadatas().clear();
        }
    }

    private void clearLinkAttribute(AttributeInterface attribute) {
        if (LinkAttribute.class.isAssignableFrom(attribute.getClass())) {
            LinkAttribute linkAttribute = (LinkAttribute) attribute;
            linkAttribute.getTextMap().clear();
            linkAttribute.setSymbolicLink(null);
        }
    }

    private void clearBooleanAttribute(AttributeInterface attribute) {
        if (BooleanAttribute.class.isAssignableFrom(attribute.getClass())) {
            BooleanAttribute booleanAttribute = (BooleanAttribute) attribute;
            booleanAttribute.setBooleanValue(null);
        }
    }

    private void clearDateAttribute(AttributeInterface attribute) {
        if (DateAttribute.class.isAssignableFrom(attribute.getClass())) {
            DateAttribute dateAttribute = (DateAttribute) attribute;
            dateAttribute.setDate(null);
        }
    }

    private void clearListAttribute(AttributeInterface attribute) {
        if (ListAttribute.class.isAssignableFrom(attribute.getClass())) {
            ListAttribute listAttribute = (ListAttribute) attribute;
            for (Entry<String, List<AttributeInterface>> entry : listAttribute.getAttributeListMap().entrySet()) {
                for (AttributeInterface element : entry.getValue()) {
                    clearAttribute(element);
                }
            }
        }
    }

    private void clearMonolistAttribute(AttributeInterface attribute) {
        if (MonoListAttribute.class.isAssignableFrom(attribute.getClass())) {
            MonoListAttribute monolistAttribute = (MonoListAttribute) attribute;
            monolistAttribute.getAttributes().clear();
        }
    }

    private void clearCompositeAttribute(AttributeInterface attribute) {
        if (CompositeAttribute.class.isAssignableFrom(attribute.getClass())) {
            CompositeAttribute compositeAttribute = (CompositeAttribute) attribute;
            for (AttributeInterface element : compositeAttribute.getAttributes()) {
                clearAttribute(element);
            }
        }
    }

    private void fillAttributeData(BindingResult bindingResult, Content content) {
        for (EntityAttributeDto attributeDto : this.getAttributes()) {
            AttributeInterface attribute = content.getAttributeMap().get(attributeDto.getCode());
            if (attribute != null) {
                fillAttribute(attribute, attributeDto);
            } else {
                bindingResult.reject(ContentValidator.ERRCODE_ATTRIBUTE_INVALID, new String[]{attributeDto.getCode()}, "content.attribute.code.invalid");
            }
        }
    }

    private void fillAttribute(AttributeInterface attribute, EntityAttributeDto attributeDto) {
        fillAbstractResourceAttribute(attribute, attributeDto);
        fillLinkAttribute(attribute, attributeDto);
        fillDateAttribute(attribute, attributeDto);
        fillListAttribute(attribute, attributeDto);
        fillMonolistAttribute(attribute, attributeDto);
        fillCompositeAttribute(attribute, attributeDto);
    }

    private void fillAbstractResourceAttribute(AttributeInterface attribute, EntityAttributeDto attributeDto) {
        if (AbstractResourceAttribute.class.isAssignableFrom(attribute.getClass())) {
            AbstractResourceAttribute resourceAttribute = (AbstractResourceAttribute) attribute;
            for (Entry<String, Object> resourceEntry : attributeDto.getValues().entrySet()) {

                String langCode = resourceEntry.getKey();
                String resourceId = (String) ((Map<String, Object>) resourceEntry.getValue()).get("id");
                String name = (String) ((Map<String, Object>) resourceEntry.getValue()).get("name");

                if (name != null) {
                    resourceAttribute.setText(name, langCode);
                }

                ResourceInterface resource = new AttachResource();
                resource.setId(resourceId);
                resourceAttribute.setResource(resource, langCode);

                Map<String, Object> values = (Map<String, Object>) ((Map<String, Object>) resourceEntry.getValue())
                        .get("metadata");

                if (values != null) {
                    Map<String, String> metadata = values.entrySet().stream()
                            .collect(Collectors.toMap(Entry::getKey, e -> (String) e.getValue()));
                    resourceAttribute.setMetadataMap(langCode, metadata);
                }
            }
        }
    }

    private void fillLinkAttribute(AttributeInterface attribute, EntityAttributeDto attributeDto) {
        if (LinkAttribute.class.isAssignableFrom(attribute.getClass())) {
            LinkAttribute linkAttribute = (LinkAttribute)attribute;
            SymbolicLink link = new SymbolicLink();
            if (attributeDto.getValue() != null) {
                Object destType = ((Map) attributeDto.getValue()).get("destType");
                if (destType != null) {
                    switch ((Integer) destType) {
                        case SymbolicLink.URL_TYPE:
                            link.setDestinationToUrl((String) ((Map) attributeDto.getValue()).get("urlDest"));
                            break;
                        case SymbolicLink.PAGE_TYPE:
                            link.setDestinationToPage(
                                    (String) ((Map) attributeDto.getValue()).get("pageDest"));
                            break;
                        case SymbolicLink.RESOURCE_TYPE:
                            link.setDestinationToResource(
                                    (String) ((Map) attributeDto.getValue()).get("resourceDest"));
                            break;
                        case SymbolicLink.CONTENT_TYPE:
                            link.setDestinationToContent(
                                    (String) ((Map) attributeDto.getValue()).get("contentDest"));
                            break;
                        case SymbolicLink.CONTENT_ON_PAGE_TYPE:
                            link.setDestinationToContentOnPage(
                                    (String) ((Map) attributeDto.getValue()).get("contentDest"),
                                    (String) ((Map) attributeDto.getValue()).get("pageDest"));
                            break;
                    }
                }
            }
            linkAttribute.setSymbolicLink(link);
        }
    }

    private void fillDateAttribute(AttributeInterface attribute, EntityAttributeDto attributeDto) {
        if (DateAttribute.class.isAssignableFrom(attribute.getClass())) {
            DateAttribute dateAttribute = (DateAttribute)attribute;
            if (dateAttribute.getDate() != null) {
                return;
            }
            String value = (String) attributeDto.getValue();
            Date data = null;
            if (value != null) {
                value = value.trim();
            }
            if (CheckFormatUtil.isValidDate(value)) {
                try {
                    SimpleDateFormat dataF = new SimpleDateFormat("dd/MM/yyyy");
                    data = dataF.parse(value);
                    dateAttribute.setFailedDateString(null);
                } catch (ParseException ex) {
                    throw new RuntimeException(
                            StringUtils.join("Error while parsing the date submitted - ", value, " -"), ex);
                }
            } else {
                dateAttribute.setFailedDateString(value);
            }
            dateAttribute.setDate(data);
        }
    }

    private void fillListAttribute(AttributeInterface attribute, EntityAttributeDto attributeDto) {
        if (ListAttribute.class.isAssignableFrom(attribute.getClass())) {
            ListAttribute listAttribute = (ListAttribute) attribute;
            int index = 0;
            for (Entry<String, List<EntityAttributeDto>> entry : attributeDto.getListElements().entrySet()) {
                for (EntityAttributeDto element : entry.getValue()) {
                    fillAttribute(listAttribute.getAttributes().get(index), element);
                    index++;
                }
            }
        }
    }

    private void fillMonolistAttribute(AttributeInterface attribute, EntityAttributeDto attributeDto) {
        if (MonoListAttribute.class.isAssignableFrom(attribute.getClass())) {
            MonoListAttribute monolistAttribute = (MonoListAttribute) attribute;
            int index = 0;
            for (EntityAttributeDto element : attributeDto.getElements()) {
                fillAttribute(monolistAttribute.getAttribute(index), element);
                index++;
            }
        }
    }

    private void fillCompositeAttribute(AttributeInterface attribute, EntityAttributeDto attributeDto) {
        if (CompositeAttribute.class.isAssignableFrom(attribute.getClass())) {
            CompositeAttribute compositeAttribute = (CompositeAttribute) attribute;
            int index = 0;
            for (EntityAttributeDto element : attributeDto.getCompositeElements()) {
                fillAttribute(compositeAttribute.getAttributes().get(index), element);
                index++;
            }
        }
    }
}
