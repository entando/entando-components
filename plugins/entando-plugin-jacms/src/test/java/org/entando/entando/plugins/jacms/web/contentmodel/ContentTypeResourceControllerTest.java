package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.plugins.jacms.aps.system.services.ContentTypeService;
import org.entando.entando.plugins.jacms.web.contentmodel.util.RestControllerTestUtil;
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

    @Before
    public void setup() {
        controller = new ContentTypeResourceController(service);
    }


    @Test
    public void listEmptyShouldReturnOkStatusAndEmptyList() {
        RestListRequest listRequest = new RestListRequest();


        when(service.findMany(listRequest)).thenReturn(RestControllerTestUtil.EMPTY_PAGED_METADATA);


        ResponseEntity<PagedMetadata<EntityTypeShortDto>> response = controller.list(listRequest);
        verify(service).findMany(listRequest);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBody()).isEmpty();
    }

    @Test
    public void listShouldReturnAllItems() {
        RestListRequest listRequest = new RestListRequest();
        PagedMetadata<EntityTypeShortDto> pagedMetadata = createNonEmptyPagedMetadata();


        when(service.findMany(listRequest)).thenReturn(pagedMetadata);


        ResponseEntity<PagedMetadata<EntityTypeShortDto>> response = controller.list(listRequest);
        verify(service).findMany(listRequest);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBody())
                .isNotEmpty()
                .hasSize(3)
                .isEqualTo(pagedMetadata.getBody());
    }


    @Test
    public void getNonExistingContentTypeReturnsNotFound() {
        ResponseEntity<ContentTypeDto> response = controller.get(null);
        verify(service).findOne(null);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void getExistingContentTypeReturnsContentType() {
        ContentType contentType = new ContentType();
        contentType.setTypeCode("ABC");
        ContentTypeDto expectedContentType = new ContentTypeDto(contentType, Collections.emptyList());
        when(service.findOne("ABC")).thenReturn(Optional.of(expectedContentType));


        ResponseEntity<ContentTypeDto> response = controller.get("ABC");
        verify(service).findOne("ABC");


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedContentType);
    }


    @Test
    public void createValidContentTypeReturnsElementWithNewId() throws URISyntaxException {
        ContentType contentType = new ContentType();
        contentType.setTypeCode("ABC");
        ContentTypeDtoRequest requestContentType = new ContentTypeDtoRequest(contentType, Collections.emptyList());

        ContentType createdContentType = new ContentType();
        createdContentType.setTypeCode("ABC");
        ContentTypeDto createdContentTypeDto =
                new ContentTypeDto(createdContentType, Collections.emptyList());

        when(service.create(requestContentType, bindingResult)).thenReturn(createdContentTypeDto);


        ResponseEntity<ContentTypeDto> response = controller.create(requestContentType, bindingResult);
        verify(service).create(requestContentType, bindingResult);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("ABC");
    }


    @Test
    public void deleteContentTypeShouldCallServiceAndReturnOkStatus() {
        ResponseEntity<Void> response = controller.delete("ABC");
        verify(service).delete("ABC");


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void updateExistingContentTypeShouldCallServiceShouldReturnOk() {
        ContentType contentType = new ContentType();
        contentType.setTypeCode("ABC");
        ContentTypeDtoRequest requestContentType =
                new ContentTypeDtoRequest(contentType, Collections.emptyList());

        ContentTypeDto contentTypeDto = new ContentTypeDto(contentType, Collections.emptyList());

        when(service.update(requestContentType, bindingResult)).thenReturn(contentTypeDto);

        ResponseEntity<ContentTypeDto> response = controller.update(requestContentType, bindingResult);
        verify(service).update(requestContentType, bindingResult);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("ABC");
    }
}