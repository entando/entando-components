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
package com.agiletec.plugins.jpblog.aps.system;

public interface JpblogSystemConstants {
	
	/**
	 * Restituisce le informazioni sui contenuti a servizio del blog
	 */
	public static final String BLOG_MANAGER = "jpblogBlogManager";

	/**
	 * Helper a servizio del tag che eroga le occorrenze dei post del blog ragguppati per mese
	 */
	public static final String BLOG_ARCHIVE_TAG_HELPER = "jpblogBlogArchiveTagHelper";


	public static final String CONFIG_ITEM = "jpblog_config";
	
	/**
	 * The name of the attribute that contains the blog post title
	 */
	public static final String ATTRIBUTE_ROLE_TITLE = "jpblog:title";

	/**
	 * The name of the attribute that contains the blog post body
	 */
	public static final String ATTRIBUTE_ROLE_BODY = "jpblog:body";
	
	/**
	 * The name of the attribute that contains the blog post date
	 */
	public static final String ATTRIBUTE_ROLE_DATE = "jpblog:date";

	/**
	 * The name of the attribute that contains the blog post author
	 */
	public static final String ATTRIBUTE_ROLE_AUTHOR = "jpblog:author";
	
}