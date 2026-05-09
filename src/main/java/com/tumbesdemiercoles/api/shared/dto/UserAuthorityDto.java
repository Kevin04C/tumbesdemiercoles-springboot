package com.tumbesdemiercoles.api.shared.dto;

public record UserAuthorityDto(
    Long roleId,
    String roleName,
    String permissionName
) {}
