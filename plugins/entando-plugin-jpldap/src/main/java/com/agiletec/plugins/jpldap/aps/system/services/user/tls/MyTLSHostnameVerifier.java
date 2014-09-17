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

import com.agiletec.aps.system.ApsSystemUtils;
import java.security.cert.Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

/**
 * @author E.Santoboni
 */
public class MyTLSHostnameVerifier implements HostnameVerifier {
	
	public MyTLSHostnameVerifier() {}
	
	@Override
	public boolean verify(String hostname, SSLSession session) {
		try {
			Certificate[] cert = session.getPeerCertificates();
			int certs	= cert.length;
			for (int i = 0; i < certs; i++) {
				ApsSystemUtils.getLogger().trace("Reading certificate " + cert[i]);
			}
		} catch (SSLPeerUnverifiedException e) {
			return false;
		}
		return true;
	}
	
}
