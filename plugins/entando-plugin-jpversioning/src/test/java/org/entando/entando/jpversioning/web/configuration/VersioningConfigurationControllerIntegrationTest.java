package org.entando.entando.jpversioning.web.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.FileTextReader;
import com.agiletec.plugins.jpversioning.aps.system.JpversioningSystemConstants;
import com.jayway.jsonpath.JsonPath;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
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
    public void testPutVersioningConfiguration() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        try {
            String accessToken = mockOAuthInterceptor(user);
            final String versioningConfigurationResult = updateVersioningConfiguration("json/1_PUT_versioning_configuration.json", accessToken);
            List<String> contentsToIgnore = JsonPath.read(versioningConfigurationResult, "$.contentsToIgnore");
            List<String> contentTypesToIgnore = JsonPath.read(versioningConfigurationResult, "$.contentTypesToIgnore");
            boolean deleteMidVersions = JsonPath.read(versioningConfigurationResult, "$.deleteMidVersions");
            assertThat(contentsToIgnore.get(0)).isEqualTo("TST1");
            assertThat(contentTypesToIgnore.get(0)).isEqualTo("CT1");
            assertThat(deleteMidVersions).isEqualTo(true);
        } finally {
            setDefaultVersioningConfiguration();
        }
    }

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

    private String updateVersioningConfiguration(String filename, String accessToken) throws Exception {
    ResultActions result = putVersioningConfiguration(filename, accessToken, status().isOk());
    String bodyResult = result.andReturn().getResponse().getContentAsString();
    return bodyResult;
}
    private ResultActions putVersioningConfiguration(String fileName,String accessToken, ResultMatcher expected) throws Exception {
        InputStream jsonStream = getClass().getResourceAsStream(fileName);
        String json = FileTextReader.getText(jsonStream);
        ResultActions result = mockMvc
                .perform(put("/plugins/versioning/configuration")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andDo(print());
        result.andExpect(expected);
        return result;
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
