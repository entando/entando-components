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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.content;

import java.util.Collection;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.WorkflowSearchFilter;

/**
 * @author E.Santoboni
 */
public interface IContentSearcherDAO {
	
    @Deprecated
	public List<String> loadContentsId(List<WorkflowSearchFilter> workflowFilters, String[] categories, EntitySearchFilter[] filters, Collection<String> userGroupCodes);

    public List<String> loadContentsId(List<WorkflowSearchFilter> workflowFilters, String[] categories, boolean orClauseCategoryFilter, EntitySearchFilter[] filters, Collection<String> userGroupCodes);

    public int countContents(List<WorkflowSearchFilter> workflowFilters, String[] categories, boolean orClauseCategoryFilter, EntitySearchFilter[] filters, Collection<String> userGroupCodes);
	
}
