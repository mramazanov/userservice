package ru.javajabka.userservice.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.javajabka.userservice.model.UserResponseDTO;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapper implements RowMapper<UserResponseDTO> {

    @Override
    public UserResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserResponseDTO.builder()
                .id(rs.getLong("id"))
                .userName(rs.getString("username"))
                .build();
    }
}