package com.example.cardmanager.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private Long id_carta;
    @NotEmpty
    private String email_intestatario;
    @NotEmpty(message = "card number should not be empty")
    private String cnumber;
    @NotNull(message = "saldo should not be empty")
    private float saldo;
    private String stato;
}