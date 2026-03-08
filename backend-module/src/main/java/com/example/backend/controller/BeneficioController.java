package com.example.backend.controller;

import com.example.backend.dto.BeneficioRequest;
import com.example.backend.dto.BeneficioResponse;
import com.example.backend.dto.TransferRequest;
import com.example.backend.service.BeneficioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/beneficios")
@Tag(name = "Beneficios", description = "Operacoes de CRUD e transferencia")
public class BeneficioController {

    private final BeneficioService beneficioService;

    public BeneficioController(BeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @GetMapping
    @Operation(summary = "Listar beneficios")
    public List<BeneficioResponse> findAll() {
        return beneficioService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar beneficio por id")
    public BeneficioResponse findById(@PathVariable Long id) {
        return beneficioService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Criar beneficio")
    public ResponseEntity<BeneficioResponse> create(@Valid @RequestBody BeneficioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficioService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar beneficio")
    public BeneficioResponse update(@PathVariable Long id, @Valid @RequestBody BeneficioRequest request) {
        return beneficioService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover beneficio")
    public void delete(@PathVariable Long id) {
        beneficioService.delete(id);
    }

    @PostMapping("/transferencias")
    @Operation(summary = "Transferir valor entre beneficios")
    public List<BeneficioResponse> transfer(@Valid @RequestBody TransferRequest request) {
        return beneficioService.transfer(request);
    }
}
