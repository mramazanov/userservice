package ru.javajabka.userservice.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.javajabka.userservice.model.UserResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapper implements RowMapper<UserResponse> {
    @Override
    public UserResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserResponse.builder()
                .id(rs.getLong("id"))
                .userName(rs.getString("username"))
                .build();
    }
}