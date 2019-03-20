package org.entando.entando.plugins.jpchangeme;

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
