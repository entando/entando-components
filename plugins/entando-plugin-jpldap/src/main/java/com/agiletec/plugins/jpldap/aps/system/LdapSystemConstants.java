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
package com.agiletec.plugins.jpldap.aps.system;

/**
 * @author E.Santoboni
 */
public interface LdapSystemConstants {

	public static final String ACTIVE_PARAM_NAME = "jpldap_active";
	public static final String PROVIDER_URL_PARAM_NAME = "jpldap_providerUrl";
	
	public static final String SECURITY_CONNECTION_TYPE_PARAM_NAME = "jpldap_securityConnectionType";
	public static final String SECURITY_CONNECTION_TYPE_NONE = "none";
	public static final String SECURITY_CONNECTION_TYPE_SSL = "ssl";
	public static final String SECURITY_CONNECTION_TYPE_TLS_FREE = "tls_free";
	public static final String SECURITY_CONNECTION_TYPE_TLS = "tls";
	
	public static final String SECURITY_PRINCIPAL_PARAM_NAME = "jpldap_securityPrincipal";
	public static final String SECURITY_CREDENTIALS_PARAM_NAME = "jpldap_securityCredentials";
	public static final String USER_OBJECT_CLASS_PARAM_NAME = "jpldap_userObjectClass";
	public static final String USER_ID_ATTRIBUTE_NAME_PARAM_NAME = "jpldap_userIdAttributeName";
	public static final String FILTER_GROUP_PARAM_NAME = "jpldap_filterGroup";
	public static final String FILTER_GROUP_ATTRIBUTE_NAME_PARAM_NAME = "jpldap_filterGroupAttributeName";
	public static final String SEARCH_RESULT_MAX_SIZE_PARAM_NAME = "jpldap_searchResultMaxSize";
	
	//-----------------
	
	public static final String USER_EDITING_ACTIVE_PARAM_NAME = "jpldap_userEditingActive";
	public static final String USER_BASE_DN_PARAM_NAME = "jpldap_userBaseDN";
	public static final String USER_REAL_ATTRIBUTE_NAME_PARAM_NAME = "jpldap_userRealAttributeName";
	public static final String USER_PASSWORD_ATTRIBUTE_NAME_PARAM_NAME = "jpldap_userPasswordAttributeName";
	
	public static final String USER_PASSWORD_ALGORITHM_PARAM_NAME = "jpldap_userPasswordAlgorithm";
	public static final String USER_PASSWORD_ALGORITHM_NONE = "none";
	public static final String USER_PASSWORD_ALGORITHM_MD5 = "md5";
	public static final String USER_PASSWORD_ALGORITHM_SHA = "sha";
	
	public static final String USER_OBJECT_CLASSES_CSV_PARAM_NAME = "jpldap_userObjectClassesCSV";
	public static final String OU_OBJECT_CLASSES_CSV_PARAM_NAME = "jpldap_ouObjectClassesCSV";
	
}
