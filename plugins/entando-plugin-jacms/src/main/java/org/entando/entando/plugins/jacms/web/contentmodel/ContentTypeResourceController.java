package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.plugins.jacms.aps.system.services.ContentTypeService;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.*;
import java.util.Optional;

@RestController
@RequestMapping(value = ContentTypeResourceController.BASE_URL)
public class ContentTypeResourceController implements ContentTypeResource {

    static final String BASE_URL = "/plugins/cms/contentTypes";

    private final ContentTypeService service;

    @Autowired
    public ContentTypeResourceController(ContentTypeService service) {
        this.service = service;
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<ContentTypeDto> create(@Valid @RequestBody ContentTypeDtoRequest contentType, BindingResult bindingResult)
            throws URISyntaxException {
        ContentTypeDto result = service.create(contentType, bindingResult);

        return ResponseEntity.created(new URI(BASE_URL + result.getCode()))
                             .body(result);
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<Void> delete(@PathVariable("id") String code) {
        service.delete(code);
        return ResponseEntity.ok().build();
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<PagedMetadata<EntityTypeShortDto>> list(RestListRequest listRequest) {
        PagedMetadata<EntityTypeShortDto> result = service.findMany(listRequest);
        return ResponseEntity.ok(result);
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<ContentTypeDto> get(@PathVariable("code") String code) {
        Optional<ContentTypeDto> maybeContentType = service.findOne(code);

        return maybeContentType.map(ResponseEntity::ok)
                               .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<ContentTypeDto> update(@Valid @RequestBody ContentTypeDtoRequest contentType, BindingResult bindingResult) {
        return ResponseEntity.ok(service.update(contentType, bindingResult));
    }
}
