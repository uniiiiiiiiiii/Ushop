package jpabook.jpashop.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MemberFormDto {


    @NotEmpty(message = "아이디를 입력해주세요")
    @Length(min=8, max=16, message = "아이디는 8~16자리까지 가능합니다.")
    private String memId;

    @NotEmpty(message = "비번을 입력해주세요")
    @Length(min=8, max=16, message = "비밀번호는 8~16자리까지 가능합니다.")
    private String memPwd;

    @NotBlank(message = "이름을 입력해주세요.") // null, .length=0, ""
    private String name;

    @NotEmpty(message = "이메일을 입력해주세요") // null, .length=0
    @Email // 이메일 형식
    private String email;

    @NotEmpty(message = "주소를 입력해주세요.")
    private String address;
}
