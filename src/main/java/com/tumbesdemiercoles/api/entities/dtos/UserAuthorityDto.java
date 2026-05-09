package com.tumbesdemiercoles.api.entities.dtos;

public record UserAuthorityDto(
    Long roleId,
    String roleName,
    String permissionName
) {}
