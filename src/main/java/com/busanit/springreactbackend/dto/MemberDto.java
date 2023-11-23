package com.busanit.springreactbackend.dto;

import com.busanit.springreactbackend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String email;

    private String password;

    private String name;

    public static MemberDto of(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.email = member.getEmail();
        memberDto.password = member.getPassword();
        memberDto.name = member.getName();
        return memberDto;
    }
}
