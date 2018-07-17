/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.page;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.lang.events.LangsChangedObserver;
import com.agiletec.aps.system.services.pagemodel.PageModelUtilizer;
import com.agiletec.aps.system.services.pagemodel.events.PageModelChangedObserver;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the page manager service class. Pages are held in a tree-like
 * structure, to allow a hierarchical access, and stored in a map, to allow a
 * key-value type access. In the tree, the father points the son and vice versa;
 * the order between the pages in the same level is always kept.
 *
 * @author M.Diana - E.Santoboni
 */
public class KiePageManager extends PageManager implements IPageManager, GroupUtilizer, LangsChangedObserver, PageModelUtilizer, PageModelChangedObserver {

    private static final Logger _kielogger = LoggerFactory.getLogger(KiePageManager.class);

    /**
     * Remove a widget from the given page.
     *
     * @param pageCode the code of the page
     * @param pos The position in the page to free
     * @throws ApsSystemException In case of error
     */
    @Override
    public void removeWidget(String pageCode, int pos) throws ApsSystemException {
        try {
            Widget widget = this.getDraftPage(pageCode).getWidgets()[pos];
            super.removeWidget(pageCode, pos);
            String stringId = widget != null ? widget.getConfig().getProperty(KieBpmSystemConstants.WIDGET_PARAM_DATA_UX_ID) : null;

            if (stringId != null) {
                int modelId = Integer.valueOf(stringId);
                DataObjectModel model = new DataObjectModel();
                model.setId(modelId);
                this.getDataObjectModelManager().removeDataObjectModel(model);
            }
        } catch (Throwable t) {
            String message = "Error removing the widget from the page '" + pageCode + "' in the frame " + pos;
            _kielogger.error("Error removing the widget from the page '{}' in the frame {}", pageCode, pos, t);
            throw new ApsSystemException(message, t);
        }
    }

    public IDataObjectModelManager getDataObjectModelManager() {
        return _dataObjectModelManager;
    }

    public void setDataObjectModelManager(IDataObjectModelManager _dataObjectModelManager) {
        this._dataObjectModelManager = _dataObjectModelManager;
    }

    private IDataObjectModelManager _dataObjectModelManager;
}
