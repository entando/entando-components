package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import org.entando.entando.plugins.jacms.web.contentmodel.util.*;
import org.entando.entando.web.common.model.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.entando.entando.plugins.jacms.web.contentmodel.util.RestControllerTestUtil.createNonEmptyPagedMetadata;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContentTypeResourceControllerTest {

    private ContentTypeResourceController controller;

    @Mock
    private ContentTypeService service;

    @Before
    public void setup() {
        controller = new ContentTypeResourceController(service);
    }


    @Test
    public void listEmptyShouldReturnOkStatusAndEmptyList() {
        RestListRequest listRequest = new RestListRequest();


        when(service.findMany(listRequest)).thenReturn(RestControllerTestUtil.EMPTY_PAGED_METADATA);


        ResponseEntity<PagedMetadata<ContentTypeDto>> response = controller.list(listRequest);
        verify(service).findMany(listRequest);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBody()).isEmpty();
    }

    @Test
    public void listShouldReturnAllItems() {
        RestListRequest listRequest = new RestListRequest();
        PagedMetadata<ContentTypeDto> pagedMetadata = createNonEmptyPagedMetadata();


        when(service.findMany(listRequest)).thenReturn(pagedMetadata);


        ResponseEntity<PagedMetadata<ContentTypeDto>> response = controller.list(listRequest);
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
        verify(service).findById(null);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void getExistingContentTypeReturnsContentType() {
        ContentTypeDto expectedContentType = new ContentTypeDtoBuilder().withId(123L).build();
        when(service.findById(123L)).thenReturn(Optional.of(expectedContentType));


        ResponseEntity<ContentTypeDto> response = controller.get(123L);
        verify(service).findById(123L);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedContentType);
    }


    @Test
    public void createValidContentTypeReturnsElementWithNewId() throws URISyntaxException {
        ContentTypeDto requestContentType = new ContentTypeDtoBuilder()
                .withId(null)
                .withCode("ABC")
                .build();

        ContentTypeDto createdContentType = new ContentTypeDtoBuilder()
                .withId(789L)
                .withCode("ABC")
                .build();

        when(service.save(requestContentType)).thenReturn(createdContentType);


        ResponseEntity<ContentTypeDto> response = controller.create(requestContentType);
        verify(service).save(requestContentType);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("ABC");
        assertThat(response.getBody().getId()).isEqualTo(789L);
    }


    @Test
    public void deleteContentTypeShouldCallServiceAndReturnOkStatus() {
        ResponseEntity<Void> response = controller.delete(345L);
        verify(service).delete(345L);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void updateExistingContentTypeShouldCallServiceShouldReturnOk() {
        ContentTypeDto requestContentType = new ContentTypeDtoBuilder().withId(376L).build();


        when(service.save(requestContentType)).thenReturn(requestContentType);


        ResponseEntity<ContentTypeDto> response = controller.update(requestContentType);
        verify(service).save(requestContentType);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(376L);
    }
}