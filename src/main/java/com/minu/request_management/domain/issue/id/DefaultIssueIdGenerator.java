package com.minu.request_management.domain.issue.id;

import com.minu.request_management.common.time.TimeProvider;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
/*
- ISSUE-{yyyyMMdd}-{sequence(6자리)}
 * 예) ISSUE-20260101-000001
 */
@Component
public class DefaultIssueIdGenerator implements IssueIdGenerator {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    private final TimeProvider timeProvider;
    /**
     * AtomicInteger (java.util.concurrent.atomic.AtomicInteger)
     * - 멀티스레드 환경에서도 안전하게 증가(increment)할 수 있는 정수 카운터.
     * - 여러 요청이 동시에 ID를 생성해도 중복된 sequence가 나오지 않도록 한다.
     *
     * sequence.incrementAndGet() (AtomicInteger 인스턴스 메서드)
     * - 값을 1 증가시키고, 증가된 값을 반환한다.
     */
    private final AtomicInteger sequence = new AtomicInteger(0);

    public DefaultIssueIdGenerator(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public String generate() {
        /*String date = timeProvider.today().format(FORMATTER);
        int seq = sequence.incrementAndGet();
        return "ISSUE-" + date + "-" + String.format("%06d", seq);*/
        return String.format(
                "ISSUE-%s-%06d",
                timeProvider.today().format(FORMATTER),
                sequence.incrementAndGet()
        );
    }
}
