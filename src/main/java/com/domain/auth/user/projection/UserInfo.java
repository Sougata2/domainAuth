package com.domain.auth.user.projection;

/**
 * Projection for {@link com.domain.auth.user.entity.UserEntity}
 */
public interface UserInfo {
    Long getId();

    String getFirstName();

    String getLastName();

    String getEmail();
}