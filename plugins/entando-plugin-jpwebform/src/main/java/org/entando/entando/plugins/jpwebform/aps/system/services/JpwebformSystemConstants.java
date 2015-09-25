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
package org.entando.entando.plugins.jpwebform.aps.system.services;

/**
 * Interface for 'jpwebform' plugin system constants.
 * @author E.Santoboni
 */
public interface JpwebformSystemConstants {
	
	public static final String MESSAGE_NOTIFIER_CONFIG_ITEM = "jpwebform_messageNotifierConfig";
	public static final String MESSAGE_STEPS_CONFIG_ITEM = "jpwebform_stepsConfig";
	public static final String FORM_MANAGER = "jpwebformFormManager";
	
	public static final String BASE_ENTITY_RENDERER = "jpwebformBaseMessageRenderer";
	
	public static final String WIDGET_PARAM_TYPECODE = "typeCode";
	public static final String WIDGET_PARAM_STATUS = "status";
	public static final String WIDGET_PARAM_MAXELEMFORITEM = "maxElemForItem";
	public static final String WIDGET_PARAM_MAXEELEMENTS = "maxElements";
	public static final String WIDGET_PARAM_TITLE = "title";
	public static final String WIDGET_PARAM_PAGE_LINK = "pageLink";
	public static final String WIDGET_PARAM_PAGE_LINK_DESCR = "linkDescr";
	
	
	public static final String SESSION_PARAM_NAME_CURRENT_FORM = "jpwebformCurrentForm";
	public static final String SESSION_PARAM_NAME_CURRENT_VERSION_CONFIG = "jpwebformCurrentVersionConfig";
	
	public static final String DISABLING_CODE_ONSTEP_PREFIX = "disableOnStep@";
	
	public static final String CONFIRM_STEP_CODE = "confirm";
	public static final String COMPLETED_STEP_CODE = "ending";
	public static final String[] RESERVED_STEP_CODES = {CONFIRM_STEP_CODE, COMPLETED_STEP_CODE};
	
	
	public static final String PREVIEW_PARAM_TYPE = "${entityTypeCode}";
	public static final String PREVIEW_PARAM_STEP = "${stepCode}";
	
	
	public static final String PREVIEW_USER_GUI = "userGui";
	public static final String PREVIEW_CSS_GUI = "userCss";
	
	public static final String WIDGET_NAME_FORM = "jpwebform_form";
	/**
	 * @deprecated Use {@link #WIDGET_NAME_FORM} instead
	 */
	public static final String SHOWLET_NAME_FORM = WIDGET_NAME_FORM;
	
	
	public static final String STEP_CONFIG_SESSION_PARAM = "jpwebform_stepConfig_sessionParam";
	
	public static final String STEP_CONFIG_MODIFY_MARKER_SESSION_PARAM = "jpwebform_stepConfig_modifyMarker_sessionParam";
	
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_TYPECODE} instead
	 */
	public static final String SHOWLET_PARAM_TYPECODE = WIDGET_PARAM_TYPECODE;
	
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_STATUS} instead
	 */
	public static final String SHOWLET_PARAM_STATUS = WIDGET_PARAM_STATUS;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_MAXELEMFORITEM} instead
	 */
	public static final String SHOWLET_PARAM_MAXELEMFORITEM = WIDGET_PARAM_MAXELEMFORITEM;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_MAXEELEMENTS} instead
	 */
	public static final String SHOWLET_PARAM_MAXEELEMENTS = WIDGET_PARAM_MAXEELEMENTS;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_TITLE} instead
	 */
	public static final String SHOWLET_PARAM_TITLE = WIDGET_PARAM_TITLE;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_PAGE_LINK} instead
	 */
	public static final String SHOWLET_PARAM_PAGE_LINK = WIDGET_PARAM_PAGE_LINK;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_PAGE_LINK_DESCR} instead
	 */
	public static final String SHOWLET_PARAM_PAGE_LINK_DESCR = WIDGET_PARAM_PAGE_LINK_DESCR;
	
}