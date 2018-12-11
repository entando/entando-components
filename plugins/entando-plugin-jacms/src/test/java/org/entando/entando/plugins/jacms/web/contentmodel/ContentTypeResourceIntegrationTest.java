package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.entando.entando.plugins.jacms.web.contentmodel.ContentTypeResourceController.BASE_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ContentTypeResourceIntegrationTest extends AbstractControllerIntegrationTest {

    private ObjectMapper jsonMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    private IContentManager contentManager;

    @Test
    public void testGetReturnsList() throws Exception {
        mockMvc.perform(
                get(BASE_URL)
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
        deleteContentType("MCT");

        Content content = new Content();
        content.setTypeCode("MCT");
        content.setDescription("My content type");
        content.setDefaultModel("My Model");
        content.setListModel("Model list");

        ContentTypeDtoRequest contentTypeRequest = new ContentTypeDtoRequest(content);
        contentTypeRequest.setName("Content request");

        mockMvc.perform(
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(contentTypeRequest))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.code").value("MCT"))
                .andReturn();
    }

    @Test
    public void testUpdateContentType() throws Exception {
        ContentTypeDto createdContentType = createContentTypeDto();

        createdContentType.setName("MyContentType");

        mockMvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(createdContentType))
                .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(createdContentType.getCode()))
                .andExpect(jsonPath("$.name").value("MyContentType"))
                .andReturn();
    }

    @Test
    public void testDeleteContentType() throws Exception {

        mockMvc.perform(
                delete(BASE_URL + "/RST")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                        .andDo(print())
                        .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testGetContentType() throws Exception {
        deleteContentType("MCT");

        mockMvc.perform(
                get(BASE_URL + "/MCT")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andReturn();


        ContentTypeDto createdContentTypeDto = createContentTypeDto();

        MvcResult mvcResult = mockMvc.perform(
                get(BASE_URL + "/" + createdContentTypeDto.getCode())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        ContentTypeDto contentTypeDto = stringToContentTypeDto(mvcResult);

        assertThat(contentTypeDto).isEqualToComparingFieldByField(createdContentTypeDto);
    }

    private ContentTypeDto createContentTypeDto() throws Exception {
        deleteContentType("MCT");

        Content content = new Content();
        content.setTypeCode("MCT");
        content.setDescription("My content type");
        content.setDefaultModel("My Model");
        content.setListModel("Model list");

        ContentTypeDtoRequest contentTypeRequest = new ContentTypeDtoRequest(content);
        contentTypeRequest.setName("Content request");

        MvcResult mvcResult = mockMvc.perform(
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(contentTypeRequest))
                        .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        return stringToContentTypeDto(mvcResult);
    }

    private ContentTypeDto stringToContentTypeDto(MvcResult mvcResult) throws IOException {
        return jsonMapper.readerFor(ContentTypeDto.class).readValue(
                mvcResult.getResponse().getContentAsString());
    }

    private void deleteContentType(String code) throws Exception {

        mockMvc.perform(
                delete(BASE_URL + "/" + code)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
    }
}