/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.agiletec.aps.system.common.entity.model.attribute;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.FieldError;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.util.BaseAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.IAttributeValidationRules;
import com.agiletec.aps.system.common.entity.parse.attribute.AttributeHandlerInterface;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * This abstract class must be used when implementing Entity Attributes.
 * @author W.Ambu - E.Santoboni
 */
public abstract class AbstractAttribute implements AttributeInterface, BeanFactoryAware, Serializable {
    
    @Override
    public boolean isMultilingual() {
        return false;
    }
    
    @Override
    public boolean isTextAttribute() {
        return false;
    }
    
    @Override
    public String getName() {
        return _name;
    }
	
	@Override
	public String getDescription() {
		return _description;
	}
	@Override
	public void setDescription(String description) {
		this._description = description;
	}
	
    @Override
    public void setName(String name) {
        this._name = name;
    }
    
    /**
     * The returned type corresponds to the attribute code as found in the declaration
     * of the attribute type.
     */
    @Override
    public String getType() {
        return _type;
    }
	
    @Override
    public void setType(String typeName) {
        this._type = typeName;
    }
    
    @Override
    public void setDefaultLangCode(String langCode) {
        this._defaultLangCode = langCode;
    }

    /**
     * Return the code of the default language.
     * @return The code of the default language.
     */
    public String getDefaultLangCode() {
        return _defaultLangCode;
    }

    /**
     * Set up the language to use in the rendering process.
     * @param langCode The code of the rendering language.
     */
    @Override
    public void setRenderingLang(String langCode) {
        _renderingLangCode = langCode;
    }

    /**
     * Return the code of the language used in the rendering process.
     * @return The code of the language used for rendering.
     */
    public String getRenderingLang() {
        return _renderingLangCode;
    }

    /**
     * @return True if the attribute is searchable, false otherwise.
     */
    @Override
    public boolean isSearcheable() {
        return isSearchable();
    }

    /**
     * Toggle the searchable condition of the attribute.
     * @param searchable True if the attribute is searchable, false otherwise.
     */
    @Override
    public void setSearcheable(boolean searchable) {
        this.setSearchable(searchable);
    }
    
    public boolean isSearchable() {
    	return _searchable;
    }

    public void setSearchable(boolean searchable) {
    	this._searchable = searchable;
    }
    
    @Override
    public Object getAttributePrototype() {
        AbstractAttribute clone = null;
        try {
            Class attributeClass = Class.forName(this.getClass().getName());
            clone = (AbstractAttribute) attributeClass.newInstance();
            clone.setName(this.getName());
            clone.setDescription(this.getDescription());
            clone.setType(this.getType());
            clone.setSearcheable(this.isSearcheable());
            clone.setDefaultLangCode(this.getDefaultLangCode());
            clone.setIndexingType(this.getIndexingType());
            clone.setParentEntity(this.getParentEntity());
            AttributeHandlerInterface handler = (AttributeHandlerInterface) this.getHandler().getAttributeHandlerPrototype();
            clone.setHandler(handler);
            if (this.getDisablingCodes() != null) {
                String[] disablingCodes = new String[this.getDisablingCodes().length];
                for (int i = 0; i < this.getDisablingCodes().length; i++) {
                    disablingCodes[i] = this.getDisablingCodes()[i];
                }
                clone.setDisablingCodes(disablingCodes);
            }
            if (this.getRoles() != null) {
                String[] roles = new String[this.getRoles().length];
                for (int i = 0; i < this.getRoles().length; i++) {
                    roles[i] = this.getRoles()[i];
                }
                clone.setRoles(roles);
            }
            clone.setValidationRules(this.getValidationRules().clone());
            clone.setBeanFactory(this.getBeanFactory());
			clone.setAttributeManagerClassName(this.getAttributeManagerClassName());
        } catch (Throwable e) {
            String message = "Error detected while creating the attribute prototype '"
                    + this.getName() + "' type '" + this.getType() + "'";
            ApsSystemUtils.logThrowable(e, this, "getAttributePrototype", message);;
            throw new RuntimeException(message, e);
        }
        return clone;
    }
    
