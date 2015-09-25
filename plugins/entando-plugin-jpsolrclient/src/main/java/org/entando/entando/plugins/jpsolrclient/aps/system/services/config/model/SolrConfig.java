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
package org.entando.entando.plugins.jpsolrclient.aps.system.services.config.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author S.Loru
 */
@XmlRootElement(name = "SolrConfig")
@XmlType(propOrder = {"providerUrl", "maxResultSize"})
public class SolrConfig {

	@XmlElement(name = "providerUrl", required = true)
	public String getProviderUrl() {
		return _providerUrl;
	}

	public void setProviderUrl(String providerUrl) {
		this._providerUrl = providerUrl;
	}

	@XmlElement(name = "maxResultSize")
	public String getMaxResultSize() {
		return _maxResultSize;
	}

	public void setMaxResultSize(String maxResultSize) {
		this._maxResultSize = maxResultSize;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SolrConfig other = (SolrConfig) obj;
		if ((this._providerUrl == null) ? (other._providerUrl != null) : !this._providerUrl.equals(other._providerUrl)) {
			return false;
		}
		if ((this._maxResultSize == null) ? (other._maxResultSize != null) : !this._maxResultSize.equals(other._maxResultSize)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SolrConfig{" + "_providerUrl=" + _providerUrl + ", _maxResultSize=" + _maxResultSize + '}';
	}
	
	
	private String _providerUrl;
	private String _maxResultSize;

}
