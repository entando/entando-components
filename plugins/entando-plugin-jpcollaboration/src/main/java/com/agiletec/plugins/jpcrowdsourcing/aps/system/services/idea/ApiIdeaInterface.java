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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.category.helper.ICategoryActionHelper;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.common.api.CollaborationAbstractApiInterface;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.api.JAXBIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.api.JAXBVote;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IIdeaInstanceManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IdeaInstance;
import com.agiletec.plugins.jpcrowdsourcing.apsadmin.util.SmallCategory;

public class ApiIdeaInterface extends CollaborationAbstractApiInterface {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiIdeaInterface.class);

	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/ideaInstances?
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public List<String> getIdeaListForApi(Properties properties) throws Throwable {
		List<String> result = new ArrayList<String>();
		try {
			Collection<String> groupCodes = this.extractGroups(properties);
			//required params
			String codeParam = properties.getProperty("code");
			if (StringUtils.isNotBlank(codeParam)) codeParam = URLDecoder.decode(codeParam, "UTF-8");
			IdeaInstance instance = this.getIdeaInstanceManager().getIdeaInstance(codeParam);
			if (null == instance) {
				_logger.warn("instance {} not found", codeParam);
				return null;
			}
			if (!CollectionUtils.containsAny(groupCodes, instance.getGroups())) {
				_logger.warn("the current user is not granted to any group required by instance {}", codeParam);
				return null;
			}
			//optional params
			String textParam = properties.getProperty("text");
			if (StringUtils.isNotBlank(textParam)) textParam = URLDecoder.decode(codeParam, "UTF-8");
			String tagParam = properties.getProperty("tag");
			String orderParam = properties.getProperty("order");
			Integer order = null;
			if (StringUtils.isNotBlank(orderParam) && orderParam.equalsIgnoreCase("latest")) {
				order = IIdeaDAO.SORT_LATEST;
			} else if (StringUtils.isNotBlank(orderParam) && orderParam.equalsIgnoreCase("rated")) {
				order = IIdeaDAO.SORT_MOST_RATED;
			}
			result = this.getIdeaManager().searchIdeas(codeParam, IIdea.STATUS_APPROVED, textParam, tagParam, order);
		} catch (Throwable t) {
			_logger.error("Error loading idea list", t);
			throw new ApsSystemException("Error loading idea list", t);
		}
		return result;
	}

	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/idea?code=1
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public JAXBIdea getIdeaForApi(Properties properties) throws Throwable {
		JAXBIdea jaxbIdea = null;
		try {
			String codeParam = properties.getProperty("code");
			if (StringUtils.isNotBlank(codeParam)) codeParam = URLDecoder.decode(codeParam, "UTF-8");
			IIdea idea = this.getIdeaManager().getIdea(codeParam);
			if (null == idea) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + codeParam + "' does not exist", Response.Status.CONFLICT);
			}
			UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
			Collection<Integer> userStatusList = this.extractIdeaStatusListForUser(user);

			IdeaInstance instance = this.getIdeaInstanceManager().getIdeaInstance(idea.getInstanceCode());
			if (null == instance) {
				_logger.warn("instance {} not found", idea.getInstanceCode());
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + idea.getId() + "' does not exist", Response.Status.CONFLICT);
			}
			if (!isAuthOnInstance(user, instance)) {
				_logger.warn("the current user is not granted to any group required by instance {}", idea.getId());
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + idea.getId() + "' does not exist", Response.Status.CONFLICT);
			}

			if (!userStatusList.contains(idea.getStatus())) {
				_logger.warn("the current user is not granted to access to idea {} due to the status {}", codeParam, idea.getStatus());
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + codeParam + "' does not exist", Response.Status.CONFLICT);
			}
			jaxbIdea = new JAXBIdea(idea);
			//TODO CREATE ROLE
			if (null == user || !this.getAuthorizationManager().isAuthOnPermission(user, Permission.SUPERUSER)) {
				if (null != jaxbIdea) {
					jaxbIdea.getCommentsNotApproved().clear();
					jaxbIdea.getCommentsToApprove().clear();
				}
			}
		} catch (ApiException ae) {
			throw ae;
		} catch (Throwable t) {
			_logger.error("Error loading idea", t);
			throw new ApsSystemException("Error loading idea", t);
		}
		return jaxbIdea;
	}

	public StringApiResponse voteIdea(JAXBVote vote, Properties properties) throws ApiException, Throwable {
		StringApiResponse response = new StringApiResponse();
		try {
			String ideaId = vote.getIdeaId();
			if (StringUtils.isNotBlank(ideaId)) ideaId = URLDecoder.decode(ideaId, "UTF-8");

			IIdea idea = this.getIdeaManager().getIdea(ideaId);
			if (null == idea) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + ideaId + "' does not exist", Response.Status.CONFLICT);
			}
			UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);

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
			String voteType = vote.getType();
			if (StringUtils.isBlank(voteType)) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Vote is required", Response.Status.CONFLICT);
			}
			if (!voteType.equals("like") && !voteType.equals("unlike")) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Vote invalid. Accepted values are 'like' and 'unlike'", Response.Status.CONFLICT);
			}
			if (StringUtils.isNotBlank(voteType)) voteType = URLDecoder.decode(voteType, "UTF-8");
			if (voteType.equalsIgnoreCase("like")) {
				((Idea)idea).setVotePositive(idea.getVotePositive() + 1);
				this.getIdeaManager().updateIdea(idea);
			} else if (voteType.equalsIgnoreCase("unlike")) {
				((Idea)idea).setVoteNegative(idea.getVoteNegative() + 1);
				this.getIdeaManager().updateIdea(idea);
			}
			response.setResult(IResponseBuilder.SUCCESS, null);
		} catch (ApiException ae) {
			response.addErrors(ae.getErrors());
			response.setResult(IResponseBuilder.FAILURE, null);
		} catch (Throwable t) {
			_logger.error("Error on vote", t);
			throw new ApsSystemException("Error on vote", t);
		}
		return response;
	}

	public StringApiResponse addIdea(JAXBIdea jaxbIdea, Properties properties) throws ApiException, Throwable {
		StringApiResponse response = new StringApiResponse();
		try {
			UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
			String langCode = (String) properties.get(SystemConstants.API_LANG_CODE_PARAMETER);

			String instanceCode = jaxbIdea.getInstanceCode();
			IdeaInstance instance = this.getIdeaInstanceManager().getIdeaInstance(instanceCode);
			if (null == instance) {
				_logger.warn("instance {} not found", instanceCode);
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "IdeaInstance with code '" + instanceCode + "' does not exist", Response.Status.CONFLICT);
			}
			if (!isAuthOnInstance(user, instance)) {
				_logger.warn("the current user is not granted to any group required by instance {}", instance.getCode());
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "IdeaInstance with code '" + instance.getCode() + "' does not exist", Response.Status.CONFLICT);
			}
			if (StringUtils.isBlank(jaxbIdea.getTitle())) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Title is required", Response.Status.CONFLICT);
			}
			if (StringUtils.isBlank(jaxbIdea.getDescr())) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Description is required", Response.Status.CONFLICT);
			}
			if (null == jaxbIdea.getTags() || jaxbIdea.getTags().isEmpty()) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Tags is required", Response.Status.CONFLICT);
			}
			Set<String> tags = this.joinCategories(jaxbIdea, langCode);
			Idea idea = jaxbIdea.getIdea();
			idea.setTags(new ArrayList<String>(tags));
			idea.setUsername(user.getUsername());
			this.getIdeaManager().addIdea(idea);
			response.setResult(IResponseBuilder.SUCCESS, null);
		} catch (ApiException ae) {
			response.addErrors(ae.getErrors());
			response.setResult(IResponseBuilder.FAILURE, null);
		} catch (Throwable t) {
			_logger.error("Error on add idea", t);
			throw new ApsSystemException("Error on add idea", t);
		}
		return response;
	}

	public StringApiResponse updateIdea(JAXBIdea jaxbIdea, Properties properties) throws ApiException, Throwable {
		StringApiResponse response = new StringApiResponse();
		try {
			String langCode = (String) properties.get(SystemConstants.API_LANG_CODE_PARAMETER);
			String instanceCode = jaxbIdea.getInstanceCode();
			String id = jaxbIdea.getId();
			IIdea clone = this.getIdeaManager().getIdea(id);
			if (null == clone) {
				_logger.warn("idea {} not found", id);
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + id + "' does not exist", Response.Status.CONFLICT);				
			}
			IdeaInstance instance = this.getIdeaInstanceManager().getIdeaInstance(instanceCode);
			if (null == instance) {
				_logger.warn("instance {} not found", instanceCode);
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "IdeaInstance with code '" + instanceCode + "' does not exist", Response.Status.CONFLICT);
			}
			if (StringUtils.isBlank(jaxbIdea.getTitle())) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Title is required", Response.Status.CONFLICT);
			}
			if (StringUtils.isBlank(jaxbIdea.getDescr())) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Description is required", Response.Status.CONFLICT);
			}
			if (null == jaxbIdea.getTags() || jaxbIdea.getTags().isEmpty()) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Al least one tag is required", Response.Status.CONFLICT);
			}
			int status = jaxbIdea.getStatus();
			if (!ArrayUtils.contains(IIdea.STATES, status)) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Invalid status specified: " + status,  Response.Status.CONFLICT);
			}
			Set<String> tags = this.joinCategories(jaxbIdea, langCode);
			Idea idea = jaxbIdea.getIdea();
			idea.setTags(new ArrayList<String>(tags));
			this.getIdeaManager().updateIdea(idea);
			response.setResult(IResponseBuilder.SUCCESS, null);
		} catch (ApiException ae) {
			response.addErrors(ae.getErrors());
			response.setResult(IResponseBuilder.FAILURE, null);
		} catch (Throwable t) {
			_logger.error("Error on update idea", t);
			throw new ApsSystemException("Error on update idea", t);
		}
		return response;
	}

	public StringApiResponse deleteIdea(Properties properties) throws ApiException, Throwable {
		StringApiResponse response = new StringApiResponse();
		try {
			String code = properties.getProperty("code");
			IIdea idea = this.getIdeaManager().getIdea(code);
			if (null == idea) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Idea with code '" + code + "' does not exist", Response.Status.CONFLICT);
			}
			this.getIdeaManager().deleteIdea(code);
			response.setResult(IResponseBuilder.SUCCESS, null);
		} catch (ApiException ae) {
			response.addErrors(ae.getErrors());
			response.setResult(IResponseBuilder.FAILURE, null);
		} catch (Throwable t) {
			_logger.error("Error on update idea", t);
			throw new ApsSystemException("Error on deleteIdea idea", t);
		}
		return response;
	}

	private Set<String> joinCategories(JAXBIdea jaxbIdea, String langCode) throws Throwable {
		Set<String> tags = new HashSet<String>();
		List<SmallCategory> ideaTags = this.getIdeaTags(false, langCode);
		Iterator<String> it = jaxbIdea.getTags().iterator();
		while (it.hasNext()) {
			String jaxbValue = it.next();
			String code = "";
			for (int i = 0; i < ideaTags.size(); i++) {
				SmallCategory smallCategory = ideaTags.get(i);
				if(jaxbValue.equals(smallCategory.getTitle())){
					code = smallCategory.getCode();
					break;
				}
			}
			Category category = this.getCategoryManager().getCategory(code);
			if (null == category) {
				String categoryCode = this.getHelper().buildCode(jaxbValue, "tag", 30);
				category = new Category();
				category.setCode(categoryCode);
				List<Lang> langs = this.getLangManager().getLangs();
				for (int i = 0; i < langs.size(); i++) {
					Lang lang = langs.get(i);
					category.setTitle(lang.getCode(), jaxbValue);
				}
				category.setParentCode(this.getIdeaManager().getCategoryRoot());
				this.getCategoryManager().addCategory(category);
				tags.add(categoryCode);
			} else {
				tags.add(code);
			}
		}
		return tags;
	}

	private List<SmallCategory> getIdeaTags(boolean completeTitle, String langCode) {
		List<SmallCategory> categories = new ArrayList<SmallCategory>();
		try {

			String nodeRootCode = this.getIdeaManager().getCategoryRoot();
			categories = this.getCategoryLeaf(nodeRootCode, langCode, completeTitle);
		} catch (Throwable t) {
			_logger.error("error in getIdeaTags", t);
			throw new RuntimeException("Errore in estrazione categorie");
		}
		return categories;
	}

	private List<SmallCategory> getCategoryLeaf(String nodeRootCode, String langCode, boolean completeTitle) {
		List<SmallCategory> categories = new ArrayList<SmallCategory>();
		try {
			Category root = (Category) this.getCategoryManager().getCategory(nodeRootCode);
			this.addSmallCategory(categories, root, langCode, completeTitle, true);
		} catch (Throwable t) {
			_logger.error("error in methodName", t);
			throw new RuntimeException("Errore in estrazione categorie");
		}
		return categories;
	}

	private void addSmallCategory(List<SmallCategory> categories, Category parentCat, String langCode, boolean completeTitle, boolean isFirst) {
		for (Category cat : parentCat.getChildren()) {
			if (null == cat.getChildren() || cat.getChildren().length == 0) {
				SmallCategory catSmall = new SmallCategory();
				catSmall.setCode(cat.getCode());
				if (!completeTitle) {
					catSmall.setTitle(cat.getTitles().getProperty(langCode));
				} else {
					catSmall.setTitle(cat.getFullTitle(langCode));
				}
				categories.add(catSmall);
			}
			this.addSmallCategory(categories, cat, langCode, completeTitle, false);
		}
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

	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}

	protected ICategoryActionHelper getHelper() {
		return _helper;
	}
	public void setHelper(ICategoryActionHelper helper) {
		this._helper = helper;
	}

	private IIdeaInstanceManager _ideaInstanceManager;
	private IIdeaManager _ideaManager;
	private ICategoryManager _categoryManager;
	private ILangManager _langManager;
	private ICategoryActionHelper _helper;
}
