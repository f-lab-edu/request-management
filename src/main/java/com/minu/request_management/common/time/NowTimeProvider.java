package com.minu.request_management.common.time;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class NowTimeProvider implements TimeProvider {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDate today() {
        return LocalDate.now();
    }
}
