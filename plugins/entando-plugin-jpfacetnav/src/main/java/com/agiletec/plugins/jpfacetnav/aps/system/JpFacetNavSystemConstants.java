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
package com.agiletec.plugins.jpfacetnav.aps.system;

/**
 * @author E.Santoboni
 */
public interface JpFacetNavSystemConstants {
	
	public static final String CONTENT_FACET_MANAGER = "jpfacetnavContentFacetManager";
	
	public static final String CONTENT_FACET_NAV_HELPER = "jpfacetnavContentFacetNavHelper";
	
	public static final String CONTENT_TYPES_FILTER_WIDGET_PARAM_NAME = "contentTypesFilter";
	public static final String FACET_ROOTS_WIDGET_PARAM_NAME = "facetRootNodes";
	
	@Deprecated
	public static final String CONTENT_TYPES_FILTER_SHOWLET_PARAM_NAME = CONTENT_TYPES_FILTER_WIDGET_PARAM_NAME;
	@Deprecated
	public static final String FACET_ROOTS_SHOWLET_PARAM_NAME = FACET_ROOTS_WIDGET_PARAM_NAME;
	
}
