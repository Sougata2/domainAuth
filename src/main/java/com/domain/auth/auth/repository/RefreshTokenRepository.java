package com.domain.auth.auth.repository;

import com.domain.auth.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    @Query("select e from RefreshTokenEntity e where e.token = :token")
    Optional<RefreshTokenEntity> findByToken(String token);

    @Query("select e from RefreshTokenEntity e where e.user.id = :userId")
    List<RefreshTokenEntity> findByUserId(Long userId);
}