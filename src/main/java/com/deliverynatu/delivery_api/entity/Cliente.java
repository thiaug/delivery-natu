package com.deliverynatu.delivery_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private boolean ativo;

    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;

    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDateTime.now();
    }

}
