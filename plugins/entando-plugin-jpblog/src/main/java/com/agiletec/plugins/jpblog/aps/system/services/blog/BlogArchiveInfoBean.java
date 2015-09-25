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
package com.agiletec.plugins.jpblog.aps.system.services.blog;

import java.util.List;

public class BlogArchiveInfoBean {

	public int getOccurrences() {
		return _occurrences;
	}
	public void setOccurrences(int occurrences) {
		this._occurrences = occurrences;
	}

	public void setGroupNames(List<String> groupNames) {
		this._groupNames = groupNames;
	}
	public List<String> getGroupNames() {
		return _groupNames;
	}

	public void setYear(String year) {
		this._year = year;
	}
	public String getYear() {
		return _year;
	}

	public void setMonth(String month) {
		this._month = month;
	}
	public String getMonth() {
		return _month;
	}

	private int _occurrences;
	private List<String> _groupNames;
	private String _year;
	private String _month;
	
}
