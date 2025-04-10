package ru.javajabka.userservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;
import ru.javajabka.userservice.exception.BadRequestException;
import ru.javajabka.userservice.model.SuccessOperation;
import ru.javajabka.userservice.model.User;
import ru.javajabka.userservice.model.UserResponseDTO;
import ru.javajabka.userservice.repository.mapper.UserMapper;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final String INSERT = """
            INSERT INTO user_service.person (username, password, deleted, created_at)
            VALUES (:userName, :password, false, now())
            RETURNING *;
            """;

    private static final String GET_BY_ID = """
            SELECT * FROM user_service.person
            WHERE deleted = false AND id = :id;
            """;

    private static final String GET_USERS = """
            SELECT * FROM user_service.person WHERE deleted = false AND id IN (:ids)
            """;

    private static final String DELETE = """
            UPDATE user_service.person SET deleted = true WHERE deleted = false AND id = :id
            RETURNING *;
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    public UserResponseDTO insert(final User user) {
        try {
            return jdbcTemplate.queryForObject(INSERT, userToSql(user), userMapper);
        } catch (DuplicateKeyException exc) {
            throw new BadRequestException(String.format("Пользователь с именем %s уже существует", user.getUserName()));
        }
    }

    public UserResponseDTO getById(final Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new MapSqlParameterSource("id", id), userMapper);
        } catch (EmptyResultDataAccessException exc) {
            throw new BadRequestException(String.format("Пользователь с id %d не найден", id));
        }
    }

    public List<UserResponseDTO> getUsers(final List<Long> ids) {
        return jdbcTemplate.query(GET_USERS, new MapSqlParameterSource("ids", ids), userMapper);
    }

    public UserResponseDTO delete(final Long id) {
        try {
            return jdbcTemplate.queryForObject(DELETE, new MapSqlParameterSource("id" , id), userMapper);
        } catch (EmptyResultDataAccessException exc) {
            throw new BadRequestException((String.format("Пользователь с id %d не найден", id)));
        }
    }

    private MapSqlParameterSource userToSql(final User userRequest) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userName", userRequest.getUserName().toLowerCase());
        params.addValue("password", userRequest.getPassword());
        return params;
    }
}