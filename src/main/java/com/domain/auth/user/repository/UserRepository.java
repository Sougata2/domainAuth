package com.domain.auth.user.repository;

import com.domain.auth.user.entity.UserEntity;
import com.domain.auth.user.projection.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Query("select e from UserEntity e where e.email = :email")
    Optional<UserInfo> getUserInfo(String email);
}
