package com.busanit.springreactbackend.service;

import com.busanit.springreactbackend.dto.MemberDto;
import com.busanit.springreactbackend.entity.Member;
import com.busanit.springreactbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    // 회원가입
    public Long register(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        Member member = new Member(memberDto, passwordEncoder);
        validateDuplicateMember(member);
        Member saved = memberRepository.save(member);
        return saved.getId();
    }

    // 중복회원 검증
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        // 가입을 위해 입력한 멤버가 존재한다면
        if (findMember != null) {
            // 예외 발생
            throw new IllegalStateException("가입된 회원입니다.");
        }
    }


    // 스프링 시큐리티와 연결
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);
        if (member == null) {
           throw new UsernameNotFoundException(email);
        }

        // Member 엔티티를 Spring Security UserDetails로 변환하여 로드
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    public MemberDto myPage(String email) {
        Member member = memberRepository.findByEmail(email);
        log.info(member.toString());

        if (member == null) {
            new RuntimeException("회원을 찾을 수 없습니다.");
        }

        MemberDto memberDto = MemberDto.of(member);

        return memberDto;
    }
}
