package jpabook.jpashop.domain.constant;

public enum Role {
    ROLE_BUYER("일반사용자"), SELLER("판매자"), ADMIN("관리자");
    private String description;

    Role(String description) {
        this.description = description;
    }

}
