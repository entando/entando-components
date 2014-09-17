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
package com.agiletec.plugins.jpldap.aps.system.services.user.tls;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 * @author E.Santoboni
 */
public class MyX509TrustManager implements X509TrustManager {
	
	public MyX509TrustManager() {}
	
	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
	
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
    
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) {}
	
}