package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.dto.MemberFormDto;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/new")
    public String memberForm(Model model) {
        MemberFormDto memberFormDto = new MemberFormDto();
        model.addAttribute("memberFormDto", memberFormDto);
        return "members/createMemberForm";
    }

    @PostMapping("/new")
    public String signup(@Valid MemberFormDto form, BindingResult result, Model model) {
//        @Valid:  검증하려는 객체(memberFormDto) 앞에 붙인다.
//        검증 완료 되면은 결과를 bindingResult 에 담아준다.
//        bindingResult:  검증 오류가 발생 -> 오류 내용 보관

//        오류가 발생하면 다시 회원가입폼으로 돌아간다.
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        try{
//            Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
            Member member = Member.createMember(form, passwordEncoder); // member: entity, createMember 에서 dto-> entity
            memberService.saveMember(member); // saveMember: 중복가입막는거 처리해주고 save

            // 예외발생-> 뷰 페이지에 errorMessage 출력 후 다시 회원가입폼으로
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "members/createMemberForm";
        }
        return "redirect:/";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginMember(){
        return "members/memberLoginForm";
    }

    // 로그인 페이지_오류났을 때
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "members/memberLoginForm";
    }


}
