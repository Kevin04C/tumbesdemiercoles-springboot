package com.tumbesdemiercoles.api.security.services;

import com.tumbesdemiercoles.api.user.application.dto.UserRequestDto;
import com.tumbesdemiercoles.api.user.application.usecase.GetUserUseCase;
import com.tumbesdemiercoles.api.user.application.usecase.UpdateUserUseCase;
import com.tumbesdemiercoles.api.security.utils.JwtUtil;
import com.tumbesdemiercoles.api.shared.utils.UserServiceText;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountSecurityService {

  private final JwtUtil jwtUtil;
  private final GetUserUseCase getUserUseCase;
  private final UpdateUserUseCase updateUserUseCase;
  private final EmailService emailService;
  private final PasswordEncoder passwordEncoder;

  /**
   * Verifica el email de un usuario utilizando un token de verificación.
   *
   * @param token El token de verificación de email.
   * @return Mono con el mensaje de éxito o error.
   */
  public Mono<String> verifyEmail(String token) {

    return Mono.fromCallable(() -> jwtUtil.parseEmailSubject(token))
        .flatMap(userId ->
            getUserUseCase.getById(userId)
                .flatMap(userDto -> {
                  UserRequestDto updateDto = UserRequestDto.builder()
                      .firstName(userDto.getFirstName())
                      .lastName(userDto.getLastName())
                      .email(userDto.getEmail())
                      .imageUrl(userDto.getImageUrl())
                      .build();
                  return updateUserUseCase.execute(userId, updateDto);
                })
        )
        .thenReturn(UserServiceText.emailVerifySuccess)
        .onErrorResume(e -> {
          log.error("Error verificando email con token: {}", e.getMessage());
          return Mono.just(UserServiceText.tokenInvalidOrExpired);
        });
  }

  /**
   * Solicita un restablecimiento de contraseña enviando un email con un token.
   *
   * @param email El email del usuario que solicita el restablecimiento de contraseña.
   * @return Mono vacío cuando se ha enviado el email de restablecimiento.
   */
//  public Mono<Void> requestPasswordReset(String email) {
//    return userService.findUserEntityByEmail(email)
//        .switchIfEmpty(Mono.error(new UserNotFoundException(
//            UserServiceText.userNotFoundForEmail + email)))
//        .flatMap(user -> {
//          try {
//            String token = jwtUtil.generatePasswordResetToken(
//                user.getUserId(), user.getPasswordHash());
//            return emailService.sendPasswordReset(user.getUserEmail(), token);
//          } catch (Exception ex) {
//            ex.printStackTrace();
//            return Mono.error(ex);
//          }
//        })
//        .doOnError(Throwable::printStackTrace)
//        .then();
//  }

  /**
   * Verifica la validez de un token de usuario
   * (por ejemplo, token de restablecimiento de
   * contraseña o verificación de email).
   *
   * @param token El token de usuario a verificar.
   * @return Mono de TokenStatusResponse con el estado del token.
   */
//  public Mono<TokenStatusResponse> verifyUserToken(String token) {
//
//    return Mono.defer(() -> {
//      try {
//        JwtUtil.ParsedReset parsed = jwtUtil.parsePasswordResetToken(token);
//        return Mono.just(new TokenStatusResponse(
//            UserServiceText.tokenStatusValid, parsed.userId()));
//
//      } catch (JwtException e) {
//        log.error(UserServiceText.errorValidToken, e);
//        return Mono.error(new ResponseStatusException(
//            HttpStatus.BAD_REQUEST, UserServiceText.tokenInvalidOrExpired));
//      }
//    });
//
//  }

  /**
   * Restablece la contraseña de un usuario utilizando un token de restablecimiento.
   *
   * @param token El token de restablecimiento de contraseña.
   * @param newPassword La nueva contraseña del usuario.
   * @return Mono vacío cuando la contraseña ha sido restablecida exitosamente.
   */
//  public Mono<Void> resetPassword(String token, String newPassword) {
//    return Mono.fromCallable(() -> jwtUtil.parsePasswordResetToken(token))
//        .flatMap(parsed ->
//            userService.findUserEntityById(parsed.userId())
//                .switchIfEmpty(Mono.error(new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, UserServiceText.tokenInvalidOrExpired)))
//                .flatMap(user -> {
//                  String expected = jwtUtil.generatePasswordResetToken(
//                      user.getUserId(), user.getPasswordHash());
//                  String expectedPhf = jwtUtil.parsePasswordResetToken(expected).phf();
//
//                  if (!expectedPhf.equals(parsed.phf())) {
//                    return Mono.error(new ResponseStatusException(
//                        HttpStatus.BAD_REQUEST,
//                        UserServiceText.tokenInvalidOrUsed));
//                  }
//
//                  user.setPasswordHash(passwordEncoder.encode(newPassword));
//                  user.setUpdatedAt(java.time.LocalDateTime.now());
//                  return userService.saveUserEntity(user).then();
//                })
//        )
//        .onErrorMap(JwtException.class,
//            e -> new ResponseStatusException(
//                HttpStatus.BAD_REQUEST,
//                UserServiceText.tokenInvalidOrExpired, e
//            ));
//  }

}