package ru.javajabka.userservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.javajabka.userservice.exception.BadRequestException;
import ru.javajabka.userservice.model.Role;
import ru.javajabka.userservice.model.SuccessOperation;
import ru.javajabka.userservice.model.User;
import ru.javajabka.userservice.model.UserResponseDTO;
import ru.javajabka.userservice.repository.UserRepository;
import ru.javajabka.userservice.service.TaskService;
import ru.javajabka.userservice.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldCreateUserCorrect() {
        User userRequest = buildUserRequest("Vasya", "VasyaPass", Role.USER);
        UserResponseDTO userResponse = buildUserResponse(1L, userRequest.getUserName(), userRequest.getRole());
        User userWithHashedPass = buildUserRequest(userRequest.getUserName(), "aa357dc8768d5f4db190b5a41ac2b79fa6909fe01e2de3d13a765f9a34f82d21", userRequest.getRole());
        Mockito.when(userRepository.insert(userWithHashedPass)).thenReturn(userResponse);
        UserResponseDTO createdUserResponse = userService.userCreate(userRequest);
        Assertions.assertEquals(userResponse, createdUserResponse);
        Mockito.verify(userRepository).insert(userWithHashedPass);
    }

    @Test
    public void shouldReturnUser_WhenGetUserByIdCorrect() {
        UserResponseDTO userResponse = buildUserResponse(1L, "Vasya", Role.USER);
        Mockito.when(userRepository.getById(1L)).thenReturn(userResponse);
        UserResponseDTO createdUserResponse = userService.getUserById(1L);
        Assertions.assertEquals(userResponse, createdUserResponse);
        Mockito.verify(userRepository).getById(1L);
    }


    @Test
    public void shouldDeleteUserCorrect() {
        UserResponseDTO userResponse = buildUserResponse(1L, "Vasya", Role.USER);
        SuccessOperation successOperation = userService.delete(1L);
        Assertions.assertEquals(true, successOperation.getSuccess());
        Mockito.verify(userRepository).delete(1L);
    }

    @Test
    public void shouldReturnError_WhenCreate_And_UserNameEmpty() {
        User userRequest = buildUserRequest("", "VasyaPass", Role.USER);
        final BadRequestException createUserException = Assertions.assertThrows(BadRequestException.class,
                () -> userService.userCreate(userRequest)
        );
        Assertions.assertEquals(createUserException.getMessage(), "Введите имя пользователя");
    }

    @Test
    public void shouldReturnError_WhenUpdate_And_NameEmpty() {
        User userRequest = buildUserRequest("", "VasyaPass", Role.USER);
        final BadRequestException createUserException = Assertions.assertThrows(BadRequestException.class,
                () -> userService.userCreate(userRequest)
        );
        Assertions.assertEquals(createUserException.getMessage(), "Введите имя пользователя");
    }

    private User buildUserRequest(String username, String password, Role role) {
        return User.builder()
                .userName(username)
                .password(password)
                .role(role)
                .build();
    }
    private UserResponseDTO buildUserResponse(Long id, String username, Role role) {
        return UserResponseDTO.builder()
                .id(id)
                .userName(username)
                .role(role)
                .build();
    }
}
