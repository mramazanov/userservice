package ru.javajabka.userservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javajabka.userservice.model.UserRequest;
import ru.javajabka.userservice.model.UserResponse;
import ru.javajabka.userservice.util.HashUtil;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final String INSERT = """
            INSERT INTO person.person (username, password, deleted, created_at)
            VALUES (:userName, :password, false, now())
            RETURNING *;
            """;

    private static final String GET_BY_ID = """
            SELECT * FROM person.person
            WHERE deleted != true AND id = :id;
            """;

    private static final String GET_USERS = """
            SELECT * FROM person.person WHERE deleted != true AND id IN (:ids)
            """;

    private static final String UPDATE = """
            UPDATE person.person SET username=:userName, password=:password, updated_at = now()
            WHERE deleted != true AND id = :id
            RETURNING *;
            """;

    private static final String DELETE = """
            UPDATE person.person SET deleted = true WHERE deleted != true AND id = :id
            RETURNING *;
            """;


    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    public UserResponse insert(final UserRequest userRequest) {
        try {
            return jdbcTemplate.queryForObject(INSERT, userToSql(null, userRequest), userMapper);
        } catch (DuplicateKeyException exc) {
            throw new DuplicateKeyException(String.format("Пользователь с именем %s уже существует", userRequest.getUserName()));
        }
    }

    public UserResponse getById(final Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, userToSql(id, null), userMapper);
        } catch (EmptyResultDataAccessException exc) {
            throw new EmptyResultDataAccessException(String.format("Пользователь с id %d не найден", id), 1);
        }

    }

    public UserResponse update(final Long id, final UserRequest userRequest) {
        try {
            return jdbcTemplate.queryForObject(UPDATE, userToSql(id, userRequest), userMapper);
        } catch (DuplicateKeyException exc) {
            throw new DuplicateKeyException(String.format("Пользователь с именем %s уже существует", userRequest.getUserName()));
        } catch (EmptyResultDataAccessException exc) {
            throw new EmptyResultDataAccessException(String.format("Пользователь с id %d не найден", id), 1);
        }

    }

    public List<UserResponse> getUsers(final List<Long> ids) {
        return jdbcTemplate.query(GET_USERS, userToSql(ids), userMapper);
    }

    public UserResponse delete(final Long id) {
        try {
            return jdbcTemplate.queryForObject(DELETE, userToSql(id, null), userMapper);
        } catch (EmptyResultDataAccessException exc) {
            throw new EmptyResultDataAccessException(String.format("Пользователь с id %d не найден", id), 1);
        }
    }

    private MapSqlParameterSource userToSql(final Long id, final UserRequest userRequest) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        if (userRequest != null) {
            params.addValue("userName", userRequest.getUserName().toLowerCase());
            params.addValue("password", HashUtil.getHashedPassword(userRequest.getPassword()));
        }

        return params;
    }

    private MapSqlParameterSource userToSql(final List<Long> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
        return params;
    }

}