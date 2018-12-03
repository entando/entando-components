package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.enums.DefaultContentModel;
import org.entando.entando.plugins.jacms.web.TestWebMvcConfig;
import org.entando.entando.plugins.jacms.web.contentmodel.util.ContentTypeDtoBuilder;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.entando.entando.plugins.jacms.web.contentmodel.util.RestControllerTestUtil.generateNextId;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestWebMvcConfig.class })
public class ContentTypeResourceIntegrationTest {

    private ObjectMapper jsonMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                                 .build();
    }

    @Test
    public void contentTypeResourceControllerIsAvailable() {
        ServletContext servletContext = wac.getServletContext();

        assertThat(servletContext).isNotNull();
        assertThat(servletContext).isInstanceOf(MockServletContext.class);
        assertThat(wac.getBean("contentTypeResourceController")).isNotNull();
    }

    @Test
    public void testGetReturnsList() throws Exception {
        mockMvc.perform(
                get("/plugins/cms/content-types")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.pageSize").value("100"))
                .andReturn();
    }

    @Test
    public void testCreateContentType() throws Exception {
        ContentTypeDto contentType = new ContentTypeDtoBuilder()
                .withId(generateNextId())
                .withCode("ABC")
                .withName("My content type")
                .withDefaultContentModel(DefaultContentModel.FULL)
                .withDefaultContentModelList(DefaultContentModel.LISTS)
                .build();

        mockMvc.perform(
                post("/plugins/cms/content-types")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(contentType))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code").value("ABC"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }

    @Test
    public void testUpdateContentType() throws Exception {
        ContentTypeDto createdContentType = createContentTypeDto();

        createdContentType.setCode("CDE");
        createdContentType.setName("MyContentType");

        mockMvc.perform(
                put("/plugins/cms/content-types/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(createdContentType))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(jsonPath("$.code").value("CDE"))
                        .andExpect(jsonPath("$.name").value("MyContentType"))
                .andReturn();
    }

    @Test
    public void testDeleteContentType() throws Exception {
        ContentTypeDto createdContentType = createContentTypeDto();

        mockMvc.perform(
                delete("/plugins/cms/content-types/" + createdContentType.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(createdContentType))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testGetContentType() throws Exception {
        mockMvc.perform(
                get("/plugins/cms/content-types/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andReturn();


        ContentTypeDto createdContentTypeDto = createContentTypeDto();

        MvcResult mvcResult = mockMvc.perform(
                get("/plugins/cms/content-types/" + createdContentTypeDto.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        ContentTypeDto contentTypeDto = stringToContentTypeDto(mvcResult);

        assertThat(contentTypeDto).isEqualToComparingFieldByField(createdContentTypeDto);
    }

    private ContentTypeDto createContentTypeDto() throws Exception {
        ContentTypeDto contentType = new ContentTypeDtoBuilder()
                .withId(generateNextId())
                .withCode("ABC")
                .withName("My content type")
                .withDefaultContentModel(DefaultContentModel.FULL)
                .withDefaultContentModelList(DefaultContentModel.LISTS)
                .build();

        MvcResult mvcResult = mockMvc.perform(
                post("/plugins/cms/content-types")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(contentType))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        return stringToContentTypeDto(mvcResult);
    }

    private ContentTypeDto stringToContentTypeDto(MvcResult mvcResult) throws IOException {
        return jsonMapper.readerFor(ContentTypeDto.class).readValue(
                mvcResult.getResponse().getContentAsString());
    }
}