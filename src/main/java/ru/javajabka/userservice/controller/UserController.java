package ru.javajabka.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javajabka.userservice.model.UserRequest;
import ru.javajabka.userservice.model.UserResponse;
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
    public UserResponse create(@RequestBody final UserRequest userRequest) {
        return userService.userCreate(userRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя")
    public UserResponse get(@PathVariable final Long id) {
        return userService.getUserById(id);
    }

    @PatchMapping
    @Operation(summary = "Обновить пользователя")
    public UserResponse update(@RequestParam final Long id, @RequestBody final UserRequest userRequest) {
        return userService.userUpdate(id, userRequest);
    }

    @GetMapping
    @Operation(summary = "Получить пользователей")
    public List<UserResponse> getUsers(@RequestParam final List<Long> ids) {
        return userService.getUsers(ids);
    }

    @DeleteMapping
    @Operation(summary = "Удалить пользователя")
    public UserResponse update(@RequestParam final Long id) {
        return userService.delete(id);
    }
}