    @Override
    public void setAttributeConfig(Element attributeElement) throws ApsSystemException {
        try {
            String name = this.extractXmlAttribute(attributeElement, "name", true);
            this.setName(name);
			String description = this.extractXmlAttribute(attributeElement, "description", false);
			this.setDescription(description);
            String searcheable = this.extractXmlAttribute(attributeElement, "searcheable", false);
            this.setSearcheable(null != searcheable && searcheable.equalsIgnoreCase("true"));
            IAttributeValidationRules validationCondition = this.getValidationRules();
            validationCondition.setConfig(attributeElement);
            //to guaranted compatibility with previsous version of jAPS 2.0.12 *** Start Block
            String required = this.extractXmlAttribute(attributeElement, "required", false);
            if (null != required && required.equalsIgnoreCase("true")) {
                this.setRequired(true);
            }
            //to guaranted compatibility with previsous version of jAPS 2.0.12 *** End Block
            String indexingType = this.extractXmlAttribute(attributeElement, "indexingtype", false);
            if (null != indexingType) {
                this.setIndexingType(indexingType);
            } else {
                this.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_NONE);
            }
            Element disablingCodesElements = attributeElement.getChild("disablingCodes");
            if (null != disablingCodesElements) {
                String[] disablingCodes = this.extractValues(disablingCodesElements, "code");
                this.setDisablingCodes(disablingCodes);
            } else {
                //to guaranted compatibility with previsous version of jAPS 2.0.12 *** Start Block
                String disablingCodesStr = this.extractXmlAttribute(attributeElement, "disablingCodes", false);
                if (disablingCodesStr != null) {
                    String[] disablingCodes = disablingCodesStr.split(",");
                    this.setDisablingCodes(disablingCodes);
                }
                //to guaranted compatibility with previsous version of jAPS 2.0.12 *** End Block
            }
            Element rolesElements = attributeElement.getChild("roles");
            if (null != rolesElements) {
                String[] roles = this.extractValues(rolesElements, "role");
                this.setRoles(roles);
            }
        } catch (Throwable e) {
            String message = "Error detected while creating the attribute config '"
                    + this.getName() + "' type '" + this.getType() + "'";
            ApsSystemUtils.logThrowable(e, this, "getAttributePrototype", message);
            throw new RuntimeException(message, e);
        }
    }

    private String[] extractValues(Element elements, String subElementName) {
        if (null == elements) {
            return null;
        }
        List<String> values = new ArrayList<String>();
        List<Element> subElements = elements.getChildren(subElementName);
        if (null == subElements || subElements.isEmpty()) {
            return null;
        }
        for (int i = 0; i < subElements.size(); i++) {
            String text = subElements.get(i).getText();
            if (null != text && text.trim().length() > 0) {
                values.add(text.trim());
            }
        }
        String[] array = new String[values.size()];
        for (int i = 0; i < values.size(); i++) {
            array[i] = values.get(i);
        }
        return array;
    }
    
    @Override
    public Element getJDOMConfigElement() {
        Element configElement = this.createRootElement(this.getTypeConfigElementName());
        if (null != this.getDescription() && this.getDescription().trim().length() > 0) {
			configElement.setAttribute("description", this.getDescription());
		}
        if (this.isSearcheable()) {
            configElement.setAttribute("searcheable", "true");
        }
        if (null != this.getValidationRules() && !this.getValidationRules().isEmpty()) {
			Element validationElement = this.getValidationRules().getJDOMConfigElement();
            configElement.addContent(validationElement);
        }
        if (null != this.getIndexingType() && !this.getIndexingType().equals(IndexableAttributeInterface.INDEXING_TYPE_NONE)) {
            configElement.setAttribute("indexingtype", this.getIndexingType());
        }
        this.addArrayElement(configElement, this.getDisablingCodes(), "disablingCodes", "code");
        this.addArrayElement(configElement, this.getRoles(), "roles", "role");
        return configElement;
    }
	
	protected Element createRootElement(String elementName) {
		Element attributeElement = new Element(elementName);
        attributeElement.setAttribute("name", this.getName());
        attributeElement.setAttribute("attributetype", this.getType());
		return attributeElement;
	}

    private void addArrayElement(Element configElement, String[] values, String elementName, String subElementName) {
        if (null != values) {
            Element arrayElem = new Element(elementName);
            for (int i = 0; i < values.length; i++) {
                Element stringElem = new Element(subElementName);
                stringElem.setText(values[i]);
                arrayElem.addContent(stringElem);
            }
            configElement.addContent(arrayElem);
        }
    }

    protected String getTypeConfigElementName() {
        return "attribute";
    }

    /**
     * Get the attribute matching the given criteria from a XML string.
     * @param currElement The element where to extract the value of the attribute from 
     * @param attributeName Name of the requested attribute.
     * @param required Select a mandatory condition of the attribute to search for.
     * @return The value of the requested attribute.
     * @throws ApsSystemException when a attribute declared mandatory is not present in the given
     * XML element.
     */
    protected String extractXmlAttribute(Element currElement, String attributeName,
            boolean required) throws ApsSystemException {
        String value = currElement.getAttributeValue(attributeName);
        if (required && value == null) {
            throw new ApsSystemException("Attribute '" + attributeName + "' not found in the tag <" + currElement.getName() + ">");
        }
        return value;
    }

    @Deprecated(/** DO NOTHING : to guaranted compatibility with previsous version of jAPS 2.0.12 */)
    protected void addListElementTypeConfig(Element configElement) {}
    
    @Override
    public String getIndexingType() {
        return _indexingType;
    }
    
    @Override
    public void setIndexingType(String indexingType) {
        this._indexingType = indexingType;
    }
    
    @Override
    public boolean isSimple() {
        return true;
    }
    
    @Override
    public boolean isRequired() {
        return this.getValidationRules().isRequired();
    }
    
    @Override
    public void setRequired(boolean required) {
        this.getValidationRules().setRequired(required);
    }
    
    @Override
    public IApsEntity getParentEntity() {
        return _parentEntity;
    }
    
    @Override
    public void setParentEntity(IApsEntity parentEntity) {
        this._parentEntity = parentEntity;
    }
    
    @Override
    public AttributeHandlerInterface getHandler() {
        return _handler;
    }
    
    @Override
    public void setHandler(AttributeHandlerInterface handler) {
        this._handler = handler;
    }
    
    @Override
    public void disable(String disablingCode) {
        if (_disablingCodes != null && disablingCode != null) {
            for (int i = 0; i < _disablingCodes.length; i++) {
                if (disablingCode.equals(_disablingCodes[i])) {
                    this._active = false;
                    return;
                }
            }
        }
    }
    
    @Override
    public void activate() {
		this._active = true;
	}
	
    @Override
    public boolean isActive() {
        return _active;
    }
    
    @Override
    public void setDisablingCodes(String[] disablingCodes) {
        this._disablingCodes = disablingCodes;
    }
    
    @Override
    public String[] getDisablingCodes() {
        return this._disablingCodes;
    }
    
    @Override
    public String[] getRoles() {
        return _roles;
    }
    
    @Override
    public void setRoles(String[] roles) {
        this._roles = roles;
    }

    protected IAttributeValidationRules getValidationRuleNewIntance() {
        return new BaseAttributeValidationRules();
    }
    
    @Override
    public IAttributeValidationRules getValidationRules() {
        if (null == this._validationRules) {
            this.setValidationRules(this.getValidationRuleNewIntance());
        }
        return _validationRules;
    }
    
    @Override
    public void setValidationRules(IAttributeValidationRules validationRules) {
        this._validationRules = validationRules;
    }
    
    @Override
    public DefaultJAXBAttribute getJAXBAttribute(String langCode) {
        if (null == this.getValue()) {
            return null;
        }
        DefaultJAXBAttribute jaxbAttribute = this.getJAXBAttributeInstance();
        jaxbAttribute.setDescription(this.getDescription());
        jaxbAttribute.setName(this.getName());
        jaxbAttribute.setType(this.getType());
        jaxbAttribute.setValue(this.getJAXBValue(langCode));
        if (null != this.getRoles() && this.getRoles().length > 0) {
            List<String> roles = Arrays.asList(this.getRoles());
            jaxbAttribute.setRoles(roles);
        }
        return jaxbAttribute;
    }
    
    protected DefaultJAXBAttribute getJAXBAttributeInstance() {
        return new DefaultJAXBAttribute();
    }
    
    protected abstract Object getJAXBValue(String langCode);
    
    @Override
    public void valueFrom(DefaultJAXBAttribute jaxbAttribute) {
        this.setName(jaxbAttribute.getName());
    }
    
    @Override
    public DefaultJAXBAttributeType getJAXBAttributeType() {
        DefaultJAXBAttributeType jaxbAttributeType = this.getJAXBAttributeTypeInstance();
        jaxbAttributeType.setName(this.getName());
        jaxbAttributeType.setDescription(this.getDescription());
        jaxbAttributeType.setType(this.getType());
        if (this.isSearcheable()) {
            jaxbAttributeType.setSearchable(true);
        }
        if (null != this.getIndexingType() && this.getIndexingType().equalsIgnoreCase(IndexableAttributeInterface.INDEXING_TYPE_TEXT)) {
            jaxbAttributeType.setIndexable(true);
        }
        if (null != this.getRoles() && this.getRoles().length > 0) {
            List<String> roles = Arrays.asList(this.getRoles());
            jaxbAttributeType.setRoles(roles);
        }
        if (null != this.getValidationRules() && !this.getValidationRules().isEmpty()) {
            jaxbAttributeType.setValidationRules(this.getValidationRules());
        }
        return jaxbAttributeType;
    }
    
    protected DefaultJAXBAttributeType getJAXBAttributeTypeInstance() {
        return new DefaultJAXBAttributeType();
    }
    
	@Override
    public List<AttributeFieldError> validate(AttributeTracer tracer) {
        List<AttributeFieldError> errors = new ArrayList<AttributeFieldError>();
        try {
            if (this.getStatus().equals(Status.INCOMPLETE)) {
                errors.add(new AttributeFieldError(this, FieldError.INVALID, tracer));
            } else {
                IAttributeValidationRules validationRules = this.getValidationRules();
                if (null == validationRules) {
					return errors;
				}
                ILangManager langManager = (ILangManager) this.getBeanFactory().getBean(SystemConstants.LANGUAGE_MANAGER, ILangManager.class);
                List<AttributeFieldError> validationRulesErrors = validationRules.validate(this, tracer, langManager);
                if (null != validationRulesErrors) {
                    errors.addAll(validationRulesErrors);
                }
            }
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "validate", "Error validating Attribute '" + this.getName() + "'");
            throw new RuntimeException("Error validating Attribute '" + this.getName() + "'", t);
        }
        return errors;
    }
    
    protected BeanFactory getBeanFactory() {
        return this._beanFactory;
    }
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this._beanFactory = beanFactory;
    }
	
	@Override
    public String getAttributeManagerClassName() {
		return _attributeManagerClassName;
	}
	public void setAttributeManagerClassName(String attributeManagerClassName) {
		this._attributeManagerClassName = attributeManagerClassName;
	}



		private String _name;
	private String _description;
    private String _type;
    private String _defaultLangCode;
    private String _renderingLangCode;
    @Deprecated
    private boolean _searcheable;
    private boolean _searchable;
    private String _indexingType;
    private IApsEntity _parentEntity;
    private transient AttributeHandlerInterface _handler;
    private String[] _disablingCodes;
    private String[] _roles;
    private boolean _active = true;
    private IAttributeValidationRules _validationRules;
    
    private BeanFactory _beanFactory;
	private String _attributeManagerClassName;
    
}