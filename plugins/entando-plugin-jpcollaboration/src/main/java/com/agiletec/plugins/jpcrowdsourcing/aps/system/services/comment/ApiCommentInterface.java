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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment;

import java.util.Collection;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment.api.JAXBComment;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.common.api.CollaborationAbstractApiInterface;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IIdeaInstanceManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IdeaInstance;

public class ApiCommentInterface extends CollaborationAbstractApiInterface {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiCommentInterface.class);

	public JAXBComment getCommentForApi(Properties properties) throws Throwable {
		JAXBComment jaxbComment = null;
		try {
			String idParam = properties.getProperty("id");
			if (!StringUtils.isNumeric(idParam)) {
				throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "The comment id must be an integer", Response.Status.ACCEPTED);
			}
			IIdeaComment comment = this.getIdeaCommentManager().getComment(new Integer(idParam));
			if (null == comment) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "comment with code '" + idParam + "' does not exist", Response.Status.CONFLICT);
			}
			UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
			this.validateComment(comment, user);
			jaxbComment = new JAXBComment(comment);
		} catch (ApiException ae) {
			throw ae;
		} catch (Throwable t) {
			_logger.error("Error extracting comment", t);
			throw new ApsSystemException("Error extracting comment", t);
		}
		return jaxbComment;
	}

	private void validateComment(IIdeaComment comment, UserDetails user) throws Throwable {
		IIdea idea = this.getIdeaManager().getIdea(comment.getIdeaId());
		if (null == idea) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + comment.getId() + "' does not exist", Response.Status.CONFLICT);
		}
		String instanceCode = idea.getInstanceCode();
		Collection<Integer> userStatusList = this.extractIdeaStatusListForUser(user);

		IdeaInstance instance = this.getIdeaInstanceManager().getIdeaInstance(idea.getInstanceCode());
		if (null == instance) {
			_logger.warn("instance {} not found", instanceCode);
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + instanceCode + "' does not exist", Response.Status.CONFLICT);
		}
		if (!isAuthOnInstance(user, instance)) {
			_logger.warn("the current user is not granted to any group required by instance {}", instanceCode);
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + instanceCode + "' does not exist", Response.Status.CONFLICT);
		}
		if (!userStatusList.contains(idea.getStatus())) {
			_logger.warn("the current user is not granted to access to idea {} due to the status {}", instanceCode, idea.getStatus());
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + comment.getIdeaId() + "' does not exist", Response.Status.CONFLICT);
		}
	}
	
	public StringApiResponse addComment(JAXBComment jaxbComment, Properties properties) throws ApiException, Throwable {
		StringApiResponse response = new StringApiResponse();
		try {
			UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);

			String ideaId = jaxbComment.getIdeaId();
			IIdea idea = this.getIdeaManager().getIdea(ideaId);
			if (null == idea) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + ideaId + "' does not exist", Response.Status.CONFLICT);
			}
			IdeaInstance instance = this.getIdeaInstanceManager().getIdeaInstance(idea.getInstanceCode());
			if (null == instance) {
				_logger.warn("instance {} not found", idea.getInstanceCode());
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "IdeaInstance with code '" + idea.getInstanceCode() + "' does not exist", Response.Status.CONFLICT);
			}
			if (!isAuthOnInstance(user, instance)) {
				_logger.warn("the current user is not granted to any group required by instance {}", instance.getCode());
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "IdeaInstance with code '" + instance.getCode() + "' does not exist", Response.Status.CONFLICT);
			}
			if (idea.getStatus() != IIdea.STATUS_APPROVED) {
				_logger.warn("the idea {} is not in statu approved ", idea.getId(), idea.getStatus());
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + idea.getId() + "' does not exist", Response.Status.CONFLICT);
			}
			if (StringUtils.isBlank(jaxbComment.getCommentText())) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Comment Text is required", Response.Status.CONFLICT);
			}
			IdeaComment comment = jaxbComment.getComment();
			comment.setUsername(user.getUsername());
			this.getIdeaCommentManager().addComment(comment);
			response.setResult(IResponseBuilder.SUCCESS, null);
		} catch (ApiException ae) {
			response.addErrors(ae.getErrors());
			response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
        	_logger.error("Error adding comment", t);
            throw new ApsSystemException("Error adding comment", t);
        }
		return response;
	}

	public StringApiResponse updateComment(JAXBComment jaxbComment, Properties properties) throws ApiException, Throwable {
		StringApiResponse response = new StringApiResponse();
		try {
			IIdeaComment ideaComment = this.getIdeaCommentManager().getComment(jaxbComment.getId());
			if (null == ideaComment) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Comment with code '" + jaxbComment.getId() + "' does not exist", Response.Status.CONFLICT);
			}
			if (!ideaComment.getIdeaId().equals(jaxbComment.getIdeaId())) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "The ideaid '" + jaxbComment.getIdeaId() + "' is invalid", Response.Status.CONFLICT);				
			}
			String ideaId = jaxbComment.getIdeaId();
			IIdea idea = this.getIdeaManager().getIdea(ideaId);
			if (null == idea) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + ideaId + "' does not exist", Response.Status.CONFLICT);
			}
			
			int status = jaxbComment.getStatus();
			if (!ArrayUtils.contains(IIdea.STATES, status)) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Invalid status specified: " + status,  Response.Status.CONFLICT);
			}

			if (StringUtils.isBlank(jaxbComment.getCommentText())) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Comment Text is required", Response.Status.CONFLICT);
			}
			IdeaComment comment = jaxbComment.getComment();
			this.getIdeaCommentManager().updateComment(comment);
			response.setResult(IResponseBuilder.SUCCESS, null);
		} catch (ApiException ae) {
			response.addErrors(ae.getErrors());
			response.setResult(IResponseBuilder.FAILURE, null);
		} catch (Throwable t) {
			_logger.error("Error updating comment", t);
			throw new ApsSystemException("Error updating comment", t);
		}
		return response;
	}
	
	public StringApiResponse deleteComment(Properties properties) throws ApiException, Throwable {
		StringApiResponse response = new StringApiResponse();
		try {
			String idParam = properties.getProperty("id");
			if (!StringUtils.isNumeric(idParam)) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Invalid param '" + idParam , Response.Status.CONFLICT);
			}			
			IIdeaComment comment = this.getIdeaCommentManager().getComment(new Integer(idParam).intValue());
			if (null == comment) {
				_logger.warn("comment {} not found", idParam);
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Comment with id '" + idParam + "' does not exist", Response.Status.CONFLICT);
			}
			this.getIdeaCommentManager().deleteComment(comment.getId());
			response.setResult(IResponseBuilder.SUCCESS, null);
		} catch (ApiException ae) {
			response.addErrors(ae.getErrors());
			response.setResult(IResponseBuilder.FAILURE, null);
		} catch (Throwable t) {
			_logger.error("Error deleting comment", t);
			throw new ApsSystemException("Error deleting comment", t);
		}
		return response;
	}

	protected IIdeaCommentManager getIdeaCommentManager() {
		return _ideaCommentManager;
	}
	public void setIdeaCommentManager(IIdeaCommentManager ideaCommentManager) {
		this._ideaCommentManager = ideaCommentManager;
	}

	protected IIdeaInstanceManager getIdeaInstanceManager() {
		return _ideaInstanceManager;
	}
	public void setIdeaInstanceManager(IIdeaInstanceManager ideaInstanceManager) {
		this._ideaInstanceManager = ideaInstanceManager;
	}

	protected IIdeaManager getIdeaManager() {
		return _ideaManager;
	}
	public void setIdeaManager(IIdeaManager ideaManager) {
		this._ideaManager = ideaManager;
	}

	private IIdeaCommentManager _ideaCommentManager;
	private IIdeaInstanceManager _ideaInstanceManager;
	private IIdeaManager _ideaManager;
}
