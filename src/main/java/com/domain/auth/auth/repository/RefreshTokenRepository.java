package com.domain.auth.auth.repository;

import com.domain.auth.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    @Query("select e from RefreshTokenEntity e where e.token = :token")
    Optional<RefreshTokenEntity> findByToken(UUID token);

    @Query("select e from RefreshTokenEntity e where e.user.id = :userId")
    List<RefreshTokenEntity> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("delete from RefreshTokenEntity e where e.expiresAt < current_timestamp or e.revoked = true")
    void deleteExpiredTokens();
}