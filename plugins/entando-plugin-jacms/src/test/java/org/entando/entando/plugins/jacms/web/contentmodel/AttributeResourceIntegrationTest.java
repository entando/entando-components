package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.enums.AttributeType;
import org.entando.entando.plugins.jacms.web.TestWebMvcConfig;
import org.entando.entando.plugins.jacms.web.contentmodel.util.AttributeDtoBuilder;
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
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestWebMvcConfig.class })
public class AttributeResourceIntegrationTest {

    private ObjectMapper jsonMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .build();
    }

    private Long generateNextId() {
        return new Random().nextLong();
    }

    @Test
    public void contentTypeResourceControllerIsAvailable() {
        ServletContext servletContext = wac.getServletContext();

        assertThat(servletContext).isNotNull();
        assertThat(servletContext).isInstanceOf(MockServletContext.class);
        assertThat(wac.getBean("attributeResourceController")).isNotNull();
    }

    @Test
    public void testGetReturnsList() throws Exception {
        mockMvc.perform(
                get("/plugins/cms/attributes")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.pageSize").value("100"))
                .andReturn();
    }

    @Test
    public void testCreateAttribute() throws Exception {
        AttributeDto attribute = new AttributeDtoBuilder()
                .withId(generateNextId())
                .withType(AttributeType.AUTHOR)
                .withCode("ABC")
                .withName("My Attribute")
                .withMandatory(true)
                .withSearcheable(false)
                .withFilterable(true)
                .build();

        mockMvc.perform(
                post("/plugins/cms/attributes")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(attribute))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code").value("ABC"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }

    @Test
    public void testUpdateContentType() throws Exception {
        AttributeDto createdAttribute = createAttributeDto();

        createdAttribute.setCode("CDE");
        createdAttribute.setName("MyContentType");

        mockMvc.perform(
                put("/plugins/cms/attributes/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(createdAttribute))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("CDE"))
                .andExpect(jsonPath("$.name").value("MyContentType"))
                .andReturn();
    }

    @Test
    public void testDeleteContentType() throws Exception {
        AttributeDto createdContentType = createAttributeDto();

        mockMvc.perform(
                delete("/plugins/cms/attributes/" + createdContentType.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(createdContentType))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testGetContentType() throws Exception {
        mockMvc.perform(
                get("/plugins/cms/attributes/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andReturn();


        AttributeDto createdAttributeDto = createAttributeDto();

        MvcResult mvcResult = mockMvc.perform(
                get("/plugins/cms/attributes/" + createdAttributeDto.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        AttributeDto AttributeDto = stringToAttributeDto(mvcResult);

        assertThat(AttributeDto).isEqualToComparingFieldByField(createdAttributeDto);
    }

    private AttributeDto createAttributeDto() throws Exception {
        AttributeDto attribute = new AttributeDtoBuilder()
                .withId(generateNextId())
                .withType(AttributeType.AUTHOR)
                .withCode("ABC")
                .withName("My Attribute")
                .withMandatory(true)
                .withSearcheable(false)
                .withFilterable(true)
                .build();

        MvcResult mvcResult = mockMvc.perform(
                post("/plugins/cms/attributes")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(attribute))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        return stringToAttributeDto(mvcResult);
    }

    private AttributeDto stringToAttributeDto(MvcResult mvcResult) throws IOException {
        return jsonMapper.readerFor(AttributeDto.class).readValue(
                mvcResult.getResponse().getContentAsString());
    }
}