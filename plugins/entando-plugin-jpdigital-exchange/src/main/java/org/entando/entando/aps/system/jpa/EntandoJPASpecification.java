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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.data.jpa.domain.Specification;

public class EntandoJPASpecification<T> implements Specification<T> {

    private final EntityFieldsConverter<T> converter;
    private final Filter[] filters;

    public EntandoJPASpecification(RestListRequest restListRequest, EntityFieldsConverter<T> converter) {
        this.converter = converter;
        this.filters = restListRequest.getFilters();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

        List<Predicate> andPredicates = new ArrayList<>();

        for (Filter filter : filters) {

            String fieldName = converter.getFieldName(filter.getAttributeName());
            Expression expression = root.get(fieldName);

            String[] values = getValues(filter.getValue());

            List<Predicate> filterPredicates = new ArrayList<>();

            for (String stringValue : values) {

                Comparable value = converter.getComparableValue(filter.getAttribute(), stringValue);

                switch (FilterOperator.parse(filter.getOperator())) {
                    case EQUAL:
                        filterPredicates.add(cb.equal(expression, value));
                        break;
                    case LIKE:
                        if (value instanceof String) {
                            filterPredicates.add(cb.like(expression, (String) value));
                        } else {
                            filterPredicates.add(cb.equal(expression, value));
                        }
                        break;
                    case NOT_EQUAL:
                        filterPredicates.add(cb.notEqual(expression, value));
                        break;
                    case GREATER:
                        filterPredicates.add(cb.greaterThan(expression, value));
                        break;
                    case LOWER:
                        filterPredicates.add(cb.lessThan(expression, value));
                        break;
                }
            }

            if (!filterPredicates.isEmpty()) {
                andPredicates.add(cb.or(toPredicatesArray(filterPredicates)));
            }
        }

        // join predicates
        return cb.and(toPredicatesArray(andPredicates));
    }

    private Predicate[] toPredicatesArray(List<Predicate> predicates) {
        return predicates.toArray(new Predicate[predicates.size()]);
    }

    // TODO: define comma escape character
    private String[] getValues(String filterValue) {
        if (filterValue == null) {
            return new String[]{};
        }
        return filterValue.split(",");
    }
}
