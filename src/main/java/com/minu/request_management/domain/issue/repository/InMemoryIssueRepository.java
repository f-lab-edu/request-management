package com.minu.request_management.domain.issue.repository;

import com.minu.request_management.domain.issue.Issue;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
/*
*  - DB 없이 Issue를 저장/조회하기 위한 임시 저장소 구현체
* */
public class InMemoryIssueRepository implements IssueRepository {
    /**
     * Map<String, Issue>
     * - key   : issueId
     * - value : Issue 객체
     * ConcurrentHashMap (java.util.concurrent.ConcurrentHashMap)
     * - 멀티스레드 환경에서도 안전하게 접근 가능한 Map 구현체
     * - 동시에 여러 요청이 들어와도 데이터 손상이 발생하지 않음
     * 일반 HashMap과의 차이:
     * - HashMap: 멀티스레드 환경에서 동시 수정 시 문제 발생 가능
     * - ConcurrentHashMap: 내부적으로 락을 분리하여 동시성 보장
     */
    private final Map<String, Issue> store = new ConcurrentHashMap<>();

    @Override
    public Issue save(Issue issue) {
        if (issue.getIssueId() == null || issue.getIssueId().isBlank()) {
            throw new IllegalStateException("저장하려면 issueId가 먼저 할당되어야 합니다.");
        }
        /*
         * Map.put(key, value)
         * (java.util.Map 인터페이스 메서드)
         *
         * - key가 없으면 → 신규 저장
         * - key가 이미 있으면 → 기존 값 덮어쓰기(UPDATE)
         */
        store.put(issue.getIssueId(), issue);
        return issue;
    }
    /**
     * Issue 단건 조회
     * @param issueId 조회할 이슈 ID
     * @return Optional<Issue>
     * Optional (java.util.Optional)
     * - null 반환 대신 "있을 수도 / 없을 수도 있음"을 명시적으로 표현
     * - 호출 측에서 반드시 존재 여부를 의식하게 만든다.
     */
    @Override
    public Optional<Issue> findById(String issueId) {
        // Optional.ofNullable()
        // - 값이 null이면 Optional.empty()
        // - null이 아니면 Optional.of(value)
        return Optional.ofNullable(store.get(issueId));
    }

    @Override
    public List<Issue> findAll() {
        /*
         * new ArrayList<>(Collection)
         * - 내부 컬렉션(store.values())를 그대로 노출하지 않기 위함
         * - 외부에서 수정해도 저장소 데이터에는 영향 없음
         */
        return new ArrayList<>(store.values());
    }
}