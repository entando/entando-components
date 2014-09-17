/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebmail.aps.system.services.webmail.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Classe minimale usata per l'autenticazione SSL.
 */
public class SimpleTrustManager implements X509TrustManager {
	
	public SimpleTrustManager(X509TrustManager tm) {
		this.tm=tm;
	}
	
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		this.chain=chain;
		this.tm.checkServerTrusted(chain, authType);
	}
	
	public X509TrustManager getTm() {
		return tm;
	}
	public void setTm(X509TrustManager tm) {
		this.tm = tm;
	}
	
	public X509Certificate[] getChain() {
		return chain;
	}
	
	private X509TrustManager tm;
	private X509Certificate[] chain;
	
}