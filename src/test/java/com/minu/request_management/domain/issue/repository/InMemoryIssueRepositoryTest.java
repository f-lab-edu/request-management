package com.minu.request_management.domain.issue.repository;

import com.minu.request_management.common.time.FixedTimeProvider;
import com.minu.request_management.domain.issue.Issue;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryIssueRepositoryTest {

    @Test
    void save_and_findById() {
        FixedTimeProvider timeProvider =
                new FixedTimeProvider(LocalDateTime.of(2026, 1, 1, 10, 0));

        InMemoryIssueRepository repository = new InMemoryIssueRepository();

        Issue issue = new Issue("REQ1", "HR", "제목", "내용", LocalDate.of(2026, 1, 10), timeProvider);
        issue.assignId("ISSUE-20260101-000001");

        // InMemory 저장
        repository.save(issue);

        Issue found = repository.findById("ISSUE-20260101-000001")
                .orElseThrow(() -> new AssertionError("저장된 이슈가 조회되지 않습니다."));

        assertEquals("ISSUE-20260101-000001", found.getIssueId());
    }

    @Test
    void save_requires_issueId() {

        FixedTimeProvider timeProvider =
                new FixedTimeProvider(LocalDateTime.of(2026, 1, 1, 10, 0));

        InMemoryIssueRepository repository = new InMemoryIssueRepository();

        Issue issue = new Issue("REQ1", "HR", "제목", "내용", LocalDate.of(2026, 1, 10), timeProvider);
        // issue.assignId(...)

        assertThrows(IllegalStateException.class, () -> repository.save(issue));
    }
}
