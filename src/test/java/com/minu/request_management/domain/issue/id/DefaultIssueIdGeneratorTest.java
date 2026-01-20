package com.minu.request_management.domain.issue.id;


import com.minu.request_management.common.time.FixedTimeProvider;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DefaultIssueIdGeneratorTest {

    @Test
    void generate_creates_incrementing_ids() {
        FixedTimeProvider timeProvider =
                new FixedTimeProvider(LocalDateTime.of(2026, 1, 1, 10, 0));

        DefaultIssueIdGenerator generator = new DefaultIssueIdGenerator(timeProvider);

        String id1 = generator.generate();
        String id2 = generator.generate();

        assertEquals("ISSUE-20260101-000001", id1);
        assertEquals("ISSUE-20260101-000002", id2);
    }
}
