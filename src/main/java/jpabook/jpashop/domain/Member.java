package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.constant.Role;
import jpabook.jpashop.dto.MemberFormDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String memId;
    private String memPwd;
    private String memPhoneNum;
    private String name;
    private String memLoginPlatform; //추후 sns 로그인시 필요 -> 기본 회원가입은 'NORMAL'

    //    private String memRole; //폰번호 인증 -> 스토어 오픈 가능(판매 가능) //BUYER, SELLER, ADMIN
    @Enumerated(EnumType.STRING)
    private Role role;

    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){

        Member member = Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .address(memberFormDto.getAddress())
                .password( passwordEncoder.encode( memberFormDto.getPassword() ) ) // BCryptPasswordEncoder Bean 을 파라미터로 넘겨서 비번을 암호화함
//                .role(Role.USER)
                .role(Role.ADMIN)
                .build();
        return member;
    }

}
