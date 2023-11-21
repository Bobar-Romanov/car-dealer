package com.service.carDealer.util;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class RegForm {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private String fullName;

    @NotBlank
    private String phone;
}
