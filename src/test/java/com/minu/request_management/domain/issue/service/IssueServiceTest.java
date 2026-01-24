package com.minu.request_management.domain.issue.service;

import com.minu.request_management.common.time.FixedTimeProvider;
import com.minu.request_management.domain.issue.Issue;
import com.minu.request_management.domain.issue.IssueStatus;
import com.minu.request_management.domain.issue.id.DefaultIssueIdGenerator;
import com.minu.request_management.domain.issue.repository.InMemoryIssueRepository;
import com.minu.request_management.domain.user.User;
import com.minu.request_management.domain.user.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class IssueServiceTest {

    @Test
    void createIssue() {
        // given
        FixedTimeProvider timeProvider =
                new FixedTimeProvider(LocalDateTime.of(2026, 1, 1, 10, 0));

        InMemoryIssueRepository repository = new InMemoryIssueRepository();
        DefaultIssueIdGenerator idGenerator = new DefaultIssueIdGenerator(timeProvider);

        IssueService service = new IssueService(repository, idGenerator, timeProvider);

        // ✅ requester(User) 생성
        User requester = new User(
                "REQ1",
                "요청자",
                null,
                null,
                null,
                null,
                UserRole.REQUESTER,
                null,
                timeProvider
        );

        // when
        Issue issue = service.createIssue(
                requester,
                "HR",
                "제목",
                "내용",
                LocalDate.of(2026, 1, 10)
        );

        // then
        assertNotNull(issue.getIssueId());
        assertTrue(issue.getIssueId().startsWith("ISSUE-20260101-"));
        assertEquals(IssueStatus.REQUESTED, issue.getStatus());
    }

    @Test
    void createIssue_assignId_is_only_once() {
        FixedTimeProvider timeProvider =
                new FixedTimeProvider(LocalDateTime.of(2026, 1, 1, 10, 0));

        // ✅ requester(User) 생성
        User requester = new User(
                "REQ1",
                "요청자",
                null,
                null,
                null,
                null,
                UserRole.REQUESTER,
                null,
                timeProvider
        );

        Issue issue = new Issue(
                requester,
                "HR",
                "제목",
                "내용",
                LocalDate.of(2026, 1, 10),
                timeProvider
        );

        issue.assignId("ISSUE-20260101-000001");

        assertThrows(IllegalStateException.class,
                () -> issue.assignId("ISSUE-20260101-000002"));
    }
}
