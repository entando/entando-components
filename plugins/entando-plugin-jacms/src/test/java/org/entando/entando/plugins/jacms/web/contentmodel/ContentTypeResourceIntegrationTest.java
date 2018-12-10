package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.entando.entando.plugins.jacms.web.contentmodel.ContentTypeResourceController.BASE_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

        ContentType contentType = new ContentType();
        contentType.setTypeCode("MCT");
        contentType.setDescription("My content type");
        ContentTypeDtoRequest contentTypeRequest = new ContentTypeDtoRequest(contentType);
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

//    @Test
//    public void testUpdateContentType() throws Exception {
//        ContentTypeDto createdContentType = createContentTypeDto();
//
//        createdContentType.setCode("CDE");
//        createdContentType.setName("MyContentType");
//
//        mockMvc.perform(
//                put("/plugins/cms/content-types/")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(jsonMapper.writeValueAsString(createdContentType))
//                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                        .andExpect(status().isOk())
//                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                        .andExpect(jsonPath("$.code").value("CDE"))
//                        .andExpect(jsonPath("$.name").value("MyContentType"))
//                .andReturn();
//    }
//
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

//    @Test
//    public void testGetContentType() throws Exception {
//        mockMvc.perform(
//                get("/plugins/cms/content-types/" + Long.MAX_VALUE)
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//
//        ContentTypeDto createdContentTypeDto = createContentTypeDto();
//
//        MvcResult mvcResult = mockMvc.perform(
//                get("/plugins/cms/content-types/" + createdContentTypeDto.getId())
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        ContentTypeDto contentTypeDto = stringToContentTypeDto(mvcResult);
//
//        assertThat(contentTypeDto).isEqualToComparingFieldByField(createdContentTypeDto);
//    }
//
//    private ContentTypeDto createContentTypeDto() throws Exception {
//        ContentTypeDto contentType = new ContentTypeDto()
//                .id(generateNextId())
//                .code("ABC")
//                .name("My content type")
//                .defaultContentModel(DefaultContentModel.FULL)
//                .defaultContentModelList(DefaultContentModel.LISTS);
//
//        MvcResult mvcResult = mockMvc.perform(
//                post("/plugins/cms/content-types")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(jsonMapper.writeValueAsString(contentType))
//                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andReturn();
//
//        return stringToContentTypeDto(mvcResult);
//    }
//
//    private ContentTypeDto stringToContentTypeDto(MvcResult mvcResult) throws IOException {
//        return jsonMapper.readerFor(ContentTypeDto.class).readValue(
//                mvcResult.getResponse().getContentAsString());
//    }

    private void deleteContentType(String code) throws Exception {

        mockMvc.perform(
                delete(BASE_URL + "/" + code)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}