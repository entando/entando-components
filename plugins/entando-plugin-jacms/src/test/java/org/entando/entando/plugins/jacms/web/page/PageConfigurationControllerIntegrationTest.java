package org.entando.entando.plugins.jacms.web.page;

import com.opensymphony.xwork2.mock.MockResult;
import java.util.Map;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.PageTestUtil;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.aps.system.services.page.model.WidgetConfigurationDto;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import org.hamcrest.Matchers;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PageConfigurationControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IPageModelManager pageModelManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPageConfiguration() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/pages/{pageCode}/configuration", "homepage")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.payload.online", is(true)));

        /**
         * The response should have the correct CORS headers and the CORS
         * configuration should reflect the one set in
         * org.entando.entando.aps.servlet.CORSFilter class
         */
        result.andExpect(header().string("Access-Control-Allow-Origin", "*"));
        result.andExpect(header().string("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH"));
        result.andExpect(header().string("Access-Control-Allow-Headers", "Content-Type, Authorization"));
        result.andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

    /**
     * Given: a page only in draft When: the user request the configuration for
     * status draft Then the result is ok
     *
     * Given: a page only in draft When: the user request the configuration for
     * status published Then an error with status code 400 is raised
     *
     * @throws Exception
     */
    @Test
    public void testGetPageConfigurationOnLineNotFound() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/configuration", new Object[]{pageCode})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            result = mockMvc
                    .perform(get("/pages/{pageCode}/configuration", new Object[]{pageCode})
                            .param("status", IPageService.STATUS_ONLINE)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is("3")));
        } finally {
            this.pageManager.deletePage(pageCode);
        }

    }

    @Test
    public void testPutPageConfiguration() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            String payloadWithOtherModelId = "{\n"
                    + "  \"code\": \"content_viewer\",\n"
                    + "  \"config\": {\n"
                    + " \"modelId\": \"list\",\n"
                    + " \"contentId\": \"EVN24\"\n"
                    + "  }\n"
                    + "}";
            result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .content(payloadWithOtherModelId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            
            result
                .andExpect(jsonPath("$.errors", hasSize(0)))
                .andExpect(jsonPath("$.payload.code", is("content_viewer")))
                .andExpect(jsonPath("$.payload.config.modelId", is("list")))
                .andExpect(jsonPath("$.payload.config.contentId", is("EVN24")));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testPutPageConfigurationFilterFormat() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String payload = "{\n"
                    + "  \"code\": \"content_viewer\",\n"
                    + "  \"config\": {\n"
                    + "      \"contentId\": \"EVN24\",\n"
                    + "      \"modelId\": \"default\",\n"
                    + "      \"userFilters\": [\n"
                    + "         {\"attributeFilter\": false, \"key\": \"fulltext\"},\n"
                    + "         {\"attributeFilter\": false, \"key\": \"category\"}\n"
                    + "      ]\n"
                    + "  }\n"
                    + "}";

            ResultActions result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .content(payload)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result
                .andDo(print())
                .andExpect(jsonPath("$.errors", hasSize(0)))
                .andExpect(jsonPath("$.payload.code", is("content_viewer")))
                .andExpect(jsonPath("$.payload.config.size()", is(3)))
                .andExpect(jsonPath("$.payload.config.userFilters.size()", is(2)));

            IPage updatedPage = pageManager.getDraftPage(pageCode);
            Widget widget = updatedPage.getWidgets()[0];
            assertThat(widget.getConfig().get("userFilters"),
                    equalTo("(attributeFilter=false;key=fulltext)+(attributeFilter=false;key=category)"));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testPutPageConfigurationCategoriesFormat() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String payload = "{\n"
                    + "  \"code\": \"content_viewer\",\n"
                    + "  \"config\": {\n"
                    + "      \"contentId\": \"EVN24\",\n"
                    + "      \"modelId\": \"default\",\n"
                    + "      \"categories\": [\"resCat1\", \"resCat2\"]\n"
                    + "  }\n"
                    + "}";

            ResultActions result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .content(payload)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result
                    .andDo(print())
                    .andExpect(jsonPath("$.errors", hasSize(0)))
                    .andExpect(jsonPath("$.payload.code", is("content_viewer")))
                    .andExpect(jsonPath("$.payload.config.size()", is(3)))
                    .andExpect(jsonPath("$.payload.config.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.config.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.config.categories[1]", is("resCat2")));

            IPage updatedPage = pageManager.getDraftPage(pageCode);
            Widget widget = updatedPage.getWidgets()[0];

            assertThat(widget.getConfig().get("categories"),
                    equalTo("resCat1,resCat2"));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testPutPageConfigurationContentsFormat() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String payload = "{\n"
                    + "  \"code\": \"content_viewer\",\n"
                    + "  \"config\": {\n"
                    + "      \"contentId\": \"EVN24\",\n"
                    + "      \"modelId\": \"default\",\n"
                    + "      \"contents\": [\n"
                    + "         {\"contentId\": \"ABC1\", \"contentDescription\": \"My Content 1\"},\n"
                    + "         {\"contentId\": \"ABC2\", \"contentDescription\": \"My Content 2\"}\n"
                    + "      ]\n"
                    + "  }\n"
                    + "}";

            ResultActions result = mockMvc
                    .perform(put("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .content(payload)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result
                    .andDo(print())
                    .andExpect(jsonPath("$.errors", hasSize(0)))
                    .andExpect(jsonPath("$.payload.code", is("content_viewer")))
                    .andExpect(jsonPath("$.payload.config.size()", is(3)))
                    .andExpect(jsonPath("$.payload.config.contents.size()", is(2)))
                    .andExpect(jsonPath("$.payload.config.contents[0].contentId", is("ABC1")))
                    .andExpect(jsonPath("$.payload.config.contents[0].contentDescription", is("My Content 1")))
                    .andExpect(jsonPath("$.payload.config.contents[1].contentId", is("ABC2")))
                    .andExpect(jsonPath("$.payload.config.contents[1].contentDescription", is("My Content 2")));

            IPage updatedPage = pageManager.getDraftPage(pageCode);
            Widget widget = updatedPage.getWidgets()[0];

            assertThat(widget.getConfig().get("contents"),
                    equalTo("[{contentId=ABC1,contentDescription=My Content 1},{contentId=ABC2,contentDescription=My Content 2}]"));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testPutPageConfigurationInvalidFrame() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, "XXX"})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            String getResult = result.andReturn().getResponse().getContentAsString();
            result.andExpect(status().isBadRequest());

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testPutPageConfigurationWrongFrame() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, "500"})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isNotFound());
            result.andExpect(jsonPath("$.errors[0].code", is("2")));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testDeletePageConfigurationWithInvalidFrameId() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            result = mockMvc
                    .perform(delete("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, "XXX"})
                            .param("status", IPageService.STATUS_DRAFT)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());

            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is("40")));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testDeletePageConfigurationWithWrongFrameId() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 0})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            result = mockMvc
                    .perform(delete("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 9999})
                            .param("status", IPageService.STATUS_DRAFT)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testGetPageWidgetConfiguration() throws Exception {
        String pageCode = "draft_page_100";
        try {
            Page mockPage = createPage(pageCode, null);
            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, 999})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isNotFound());
            result.andExpect(jsonPath("$.errors[0].code", is("2")));

            result = mockMvc
                    .perform(get("/pages/{pageCode}/widgets/{frame}", new Object[]{pageCode, "ASD"})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is("40")));

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    /**
     * creates a page without configured frames than applies the default widgets
     */
    @Test
    public void testApplyDefautWidgets() throws Exception {
        String pageCode = "draft_page_100";
        try {
            PageModel pageModel = this.pageModelManager.getPageModel("internal");
            Page mockPage = createPage(pageCode, pageModel);

            mockPage.setWidgets(new Widget[mockPage.getWidgets().length]);

            this.pageManager.addPage(mockPage);
            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                    .perform(get("/pages/{pageCode}/configuration", new Object[]{pageCode})
                            .param("status", IPageService.STATUS_DRAFT)
                            .header("Authorization", "Bearer " + accessToken));

            Widget[] defaultWidgetConfiguration = pageModel.getDefaultWidget();
            
            result.andExpect(status().isOk());
            String stringResult = result.andReturn().getResponse().getContentAsString();
            System.out.println("*********************************************");
            System.out.println(stringResult);
            System.out.println("*********************************************");
            result.andExpect(jsonPath("$.payload.widgets", Matchers.hasSize(pageModel.getConfiguration().length)));
            for (int i = 0; i < pageModel.getConfiguration().length; i++) {
                String path = String.format("$.payload.widgets[%d]", i);
                result.andExpect(jsonPath(path, is(nullValue())));
            }

            result = mockMvc
                    .perform(put("/pages/{pageCode}/configuration/defaultWidgets", new Object[]{pageCode})
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());
            for (int i = 0; i < pageModel.getConfiguration().length; i++) {
                String path = String.format("$.payload.widgets[%d]", i);

                if (null != defaultWidgetConfiguration[i]) {
                    WidgetConfigurationDto exp = new WidgetConfigurationDto(defaultWidgetConfiguration[i].getType().getCode(), defaultWidgetConfiguration[i].getConfig());
                    Map actual = mapper.convertValue(exp, Map.class); //jsonPath workaround
                    result.andExpect(jsonPath(path, is(actual)));
                } else {
                    result.andExpect(jsonPath(path, is(nullValue())));
                }
            }

        } finally {
            this.pageManager.deletePage(pageCode);
        }
    }

    protected Page createPage(String pageCode, PageModel pageModel) {
        IPage parentPage = pageManager.getDraftPage("service");
        if (null == pageModel) {
            pageModel = parentPage.getMetadata().getModel();
        }
        PageMetadata metadata = PageTestUtil.createPageMetadata(pageModel, true, pageCode + "_title", null, null, false, null, null);
        ApsProperties config = PageTestUtil.createProperties("modelId", "default", "contentId", "EVN24");
        Widget widgetToAdd = PageTestUtil.createWidget("content_viewer", config, this.widgetTypeManager);
        Widget[] widgets = new Widget[pageModel.getFrames().length];
        widgets[0] = widgetToAdd;
        Page pageToAdd = PageTestUtil.createPage(pageCode, parentPage.getCode(), "free", metadata, widgets);
        return pageToAdd;
    }

}
