package com.exercise.publicapi.service;

import com.exercise.publicapi.dto.PublicCreateListingRequestDto;
import com.exercise.publicapi.dto.PublicCreateListingResponseDto;
import com.exercise.publicapi.dto.PublicCreateUserRequestDto;
import com.exercise.publicapi.dto.PublicCreateUserResponseDto;
import com.exercise.publicapi.dto.PublicGetListingsResponseDto;

public interface PublicApiService {

    PublicGetListingsResponseDto getListings(Integer pageNum, Integer pageSize, Integer userId);

    PublicCreateUserResponseDto createUser(PublicCreateUserRequestDto requestDto);

    PublicCreateListingResponseDto createListing(PublicCreateListingRequestDto requestDto);
}
