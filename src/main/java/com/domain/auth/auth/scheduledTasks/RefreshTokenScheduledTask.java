package com.domain.auth.auth.scheduledTasks;

import com.domain.auth.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenScheduledTask {
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanUpExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens();
    }
}
