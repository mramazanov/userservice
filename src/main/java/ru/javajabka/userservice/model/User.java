package ru.javajabka.userservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private final String userName;
    private final String password;
    private final Role role;
}