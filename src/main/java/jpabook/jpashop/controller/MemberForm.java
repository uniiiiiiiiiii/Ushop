package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;

    @NotEmpty(message = "아이디는 필수 입니다")
    private String memId;

    @NotEmpty(message = "비밀번호는 필수 입니다")
    private String memPwd;


}
