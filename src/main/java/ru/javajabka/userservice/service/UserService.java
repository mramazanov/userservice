package ru.javajabka.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.javajabka.userservice.exception.BadRequestException;
import ru.javajabka.userservice.model.SuccessOperation;
import ru.javajabka.userservice.model.User;
import ru.javajabka.userservice.model.UserResponseDTO;
import ru.javajabka.userservice.repository.UserRepository;
import ru.javajabka.userservice.util.HashUtil;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO userCreate(final User userRequest) {
        validate(userRequest);

        User userWithHashedPassword = User.builder()
                .userName(userRequest.getUserName())
                .password(HashUtil.getHashedPassword(userRequest.getPassword()))
                .build();

        return userRepository.insert(userWithHashedPassword);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(final Long id) {
        return userRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsers(final List<Long> ids) {
        return userRepository.getUsers(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public SuccessOperation delete(Long id) {
        userRepository.delete(id);
        return SuccessOperation.builder()
                .success(true)
                .build();
    }

    private void validate(final User userRequest) {
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