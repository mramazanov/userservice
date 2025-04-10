package ru.javajabka.userservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessOperation {

    private final Boolean success;
}