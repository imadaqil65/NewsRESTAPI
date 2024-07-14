package nl.fontys.s3.individual.news.business.converters;

import nl.fontys.s3.individual.news.persistence.entities.UserEntity;
import nl.fontys.s3.individual.news.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {
    private UserConverter(){}
    public static User convert(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .usertype(entity.getUsertype())
                .bio(entity.getBio())
                .build();
    }

    public static List<User> convert(List<UserEntity> entities) {
        List<User> users = new ArrayList<>();

        entities.forEach(u-> users.add(convert(u)));

        return users;
    }
}
