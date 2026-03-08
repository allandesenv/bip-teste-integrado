package com.example.backend.repository;

import com.example.ejb.Beneficio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficioRepository extends JpaRepository<Beneficio, Long> {
}
