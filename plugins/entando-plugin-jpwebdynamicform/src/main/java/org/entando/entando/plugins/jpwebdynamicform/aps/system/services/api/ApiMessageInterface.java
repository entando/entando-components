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
package org.entando.entando.plugins.jpwebdynamicform.aps.system.services.api;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiError;

import org.entando.entando.plugins.jpwebdynamicform.aps.system.services.api.model.JAXBMessage;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.helper.BaseFilterUtils;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;

/**
 * @author E.Santoboni
 */
public class ApiMessageInterface {
    
    public List<String> getMessages(Properties properties) throws Throwable {
        List<String> usernames = null;
        try {
            String userMessageType = properties.getProperty("typeCode");
            Message prototype = (Message) this.getMessageManager().getEntityPrototype(userMessageType);
            if (null == prototype) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, 
						"Message Type '" + userMessageType + "' does not exist", Response.Status.CONFLICT);
            }
            String langCode = properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER);
            String filtersParam = properties.getProperty("filters");
            BaseFilterUtils filterUtils = new BaseFilterUtils();
            EntitySearchFilter[] filters = filterUtils.getFilters(prototype, filtersParam, langCode);
            usernames = this.getMessageManager().searchId(userMessageType, filters);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getMessages");
            throw new ApsSystemException("Error searching usernames", t);
        }
        return usernames;
    }
    
    public JAXBMessage getMessage(Properties properties) throws ApiException, Throwable {
        JAXBMessage jaxbMessage = null;
        try {
            String id = properties.getProperty("id");
            Message userMessage = this.getMessageManager().getMessage(id);
            if (null == userMessage) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, 
						"Message '" + id + "' does not exist", Response.Status.CONFLICT);
            }
            jaxbMessage = new JAXBMessage(userMessage, null);
			List<Answer> answers = this.getMessageManager().getAnswers(id);
			if (null != answers && !answers.isEmpty()) {
				jaxbMessage.addAnswers(answers);
			}
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getMessage");
            throw new ApsSystemException("Error extracting Message", t);
        }
        return jaxbMessage;
    }
    
    public StringApiResponse addMessage(JAXBMessage jaxbMessage) throws Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            String username = jaxbMessage.getId();
            if (null != this.getMessageManager().getMessage(username)) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, 
						"Message of user '" + username + "' already exist", Response.Status.CONFLICT);
            }
            IApsEntity profilePrototype = this.getMessageManager().getEntityPrototype(jaxbMessage.getTypeCode());
            if (null == profilePrototype) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, 
						"User Message type with code '" + jaxbMessage.getTypeCode() + "' does not exist", Response.Status.CONFLICT);
            }
            Message message = (Message) jaxbMessage.buildEntity(profilePrototype, null);
            List<ApiError> errors = this.validate(message);
            if (errors.size() > 0) {
                response.addErrors(errors);
                response.setResult(IResponseBuilder.FAILURE, null);
                return response;
            }
            this.getMessageManager().addMessage(message);
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "addMessage");
            throw new ApsSystemException("Error adding Message", t);
        }
        return response;
    }
    
    private List<ApiError> validate(Message userMessage) throws ApsSystemException {
        List<ApiError> errors = new ArrayList<ApiError>();
        try {
            List<FieldError> fieldErrors = userMessage.validate(this.getGroupManager());
            if (null != fieldErrors) {
                for (int i = 0; i < fieldErrors.size(); i++) {
                    FieldError fieldError = fieldErrors.get(i);
                    if (fieldError instanceof AttributeFieldError) {
                        AttributeFieldError attributeError = (AttributeFieldError) fieldError;
                        errors.add(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, 
								attributeError.getFullMessage(), Response.Status.CONFLICT));
                    } else {
                        errors.add(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, 
								fieldError.getMessage(), Response.Status.CONFLICT));
                    }
                }
            }
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "validate");
            throw new ApsSystemException("Error validating profile", t);
        }
        return errors;
    }
    
    public void deleteMessage(Properties properties) throws ApiException, Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            String id = properties.getProperty("id");
            Message message = this.getMessageManager().getMessage(id);
            if (null == message) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, 
						"Message '" + id + "' does not exist", Response.Status.CONFLICT);
            }
			this.getMessageManager().deleteMessage(id);
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "deleteMessage");
            throw new ApsSystemException("Error deleting Message", t);
        }
    }
    
	protected IMessageManager getMessageManager() {
		return _messageManager;
	}
	public void setMessageManager(IMessageManager messageManager) {
		this._messageManager = messageManager;
	}
	
    protected IGroupManager getGroupManager() {
        return _groupManager;
    }
    public void setGroupManager(IGroupManager groupManager) {
        this._groupManager = groupManager;
    }
    
    private IMessageManager _messageManager;
    private IGroupManager _groupManager;
    
}