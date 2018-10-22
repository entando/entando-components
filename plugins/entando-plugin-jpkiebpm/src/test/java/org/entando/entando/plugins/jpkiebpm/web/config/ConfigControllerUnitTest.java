package org.entando.entando.plugins.jpkiebpm.web.config;

import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.IKieBpmService;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieConfigService;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieServerConfigDto;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.model.DatatableWidgetConfigDto;
import org.entando.entando.plugins.jpkiebpm.web.config.validator.ConfigValidator;
import org.entando.entando.plugins.jpkiebpm.web.model.DatatableWidgetConfigRequest;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ConfigControllerUnitTest extends AbstractControllerTest {

    @Mock
    private IKieConfigService kieConfigService;

    @Mock
    private ConfigValidator configValidator;

    @Mock
    IKieFormManager kieFormManager;

    @Mock
    CaseManager caseManager;

    @Mock
    IKieBpmService kieBpmService;

    @InjectMocks
    private ConfigController configController;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(configController)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @Test
    public void testGetConfigs() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc.perform(
                get("/kiebpm/serverConfigs")
                        .header("Authorization", "Bearer " + accessToken)
        );

        result.andExpect(status().isOk());

        RestListRequest restListReq = new RestListRequest();
        restListReq.setPage(1);
        restListReq.setPageSize(4);
        Mockito.verify(kieConfigService, Mockito.times(1)).getConfigs();
    }

    @Test
    public void testGetConfig() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc.perform(
                get("/kiebpm/serverConfigs/testConfigCode")
                        .header("Authorization", "Bearer " + accessToken)
        );

        result.andExpect(status().isOk());

        RestListRequest restListReq = new RestListRequest();
        restListReq.setPage(1);
        restListReq.setPageSize(4);
        Mockito.verify(kieConfigService, Mockito.times(1)).getConfig("testConfigCode");
    }

    @Test
    public void testAddConfig() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        KieServerConfigDto configDto = getTestConfig();
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(configDto);

        ResultActions result = mockMvc.perform(
                post("/kiebpm/serverConfigs")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        Mockito.verify(kieConfigService, Mockito.times(1)).addConfig(Mockito.any(), Mockito.any());

        String response = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testUpdateConfig() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        KieServerConfigDto configDto = getTestConfig();
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(configDto);

        ResultActions result = mockMvc.perform(
                put("/kiebpm/serverConfigs/test")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        Mockito.verify(kieConfigService, Mockito.times(1)).updateConfig(Mockito.any(), Mockito.any());

        String response = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testDeleteConfig() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc.perform(
                delete("/kiebpm/serverConfigs/test")
                        .header("Authorization", "Bearer " + accessToken));

        Mockito.verify(kieConfigService, Mockito.times(1)).removeConfig("test");
        String response = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testTestServerConfigs() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        KieServerConfigDto configDto = getTestConfig();
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(configDto);

        ResultActions result = mockMvc.perform(
                post("/kiebpm/testServerConfigs")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        Mockito.verify(kieConfigService, Mockito.times(1)).testServerConfigs(Mockito.any());
        String response = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testTestAllServerConfigs() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        KieServerConfigDto configDto = getTestConfig();
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(configDto);

        ResultActions result = mockMvc.perform(
                post("/kiebpm/testAllServerConfigs")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        Mockito.verify(kieConfigService, Mockito.times(1)).testAllServerConfigs();
        String response = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetDeploymentUnits() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc.perform(
                get("/kiebpm/serverConfigs/1/deploymentUnits")
                        .header("Authorization", "Bearer " + accessToken));

        Mockito.verify(kieFormManager, Mockito.times(1)).getContainersList(any());
        String response = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetCaseDefinitions() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        //This is necessary because there is a separately defeind case manager that doesn't conform to the interface
        //should be refactored away in the future
        configController.caseManager = caseManager;

        ResultActions result = mockMvc.perform(
                get("/kiebpm/serverConfigs/1/caseDefinitions/test")
                        .header("Authorization", "Bearer " + accessToken));

        Mockito.verify(caseManager, Mockito.times(1)).getCasesDefinitions(any(), eq("test"));
        String response = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetProcessList() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc.perform(
                get("/kiebpm/serverConfigs/1/processList")
                        .header("Authorization", "Bearer " + accessToken));

        Mockito.verify(kieFormManager, Mockito.times(1)).getProcessDefinitionsList(any());
        String response = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetWidgetConfig() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        when(kieBpmService.getDataTableWidgetConfig(any(Integer.class))).thenReturn(getWidgetConfig(1));

        ResultActions result = mockMvc.perform(
                get("/kiebpm/datatableWidget/{configId}", "1")
                        .header("Authorization", "Bearer " + accessToken));

        String response = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testPutWidgetConfigMismatch() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String mockJsonResult = "{\n"
                + "        \"id\": 13,\n"
                + "        \"informationOnline\": [],\n"
                + "        \"informationDraft\": [],\n"
                + "        \"widgetType\": \"bpm-datatable-task-list\",\n"
                + "        \"pageCode\": \"hello2\",\n"
                + "        \"framePosOnline\": 0,\n"
                + "        \"framePosDraft\": 0,\n"
                + "        \"containerId\": \"mortgage\",\n"
                + "        \"processId\": \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n"
                + "        \"knowledgeSourceId\": \"4e85caf304e04af1a413836daff70d9520180612T152311942\"\n"
                + "    }";
        DatatableWidgetConfigDto mock = (DatatableWidgetConfigDto) this.createMetadata(mockJsonResult, DatatableWidgetConfigDto.class);
        when(kieBpmService.updateDataTableWidgetConfig(any(DatatableWidgetConfigRequest.class))).thenReturn(mock);

        ResultActions result = mockMvc.perform(
                put("/kiebpm/datatableWidget/{configId}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockJsonResult)
                        .header("Authorization", "Bearer " + accessToken));

        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is("2")));
    }

    private KieServerConfigDto getTestConfig() {
        KieServerConfigDto configRequest = new KieServerConfigDto();
        configRequest.setActive(true);
        configRequest.setDebug(true);
        configRequest.setHostName("localhost");
        configRequest.setId("test");
        configRequest.setName("name");
        configRequest.setPort(1234);
        configRequest.setWebappName("tester");
        configRequest.setSchema("schema");
        configRequest.setPassword("test");
        configRequest.setUsername("user");

        return configRequest;

    }

    private DatatableWidgetConfigDto getWidgetConfig(int id) {
        DatatableWidgetConfigDto dto = new DatatableWidgetConfigDto();
        dto.setId(id++);
        dto.setContainerId("containerId");
        dto.setFramePosDraft(id++);
        dto.setFramePosOnline(id++);
        dto.setKnowledgeSourceId("knowledgeSourceId");
        dto.setPageCode("pageCode");
        dto.setProcessId("processId");
        dto.setWidgetType("widgetType");
        return dto;
    }
}
