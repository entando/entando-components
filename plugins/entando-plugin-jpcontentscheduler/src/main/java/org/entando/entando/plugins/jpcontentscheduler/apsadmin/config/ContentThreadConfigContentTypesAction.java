/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpcontentscheduler.apsadmin.config;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.ITreeNodeBaseActionHelper;
import com.agiletec.apsadmin.system.TreeNodeWrapper;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.IContentSchedulerManager;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentThreadConfig;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentTypeElem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.apsadmin.system.AbstractTreeAction;

import static com.agiletec.apsadmin.system.BaseAction.FAILURE;

import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class ContentThreadConfigContentTypesAction extends AbstractTreeAction {

    private static final Logger _logger = LoggerFactory.getLogger(ContentThreadConfigContentTypesAction.class);

    // private static final String THREAD_CONFIG_SESSION_PARAM = "threadConfig";
    private static final String THREAD_CONFIG_SESSION_PARAM_CONTENT_TYPES = "threadConfigContentTypes";

    private static final String ALL_TYPES = "*";

    /**
     * entrypoint
     *
     * @return
     */
    public String viewContentTypes() {
        try {
            this.setConfigItemOnSession();

        } catch (Throwable t) {
            _logger.error("Error in viewUsers", t);
            return FAILURE;
        }
        return Action.SUCCESS;
    }

    public String entryContentType() {

        String contentTypeParam = this.getRequest().getParameter("contentType");
        if (StringUtils.isNotBlank(contentTypeParam)) {
            List<ContentTypeElem> types = this.getTypes();
            ContentTypeElem elem = null;
            for (ContentTypeElem contentTypeElem : types) {
                if (contentTypeElem.getContentType().equalsIgnoreCase(this.getContentType())) {
                    if (contentTypeElem.getContentType().equalsIgnoreCase(contentTypeParam)) {
                        elem = contentTypeElem;
                        break;
                    }

                }
            }
            this.setCategoryCodes(elem.getIdsCategories());
            this.setContentTypeElem(elem);
        }

        return Action.SUCCESS;
    }

    public String addContentType() {
        try {
            boolean alreadyExist = false;
            List<ContentTypeElem> config = this.getTypes();
            ContentTypeElem elem = this.getContentTypeElem();
            elem.setIdsCategories(this.getCategoryCodes());
            ContentTypeElem existingElem = null;
            for (int i = 0; i < config.size(); i++) {
                if (config.get(i).getContentType().equalsIgnoreCase(elem.getContentType())) {
                    alreadyExist = true;
                    config.set(i, elem);
                    break;
                }
            }
            if (!alreadyExist) {
                config.add(elem);
            }

            this.setConfigItemOnSession(config);

        } catch (Throwable t) {
            _logger.error("Error in addContentType", t);
            return FAILURE;
        }
        return Action.SUCCESS;
    }

    public String trashContentType() {
        try {

            List<ContentTypeElem> config = this.getTypes();
            String contentType = this.getContentType();
            boolean existsContentType = false;
            for (ContentTypeElem contentTypeElem : config) {
                if (contentTypeElem.getContentType().equalsIgnoreCase(contentType)) {
                    existsContentType = true;
                    break;
                }
            }
            if (!existsContentType) {
                String[] args = {contentType};
                this.addActionError(this.getText("jpcontentscheduler.removeContentType.error", args));
                return INPUT;
            }

        } catch (Throwable t) {
            _logger.error("error in trash", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String removeContentType() {
        try {
            List<ContentTypeElem> config = this.getTypes();

            if (StringUtils.isNotBlank(this.getContentType())) {
                ContentTypeElem elem = null;
                for (ContentTypeElem contentTypeElem : config) {
                    if (contentTypeElem.getContentType().equalsIgnoreCase(this.getContentType())) {
                        elem = contentTypeElem;
                        break;
                    }
                }
                if (null != elem) {
                    config.remove(elem);
                }
            }
            this.setConfigItemOnSession(config);

            this.addActionMessage(this.getText("jpcontentscheduler.removeContentType.success"));
        } catch (Throwable t) {
            _logger.error("Error in removeContentType", t);
            return FAILURE;
        }
        return Action.SUCCESS;
    }

    public String saveContentTypeItem() {
        try {
            ContentThreadConfig config = this.getContentSchedulerManager().getConfig();
            config.setTypesList(this.getTypes());

            this.getContentSchedulerManager().updateConfig(config);

            this.addActionMessage(this.getText("jpcontentscheduler.saveItem.success"));
        } catch (Throwable t) {
            _logger.error("Error saving ContentTypeItem", t);
            return FAILURE;
        }
        return Action.SUCCESS;
    }

    public String joinCategory() {
        return this.joinRemoveCategory(true, this.getCategoryCode());
    }

    /**
     * Executes the specific action in order to remove the association between a
     * category and the resource on edit.
     *
     * @return The result code.
     */
    public String removeCategory() {
        return this.joinRemoveCategory(false, this.getCategoryCode());
    }

    private String joinRemoveCategory(boolean isJoin, String categoryCode) {
        try {
            ITreeNode category = this.getCategory(categoryCode);
            if (category == null) {
                return SUCCESS;
            }
            List<String> categories = this.getCategoryCodes();
            if (isJoin) {
                if (!categories.contains(categoryCode)) {
                    categories.add(categoryCode);
                }
            } else {
                categories.remove(categoryCode);
            }
        } catch (Throwable t) {
            _logger.error("error in joinRemoveCategory", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public TreeNodeWrapper getCategory(String categoryCode) {
        TreeNodeWrapper result = null;
        ITreeNode category = this.getCategoryManager().getNode(categoryCode);
        if (category != null) {
            ITreeNode categoryParent = this.getCategoryManager().getNode(category.getParentCode());
            result = new TreeNodeWrapper(category, categoryParent, this.getCurrentLang().getCode(), this.getCategoryManager());
        }
        return result;
    }

    /**
     * Restutuisce la root delle categorie.
     *
     * @return La root delle categorie.
     */
    public TreeNodeWrapper getCategoryRoot() {
        ITreeNode categoryNode = this.getCategoryManager().getRoot();
        return new TreeNodeWrapper(categoryNode, categoryNode, this.getCurrentLang().getCode(), this.getCategoryManager());
    }

    private List<ContentTypeElem> setConfigItemOnSession() {
        List<ContentTypeElem> types = this.getContentSchedulerManager().getConfig().getTypesList();
        List<ContentTypeElem> types2 = new ArrayList<>();
        for (ContentTypeElem contentTypeElem : types) {
            types2.add(contentTypeElem);
        }
        this.getRequest().getSession().setAttribute(THREAD_CONFIG_SESSION_PARAM_CONTENT_TYPES, types2);
        return types;
    }

    private void setConfigItemOnSession(List<ContentTypeElem> config) {
        this.getRequest().getSession().setAttribute(THREAD_CONFIG_SESSION_PARAM_CONTENT_TYPES, config);
    }

    public List<ContentTypeElem> getTypes() {
        return (List<ContentTypeElem>) this.getRequest().getSession().getAttribute(THREAD_CONFIG_SESSION_PARAM_CONTENT_TYPES);
    }

    public List<SmallEntityType> getContentTypes() {
        List<SmallEntityType> smallContentTypes = this.getContentManager().getSmallEntityTypes();
        return smallContentTypes;
    }

    public void setBaseConfigManager(ConfigInterface baseConfigManager) {
        this._baseConfigManager = baseConfigManager;
    }

    public ConfigInterface getBaseConfigManager() {
        return _baseConfigManager;
    }

    public IContentSchedulerManager getContentSchedulerManager() {
        return _contentSchedulerManager;
    }

    public void setContentSchedulerManager(IContentSchedulerManager contentSchedulerManager) {
        this._contentSchedulerManager = contentSchedulerManager;
    }

    public IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    public ICategoryManager getCategoryManager() {
        return _categoryManager;
    }

    public void setCategoryManager(ICategoryManager categoryManager) {
        this._categoryManager = categoryManager;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ContentTypeElem getContentTypeElem() {
        return contentTypeElem;
    }

    public void setContentTypeElem(ContentTypeElem contentTypeElem) {
        this.contentTypeElem = contentTypeElem;
    }

    public String getCategoryCode() {
        return _categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this._categoryCode = categoryCode;
    }

    public List<String> getCategoryCodes() {
        return _categoryCodes;
    }

    public void setCategoryCodes(List<String> categoryCodes) {
        this._categoryCodes = categoryCodes;
    }

    protected ITreeNodeBaseActionHelper getTreeHelper() {
        return _treeHelper;
    }

    public void setTreeHelper(ITreeNodeBaseActionHelper treeHelper) {
        this._treeHelper = treeHelper;
    }

    private List<String> _categoryCodes = new ArrayList<String>();
    private ConfigInterface _baseConfigManager;
    private IContentSchedulerManager _contentSchedulerManager;
    private ITreeNodeBaseActionHelper _treeHelper;
    private IContentManager contentManager;
    private ICategoryManager _categoryManager;
    private String contentType;

    private ContentTypeElem contentTypeElem;
    private String _categoryCode;
}
