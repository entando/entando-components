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
package org.entando.entando.plugins.jpsolrclient.aps.system;

/**
 * @author E.Santoboni
 */
public interface SolrConnectorSystemConstants {
	
	public static final String JPSOLRCLIENT_SYSTEM_CONFIG_NAME = "jpsolrclient_config";
	
	public static final String SERVER_URL_PARAM_NAME = "jpsolrclient_providerUrl";
	public static final String MAX_RESULT_SIZE_PARAM_NAME = "jpsolrclient_maxResultSize";
	
	public static final String SERVER_URL_DEFAULT_VALUE = "http://localhost:9080/solr/";
	public static final int MAX_RESULT_SIZE_DEFAULT_VALUE = 250;
	
}
