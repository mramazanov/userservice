package ru.javajabka.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javajabka.userservice.model.SuccessOperation;
import ru.javajabka.userservice.model.User;
import ru.javajabka.userservice.model.UserResponseDTO;
import ru.javajabka.userservice.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Пользователь")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Создать пользователя")
    public UserResponseDTO create(@RequestBody final User userRequest) {
        return userService.userCreate(userRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя")
    public UserResponseDTO get(@PathVariable final Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    @Operation(summary = "Получить пользователей")
    public List<UserResponseDTO> getUsers(@RequestParam final List<Long> ids) {
        return userService.getUsers(ids);
    }

    @DeleteMapping
    @Operation(summary = "Удалить пользователя")
    public SuccessOperation delete(@RequestParam final Long id) {
        return userService.delete(id);
    }
}