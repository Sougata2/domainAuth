package com.domain.auth.auth.entity;

import com.domain.auth.user.entity.UserEntity;
import com.domain.mapper.references.MasterEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshTokenEntity implements MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 100, unique = true)
    private UUID token;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked;

    @Column
    private String device;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
