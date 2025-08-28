package com.deliverynatu.delivery_api.repository;

import java.math.BigDecimal;

public interface RelatorioVendas {
    String getNomeRestaurante();

    BigDecimal getTotalVendas();

    Long getQuanƟdePedidos();
}
