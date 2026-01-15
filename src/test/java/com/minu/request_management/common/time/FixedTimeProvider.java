package com.minu.request_management.common.time;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FixedTimeProvider implements TimeProvider {

    //private final LocalDateTime fixedDateTime;
    //초기 내가 원하는 특정 날짜를 고정하면 변하지 않는다는 개념으로 final로 선언했으나
    //in-progress, complete 등 상황이 진행되면서 첫 고정날짜에서 시간이 흐른 것을 표현, 테스트의 필요로 수정
    private LocalDateTime fixedDateTime;

    /* 참고
    public static LocalDateTime of(
    int year,
    int month,
    int dayOfMonth,
    int hour,
    int minute
    )
    테스트에 해당 메소드로 고정 시간 담아 사용가능
     */

    //LocalDate 타입은 LocalDateTime 가공하여 사용가능해서 파라미터는 LocalDateTime만
    public FixedTimeProvider(LocalDateTime fixedTime) {
        this.fixedDateTime = fixedTime;
    }

    // 날짜 단위로 시간 이동
    public void plusDays(long days) {
        this.fixedDateTime = this.fixedDateTime.plusDays(days);
    }

    // 시간 단위로 이동 (필요 시)
    public void plusHours(long hours) {
        this.fixedDateTime = this.fixedDateTime.plusHours(hours);
    }

    @Override
    public LocalDateTime now() {
        return fixedDateTime;
    }

    @Override
    public LocalDate today() {
        return fixedDateTime.toLocalDate();
    }
}
