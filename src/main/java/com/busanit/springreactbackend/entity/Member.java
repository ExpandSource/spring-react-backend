package com.busanit.springreactbackend.entity;

import com.busanit.springreactbackend.constant.Role;
import com.busanit.springreactbackend.dto.MemberDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private Role role;

    public Member(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        this.id = null;
        this.email = memberDto.getEmail();
        // 암호화된 패스워드
        this.password = passwordEncoder.encode(memberDto.getPassword());
        this.name = memberDto.getName();
        this.role = Role.USER;
    }
}
