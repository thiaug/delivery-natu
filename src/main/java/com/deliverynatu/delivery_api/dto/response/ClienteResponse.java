package com.deliverynatu.delivery_api.dto.response;

import lombok.Data;

@Data
public class ClienteResponse {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
}