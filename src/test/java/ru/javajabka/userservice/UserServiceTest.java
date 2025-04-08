package ru.javajabka.userservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.javajabka.userservice.exception.BadRequestException;
import ru.javajabka.userservice.model.User;
import ru.javajabka.userservice.model.UserResponseDTO;
import ru.javajabka.userservice.repository.UserRepository;
import ru.javajabka.userservice.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldCreateUserCorrect() {
        User userRequest = buildUserRequest("Vasya", "VasyaPass");
        UserResponseDTO userResponse = buildUserRequest(1L, "Vasya");
        User userWithHashedPass = buildUserRequest("Vasya", "aa357dc8768d5f4db190b5a41ac2b79fa6909fe01e2de3d13a765f9a34f82d21");
        Mockito.when(userRepository.insert(userWithHashedPass)).thenReturn(userResponse);
        UserResponseDTO createdUserResponse = userService.userCreate(userRequest);
        Assertions.assertEquals(userResponse, createdUserResponse);
        Mockito.verify(userRepository).insert(userWithHashedPass);
    }

    @Test
    public void shouldReturnUser_WhenGetUserByIdCorrect() {
        UserResponseDTO userResponse = buildUserRequest(1L, "Vasya");
        Mockito.when(userRepository.getById(1L)).thenReturn(userResponse);
        UserService userService = new UserService(userRepository);
        UserResponseDTO createdUserResponse = userService.getUserById(1L);
        Assertions.assertEquals(userResponse, createdUserResponse);
        Mockito.verify(userRepository).getById(1L);
    }


    @Test
    public void shouldDeleteUserCorrect() {
        UserResponseDTO userResponse = buildUserRequest(1L, "Vasya");
        UserService userService = new UserService(userRepository);
        userService.delete(1L);
        Mockito.verify(userRepository).delete(1L);
    }

    @Test
    public void shouldReturnError_WhenCreate_And_UserNameEmpty() {
        User userRequest = buildUserRequest("", "VasyaPass");
        UserService userService = new UserService(userRepository);
        final BadRequestException createUserException = Assertions.assertThrows(BadRequestException.class,
                () -> userService.userCreate(userRequest)
        );
        Assertions.assertEquals(createUserException.getMessage(), "Введите имя пользователя");
    }

    @Test
    public void shouldReturnError_WhenUpdate_And_Empty() {
        User userRequest = buildUserRequest("", "VasyaPass");
        UserService userService = new UserService(userRepository);
        final BadRequestException createUserException = Assertions.assertThrows(BadRequestException.class,
                () -> userService.userCreate(userRequest)
        );
        Assertions.assertEquals(createUserException.getMessage(), "Введите имя пользователя");
    }

    private User buildUserRequest(String username, String password) {
        return User.builder()
                .userName(username)
                .password(password)
                .build();
    }
    private UserResponseDTO buildUserRequest(Long id, String username) {
        return UserResponseDTO.builder()
                .id(id)
                .userName(username)
                .build();
    }
}
