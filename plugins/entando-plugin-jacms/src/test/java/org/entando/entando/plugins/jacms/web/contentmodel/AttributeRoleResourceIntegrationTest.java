package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeRoleDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.plugins.jacms.web.TestWebMvcConfig;
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
public class AttributeRoleResourceIntegrationTest {

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
        assertThat(wac.getBean("attributeResourceController")).isNotNull();
    }

    @Test
    public void testGetReturnsList() throws Exception {
        mockMvc.perform(
                get("/plugins/cms/attributeroles")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.pageSize").value("100"))
                .andReturn();
    }

    @Test
    public void testCreateAttributeRole() throws Exception {
        AttributeRoleDto attribute = new AttributeRoleDto();
        attribute.setId(generateNextId());
        attribute.setName("My AttributeRole");

        mockMvc.perform(
                post("/plugins/cms/attributeroles")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(attribute))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value("My AttributeRole"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }

    @Test
    public void testUpdateContentType() throws Exception {
        AttributeRoleDto createdAttributeRole = createAttributeRoleDto();
        createdAttributeRole.setName("MyContentType");

        mockMvc.perform(
                put("/plugins/cms/attributeroles/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(createdAttributeRole))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MyContentType"))
                .andReturn();
    }

    @Test
    public void testDeleteContentType() throws Exception {
        AttributeRoleDto createdDto = createAttributeRoleDto();

        mockMvc.perform(
                delete("/plugins/cms/attributeroles/" + createdDto.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(createdDto))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testGetContentType() throws Exception {
        mockMvc.perform(
                get("/plugins/cms/attributeroles/" + Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andReturn();


        AttributeRoleDto createdDto = createAttributeRoleDto();

        MvcResult mvcResult = mockMvc.perform(
                get("/plugins/cms/attributeroles/" + createdDto.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        AttributeRoleDto AttributeRoleDto = stringToAttributeRoleDto(mvcResult);

        assertThat(AttributeRoleDto).isEqualToComparingFieldByField(createdDto);
    }

    private AttributeRoleDto createAttributeRoleDto() throws Exception {
        AttributeRoleDto attribute = new AttributeRoleDto();
        attribute.setId(generateNextId());
        attribute.setName("My AttributeRole");

        MvcResult mvcResult = mockMvc.perform(
                post("/plugins/cms/attributeroles")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(attribute))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        return stringToAttributeRoleDto(mvcResult);
    }

    private AttributeRoleDto stringToAttributeRoleDto(MvcResult mvcResult) throws IOException {
        return jsonMapper.readerFor(AttributeRoleDto.class).readValue(
                mvcResult.getResponse().getContentAsString());
    }
}