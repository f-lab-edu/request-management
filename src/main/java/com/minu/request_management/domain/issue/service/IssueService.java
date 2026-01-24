package com.minu.request_management.domain.issue.service;


import com.minu.request_management.common.time.TimeProvider;
import com.minu.request_management.domain.issue.Issue;
import com.minu.request_management.domain.issue.id.IssueIdGenerator;
import com.minu.request_management.domain.issue.repository.IssueRepository;
import com.minu.request_management.domain.user.User;

import java.time.LocalDate;

public class IssueService {

    private final IssueRepository issueRepository;
    private final IssueIdGenerator issueIdGenerator;
    private final TimeProvider timeProvider;

    public IssueService(IssueRepository issueRepository, IssueIdGenerator issueIdGenerator,TimeProvider timeProvider) {
        this.issueRepository = issueRepository;
        this.issueIdGenerator = issueIdGenerator;
        this.timeProvider = timeProvider;
    }

    /**
     * 이슈 등록
     */
    public Issue createIssue(User requester, String moduleCode, String title, String content, LocalDate desiredDueDate) {

        Issue issue = new Issue(requester, moduleCode, title, content, desiredDueDate, timeProvider);

        String issueId = issueIdGenerator.generate();
        issue.assignId(issueId);

        return issueRepository.save(issue);
    }
}
