package com.example.backend.service;

import com.example.backend.dto.BeneficioRequest;
import com.example.backend.dto.BeneficioResponse;
import com.example.backend.dto.TransferRequest;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.integration.EjbTransferGateway;
import com.example.backend.repository.BeneficioRepository;
import com.example.ejb.Beneficio;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeneficioService {

    private final BeneficioRepository beneficioRepository;
    private final EjbTransferGateway ejbTransferGateway;

    public BeneficioService(BeneficioRepository beneficioRepository, EjbTransferGateway ejbTransferGateway) {
        this.beneficioRepository = beneficioRepository;
        this.ejbTransferGateway = ejbTransferGateway;
    }

    @Transactional(readOnly = true)
    public List<BeneficioResponse> findAll() {
        return beneficioRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BeneficioResponse findById(Long id) {
        return toResponse(getByIdOrThrow(id));
    }

    @Transactional
    public BeneficioResponse create(BeneficioRequest request) {
        Beneficio beneficio = new Beneficio();
        applyRequest(beneficio, request);
        return toResponse(beneficioRepository.save(beneficio));
    }

    @Transactional
    public BeneficioResponse update(Long id, BeneficioRequest request) {
        Beneficio beneficio = getByIdOrThrow(id);
        applyRequest(beneficio, request);
        return toResponse(beneficioRepository.save(beneficio));
    }

    @Transactional
    public void delete(Long id) {
        Beneficio beneficio = getByIdOrThrow(id);
        beneficioRepository.delete(beneficio);
    }

    @Transactional
    public List<BeneficioResponse> transfer(TransferRequest request) {
        ejbTransferGateway.transfer(request.fromId(), request.toId(), request.amount());
        return List.of(findById(request.fromId()), findById(request.toId()));
    }

    private Beneficio getByIdOrThrow(Long id) {
        return beneficioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficio nao encontrado para id: " + id));
    }

    private void applyRequest(Beneficio beneficio, BeneficioRequest request) {
        beneficio.setNome(request.nome());
        beneficio.setDescricao(request.descricao());
        beneficio.setValor(request.valor());
        beneficio.setAtivo(request.ativo());
    }

    private BeneficioResponse toResponse(Beneficio beneficio) {
        return new BeneficioResponse(
                beneficio.getId(),
                beneficio.getNome(),
                beneficio.getDescricao(),
                beneficio.getValor(),
                beneficio.getAtivo(),
                beneficio.getVersion()
        );
    }
}
