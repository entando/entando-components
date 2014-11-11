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
