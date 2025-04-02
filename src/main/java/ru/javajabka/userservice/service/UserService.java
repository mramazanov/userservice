package ru.javajabka.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.javajabka.userservice.exception.BadRequestException;
import ru.javajabka.userservice.model.UserRequest;
import ru.javajabka.userservice.model.UserResponse;
import ru.javajabka.userservice.repository.UserRepository;
import ru.javajabka.userservice.util.HashUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public UserResponse userCreate(final UserRequest userRequest) {
        validate(userRequest);

        UserRequest userWithHashedPassword = UserRequest.builder()
                .userName(userRequest.getUserName())
                .password(HashUtil.getHashedPassword(userRequest.getPassword()))
                .build();

        return userRepository.insert(userWithHashedPassword);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(final Long id) {
        return userRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers(final List<Long> ids) {
        return userRepository.getUsers(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponse userUpdate(final Long id, final UserRequest userRequest) {
        return userRepository.update(id, userRequest);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponse delete(Long id) {
        return userRepository.delete(id);
    }

    private void validate(final UserRequest userRequest) {
        if (userRequest == null) {
            throw new BadRequestException("Введите информацию о пользователе");
        }

        if (!StringUtils.hasText(userRequest.getUserName())) {
            throw new BadRequestException("Введите имя пользователя");
        }

        if (!StringUtils.hasText(userRequest.getUserName())) {
            throw new BadRequestException("Введите пароль");
        }

    }
}