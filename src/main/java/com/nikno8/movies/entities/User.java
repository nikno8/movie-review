package com.nikno8.movies.entities;

import com.nikno8.movies.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "users")
public class User {
    @Id
    private ObjectId id;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private Role role;
    private List<String> watchList; // Список идентификаторов фильмов

    public String getStringId(){
        return id.toHexString();
    }
}
