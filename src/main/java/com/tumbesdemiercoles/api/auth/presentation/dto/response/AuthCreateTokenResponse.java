package com.tumbesdemiercoles.api.auth.presentation.dto.response;

import com.tumbesdemiercoles.api.auth.application.dto.AuthUserResponseDto;
import com.tumbesdemiercoles.api.user.application.dto.UserResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthCreateTokenResponse {
    private AuthUserResponse user;
    private String token;
}
