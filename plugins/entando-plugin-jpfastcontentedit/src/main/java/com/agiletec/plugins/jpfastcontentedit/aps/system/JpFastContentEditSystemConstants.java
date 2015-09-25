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
package com.agiletec.plugins.jpfastcontentedit.aps.system;

/**
 * @author E.Santoboni
 */
public interface JpFastContentEditSystemConstants {
	
	public static final String FRONT_CONTENT_ACTION_HELPER = "jpfastcontenteditContentActionHelper";
	
	/**
	 * Nome del parametro (di request e di sessione) rappresentante il codice della pagina 
	 * di provenienza e di destinazione finale una volta terminate le operazione di edit contenuto. 
	 */
	public static final String FINAL_DEST_PAGE_PARAM_NAME = "jpfastcontentedit_finalDestPage";
	
	public static final String TYPE_CODE_SHOWLET_PARAM_NAME = "typeCode";
	
	public static final String AUTHOR_ATTRIBUTE_SHOWLET_PARAM_NAME = "authorAttribute";
	
	public static final String CONTENT_DISABLING_CODE = "onFastContentEdit";
	
	public final static String ACTION_PATH_FOR_CONTENT_EDIT = "/ExtStr2/do/jpfastcontentedit/Content/entryAction.action";
	
}