package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long saveMember(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

//    회원 중복확인
    private void validateDuplicateMember(Member member) {
        Member findMembers = memberRepository.findByName(member.getName());
        if (findMembers != null) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }


//    public Member findOne(Long memberId) {
//        return memberRepository.findOne(memberId);
//    }


    // UserDetailsService 상속받아서 로그인/로그아웃 구현
    @Override
    public UserDetails loadUserByUsername(String memid) throws UsernameNotFoundException { // user 의 이메일을 전달받는다.(중복ㄴㄴ인애)
        Member member = memberRepository.findById(memid);

        if (member == null){
            throw new UsernameNotFoundException(memid);
        }

        return User.builder()
                .username(member.getMemId())
                .password(member.getMemPwd())
                .roles(member.getRole().toString()) // enum 이니까 toString 해준다.
                .build();
    }
}
