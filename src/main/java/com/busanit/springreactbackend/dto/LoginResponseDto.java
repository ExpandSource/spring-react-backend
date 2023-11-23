package com.busanit.springreactbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    String token;
    String authority;
}
