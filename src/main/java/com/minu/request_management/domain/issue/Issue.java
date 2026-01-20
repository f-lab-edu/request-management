package com.minu.request_management.domain.issue;

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
    public Issue(String requesterId, String moduleCode, String title, String content, LocalDate desiredDueDate,TimeProvider timeProvider) {
        //초기 입력시 필수체크 추가
        validateNotBlank(requesterId, "requesterId");
        validateNotBlank(moduleCode, "moduleCode");
        validateNotBlank(title, "title");
        validateNotBlank(content, "content");

        this.requesterId = requesterId;
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
    public void assignTo(String assigneeId, LocalDate expectedDueDate) {
        if (this.status != IssueStatus.REQUESTED) {
            throw new IllegalStateException("요청 상태에서만 담당자 배정이 가능합니다.");
        }
        validateNotBlank(assigneeId, "assigneeId");

        this.assigneeId = assigneeId;
        this.expectedDueDate = expectedDueDate;
        this.status = IssueStatus.ASSIGNED;

        //this.updateDate = LocalDateTime.now();
        setUpdateDate();
    }

    public void inProgress() {
        if (this.status != IssueStatus.ASSIGNED) {
            throw new IllegalStateException("담당자가 배정된 이슈만 처리 시작할 수 있습니다.");
        }
        this.status = IssueStatus.IN_PROGRESS;

        //this.updateDate = LocalDateTime.now();
        setUpdateDate();
    }

    public void complete() {
        if (this.status != IssueStatus.IN_PROGRESS) {
            throw new IllegalStateException("처리 중인 이슈만 완료할 수 있습니다.");
        }
        this.status = IssueStatus.COMPLETED;
        this.completedDate = timeProvider.today(); // 완료 시점의 날짜

        //this.updateDate = LocalDateTime.now();
        setUpdateDate();
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
