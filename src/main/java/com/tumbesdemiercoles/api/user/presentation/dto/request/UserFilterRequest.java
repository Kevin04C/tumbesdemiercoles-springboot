package com.tumbesdemiercoles.api.user.presentation.dto.request;

import com.tumbesdemiercoles.api.shared.dto.BasePageRequestDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserFilterRequest extends BasePageRequestDto {

  private UUID userId;

  @Size(max = 30, message = "El Nombre no puede exceder los 30 caracteres")
  private String firstName;

  @Size(max = 30, message = "El Apellido no puede exceder los 30 caracteres")
  private String lastName;

  @Email(message = "Debe proporcionar un formato de correo válido para la búsqueda")
  private String email;

}
