package com.minu.request_management.domain.issue.id;

public class FixedIssueIdGenerator implements IssueIdGenerator {

    private final String fixedId;

    public FixedIssueIdGenerator(String fixedId) {
        this.fixedId = fixedId;
    }

    @Override
    public String generate() {
        return fixedId;
    }
}
