package com.bts.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String authenticationToken;
    private String refreshToken;
    private String username;
}
