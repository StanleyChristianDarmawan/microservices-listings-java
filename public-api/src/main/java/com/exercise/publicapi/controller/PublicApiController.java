package com.exercise.publicapi.controller;

import com.exercise.publicapi.dto.*;
import com.exercise.publicapi.service.PublicApiService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/public-api")
public class PublicApiController {

    @Autowired
    private PublicApiService publicApiService;

    @GetMapping("/listings")
    public PublicGetListingsResponseDto getListings(
            @RequestParam(name = "pageNum", defaultValue = "1") @Min(1) Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam(name = "userId", required = false) @Min(1) Integer userId
    ) {
        return publicApiService.getListings(pageNum, pageSize, userId);
    }

    @PostMapping("/users")
    public PublicCreateUserResponseDto createUser(
            @Valid @RequestBody PublicCreateUserRequestDto requestDto
    ) {
        return publicApiService.createUser(requestDto);
    }

    @PostMapping("/listings")
    public PublicCreateListingResponseDto createListing(
            @Valid @RequestBody PublicCreateListingRequestDto requestDto
    ) {
        return publicApiService.createListing(requestDto);
    }
}
