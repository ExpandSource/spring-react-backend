package com.busanit.springreactbackend.controller;

import com.busanit.springreactbackend.dto.MemberDto;
import com.busanit.springreactbackend.entity.Member;
import com.busanit.springreactbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/member")
    public ResponseEntity register(@RequestBody MemberDto memberDto) {
        System.out.println(memberDto);
        Long id = memberService.register(memberDto, passwordEncoder);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/member/myPage")
    public ResponseEntity myPage(Principal principal) {
        String email = principal.getName();
        MemberDto memberDto = memberService.myPage(email);
        return ResponseEntity.status(HttpStatus.OK).body(memberDto);
    }
}
