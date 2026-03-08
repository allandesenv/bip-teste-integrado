package com.example.backend.controller;

import com.example.backend.dto.BeneficioResponse;
import com.example.backend.exception.GlobalExceptionHandler;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.service.BeneficioService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BeneficioControllerTest {

    private MockMvc mockMvc;
    private BeneficioService beneficioService;

    @BeforeEach
    void setUp() {
        beneficioService = mock(BeneficioService.class);
        BeneficioController controller = new BeneficioController(beneficioService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldListBeneficios() throws Exception {
        when(beneficioService.findAll()).thenReturn(List.of(
                new BeneficioResponse(1L, "A", "Desc", new BigDecimal("10.00"), true, 0L)
        ));

        mockMvc.perform(get("/api/v1/beneficios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("A"));
    }

    @Test
    void shouldCreateBeneficio() throws Exception {
        when(beneficioService.create(any())).thenReturn(
                new BeneficioResponse(1L, "Vale", "Desc", new BigDecimal("100.00"), true, 0L)
        );

        String payload = """
                {
                  "nome": "Vale",
                  "descricao": "Desc",
                  "valor": 100.00,
                  "ativo": true
                }
                """;

        mockMvc.perform(post("/api/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldReturnBadRequestForInvalidCreatePayload() throws Exception {
        String payload = """
                {
                  "nome": "",
                  "descricao": "Desc",
                  "valor": 100.00,
                  "ativo": true
                }
                """;

        mockMvc.perform(post("/api/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturnNotFoundWhenGetByIdMissing() throws Exception {
        when(beneficioService.findById(999L)).thenThrow(new ResourceNotFoundException("Beneficio nao encontrado"));

        mockMvc.perform(get("/api/v1/beneficios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldReturnConflictWhenTransferFailsByBusinessRule() throws Exception {
        when(beneficioService.transfer(any())).thenThrow(new IllegalStateException("Saldo insuficiente"));

        String payload = """
                {
                  "fromId": 1,
                  "toId": 2,
                  "amount": 9999.00
                }
                """;

        mockMvc.perform(post("/api/v1/beneficios/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void shouldDeleteBeneficio() throws Exception {
        doNothing().when(beneficioService).delete(1L);

        mockMvc.perform(delete("/api/v1/beneficios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeleteMissing() throws Exception {
        doThrow(new ResourceNotFoundException("Nao encontrado")).when(beneficioService).delete(eq(999L));

        mockMvc.perform(delete("/api/v1/beneficios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldUpdateBeneficio() throws Exception {
        when(beneficioService.update(eq(1L), any())).thenReturn(
                new BeneficioResponse(1L, "Atualizado", "Desc", new BigDecimal("55.00"), true, 1L)
        );

        String payload = """
                {
                  "nome": "Atualizado",
                  "descricao": "Desc",
                  "valor": 55.00,
                  "ativo": true
                }
                """;

        mockMvc.perform(put("/api/v1/beneficios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Atualizado"));
    }
}
