package org.entando.entando.web.digitalexchange.rating;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Validated
public class DEComponentRatingRequest {

    @NotBlank
    private String componentId;

    @NotBlank
    private String exchangeId;

    /**
     * On the DE persistence layer rating can range from 0-100 to make it
     * compatible with different clients using different scales.
     */
    @Min(0)
    @Max(100)
    private int rating;

    @NotEmpty
    @Size(max = 200)
    private String reviewerId;

    public DEComponentRatingRequest() {
        // Empty constructor for json serialization
    }

    public DEComponentRatingRequest(String componentId, String exchangeId, int rating, String reviewerId) {
        this.componentId = componentId;
        this.exchangeId = exchangeId;
        this.rating = rating;
        this.reviewerId = reviewerId;
    }

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

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }
}
