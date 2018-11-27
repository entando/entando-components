package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeDto;
import org.entando.entando.plugins.jacms.aps.system.services.AttributeService;
import org.entando.entando.web.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.*;
import java.util.Optional;

@RestController
@RequestMapping(value = "/plugins/cms/attributes")
public class AttributeResourceController implements AttributeResource {

    private final AttributeService service;

    @Autowired
    public AttributeResourceController(AttributeService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<AttributeDto> create(@Valid @RequestBody AttributeDto attribute) throws URISyntaxException {
        AttributeDto result = service.create(attribute);

        return ResponseEntity.created(new URI("/plugins/cms/attributes/" + result.getId()))
                .body(result);
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AttributeDto> get(@PathVariable("id") Long id) {
        Optional<AttributeDto> maybeContentType = service.findById(id);

        return maybeContentType.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<AttributeDto> update(@Valid @RequestBody AttributeDto attribute) {
        return ResponseEntity.ok(service.update(attribute));
    }

    @Override
    public ResponseEntity<PagedMetadata<AttributeDto>> list(RestListRequest listRequest) {
        PagedMetadata<AttributeDto> result = service.findMany(listRequest);
        return ResponseEntity.ok(result);
    }
}
