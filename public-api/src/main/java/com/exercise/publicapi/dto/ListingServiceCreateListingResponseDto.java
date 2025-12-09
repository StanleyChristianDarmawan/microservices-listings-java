package com.exercise.publicapi.dto;

public class ListingServiceCreateListingResponseDto {

    private boolean result;
    private ListingDto listing;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public ListingDto getListing() {
        return listing;
    }

    public void setListing(ListingDto listing) {
        this.listing = listing;
    }
}
