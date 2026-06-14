package com.tumbesdemiercoles.api.auth.application.usecase;

import com.tumbesdemiercoles.api.auth.application.dto.AuthResponseDto;
import com.tumbesdemiercoles.api.auth.application.dto.RegisterRequestDto;
import com.tumbesdemiercoles.api.auth.application.ports.In.RegisterUseCase;
import com.tumbesdemiercoles.api.auth.application.ports.out.PasswordEncoderPort;
import com.tumbesdemiercoles.api.auth.application.ports.out.TokenProviderPort;
import com.tumbesdemiercoles.api.auth.application.ports.out.UserIdentityPort;
import com.tumbesdemiercoles.api.auth.application.ports.out.UserRolAccessPort;
import com.tumbesdemiercoles.api.auth.application.ports.out.dto.UserIdentityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RegisterUseCaseImpl implements RegisterUseCase {

  private final UserIdentityPort userIdentityPort;
  private final PasswordEncoderPort passwordEncoderPort;
  private final TokenProviderPort tokenProviderPort;
  private final UserRolAccessPort userRolAccessPort;

  @Override
  public Mono<AuthResponseDto> register(RegisterRequestDto registerRequestDto) {

    return passwordEncoderPort.encode(registerRequestDto.getPassword())
        .flatMap(encodedPassword -> {

          UserIdentityDto userIdentityDto = UserIdentityDto.builder()
              .email(registerRequestDto.getEmail())
              .firstName(registerRequestDto.getFirstName())
              .lastName(registerRequestDto.getLastName())
              .userName(registerRequestDto.getUserName())
              .imageUrl(registerRequestDto.getImageUrl())
              .encodedPassword(encodedPassword)
              .build();

          return userIdentityPort.createIdentity(userIdentityDto);
        })
        .flatMap(userDto ->{
          return  userRolAccessPort.assignRole(userDto.getId(),"USER")
              .thenReturn(userDto);
        })
        .flatMap(userDto -> tokenProviderPort.generateToken(userDto)
            .map(token -> AuthResponseDto.builder()
                .token(token)
                .user(userDto)
                .build())
        );
  }
}
