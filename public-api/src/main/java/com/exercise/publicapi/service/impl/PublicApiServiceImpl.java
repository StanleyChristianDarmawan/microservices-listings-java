package com.exercise.publicapi.service.impl;

import com.exercise.publicapi.dto.*;
import com.exercise.publicapi.service.PublicApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicApiServiceImpl implements PublicApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.listing.base-url}")
    private String listingServiceBaseUrl;

    @Value("${services.user.base-url}")
    private String userServiceBaseUrl;

    @Override
    public PublicGetListingsResponseDto getListings(Integer pageNum, Integer pageSize, Integer userId) {
        StringBuilder urlBuilder = new StringBuilder(listingServiceBaseUrl)
                .append("/listings")
                .append("?pageNum=").append(pageNum)
                .append("&pageSize=").append(pageSize);

        if (userId != null) {
            urlBuilder.append("&userId=").append(userId);
        }

        ListingServiceGetListingsResponseDto listingResponse =
                restTemplate.getForObject(urlBuilder.toString(), ListingServiceGetListingsResponseDto.class);

        List<ListingDto> listings = listingResponse.getListings();

        List<ListingDto> enrichedListings = listings.stream()
                .map(listing -> {
                    UserDto user = fetchUser(listing.getUserId());
                    listing.setUser(user);
                    return listing;
                })
                .collect(Collectors.toList());

        PublicGetListingsResponseDto response = new PublicGetListingsResponseDto();
        response.setResult(listingResponse.isResult());
        response.setListings(enrichedListings);

        return response;
    }

    @Override
    public PublicCreateUserResponseDto createUser(PublicCreateUserRequestDto requestDto) {
        String url = userServiceBaseUrl + "/users";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name", requestDto.getName());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

        UserServiceCreateUserResponseDto userServiceResponse =
                restTemplate.postForObject(url, entity, UserServiceCreateUserResponseDto.class);

        PublicCreateUserResponseDto response = new PublicCreateUserResponseDto();
        response.setUser(userServiceResponse.getUser());
        return response;
    }

    @Override
    public PublicCreateListingResponseDto createListing(PublicCreateListingRequestDto requestDto) {
        String url = listingServiceBaseUrl + "/listings";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("userId", String.valueOf(requestDto.getUserId()));
        form.add("listingType", requestDto.getListingType());
        form.add("price", String.valueOf(requestDto.getPrice()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

        ListingServiceCreateListingResponseDto listingServiceResponse =
                restTemplate.postForObject(url, entity, ListingServiceCreateListingResponseDto.class);

        PublicCreateListingResponseDto response = new PublicCreateListingResponseDto();
        response.setListing(listingServiceResponse.getListing());
        return response;
    }

    private UserDto fetchUser(Integer userId) {
        String url = userServiceBaseUrl + "/users/" + userId;
        UserServiceGetUserResponseDto userResponse =
                restTemplate.getForObject(url, UserServiceGetUserResponseDto.class);
        return userResponse.getUser();
    }
}
