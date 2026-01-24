package com.minu.request_management.domain.issue;

import com.minu.request_management.domain.user.User;
import com.minu.request_management.domain.user.UserRole;
import org.springframework.util.Assert;
import com.minu.request_management.common.time.TimeProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Issue {
// 필드
    private String issueId;                 // 이슈 ID
    private String requesterId;            // 요청자 ID
    private String assigneeId;             // 담당자 ID

    private String title;                  // 요청 제목
    private String content;                // 요청 내용

    private IssueStatus status;             // 이슈 상태

    private String moduleCode;              // 모듈 코드

    private LocalDate desiredDueDate;       // 요청 희망 완료일자
    private LocalDate expectedDueDate;      // 예상 완료일자
    private LocalDate completedDate;        // 실제 완료일자

    private LocalDateTime insertDate;          // 요청 등록일자
    private LocalDateTime updateDate;          // 최종 수정일자

    private final TimeProvider timeProvider;

    // 생성자
    // 요청 시점에 issue에 필요한 필드
    public Issue(User requester, String moduleCode, String title, String content, LocalDate desiredDueDate,TimeProvider timeProvider) {
        //초기 입력시 필수체크 추가
        Assert.notNull(requester, "requester는 필수입니다.");
        validateNotBlank(moduleCode, "moduleCode");
        validateNotBlank(title, "title");
        validateNotBlank(content, "content");

        this.requesterId = requester.getUserId();
        this.moduleCode = moduleCode;
        this.title = title;
        this.content = content;
        this.desiredDueDate = desiredDueDate;

        this.status = IssueStatus.REQUESTED;
        this.insertDate = timeProvider.now();
        this.timeProvider = timeProvider;
    }

    //issueId 할당 (최초 1회)
    public void assignId(String issueId) {
        validateNotBlank(issueId, "issueId");

        if (this.issueId != null) {
            throw new IllegalStateException("issueId는 이미 할당되어 변경할 수 없습니다.");
        }

        this.issueId = issueId;
    }

    // 행위 로직
    public void assignTo(User staff, LocalDate expectedDueDate) {
        assertStaffOrAdmin(staff);
        if (this.status != IssueStatus.REQUESTED) {
            throw new IllegalStateException("요청 상태에서만 담당자 배정이 가능합니다.");
        }
        this.assigneeId = staff.getUserId();
        validateNotBlank(assigneeId, "assigneeId");
        this.expectedDueDate = expectedDueDate;
        this.status = IssueStatus.ASSIGNED;

        //this.updateDate = LocalDateTime.now();
        setUpdateDate();
    }

    public void inProgress(User staff) {
        // 관리자라면 무조건 가능
        if (staff.getRole() == UserRole.ADMIN) {
            // 통과
        }
        // STAFF 인 경우 → 본인 담당 이슈만 가능
        else if (staff.getRole() == UserRole.STAFF) {
            if (!staff.getUserId().equals(this.assigneeId)) {
                throw new IllegalStateException("담당자가 아닌 직원은 처리할 수 없습니다.");
            }
        }
        // 그 외 (REQUESTER 등)
        else {
            throw new IllegalStateException("처리 권한이 없습니다.");
        }

        if (this.status != IssueStatus.ASSIGNED) {
            throw new IllegalStateException("담당자가 배정된 이슈만 처리 시작할 수 있습니다.");
        }
        this.status = IssueStatus.IN_PROGRESS;

        //this.updateDate = LocalDateTime.now();
        setUpdateDate();
    }

    public void complete(User staff) {
        // 관리자라면 무조건 가능
        if (staff.getRole() == UserRole.ADMIN) {
            // 통과
        }
        // STAFF 인 경우 → 본인 담당 이슈만 가능
        else if (staff.getRole() == UserRole.STAFF) {
            if (!staff.getUserId().equals(this.assigneeId)) {
                throw new IllegalStateException("담당자가 아닌 직원은 처리할 수 없습니다.");
            }
        }
        // 그 외 (REQUESTER 등)
        else {
            throw new IllegalStateException("처리 권한이 없습니다.");
        }

        if (this.status != IssueStatus.IN_PROGRESS) {
            throw new IllegalStateException("처리 중인 이슈만 완료할 수 있습니다.");
        }
        this.status = IssueStatus.COMPLETED;
        this.completedDate = timeProvider.today(); // 완료 시점의 날짜

        //this.updateDate = LocalDateTime.now();
        setUpdateDate();
    }

    public boolean isOverdue() {
        if (this.status == IssueStatus.COMPLETED) return false;
        if (this.expectedDueDate == null) return false; // 배정 전이면 판단 불가
        return timeProvider.today().isAfter(this.expectedDueDate);
    }

    public boolean isLateCompleted() {
        // 완료된 건만 "지연 완료" 판단 가능
        if (this.status != IssueStatus.COMPLETED) {
            return false;
        }

        // 예상 완료일/완료일이 없으면 비교 불가
        if (this.expectedDueDate == null || this.completedDate == null) {
            return false;
        }

        // LocalDate#isAfter(다른 날짜): "이 날짜가 다른 날짜보다 이후인지" boolean 반환
        // 예) 2026-01-10.isAfter(2026-01-08) == true
        return this.completedDate.isAfter(this.expectedDueDate);
    }

    public void reassignTo(User admin, User newAssignee, LocalDate expectedDueDate) {
        assertAdmin(admin); // 재배정은 관리자만

        Assert.notNull(newAssignee, "newAssignee는 필수입니다.");

        if (this.status == IssueStatus.REQUESTED) {
            throw new IllegalStateException("배정 전 이슈는 재배정할 수 없습니다. 먼저 배정하세요.");
        }

        if (this.status == IssueStatus.COMPLETED) {
            throw new IllegalStateException("완료된 이슈는 재배정할 수 없습니다.");
        }

        // 담당자 변경
        this.assigneeId = newAssignee.getUserId();
        this.expectedDueDate = expectedDueDate;

        setUpdateDate();
    }

    //staff 또는 admin
    private static void assertStaffOrAdmin(User staff) {
        Assert.notNull(staff, "staff 필수입니다.");
        if (!staff.isStaffOrAdmin()) {
            throw new IllegalStateException("권한이 없습니다. (담당자 또는 관리자만 가능합니다.)");
        }
    }

    //admin 인지 아닌지
    private static void assertAdmin(User admin) {
        Assert.notNull(admin, "admin 필수입니다.");
        if (admin.getRole() != UserRole.ADMIN) {
            throw new IllegalStateException("권한이 없습니다. (관리자만 가능합니다.)");
        }
    }

    //모든 이슈를 수정하는 행위에 들어가는 updateDate 메소드로 추출
    private void setUpdateDate() {
        this.updateDate = timeProvider.now();
    }

    //요청 초기 생성 시 필수체크 메소드
    private static void validateNotBlank(String value, String fieldName) {
        Assert.hasText(value, fieldName + "는 필수입니다.");  //null과 문자포함 검사
    }

    // getter

    public String getIssueId() {
        return issueId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public LocalDate getDesiredDueDate() {
        return desiredDueDate;
    }

    public LocalDate getExpectedDueDate() {
        return expectedDueDate;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}
