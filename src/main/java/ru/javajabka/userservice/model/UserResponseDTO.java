package ru.javajabka.userservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private final Long id;
    private final String userName;
}