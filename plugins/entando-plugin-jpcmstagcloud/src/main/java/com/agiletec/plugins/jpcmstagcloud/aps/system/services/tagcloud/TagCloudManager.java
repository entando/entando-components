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
package com.agiletec.plugins.jpcmstagcloud.aps.system.services.tagcloud;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.IPublicContentSearcherDAO;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedObserver;
import com.agiletec.plugins.jpcmstagcloud.aps.system.JpcmstagcloudSystemConstants;

/**
 * @author E.Santoboni
 */
public class TagCloudManager extends AbstractService 
        implements ITagCloudManager, PublicContentChangedObserver {

	private static final Logger _logger = LoggerFactory.getLogger(TagCloudManager.class);
	
	@Override
    public void init() throws Exception {
        this.checkCategoryRoot();
        _logger.debug("{}: ready ", this.getClass().getName());
    }
    
    private void checkCategoryRoot() {
        Category tagCloudRoot = this.getCategoryManager().getCategory(this.getTagCloudCategoryRoot());
        if (null != tagCloudRoot) {
            return;
        }
        try {
            tagCloudRoot = new Category();
            tagCloudRoot.setCode(this.getTagCloudCategoryRoot());
            Category root = this.getCategoryManager().getRoot();
            ApsProperties titles = new ApsProperties();
            Set<Object> langCodes = root.getTitles().keySet();
            Iterator<Object> iter = langCodes.iterator();
            while (iter.hasNext()) {
                Object langCode = (Object) iter.next();
                titles.put(langCode, "Tag Cloud Root");
            }
            tagCloudRoot.setTitles(titles);
            tagCloudRoot.setParent(root);
            tagCloudRoot.setParentCode(root.getCode());
            this.getCategoryManager().addCategory(tagCloudRoot);
            _logger.debug("TagCloud category root Created ");
        } catch (Throwable t) {
        	_logger.error("Error on adding tag cloud category root", t);
            throw new RuntimeException("Error on adding tag cloud category root", t);
        }
    }
    
	@Override
    public void updateFromPublicContentChanged(PublicContentChangedEvent event) {
        try {
            this.refresh();
        } catch (Throwable t) {
        	_logger.error("Error refreshing service", t);
        }
    }
    
	@Override
    protected void release() {
        this.setElaborationDate(null);
        this.getGlobalCloudInfos().clear();
    }
    
	@Override
    public Map<ITreeNode, Integer> getCloudInfos(UserDetails currentUser) throws ApsSystemException {
        Map<ITreeNode, Integer> cloudInfos = null;
        if (null == this.getElaborationDate() || !this.getElaborationDate().equals(DateConverter.getFormattedDate(new Date(), "yyyyMMdd"))) {
            this.release();
            this.setElaborationDate(DateConverter.getFormattedDate(new Date(), "yyyyMMdd"));
        }
        try {
            Category root = this.getCategoryManager().getCategory(this.getTagCloudCategoryRoot());
            if (root == null || root.getChildren() == null || root.getChildren().length == 0) {
                _logger.error("Category Root '{}' null or dosn't has children", this.getTagCloudCategoryRoot());
                return new HashMap<ITreeNode, Integer>();
            }
            Set<String> userGroupCodes = this.getGroupsForSearch(currentUser);
            String key = this.createGroupMappingKey(userGroupCodes);
            Map<String, Integer> cloudInfosSmall = this.getGlobalCloudInfos().get(key);
            if (null == cloudInfosSmall) {
                cloudInfosSmall = new HashMap<String, Integer>();
                this.getGlobalCloudInfos().put(key, cloudInfosSmall);
                EntitySearchFilter[] filters = (null != this.getDelayDays() && this.getDelayDays().intValue() > 0) ? new EntitySearchFilter[]{this.getStartDateFilter()} : null;
                Category[] children = root.getChildren();
                for (int i = 0; i < children.length; i++) {
                    Category child = children[i];
                    List<String> contentsId = this.getTagCloudDAO().loadPublicContentsId(new String[]{child.getCode()}, filters, userGroupCodes);
                    cloudInfosSmall.put(child.getCode(), new Integer(contentsId.size()));
                }
            }
            cloudInfos = new HashMap<ITreeNode, Integer>(cloudInfosSmall.size());
            Iterator<String> iterCodes = cloudInfosSmall.keySet().iterator();
            while (iterCodes.hasNext()) {
                String categoryCode = iterCodes.next();
                cloudInfos.put(this.getCategoryManager().getCategory(categoryCode), cloudInfosSmall.get(categoryCode));
            }
        } catch (Throwable t) {
        	_logger.error("Error extracting cloud Infos by user {}", currentUser, t);
            throw new ApsSystemException("Error extracting cloud Infos by user " + currentUser, t);
        }
        return cloudInfos;
    }
    
	@Override
    public List<String> loadPublicTaggedContentsId(String categoryCode, UserDetails currentUser) throws ApsSystemException {
        List<String> contentsId = null;
        try {
            EntitySearchFilter[] filters = (null != this.getDelayDays() && this.getDelayDays().intValue() > 0) ? new EntitySearchFilter[]{this.getStartDateFilter()} : null;
            Set<String> userGroupCodes = this.getGroupsForSearch(currentUser);
            String[] categories = {categoryCode};
            contentsId = this.getContentManager().loadPublicContentsId(categories, filters, userGroupCodes);
        } catch (Throwable t) {
        	_logger.error("Error extracting cloud Infos by user {}", currentUser, t);
            throw new ApsSystemException("Error extracting cloud Infos by user " + currentUser, t);
        }
        return contentsId;
    }
	
    private String createGroupMappingKey(Set<String> groupSet) {
        if (groupSet.contains(Group.ADMINS_GROUP_NAME)) {
            return Group.ADMINS_GROUP_NAME;
        } else {
            StringBuilder buffer = new StringBuilder();
            List<String> groups = new ArrayList<String>(groupSet);
            Collections.sort(groups);
            for (int i = 0; i < groups.size(); i++) {
                if (i > 0) {
                    buffer.append("-");
                }
                buffer.append(groups.get(i));
            }
            return buffer.toString();
        }
    }
	
    private Set<String> getGroupsForSearch(UserDetails currentUser) {
        Set<String> groupForSearch = new HashSet<String>();
        groupForSearch.add(Group.FREE_GROUP_NAME);
		List<Authorization> authorizations = currentUser.getAuthorizations();
		for (int i = 0; i < authorizations.size(); i++) {
			Authorization authorization = authorizations.get(i);
			Group group = (null != authorization) ? authorization.getGroup() : null;
			if (null != group) {
				groupForSearch.add(group.getName());
			}
		}
        return groupForSearch;
    }

    private EntitySearchFilter getStartDateFilter() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -this.getDelayDays());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        Date startDate = calendar.getTime();
        String DATE_FORMAT = "yyyyMMddHHmmss";
        String formattedDate = DateConverter.getFormattedDate(startDate, DATE_FORMAT);
        EntitySearchFilter filter = new EntitySearchFilter(IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY, false, formattedDate, null);
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        return filter;
    }

    protected String getElaborationDate() {
        return _elaborationDate;
    }
    protected void setElaborationDate(String elaborationDate) {
        this._elaborationDate = elaborationDate;
    }
    
    protected Integer getDelayDays() {
        Integer delay = null;
        String delayParamName = JpcmstagcloudSystemConstants.DELAY_DAYS_PARAM_NAME;
        try {
            delay = Integer.parseInt(this.getConfigManager().getParam(delayParamName));
        } catch (Throwable e) {
            //nothing to catch
        }
        if (null == delay) {
            return JpcmstagcloudSystemConstants.DEFAULT_DELAY_DAYS;
        }
        return delay;
    }
    
    protected String getTagCloudCategoryRoot() {
        String categoryRootParamName = JpcmstagcloudSystemConstants.CATEGORY_ROOT_PARAM_NAME;
        String categoryRoot = this.getConfigManager().getParam(categoryRootParamName);
        if (null == categoryRoot || categoryRoot.trim().length() == 0) {
            return JpcmstagcloudSystemConstants.DEFAULT_CATEGORY_ROOT;
        }
        return categoryRoot;
    }
    
    protected Map<String, Map<String, Integer>> getGlobalCloudInfos() {
        return _globalCloudInfos;
    }
    protected void setGlobalCloudInfos(Map<String, Map<String, Integer>> globalCloudInfos) {
        this._globalCloudInfos = globalCloudInfos;
    }

    protected IContentManager getContentManager() {
        return _contentManager;
    }
    public void setContentManager(IContentManager contentManager) {
        this._contentManager = contentManager;
    }

    protected ICategoryManager getCategoryManager() {
        return _categoryManager;
    }
    public void setCategoryManager(ICategoryManager categoryManager) {
        this._categoryManager = categoryManager;
    }

    protected IGroupManager getGroupManager() {
        return _groupManager;
    }
    public void setGroupManager(IGroupManager groupManager) {
        this._groupManager = groupManager;
    }
    
    protected ConfigInterface getConfigManager() {
        return _configManager;
    }
    public void setConfigManager(ConfigInterface configManager) {
        this._configManager = configManager;
    }
    
    protected IPublicContentSearcherDAO getTagCloudDAO() {
        return _tagCloudDAO;
    }
    public void setTagCloudDAO(IPublicContentSearcherDAO tagCloudDAO) {
        this._tagCloudDAO = tagCloudDAO;
    }
    
    private String _elaborationDate;
    
    private Map<String, Map<String, Integer>> _globalCloudInfos = new HashMap<String, Map<String, Integer>>();
    
    private IContentManager _contentManager;
    private ICategoryManager _categoryManager;
    private IGroupManager _groupManager;
    private ConfigInterface _configManager;
    
    private IPublicContentSearcherDAO _tagCloudDAO;
    
}