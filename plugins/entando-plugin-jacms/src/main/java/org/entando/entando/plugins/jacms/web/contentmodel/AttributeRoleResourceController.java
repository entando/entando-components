package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeRoleDto;
import org.entando.entando.plugins.jacms.aps.system.services.AttributeRoleService;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.*;
import java.util.Optional;

@RestController
@RequestMapping(value = "/plugins/cms/attributeroles")
public class AttributeRoleResourceController implements AttributeRoleResource {

    private final AttributeRoleService service;

    @Autowired
    public AttributeRoleResourceController(AttributeRoleService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<AttributeRoleDto> create(@Valid @RequestBody AttributeRoleDto entity) throws URISyntaxException {
        AttributeRoleDto result = service.create(entity);

        return ResponseEntity.created(new URI("/plugins/cms/attributeroles/" + result.getId()))
                .body(result);
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PagedMetadata<AttributeRoleDto>> list(RestListRequest listRequest) {
        PagedMetadata<AttributeRoleDto> result = service.findMany(listRequest);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<AttributeRoleDto> get(@PathVariable("id") Long id) {
        Optional<AttributeRoleDto> maybeContentType = service.findById(id);

        return maybeContentType.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<AttributeRoleDto> update(@Valid @RequestBody AttributeRoleDto attributeRole) {
        return ResponseEntity.ok(service.update(attributeRole));
    }
}
