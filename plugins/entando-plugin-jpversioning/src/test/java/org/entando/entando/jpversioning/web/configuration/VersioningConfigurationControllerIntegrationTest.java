package org.entando.entando.jpversioning.web.configuration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpversioning.aps.system.JpversioningSystemConstants;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class VersioningConfigurationControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static String DELETE_MID_VERSION_FALSE = "false";
    private static String DELETE_MID_VERSION_TRUE = "true";
    private static String CONTENTS_TO_IGNORE = "TST1, TST2";
    private static String CONTENT_TYPES_TO_IGNORE = "CT1, CT2";
    private static String SPLIT_REGEX = "\\s*,\\s*";
    private final List<String> contentsToIgnoreList = Arrays.asList(CONTENTS_TO_IGNORE.trim().split(SPLIT_REGEX));
    private final List<String> contentTypesToIgnoreList = Arrays.asList(CONTENT_TYPES_TO_IGNORE.trim().split(SPLIT_REGEX));

    @Autowired
    private BaseConfigManager baseConfigManager;


    @Test
    public void testGetVersioningConfiguration() throws Exception {
        initConfig();
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();

        try {
            getVersioningConfiguration(user)
                    .andExpect(jsonPath("$.deleteMidVersions", is(true)))
                    .andExpect(jsonPath("$.contentsToIgnore", is(contentsToIgnoreList)))
                    .andExpect(jsonPath("$.contentTypesToIgnore", is(contentTypesToIgnoreList)));

        } finally {
            setDefaultVersioningConfiguration();
        }
    }

    private void initConfig() throws Exception {
        this.updateConfigItem(JpversioningSystemConstants.CONFIG_PARAM_DELETE_MID_VERSIONS, DELETE_MID_VERSION_TRUE);
        this.updateConfigItem(JpversioningSystemConstants.CONFIG_PARAM_CONTENTS_TO_IGNORE, CONTENTS_TO_IGNORE);
        this.updateConfigItem(JpversioningSystemConstants.CONFIG_PARAM_CONTENT_TYPES_TO_IGNORE, CONTENT_TYPES_TO_IGNORE);
    }

    private void setDefaultVersioningConfiguration() throws ApsSystemException {
        baseConfigManager.updateParam(JpversioningSystemConstants.CONFIG_PARAM_DELETE_MID_VERSIONS, DELETE_MID_VERSION_FALSE);
        baseConfigManager.updateParam(JpversioningSystemConstants.CONFIG_PARAM_CONTENTS_TO_IGNORE,null);
        baseConfigManager.updateParam(JpversioningSystemConstants.CONFIG_PARAM_CONTENT_TYPES_TO_IGNORE,null);
    }

    private void updateConfigItem(String paramKey, String paramValue) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(paramKey, paramValue);
        String xmlParams = baseConfigManager.getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
        String newXmlParams = SystemParamsUtils.getNewXmlParams(xmlParams, params, true);
        baseConfigManager.updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
    }

    private ResultActions getVersioningConfiguration(UserDetails user) throws Exception {
        String accessToken = mockOAuthInterceptor(user);

        final MockHttpServletRequestBuilder requestBuilder = get("/plugins/versioning/configuration")
                .sessionAttr("user", user)
                .header("Authorization", "Bearer " + accessToken);

        return mockMvc.perform(requestBuilder)
                .andDo(print());
    }

}
