package org.entando.entando.aps.system.services;

import org.entando.entando.aps.system.jpa.EntandoJPASpecification;
import org.entando.entando.aps.system.jpa.EntityFieldsConverter;
import org.entando.entando.aps.system.jpa.JpaSpecificationRepository;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public abstract class EntandoBaseJpaService<T, ID> {

    protected abstract EntityFieldsConverter<T> getFieldsConverter();

    protected abstract JpaSpecificationRepository<T, ID>  getRepository();

    public PagedRestResponse<T> findAll(RestListRequest request) {
        Pageable pageable = pageableFromRequest(request);
        Page<T> page;
        if (request.getFilters() != null && request.getFilters().length > 0) {
            EntandoJPASpecification<T> specification = new EntandoJPASpecification<>(request, this.getFieldsConverter());
            page = this.getRepository().findAll(specification, pageable);
        } else {
            page = this.getRepository().findAll(pageable);
        }
        return buildResponse(page, request);
    }

    protected Pageable pageableFromRequest(RestListRequest restListRequest) {

        int page = restListRequest.getPage() - 1;
        int size = restListRequest.getPageSize();

        if (restListRequest.getSort() != null && !restListRequest.getSort().isEmpty()) {
            Sort sort = Sort.by(getDirection(restListRequest), getSortValues(restListRequest));
            return PageRequest.of(page, size, sort);
        } else {
            return PageRequest.of(page, size);
        }
    }

    protected String[] getSortValues(RestListRequest restListRequest) {
        String[] values = restListRequest.getSort().split(",");
        String[] remappedValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            remappedValues[i] = this.getFieldsConverter().getFieldName(values[i]);
        }
        return remappedValues;
    }

    protected static Sort.Direction getDirection(RestListRequest restListRequest) {
        switch (restListRequest.getDirection()) {
            case "ASC":
                return Sort.Direction.ASC;
            case "DESC":
                return Sort.Direction.DESC;
            default:
                throw new IllegalArgumentException("Unrecognized direction: " + restListRequest.getDirection());
        }
    }

    protected <T> PagedRestResponse<T> buildResponse(Page<T> page, RestListRequest restListRequest) {

        PagedMetadata<T> pagedMetadata = new PagedMetadata<>(
                restListRequest, page.getContent(), (int) page.getTotalElements());

        PagedRestResponse<T> response = new PagedRestResponse<>(pagedMetadata);
        response.setPayload(page.getContent());

        return response;
    }
}
