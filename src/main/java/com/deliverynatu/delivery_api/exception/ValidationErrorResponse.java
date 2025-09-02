package com.deliverynatu.delivery_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ValidationErrorResponse {

    private int status;
    private String titulo;
    private Map<String, String> erros; // O mapa com os erros de cada campo
    private LocalDateTime timestamp;

}