package com.example.backend.service;

import com.example.backend.dto.BeneficioRequest;
import com.example.backend.dto.BeneficioResponse;
import com.example.backend.dto.TransferRequest;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.integration.EjbTransferGateway;
import com.example.backend.repository.BeneficioRepository;
import com.example.ejb.Beneficio;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BeneficioServiceTest {

    private BeneficioRepository repository;
    private EjbTransferGateway transferGateway;
    private BeneficioService service;

    @BeforeEach
    void setUp() {
        repository = mock(BeneficioRepository.class);
        transferGateway = mock(EjbTransferGateway.class);
        service = new BeneficioService(repository, transferGateway);
    }

    @Test
    void shouldCreateBeneficio() {
        BeneficioRequest request = new BeneficioRequest("Vale", "Alimentacao", new BigDecimal("100.00"), true);

        Beneficio saved = beneficio(1L, "Vale", "Alimentacao", "100.00", true, 0L);
        when(repository.save(org.mockito.ArgumentMatchers.any(Beneficio.class))).thenReturn(saved);

        BeneficioResponse result = service.create(request);

        assertEquals(1L, result.id());
        assertEquals("Vale", result.nome());
        assertEquals(0, result.valor().compareTo(new BigDecimal("100.00")));

        ArgumentCaptor<Beneficio> captor = ArgumentCaptor.forClass(Beneficio.class);
        verify(repository).save(captor.capture());
        assertEquals("Vale", captor.getValue().getNome());
    }

    @Test
    void shouldUpdateBeneficio() {
        Beneficio existing = beneficio(1L, "Old", "Desc", "50.00", true, 0L);
        Beneficio updated = beneficio(1L, "New", "Nova", "120.00", false, 1L);
        BeneficioRequest request = new BeneficioRequest("New", "Nova", new BigDecimal("120.00"), false);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(updated);

        BeneficioResponse result = service.update(1L, request);

        assertEquals("New", result.nome());
        assertEquals("Nova", result.descricao());
        assertEquals(false, result.ativo());
    }

    @Test
    void shouldThrowWhenNotFoundById() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Executable action = () -> service.findById(999L);

        assertThrows(ResourceNotFoundException.class, action);
    }

    @Test
    void shouldDeleteExistingBeneficio() {
        Beneficio existing = beneficio(1L, "A", "B", "10.00", true, 0L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);

        verify(repository).delete(existing);
    }

    @Test
    void shouldTransferAndReturnUpdatedBalances() {
        TransferRequest request = new TransferRequest(1L, 2L, new BigDecimal("20.00"));
        Beneficio from = beneficio(1L, "A", "Desc A", "80.00", true, 1L);
        Beneficio to = beneficio(2L, "B", "Desc B", "120.00", true, 1L);

        when(repository.findById(1L)).thenReturn(Optional.of(from));
        when(repository.findById(2L)).thenReturn(Optional.of(to));

        List<BeneficioResponse> result = service.transfer(request);

        verify(transferGateway).transfer(1L, 2L, new BigDecimal("20.00"));
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());
    }

    private Beneficio beneficio(Long id, String nome, String descricao, String valor, boolean ativo, Long version) {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(id);
        beneficio.setNome(nome);
        beneficio.setDescricao(descricao);
        beneficio.setValor(new BigDecimal(valor));
        beneficio.setAtivo(ativo);
        beneficio.setVersion(version);
        return beneficio;
    }
}
