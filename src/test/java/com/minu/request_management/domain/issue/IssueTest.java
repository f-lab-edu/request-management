package com.minu.request_management.domain.issue;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class IssueTest {
    @Test
    void assignTo() {
        /*
         - 이슈가 REQUESTED 상태일 때만
         - 담당자 배정이 가능해야 한다
         - 배정되면 상태는 ASSIGNED로 변경된다
        */
        Issue issue = new Issue("REQ1", "HR", "제목", "내용", LocalDate.now().plusDays(7));

        issue.assignTo("김민우", LocalDate.now().plusDays(3));

        assertEquals(IssueStatus.ASSIGNED, issue.getStatus());
        assertEquals("김민우", issue.getAssigneeId());
    }

    @Test
    void inProgress() {
        /*
         - ASSIGNED 상태에서만 처리 시작이 가능하다
         - REQUESTED 상태에서는 inProgress() 호출 시 예외가 발생해야 한다
        */
        Issue issue = new Issue("REQ1", "HR", "제목", "내용", LocalDate.now().plusDays(7));

        assertThrows(IllegalStateException.class, issue::inProgress);

        issue.assignTo("김민우", LocalDate.now().plusDays(3));
        issue.inProgress();

        assertEquals(IssueStatus.IN_PROGRESS, issue.getStatus());
    }

    @Test
    void complete() {
        /*
         - IN_PROGRESS 상태에서만 완료 처리가 가능하다
         - 완료 시 상태는 COMPLETED로 변경되고
         - 완료일자가 자동으로 세팅되어야 한다
        */
        Issue issue = new Issue("REQ1", "HR", "제목", "내용", LocalDate.now().plusDays(7));

        issue.assignTo("김민우", LocalDate.now().plusDays(3));
        issue.inProgress();
        issue.complete();

        assertEquals(IssueStatus.COMPLETED, issue.getStatus());
        assertNotNull(issue.getCompletedDate());
    }
}
