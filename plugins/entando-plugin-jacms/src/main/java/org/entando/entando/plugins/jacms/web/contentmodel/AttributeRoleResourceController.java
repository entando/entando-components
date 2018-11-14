package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeRoleDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/plugins/cms/attribute-roles")
public class AttributeRoleResourceController implements AttributeRoleResource {
    @Override
    public ResponseEntity<AttributeRoleDto> create(AttributeRoleDto attributeRole) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<List<AttributeRoleDto>> list(Long offset, Integer page, Integer pageNumber, Integer pageSize, Boolean paged, Integer size, List<String> sort, Boolean sortSorted, Boolean sortUnsorted, Boolean unpaged) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<AttributeRoleDto> get(Long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<AttributeRoleDto> update(AttributeRoleDto attributeRole) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
