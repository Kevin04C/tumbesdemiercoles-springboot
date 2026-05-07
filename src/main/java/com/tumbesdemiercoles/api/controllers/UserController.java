package com.tumbesdemiercoles.api.controllers;

import com.tumbesdemiercoles.api.controllers.dto.UserRequest;
import com.tumbesdemiercoles.api.controllers.dto.UserResponse;
import com.tumbesdemiercoles.api.controllers.interfaces.UsuariosApi;
import com.tumbesdemiercoles.api.facade.UserFacade;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserController implements UsuariosApi {

  private final UserFacade  userFacade;

  @Override
  public Mono<ResponseEntity<UserResponse>> createUser(Mono<UserRequest> userRequest, ServerWebExchange exchange) {
    return userFacade.createUser(userRequest);
  }

  @Override
  public Mono<ResponseEntity<Flux<UserResponse>>> getAllUsers(ServerWebExchange exchange) {
    return userFacade.getAllUsers();
  }

  @Override
  public Mono<ResponseEntity<UserResponse>> getUserById(UUID id, ServerWebExchange exchange) {
    return userFacade.getUserById(id);
  }
}
