package com.exercise.userservice.service.impl;

import com.exercise.userservice.dto.CreateUserRequestDto;
import com.exercise.userservice.dto.CreateUserResponseDto;
import com.exercise.userservice.dto.GetUserResponseDto;
import com.exercise.userservice.dto.GetUsersRequestDto;
import com.exercise.userservice.dto.GetUsersResponseDto;
import com.exercise.userservice.dto.UserDto;
import com.exercise.userservice.entity.User;
import com.exercise.userservice.repository.UserRepository;
import com.exercise.userservice.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public GetUsersResponseDto getUsers(GetUsersRequestDto requestDto) {
        PageRequest pageRequest = PageRequest.of(
                requestDto.getPageNum(),
                requestDto.getPageSize(),
                Sort.by("createdAt").descending()
        );

        List<User> result = userRepository.findAll(pageRequest).getContent();

        List<UserDto> userDtoList = result.stream()
                .map(this::convertUserToUserDto)
                .collect(Collectors.toList());

        GetUsersResponseDto responseDto = new GetUsersResponseDto();
        responseDto.setResult(true);
        responseDto.setUsers(userDtoList);

        return responseDto;
    }

    @Override
    public GetUserResponseDto getUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found")
                );

        GetUserResponseDto responseDto = new GetUserResponseDto();
        responseDto.setResult(true);
        responseDto.setUser(convertUserToUserDto(user));

        return responseDto;
    }

    @Override
    public CreateUserResponseDto createUser(CreateUserRequestDto requestDto) {
        User user = new User();
        user.setName(requestDto.getName());

        final Long timestampInMicroSecond = nowInEpochMicroSecond();
        user.setCreatedAt(timestampInMicroSecond);
        user.setUpdatedAt(timestampInMicroSecond);

        userRepository.save(user);

        CreateUserResponseDto responseDto = new CreateUserResponseDto();
        responseDto.setResult(true);
        responseDto.setUser(convertUserToUserDto(user));

        return responseDto;
    }

    private UserDto convertUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    private Long nowInEpochMicroSecond() {
        return ChronoUnit.MICROS.between(Instant.EPOCH, Instant.now());
    }
}
