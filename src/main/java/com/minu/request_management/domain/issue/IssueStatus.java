package com.minu.request_management.domain.issue;
/*
*  요청 진행 상태
* 진행상태 - REQUESTED(요청) → ASSIGNED(담당자 배정) → IN_PROGRESS(진행중) → COMPLETED(완료)
*
* */
public enum IssueStatus {
    REQUESTED,
    ASSIGNED,
    IN_PROGRESS,
    COMPLETED
}
