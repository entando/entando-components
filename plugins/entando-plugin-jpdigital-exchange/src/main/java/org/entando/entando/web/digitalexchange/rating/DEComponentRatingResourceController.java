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
package org.entando.entando.web.digitalexchange.rating;

import com.agiletec.aps.system.services.user.UserDetails;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.entando.entando.aps.system.services.digitalexchange.rating.DEComponentRatingResult;
import org.entando.entando.aps.system.services.digitalexchange.rating.DERatingService;
import org.entando.entando.aps.system.services.digitalexchange.rating.DERatingsSummary;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DEComponentRatingResourceController implements DEComponentRatingResource {

    private static final String ERROR_CODE_RATING_NOT_SUPPORTED = "2";

    private final DERatingService ratingService;

    @Autowired
    public DEComponentRatingResourceController(DERatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public ResponseEntity<SimpleRestResponse<DERatingsSummary>> rateComponent(@PathVariable("exchange") String exchangeId,
            @PathVariable("component") String componentId, @Valid @RequestBody DERatingValue rating, HttpServletRequest request) {

        UserDetails currentUser = (UserDetails) request.getSession().getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String reviewer = String.format("%s@%s", currentUser.getUsername(), request.getServerName());

        DEComponentRatingRequest ratingRequest = new DEComponentRatingRequest();
        ratingRequest.setComponentId(componentId);
        ratingRequest.setReviewerId(reviewer);
        ratingRequest.setExchangeId(exchangeId);
        ratingRequest.setRating(rating.getRating());

        DEComponentRatingResult result = ratingService.rateComponent(ratingRequest);

        SimpleRestResponse<DERatingsSummary> response = new SimpleRestResponse<>(null);

        if (!result.isRatingSupported()) {
            response.addError(new RestError(ERROR_CODE_RATING_NOT_SUPPORTED, "digitalExchange.rating.notSupported"));
            return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
        }

        if (!result.getErrors().isEmpty()) {
            response.setErrors(result.getErrors());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.setPayload(result.getRatingsSummary());
        return ResponseEntity.ok(response);
    }
}
