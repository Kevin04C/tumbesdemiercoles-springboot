package com.tumbesdemiercoles.api.user.presentation.dto.response;

import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthCreateTokenResponse {
    private UserResponseDto user;
    private String token;
}
