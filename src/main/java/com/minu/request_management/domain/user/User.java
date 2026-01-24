package com.minu.request_management.domain.user;

import org.springframework.util.Assert;
import com.minu.request_management.common.time.TimeProvider;
import java.time.LocalDateTime;

public class User {

    // 필드
    private String userId;          // 사용자 ID (사번 / 로그인ID)
    private String userName;        // 사용자명
    private String email;           // 이메일

    private String companyName;     // 회사명
    private String deptName;        // 부서명
    private String positionName;    // 직급명

    private UserRole role;          // 요청자 / IT담당자 / 관리자

    private String phoneNo;         // 연락처

    private LocalDateTime insertDate;   // 생성일시
    private LocalDateTime updateDate;   // 수정일시

    private final TimeProvider timeProvider;

    // 생성자
    public User(String userId,
                String userName,
                String email,
                String companyName,
                String deptName,
                String positionName,
                UserRole role,
                String phoneNo,
                TimeProvider timeProvider) {

        // 요청 초기 생성 시 필수체크
        validateNotBlank(userId, "userId");
        validateNotBlank(userName, "userName");
        Assert.notNull(role, "role은 필수입니다.");

        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.companyName = companyName;
        this.deptName = deptName;
        this.positionName = positionName;
        this.role = role;
        this.phoneNo = phoneNo;
        this.insertDate = timeProvider.now();
        this.timeProvider = timeProvider;
    }

    //행위 로직

    //권한 판단 용
    public boolean isRequester() {
        return this.role == UserRole.REQUESTER;
    }

    public boolean isStaffOrAdmin() {
        return this.role == UserRole.STAFF || this.role == UserRole.ADMIN;
    }

    //모든 이슈를 수정하는 행위에 들어가는 updateDate 메소드로 추출
    private void setUpdateDate() {
        this.updateDate = timeProvider.now();
    }

    // 요청 초기 생성 시 필수체크 메소드
    private static void validateNotBlank(String value, String fieldName) {
        Assert.hasText(value, fieldName + "는 필수입니다.");
    }

    // Getter
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getPositionName() {
        return positionName;
    }

    public UserRole getRole() {
        return role;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}