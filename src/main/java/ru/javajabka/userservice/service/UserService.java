package ru.javajabka.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javajabka.userservice.exception.BadRequestException;
import ru.javajabka.userservice.model.UserRequest;
import ru.javajabka.userservice.model.UserResponse;
import ru.javajabka.userservice.repository.UserRepository;

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
        return userRepository.insert(userRequest);
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

        if (userRequest.getUserName() == null || userRequest.getUserName().isEmpty()) {
            throw new BadRequestException("Введите имя пользователя");
        }

        if (userRequest.getPassword() == null || userRequest.getPassword().isEmpty()) {
            throw new BadRequestException("Введите пароль");
        }

    }

    private boolean validatePassword(final UserRequest userRequest) {
        Pattern p = Pattern.compile("(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}");
        Matcher m = p.matcher(userRequest.getPassword());
        return m.matches();
    }
}