package org.entando.entando.aps.system.services.digitalexchange.job;

import org.entando.entando.aps.system.jpa.EntandoJPASpecification;
import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJobRepository;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DigitalExchangeJobService {

    private final DigitalExchangeJobRepository repository;
    private final JobFieldsConverter fieldsConverter;

    public DigitalExchangeJobService(DigitalExchangeJobRepository repository) {
       this.repository = repository;
       this.fieldsConverter = new JobFieldsConverter();
    }

    public PagedRestResponse<DigitalExchangeJob> findAll(RestListRequest request) {
        Pageable pageable = pageableFromRequest(request);
        Page<DigitalExchangeJob> page;
        if (request.getFilters() != null && request.getFilters().length > 0) {
            EntandoJPASpecification<DigitalExchangeJob> specification = new EntandoJPASpecification<>(request, fieldsConverter);
            page = this.repository.findAll(specification, pageable);
        } else {
            page = this.repository.findAll(pageable);
        }
        return buildResponse(page, request);
    }

    public Optional<DigitalExchangeJob> findById(String id) {
        return this.repository.findById(id);
    }

    private Pageable pageableFromRequest(RestListRequest restListRequest) {

        int page = restListRequest.getPage() - 1;
        int size = restListRequest.getPageSize();

        if (restListRequest.getSort() != null && !restListRequest.getSort().isEmpty()) {
            Sort sort = Sort.by(getDirection(restListRequest), getSortValues(restListRequest));
            return PageRequest.of(page, size, sort);
        } else {
            return PageRequest.of(page, size);
        }
    }

    private String[] getSortValues(RestListRequest restListRequest) {
        String[] values = restListRequest.getSort().split(",");
        String[] remappedValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            remappedValues[i] = fieldsConverter.getFieldName(values[i]);
        }
        return remappedValues;
    }

    private static Sort.Direction getDirection(RestListRequest restListRequest) {
        switch (restListRequest.getDirection()) {
            case "ASC":
                return Sort.Direction.ASC;
            case "DESC":
                return Sort.Direction.DESC;
            default:
                throw new IllegalArgumentException("Unrecognized direction: " + restListRequest.getDirection());
        }
    }

    private <T> PagedRestResponse<T> buildResponse(Page<T> page, RestListRequest restListRequest) {

        PagedMetadata<T> pagedMetadata = new PagedMetadata<>(
                restListRequest, page.getContent(), (int) page.getTotalElements());

        PagedRestResponse<T> response = new PagedRestResponse<>(pagedMetadata);
        response.setPayload(page.getContent());

        return response;
    }
}
