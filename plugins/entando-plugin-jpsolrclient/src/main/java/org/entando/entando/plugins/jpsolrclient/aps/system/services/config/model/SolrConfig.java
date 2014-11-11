/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
