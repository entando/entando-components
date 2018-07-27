/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo;

import com.agiletec.ConfigTestUtils;

public class PluginConfigTestUtils extends ConfigTestUtils {
	
	@Override
	protected String[] getSpringConfigFilePaths() {
    	String[] filePaths = new String[6];
		filePaths[0] = "classpath:spring/propertyPlaceholder.xml";
		filePaths[1] = "classpath:spring/baseSystemConfig.xml";
		filePaths[2] = "classpath*:spring/aps/**/**.xml";
		filePaths[3] = "classpath*:spring/apsadmin/**/**.xml";
		filePaths[4] = "classpath*:spring/plugins/**/aps/**/**.xml";
		filePaths[5] = "classpath*:spring/plugins/**/apsadmin/**/**.xml";
		return filePaths;
    }
	
}
