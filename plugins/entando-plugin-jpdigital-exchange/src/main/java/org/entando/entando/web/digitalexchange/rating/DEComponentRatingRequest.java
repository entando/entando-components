package org.entando.entando.web.digitalexchange.rating;

import org.hibernate.validator.constraints.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Validated
public class DEComponentRatingRequest {

    @NotBlank
    private String componentId;

    @Min(1)
    @Max(5)
    private int rating;

    @NotEmpty
    @Size(max = 200)
    private String reviewerId;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }
}
