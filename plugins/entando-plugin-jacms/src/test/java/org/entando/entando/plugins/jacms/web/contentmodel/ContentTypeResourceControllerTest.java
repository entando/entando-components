package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.plugins.jacms.aps.system.services.ContentTypeService;
import org.entando.entando.plugins.jacms.web.contentmodel.util.RestControllerTestUtil;
import org.entando.entando.plugins.jacms.web.contentmodel.validator.ContentTypeValidator;
import org.entando.entando.web.common.model.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;

import java.net.URISyntaxException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.entando.entando.plugins.jacms.web.contentmodel.util.RestControllerTestUtil.createNonEmptyPagedMetadata;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContentTypeResourceControllerTest {

    private ContentTypeResourceController controller;

    @Mock
    private ContentTypeService service;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private ContentTypeValidator validator;

    @Before
    public void setup() {
        controller = new ContentTypeResourceController(service, validator);
    }


    @Test
    public void listEmptyShouldReturnOkStatusAndEmptyList() {
        RestListRequest listRequest = new RestListRequest();


        when(service.findMany(listRequest)).thenReturn(RestControllerTestUtil.EMPTY_PAGED_METADATA);


        ResponseEntity<PagedRestResponse<EntityTypeShortDto>> response = controller.list(listRequest);
        verify(service).findMany(listRequest);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPayload()).isEmpty();
    }

    @Test
    public void listShouldReturnAllItems() {
        RestListRequest listRequest = new RestListRequest();
        PagedMetadata<EntityTypeShortDto> pagedMetadata = createNonEmptyPagedMetadata();


        when(service.findMany(listRequest)).thenReturn(pagedMetadata);


        ResponseEntity<PagedRestResponse<EntityTypeShortDto>> response = controller.list(listRequest);
        verify(service).findMany(listRequest);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPayload())
                .isNotEmpty()
                .hasSize(3)
                .isEqualTo(pagedMetadata.getBody());
    }


    @Test
    public void getNonExistingContentTypeReturnsNotFound() {
        ResponseEntity<SimpleRestResponse<ContentTypeDto>> response = controller.get(null);
        verify(service).findOne(null);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void getExistingContentTypeReturnsContentType() {
        Content content = new Content();
        content.setTypeCode("ABC");
        ContentTypeDto expectedContentType = new ContentTypeDto(content, Collections.emptyList());
        when(service.findOne("ABC")).thenReturn(Optional.of(expectedContentType));


        ResponseEntity<SimpleRestResponse<ContentTypeDto>> response = controller.get("ABC");
        verify(service).findOne("ABC");


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPayload()).isEqualTo(expectedContentType);
    }


    @Test
    public void createValidContentTypeReturnsElementWithNewId() throws URISyntaxException {
        Content content = new Content();
        content.setTypeCode("ABC");
        ContentTypeDtoRequest requestContentType = new ContentTypeDtoRequest(content, Collections.emptyList());

        ContentTypeDto createdContentTypeDto =
                new ContentTypeDto(content, Collections.emptyList());

        when(service.create(requestContentType, bindingResult)).thenReturn(createdContentTypeDto);


        ResponseEntity<SimpleRestResponse<ContentTypeDto>> response = controller.create(requestContentType, bindingResult);
        verify(service).create(requestContentType, bindingResult);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPayload().getCode()).isEqualTo("ABC");
    }


    @Test
    public void deleteContentTypeShouldCallServiceAndReturnOkStatus() {
        ResponseEntity<Void> response = controller.delete("ABC");
        verify(service).delete("ABC");


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void updateExistingContentTypeShouldCallServiceShouldReturnOk() {
        Content content = new Content();
        content.setTypeCode("ABC");
        ContentTypeDtoRequest requestContentType =
                new ContentTypeDtoRequest(content, Collections.emptyList());

        ContentTypeDto contentTypeDto = new ContentTypeDto(content, Collections.emptyList());

        when(service.update(requestContentType, bindingResult)).thenReturn(contentTypeDto);

        ResponseEntity<SimpleRestResponse<ContentTypeDto>> response = controller.update(requestContentType, bindingResult);
        verify(service).update(requestContentType, bindingResult);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPayload().getCode()).isEqualTo("ABC");
    }
}