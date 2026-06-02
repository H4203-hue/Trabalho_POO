package com.tarefas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarefas.dto.TarefaRequestDTO;
import com.tarefas.dto.TarefaResponseDTO;
import com.tarefas.enums.DiaSemana;
import com.tarefas.enums.Prioridade;
import com.tarefas.exception.TarefaNotFoundException;
import com.tarefas.service.TarefaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes do TarefaController usando MockMvc.
 * O @WebMvcTest carrega apenas a camada web, sem subir o banco.
 */
@WebMvcTest(TarefaController.class)
class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TarefaService tarefaService;

    // DTO de resposta reutilizado nos testes
    private final TarefaResponseDTO respostaExemplo = new TarefaResponseDTO(
            1L, "Estudar Java", "Revisar Spring Boot",
            Prioridade.URGENTE, "vermelho",
            DiaSemana.SEGUNDA, LocalDate.now().plusDays(1),
            false, LocalDateTime.now()
    );

    // DTO de requisição válido
    private final TarefaRequestDTO requestValido = new TarefaRequestDTO(
            "Estudar Java", "Revisar Spring Boot",
            Prioridade.URGENTE, DiaSemana.SEGUNDA,
            LocalDate.now().plusDays(1)
    );

    @Test
    @DisplayName("POST /tarefas - deve criar tarefa e retornar 201")
    void deveCriarTarefa() throws Exception {
        when(tarefaService.criar(any())).thenReturn(respostaExemplo);

        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.dados.titulo").value("Estudar Java"));
    }

    @Test
    @DisplayName("POST /tarefas - deve retornar 400 quando título está vazio")
    void deveRetornar400QuandoTituloVazio() throws Exception {
        TarefaRequestDTO requestInvalido = new TarefaRequestDTO(
                "", "Descrição", Prioridade.URGENTE, DiaSemana.SEGUNDA,
                LocalDate.now().plusDays(1)
        );

        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("GET /tarefas - deve listar todas as tarefas")
    void deveListarTarefas() throws Exception {
        when(tarefaService.listarTodas()).thenReturn(List.of(respostaExemplo));

        mockMvc.perform(get("/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dados[0].titulo").value("Estudar Java"));
    }

    @Test
    @DisplayName("GET /tarefas/{id} - deve retornar tarefa por ID")
    void deveBuscarPorId() throws Exception {
        when(tarefaService.buscarPorId(1L)).thenReturn(respostaExemplo);

        mockMvc.perform(get("/tarefas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dados.id").value(1));
    }

    @Test
    @DisplayName("GET /tarefas/{id} - deve retornar 404 quando não encontrado")
    void deveRetornar404QuandoNaoEncontrado() throws Exception {
        when(tarefaService.buscarPorId(99L)).thenThrow(new TarefaNotFoundException(99L));

        mockMvc.perform(get("/tarefas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("PUT /tarefas/{id} - deve atualizar tarefa")
    void deveAtualizarTarefa() throws Exception {
        when(tarefaService.atualizar(eq(1L), any())).thenReturn(respostaExemplo);

        mockMvc.perform(put("/tarefas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Tarefa atualizada com sucesso"));
    }

    @Test
    @DisplayName("DELETE /tarefas/{id} - deve deletar tarefa")
    void deveDeletarTarefa() throws Exception {
        mockMvc.perform(delete("/tarefas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Tarefa deletada com sucesso"));
    }

    @Test
    @DisplayName("DELETE /tarefas/{id} - deve retornar 404 quando ID não existe")
    void deveRetornar404AoDeletar() throws Exception {
        doThrow(new TarefaNotFoundException(99L)).when(tarefaService).deletar(99L);

        mockMvc.perform(delete("/tarefas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /tarefas/{id}/concluir - deve concluir tarefa")
    void deveConcluirTarefa() throws Exception {
        TarefaResponseDTO concluida = new TarefaResponseDTO(
                1L, "Estudar Java", "Desc", Prioridade.URGENTE, "vermelho",
                DiaSemana.SEGUNDA, LocalDate.now().plusDays(1), true, LocalDateTime.now()
        );
        when(tarefaService.concluir(1L)).thenReturn(concluida);

        mockMvc.perform(patch("/tarefas/1/concluir"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dados.concluida").value(true));
    }

    @Test
    @DisplayName("GET /tarefas/prioridade - deve filtrar por prioridade")
    void deveFiltrarPorPrioridade() throws Exception {
        when(tarefaService.filtrarPorPrioridade(Prioridade.URGENTE)).thenReturn(List.of(respostaExemplo));

        mockMvc.perform(get("/tarefas/prioridade").param("prioridade", "URGENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dados[0].prioridade").value("URGENTE"));
    }

    @Test
    @DisplayName("GET /tarefas/dia-semana - deve filtrar por dia da semana")
    void deveFiltrarPorDiaSemana() throws Exception {
        when(tarefaService.filtrarPorDiaSemana(DiaSemana.SEGUNDA)).thenReturn(List.of(respostaExemplo));

        mockMvc.perform(get("/tarefas/dia-semana").param("diaSemana", "SEGUNDA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dados[0].diaSemana").value("SEGUNDA"));
    }
}
