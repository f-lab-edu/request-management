package com.minu.request_management.domain.issue.repository;

import com.minu.request_management.domain.issue.Issue;
import java.util.List;
import java.util.Optional;

public interface IssueRepository {
    //이슈 저장(신규/수정)
    Issue save(Issue issue);

    // 단건 조회
    Optional<Issue> findById(String issueId);

    // 전체 조회
    List<Issue> findAll();
}
