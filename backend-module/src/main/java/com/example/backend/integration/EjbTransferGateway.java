package com.example.backend.integration;

import com.example.ejb.BeneficioEjbService;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class EjbTransferGateway {

    private final BeneficioEjbService beneficioEjbService;

    public EjbTransferGateway(BeneficioEjbService beneficioEjbService) {
        this.beneficioEjbService = beneficioEjbService;
    }

    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        beneficioEjbService.transfer(fromId, toId, amount);
    }
}
