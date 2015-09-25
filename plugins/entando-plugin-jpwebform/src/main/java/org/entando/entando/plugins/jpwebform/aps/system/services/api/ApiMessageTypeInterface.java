/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpwebform.aps.system.services.api;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.entando.entando.plugins.jpwebform.aps.system.services.api.model.JAXBMessageType;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class ApiMessageTypeInterface {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiMessageTypeInterface.class);
	
    public JAXBMessageType getMessageType(Properties properties) throws ApiException, Throwable {
        JAXBMessageType jaxbMessageType = null;
        try {
            String typeCode = properties.getProperty("typeCode");
            IApsEntity masterMessageType = this.getMessageManager().getEntityPrototype(typeCode);
            if (null == masterMessageType) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, 
						"Message type with code '" + typeCode + "' does not exist", Response.Status.CONFLICT);
            }
            jaxbMessageType = new JAXBMessageType(masterMessageType);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("Error extracting message type", t);
            throw new ApsSystemException("Error extracting message type", t);
        }
        return jaxbMessageType;
    }
    
    public StringApiResponse addMessageType(JAXBMessageType jaxbMessageType) throws Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            String typeCode = jaxbMessageType.getTypeCode();
            IApsEntity masterMessageType = this.getMessageManager().getEntityPrototype(typeCode);
            if (null != masterMessageType) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, 
						"Message type with code '" + typeCode + "' already exists", Response.Status.CONFLICT);
            }
            if (typeCode == null || typeCode.length() != 3) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, 
						"Invalid type code - '" + typeCode + "'", Response.Status.CONFLICT);
            }
            Map<String, AttributeInterface> attributes = this.getMessageManager().getEntityAttributePrototypes();
            IApsEntity messageType = jaxbMessageType.buildEntityType(this.getMessageManager().getEntityClass(), attributes);
            ((IEntityTypesConfigurer) this.getMessageManager()).addEntityPrototype(messageType);
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
        	_logger.error("Error adding message type", t);
            throw new ApsSystemException("Error adding message type", t);
        }
        return response;
    }
    
    public StringApiResponse updateMessageType(JAXBMessageType jaxbMessageType) throws Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            String typeCode = jaxbMessageType.getTypeCode();
            IApsEntity masterMessageType = this.getMessageManager().getEntityPrototype(typeCode);
            if (null == masterMessageType) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, 
						"Message type with code '" + typeCode + "' doesn't exist", Response.Status.CONFLICT);
            }
            Map<String, AttributeInterface> attributes = this.getMessageManager().getEntityAttributePrototypes();
            IApsEntity messageType = jaxbMessageType.buildEntityType(this.getMessageManager().getEntityClass(), attributes);
            ((IEntityTypesConfigurer) this.getMessageManager()).updateEntityPrototype(messageType);
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
        	_logger.error("Error updating Message type", t);
            throw new ApsSystemException("Error updating Message type", t);
        }
        return response;
    }
    
    public void deleteMessageType(Properties properties) throws ApiException, Throwable {
        try {
            String typeCode = properties.getProperty("typeCode");
            IApsEntity masterMessageType = this.getMessageManager().getEntityPrototype(typeCode);
            if (null == masterMessageType) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, 
						"Message type with code '" + typeCode + "' doesn't exist", Response.Status.CONFLICT);
            }
            EntitySearchFilter filter = new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, false, typeCode, false);
            List<String> messageIds = this.getMessageManager().searchId(new EntitySearchFilter[]{filter});
            if (null != messageIds && !messageIds.isEmpty()) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, 
						"Message type '" + typeCode + "' are used into " + messageIds.size() + " messages", Response.Status.CONFLICT);
            }
            ((IEntityTypesConfigurer) this.getMessageManager()).removeEntityPrototype(typeCode);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("Error deleting message type", t);
            throw new ApsSystemException("Error deleting message type", t);
        }
    }
	
	protected IFormManager getMessageManager() {
		return _messageManager;
	}
	public void setMessageManager(IFormManager messageManager) {
		this._messageManager = messageManager;
	}
	
	private IFormManager _messageManager;
	
}
