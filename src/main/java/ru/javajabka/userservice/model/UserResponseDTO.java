package ru.javajabka.userservice.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResponseDTO {
    private final Long id;
    private final String userName;
    private final Role role;
}