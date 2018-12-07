package org.entando.entando.plugins.jacms.web.contentmodel;

import org.entando.entando.plugins.jacms.web.TestWebMvcConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestWebMvcConfig.class })
public class ContentTypeValidationTest {

//    private ObjectMapper jsonMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//    @Autowired
//    private WebApplicationContext wac;
//
//    private MockMvc mockMvc;
//
//    @Before
//    public void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
//                .build();
//    }
//
//    @Test
//    public void createValidContentTypeReturnsCreated() throws Exception {
//        ContentTypeDto contentType = buildValidContentType();
//
//        createContentType(contentType)
//                .andExpect(status().isCreated())
//                .andReturn();
//    }
//
//    @Test
//    public void idNullReturnsError() throws Exception {
//        ContentTypeDto contentType = buildValidContentType();
//        contentType.setId(null);
//
//        checkFieldNull(contentType, "id");
//    }
//
//    @Test
//    public void idNegativeReturnsError() throws Exception {
//        ContentTypeDto contentType = buildValidContentType();
//        contentType.setId(-1L);
//
//        createContentType(contentType)
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors[0].message")
//                        .value("Value of field 'id' must be greater than or equal to '0'"))
//                .andReturn();
//    }
//
//    @Test
//    public void codeNullReturnsError() throws Exception {
//        ContentTypeDto contentType = buildValidContentType();
//        contentType.setCode(null);
//
//        checkFieldNull(contentType, "code");
//    }
//
//    @Test
//    public void nameNullReturnsError() throws Exception {
//        ContentTypeDto contentType = buildValidContentType();
//        contentType.setName(null);
//
//        checkFieldNull(contentType, "name");
//    }
//
//    @Test
//    public void defaultContentModelNullReturnsError() throws Exception {
//        ContentTypeDto contentType = buildValidContentType();
//        contentType.setDefaultContentModel(null);
//
//        checkFieldNull(contentType, "defaultContentModel");
//    }
//
//    @Test
//    public void defaultContentModelListNullReturnsError() throws Exception {
//        ContentTypeDto contentType = buildValidContentType();
//        contentType.setDefaultContentModelList(null);
//
//        checkFieldNull(contentType, "defaultContentModelList");
//    }
//
//    private void checkFieldNull(ContentTypeDto contentType, String fieldName) throws Exception {
//        createContentType(contentType)
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors[0].message")
//                        .value(String.format("Value of field '%s' can't be null", fieldName)))
//                .andReturn();
//    }
//
//    private ResultActions createContentType(ContentTypeDto contentType) throws Exception {
//        return mockMvc.perform(
//                post("/plugins/cms/content-types")
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(jsonMapper.writeValueAsString(contentType))
//                        .accept(MediaType.APPLICATION_JSON_UTF8));
//    }
//
//    private ContentTypeDto buildValidContentType() {
//        return new ContentTypeDto()
//                .id(generateNextId())
//                .code("ABC")
//                .name("My content type")
//                .defaultContentModel(DefaultContentModel.FULL)
//                .defaultContentModelList(DefaultContentModel.LISTS);
//    }
}
