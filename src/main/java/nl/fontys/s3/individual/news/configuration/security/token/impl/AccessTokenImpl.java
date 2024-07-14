package nl.fontys.s3.individual.news.configuration.security.token.impl;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.fontys.s3.individual.news.configuration.security.token.AccessToken;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode
@Getter
public class AccessTokenImpl implements AccessToken {
    private final String subject;
    private final Long userId;
    private final String role;

    public AccessTokenImpl(String subject, Long userId, String role) {
        this.subject = subject;
        this.userId = userId;
        this.role = role;
    }


    @Override
    public boolean hasRole(String roleName) {
        return false;
    }
}
