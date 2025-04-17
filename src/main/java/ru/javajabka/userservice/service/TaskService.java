package ru.javajabka.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.javajabka.userservice.exception.BadRequestException;
import ru.javajabka.userservice.model.Task;
import ru.javajabka.userservice.model.TaskStatus;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final RestTemplate restTemplate;

    @Value("${url.service.task}")
    private String taskServiceUrl;

    public void checkUserTasks(final Long userId) {
        String url = UriComponentsBuilder
                .fromUriString(taskServiceUrl + "/api/v1/task")
                .queryParam("assignee", userId)
                .encode()
                .build()
                .toString();

        ResponseEntity<List<Task>> responseEntity =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );

        responseEntity.getBody().stream()
                .filter(task -> task.getStatus().equals(TaskStatus.IN_PROGRESS) || task.getStatus().equals(TaskStatus.TO_DO))
                .filter(task -> task.getAssignee().equals(userId))
                .findFirst()
                .ifPresent(task -> {
                    throw new BadRequestException(String.format("Для пользователя с id %d есть назначенные задачи", userId));
                });
    }
}