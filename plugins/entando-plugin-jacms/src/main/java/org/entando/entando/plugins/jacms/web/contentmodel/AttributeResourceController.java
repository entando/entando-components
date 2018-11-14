package org.entando.entando.plugins.jacms.web.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/plugins/cms/attributes")
public class AttributeResourceController implements AttributeResource {
    @Override
    public ResponseEntity<AttributeDto> create(AttributeDto attribute) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<List<AttributeDto>> list(Long offset, Integer page, Integer pageNumber, Integer pageSize, Boolean paged, Integer size, List<String> sort, Boolean sortSorted, Boolean sortUnsorted, Boolean unpaged) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<AttributeDto> get(Long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<AttributeDto> update(AttributeDto attribute) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
