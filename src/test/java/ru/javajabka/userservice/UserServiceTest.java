package ru.javajabka.userservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.javajabka.userservice.exception.BadRequestException;
import ru.javajabka.userservice.model.UserRequest;
import ru.javajabka.userservice.model.UserResponse;
import ru.javajabka.userservice.repository.UserRepository;
import ru.javajabka.userservice.service.UserService;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Test
    public void shouldCreateUserCorrect() {
        UserRequest userRequest = buildUserRequest("Vasya", "VasyaPass");
        UserResponse userResponse = buildUserRequest(1L, "Vasya");
        Mockito.when(userRepository.insert(userRequest)).thenReturn(userResponse);
        UserService userService = new UserService(userRepository);
        UserResponse createdUserResponse = userService.userCreate(userRequest);
        Assertions.assertEquals(userResponse, createdUserResponse);
        Mockito.verify(userRepository).insert(userRequest);
    }

    @Test
    public void shouldUpdateUserCorrect() {
        UserRequest userRequest = buildUserRequest("Vasya", "VasyaPass");
        UserResponse userResponse = buildUserRequest(1L, "Vasya");
        Mockito.when(userRepository.update(1L, userRequest)).thenReturn(userResponse);
        UserService userService = new UserService(userRepository);
        UserResponse createdUserResponse = userService.userUpdate(1L, userRequest);
        Assertions.assertEquals(userResponse, createdUserResponse);
        Mockito.verify(userRepository).update(1L, userRequest);
    }

    @Test
    public void shouldReturnUser_WhenGetUserByIdCorrect() {
        UserResponse userResponse = buildUserRequest(1L, "Vasya");
        Mockito.when(userRepository.getById(1L)).thenReturn(userResponse);
        UserService userService = new UserService(userRepository);
        UserResponse createdUserResponse = userService.getUserById(1L);
        Assertions.assertEquals(userResponse, createdUserResponse);
        Mockito.verify(userRepository).getById(1L);
    }


    @Test
    public void shouldDeleteUserCorrect() {
        UserResponse userResponse = buildUserRequest(1L, "Vasya");
        Mockito.when(userRepository.delete(1L)).thenReturn(userResponse);
        UserService userService = new UserService(userRepository);
        UserResponse deletedUserResponse = userService.delete(1L);
        Assertions.assertEquals(userResponse, deletedUserResponse);
        Mockito.verify(userRepository).delete(1L);
    }

    @Test
    public void shouldReturnError_WhenCreate_And_UserNameEmpty() {
        UserRequest userRequest = buildUserRequest("", "VasyaPass");
        UserService userService = new UserService(userRepository);
        final BadRequestException createUserException = Assertions.assertThrows(BadRequestException.class,
                () -> userService.userCreate(userRequest)
        );
        Assertions.assertEquals(createUserException.getMessage(), "Введите имя пользователя");
    }

    @Test
    public void shouldReturnError_WhenUpdate_And_Empty() {
        UserRequest userRequest = buildUserRequest("", "VasyaPass");
        UserService userService = new UserService(userRepository);
        final BadRequestException createUserException = Assertions.assertThrows(BadRequestException.class,
                () -> userService.userCreate(userRequest)
        );
        Assertions.assertEquals(createUserException.getMessage(), "Введите имя пользователя");
    }

    private UserRequest buildUserRequest(String username, String password) {
        return UserRequest.builder()
                .userName(username)
                .password(password)
                .build();
    }
    private UserResponse buildUserRequest(Long id, String username) {
        return UserResponse.builder()
                .id(id)
                .userName(username)
                .build();
    }
}
