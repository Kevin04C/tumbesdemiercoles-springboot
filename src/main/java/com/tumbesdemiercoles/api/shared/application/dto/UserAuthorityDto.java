package com.tumbesdemiercoles.api.shared.application.dto;

public record UserAuthorityDto(
    Long roleId,
    String roleName,
    String permissionName
) {}
