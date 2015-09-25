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
package com.agiletec.plugins.jpblog.aps.system.services.blog.model;

import java.util.Set;

public class GroupStatistic implements Comparable<GroupStatistic> {
	
	@Override
	public int compareTo(GroupStatistic stat) {
		int comparison = this._occurrences - stat._occurrences;
		if (comparison==0 && this._groups!=null && stat._groups!=null) {
			comparison = this._groups.size() - stat._groups.size();
		}
		if (comparison==0) {
			comparison = 1;
		}
		return comparison;
	}
	
	public Set<String> getGroups() {
		return _groups;
	}
	public void setGroups(Set<String> groups) {
		this._groups = groups;
	}
	
	public int getOccurrences() {
		return _occurrences;
	}
	public void setOccurrences(int occurrences) {
		this._occurrences = occurrences;
	}
	
	private Set<String> _groups;
	private int _occurrences;
	
}