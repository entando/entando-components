package org.entando.entando.plugins.jacms.web.contentmodel;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.dictionary.ContentModelDictionary;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.plugins.jacms.aps.system.services.ContentModelService;
import org.entando.entando.plugins.jacms.apsadmin.tags.CmsAdminPagerTag;
import org.entando.entando.plugins.jacms.web.contentmodel.model.ContentModelReferenceDTO;
import org.entando.entando.plugins.jacms.web.contentmodel.validator.ContentModelReferencesValidator;
import org.entando.entando.plugins.jacms.web.contentmodel.validator.ContentModelUsageDetailsValidator;
import org.entando.entando.plugins.jacms.web.contentmodel.validator.ContentModelValidator;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ContentModelControllerUnitTest extends AbstractControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(CmsAdminPagerTag.class);

    private static final String BASE_URI = "/plugins/cms/contentmodels";
    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private ContentModelService contentModelService;

    @Spy
    private ContentModelValidator contentModelValidator;

    @Mock
    private ContentModelReferencesValidator contentModelReferencesValidator;

    @Mock
    private ContentModelUsageDetailsValidator contentModelUsageDetailsValidator;

    @InjectMocks
    private ContentModelResourceController controller;

    private Map<Long, ContentModelDto> mockedContentModels;
    private List<ContentModelReferenceDTO> mockedReferences;
    private List<ComponentUsageEntity> mockedUsageDetails;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();

        initMockedContentModels();
        initMockedContentModelReferences();
        initMockedUsageDetails();
        initContentModelServiceMocks();
    }

    @Test
    public void shouldGetContentModels() throws Exception {
        ResultActions result = performRequest(get(BASE_URI));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", hasSize(3)));
    }

    @Test
    public void shouldGetContentModel() throws Exception {
        ResultActions result = performRequest(get(BASE_URI + "/{id}", 1));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.id", is(1)));
    }

    @Test
    public void shouldFailGettingContentModel() throws Exception {
        ResultActions result = performRequest(get(BASE_URI + "/{id}", 20l));
        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void shouldCreateContentModel() throws Exception {

        String contentType = "AAA";
        String description = "description";
        String contentShape = "contentShape";

        ContentModelDto contentModelDto = new ContentModelDto();
        contentModelDto.setId(4l);
        contentModelDto.setContentType(contentType);
        contentModelDto.setContentShape(contentShape);
        contentModelDto.setDescr(description);

        ResultActions result = performRequest(post(BASE_URI), contentModelDto);
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.id", is(4)));
        result.andExpect(jsonPath("$.payload.contentType", is(contentType)));
        result.andExpect(jsonPath("$.payload.contentShape", is(contentShape)));
        result.andExpect(jsonPath("$.payload.descr", is(description)));
    }

    @Test
    public void shouldFailCreatingContentModelBecauseContentTypeTooShort() throws Exception {
        ContentModelDto contentModelDto = new ContentModelDto();
        contentModelDto.setId(4l);
        contentModelDto.setContentType("");
        contentModelDto.setDescr("description");

        ResultActions result = performRequest(post(BASE_URI), contentModelDto);
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailCreatingContentModelBecauseContentTypeTooLong() throws Exception {
        ContentModelDto contentModelDto = new ContentModelDto();
        contentModelDto.setId(4l);
        contentModelDto.setContentType("XXXX");
        contentModelDto.setDescr("description");

        ResultActions result = performRequest(post(BASE_URI), contentModelDto);
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateContentModel() throws Exception {
        ContentModelDto contentModelDto = new ContentModelDto();
        contentModelDto.setId(1l);
        contentModelDto.setContentType("AAA");
        contentModelDto.setDescr("updated");
        contentModelDto.setContentShape("contentShape");

        ResultActions result = performRequest(put(BASE_URI + "/{id}", 1), contentModelDto);
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.id", is(1)));
        result.andExpect(jsonPath("$.payload.descr", is("updated")));
    }

    @Test
    public void shouldFailUpdatingContentModelBecauseWrongId() throws Exception {
        ContentModelDto contentModelDto = new ContentModelDto();
        contentModelDto.setId(4l);
        contentModelDto.setContentType("AAA");
        contentModelDto.setDescr("updated");

        ResultActions result = performRequest(put(BASE_URI + "/{id}", 1), contentModelDto);
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldDeleteContentModel() throws Exception {
        ResultActions result = performRequest(delete(BASE_URI + "/{id}", 1));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.modelId", is("1")));
    }

    @Test
    public void shouldGetContentModelReferences() throws Exception {
        ResultActions result = performRequest(get(BASE_URI + "/{id}/pagereferences", 1));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload[0].pageCode", is("AAA")));
        result.andExpect(jsonPath("$.payload[0].online", is(true)));
        result.andExpect(jsonPath("$.payload[1].pageCode", is("ABC")));
        result.andExpect(jsonPath("$.payload[1].online", is(false)));
        result.andExpect(jsonPath("$.payload[2].pageCode", is("BBB")));
        result.andExpect(jsonPath("$.payload[2].online", is(true)));
        result.andExpect(jsonPath("$.payload[3].pageCode", is("BCD")));
        result.andExpect(jsonPath("$.payload[3].online", is(false)));
    }

    @Test
    public void shouldGetContentModelUsageDetails() throws Exception {
        ResultActions result = performRequest(get(BASE_URI + "/{id}/usage/details", 1));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload[0].code", is("AAA")));
        result.andExpect(jsonPath("$.payload[0].status", is("online")));
        result.andExpect(jsonPath("$.payload[0].type", is("page")));
        result.andExpect(jsonPath("$.payload[1].code", is("ABC")));
        result.andExpect(jsonPath("$.payload[1].status", is("offline")));
        result.andExpect(jsonPath("$.payload[1].type", is("page")));
        result.andExpect(jsonPath("$.payload[2].code", is("BBB")));
        result.andExpect(jsonPath("$.payload[2].status", is("")));
        result.andExpect(jsonPath("$.payload[2].type", is("contentType")));
    }

    @Test
    public void shouldGetDictionary() throws Exception {
        ResultActions result = performRequest(get(BASE_URI + "/dictionary"));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload").isNotEmpty());
        result.andExpect(jsonPath("$.payload.$content").exists());
    }

    private ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder, ContentModelDto contentModelDto) throws Exception {
        String payload = mapper.writeValueAsString(contentModelDto);
        return performRequest(requestBuilder, payload);
    }

    private ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return performRequest(requestBuilder, (String) null);
    }

    private ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder, String payload) throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        requestBuilder = requestBuilder
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + accessToken);

        if (payload != null) {
            requestBuilder = requestBuilder.content(payload);
        }

        ResultActions result = mockMvc.perform(requestBuilder);

        return result;
    }

    private void initMockedContentModels() {
        mockedContentModels = new HashMap<>();
        addMockedModelToMap(1l, "AAA");
        addMockedModelToMap(2l, "BBB");
        addMockedModelToMap(3l, "CCC");
    }

    private void initMockedContentModelReferences() {
        mockedReferences = new ArrayList<>();
        addMockedReferences("AAA", true,1);
        addMockedReferences("ABC", false,2);
        addMockedReferences("BBB", true,3);
        addMockedReferences("BCD", false,4);
    }
    private void initMockedUsageDetails() {
        mockedUsageDetails = new ArrayList<>();
        addMockedUsageDetail("AAA", "online","page");
        addMockedUsageDetail("ABC", "offline","page");
        addMockedUsageDetail("BBB", "","contentType");

    }
    private void addMockedModelToMap(long id, String contentType) {
        ContentModelDto contentModelDto = new ContentModelDto();
        contentModelDto.setId(id);
        contentModelDto.setContentType(contentType);
        contentModelDto.setDescr("description");
        mockedContentModels.put(id, contentModelDto);
    }

    private void addMockedReferences(String id, Boolean online, int widgetPosition) {
        logger.info("addMockedReferences {} - online : {} , widgetPosition {}",id, online, widgetPosition);

        ContentModelReferenceDTO contentModelReferenceDTO = new ContentModelReferenceDTO();
        contentModelReferenceDTO.setPageCode(id);
        contentModelReferenceDTO.setWidgetPosition(widgetPosition);
        contentModelReferenceDTO.setOnline(online);
        mockedReferences.add(contentModelReferenceDTO);
    }


    private void addMockedUsageDetail(String id, String status, String type) {
        logger.info("addMockedUsageDetail {} - status : {} , type {}",id, status, type);

        ComponentUsageEntity componentUsageEntity = new ComponentUsageEntity();
        componentUsageEntity.setCode(id);
        componentUsageEntity.setStatus(status);
        componentUsageEntity.setType(type);
        mockedUsageDetails.add(componentUsageEntity);
    }

    private void initContentModelServiceMocks() {
        when(contentModelService.getContentModel(anyLong()))
                .thenAnswer(invocation -> {
                    ContentModelDto dto = mockedContentModels.get((long) invocation.getArgument(0));
                    if (dto == null) {
                        throw new ResourceNotFoundException("", "", "");
                    }
                    return dto;
                });

        when(contentModelService.create(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(contentModelService.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PagedMetadata<ContentModelDto> pagedMetadata = new PagedMetadata<>();
        pagedMetadata.setBody(new ArrayList<>(mockedContentModels.values()));
        when(contentModelService.findMany(any())).thenReturn(pagedMetadata);

        PagedMetadata<ContentModelReferenceDTO> pagedMetadataReferences = new PagedMetadata<>();
        pagedMetadataReferences.setBody(mockedReferences);

        PagedMetadata<ComponentUsageEntity> pagedMetadataUsageDetails = new PagedMetadata<>();
        pagedMetadataUsageDetails.setBody(mockedUsageDetails);

        when(contentModelService.findMany(any())).thenReturn(pagedMetadata);

        when(contentModelService.getContentModelReferences(anyLong(), Mockito.any(RestListRequest.class)))
                .thenReturn(pagedMetadataReferences);

        when(contentModelService.getContentModelDictionary(any()))
                .thenReturn(getContentModelDictionary());
        when(contentModelService.getComponentUsageDetails(anyLong(), Mockito.any(RestListRequest.class))).thenReturn(pagedMetadataUsageDetails);;

    }

    private ContentModelDictionary getContentModelDictionary() {
        return new ContentModelDictionary(
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                new Properties(), new Content());
    }
}
