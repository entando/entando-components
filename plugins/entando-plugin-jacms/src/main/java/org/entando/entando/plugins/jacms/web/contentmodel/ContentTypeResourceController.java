package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.*;
import java.util.Optional;

@RestController
@RequestMapping(value = "/plugins/cms/content-types")
public class ContentTypeResourceController implements ContentTypeResource {

    private final ContentTypeService contentTypeService;

    @Autowired
    public ContentTypeResourceController(ContentTypeService contentTypeService) {
        this.contentTypeService = contentTypeService;
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<ContentTypeDto> create(@Valid @RequestBody ContentTypeDto contentType) throws URISyntaxException {
        ContentTypeDto result = contentTypeService.create(contentType);

        return ResponseEntity.created(new URI("/plugins/cms/content-types/" + result.getId()))
                             .body(result);
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        contentTypeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PagedMetadata<ContentTypeDto>> list(RestListRequest listRequest) {
        PagedMetadata<ContentTypeDto> result = contentTypeService.findMany(listRequest);
        return ResponseEntity.ok(result);
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<ContentTypeDto> get(@PathVariable("id") Long id) {
        Optional<ContentTypeDto> maybeContentType = contentTypeService.findById(id);

        return maybeContentType.map(ResponseEntity::ok)
                               .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    public ResponseEntity<ContentTypeDto> update(@Valid @RequestBody ContentTypeDto contentType) {
        return ResponseEntity.ok(contentTypeService.update(contentType));
    }
}
