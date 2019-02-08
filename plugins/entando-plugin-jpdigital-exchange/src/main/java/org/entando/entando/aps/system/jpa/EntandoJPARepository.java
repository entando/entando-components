/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.jpa;

import javax.persistence.EntityManager;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * JPA Repository able to convert Entando paging, sorting and filtering models
 * into Spring Data JPA ones and vice-versa.
 */
public class EntandoJPARepository<T, ID> extends SimpleJpaRepository<T, ID> {

    private final EntityFieldsConverter<T> fieldsConverter;

    public EntandoJPARepository(Class<T> domainClass, EntityManager em, EntityFieldsConverter<T> fieldsConverter) {
        super(domainClass, em);
        this.fieldsConverter = fieldsConverter;
    }

    public PagedRestResponse<T> findAll(RestListRequest request) {
        Pageable pageable = pageableFromRequest(request);
        Page<T> page;
        if (request.getFilters() != null && request.getFilters().length > 0) {
            EntandoJPASpecification<T> specification = new EntandoJPASpecification<>(request, fieldsConverter);
            page = findAll(specification, pageable);
        } else {
            page = findAll(pageable);
        }
        return buildResponse(page, request);
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
