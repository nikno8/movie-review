package com.nikno8.movies.dtos;

import com.nikno8.movies.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private ObjectId id;
    private String firstName;
    private String lastName;
    private String login;
    private String token;
    private Role role;
}
