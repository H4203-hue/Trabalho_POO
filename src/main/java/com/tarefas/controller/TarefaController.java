package com.tarefas.controller;

import com.tarefas.dto.RespostaDTO;
import com.tarefas.dto.TarefaRequestDTO;
import com.tarefas.dto.TarefaResponseDTO;
import com.tarefas.enums.DiaSemana;
import com.tarefas.enums.Prioridade;
import com.tarefas.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável por receber as requisições HTTP das tarefas.
 * Não contém regras de negócio — apenas delega para o service.
 */
@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@Tag(name = "Tarefas", description = "Endpoints para gerenciamento de tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    /**
     * POST /tarefas
     * Cria uma nova tarefa.
     */
    @PostMapping
    @Operation(summary = "Criar tarefa", description = "Cadastra uma nova tarefa no sistema")
    public ResponseEntity<RespostaDTO<TarefaResponseDTO>> criar(
            @Valid @RequestBody TarefaRequestDTO dto) {

        TarefaResponseDTO tarefa = tarefaService.criar(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RespostaDTO.sucesso(201, "Tarefa criada com sucesso", tarefa));
    }

    /**
     * GET /tarefas
     * Lista todas as tarefas.
     */
    @GetMapping
    @Operation(summary = "Listar tarefas", description = "Retorna todas as tarefas cadastradas")
    public ResponseEntity<RespostaDTO<List<TarefaResponseDTO>>> listarTodas() {
        List<TarefaResponseDTO> tarefas = tarefaService.listarTodas();
        return ResponseEntity.ok(RespostaDTO.sucesso(200, "Tarefas listadas com sucesso", tarefas));
    }

    /**
     * GET /tarefas/{id}
     * Busca uma tarefa pelo ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarefa por ID", description = "Retorna uma tarefa específica pelo seu ID")
    public ResponseEntity<RespostaDTO<TarefaResponseDTO>> buscarPorId(
            @Parameter(description = "ID da tarefa") @PathVariable Long id) {

        TarefaResponseDTO tarefa = tarefaService.buscarPorId(id);
        return ResponseEntity.ok(RespostaDTO.sucesso(200, "Tarefa encontrada com sucesso", tarefa));
    }

    /**
     * PUT /tarefas/{id}
     * Atualiza uma tarefa existente.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa", description = "Atualiza todos os dados de uma tarefa existente")
    public ResponseEntity<RespostaDTO<TarefaResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TarefaRequestDTO dto) {

        TarefaResponseDTO tarefa = tarefaService.atualizar(id, dto);
        return ResponseEntity.ok(RespostaDTO.sucesso(200, "Tarefa atualizada com sucesso", tarefa));
    }

    /**
     * DELETE /tarefas/{id}
     * Remove uma tarefa pelo ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar tarefa", description = "Remove uma tarefa do sistema")
    public ResponseEntity<RespostaDTO<Void>> deletar(
            @PathVariable Long id) {

        tarefaService.deletar(id);
        return ResponseEntity.ok(RespostaDTO.sucesso(200, "Tarefa deletada com sucesso", null));
    }

    /**
     * PATCH /tarefas/{id}/concluir
     * Marca uma tarefa como concluída.
     */
    @PatchMapping("/{id}/concluir")
    @Operation(summary = "Concluir tarefa", description = "Marca uma tarefa como concluída")
    public ResponseEntity<RespostaDTO<TarefaResponseDTO>> concluir(
            @PathVariable Long id) {

        TarefaResponseDTO tarefa = tarefaService.concluir(id);
        return ResponseEntity.ok(RespostaDTO.sucesso(200, "Tarefa concluída com sucesso", tarefa));
    }

    /**
     * PATCH /tarefas/{id}/iniciar
     * Marca uma tarefa como não concluída (iniciada).
     */
    @PatchMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar tarefa", description = "Marca uma tarefa como não concluída")
    public ResponseEntity<RespostaDTO<TarefaResponseDTO>> iniciar(
            @PathVariable Long id) {

        TarefaResponseDTO tarefa = tarefaService.iniciar(id);
        return ResponseEntity.ok(RespostaDTO.sucesso(200, "Tarefa marcada como iniciada", tarefa));
    }

    /**
     * GET /tarefas/prioridade?prioridade=URGENTE
     * Filtra tarefas por prioridade.
     */
    @GetMapping("/prioridade")
    @Operation(summary = "Filtrar por prioridade", description = "Retorna tarefas filtradas por prioridade (URGENTE, MEDIANO, NO_PRAZO)")
    public ResponseEntity<RespostaDTO<List<TarefaResponseDTO>>> filtrarPorPrioridade(
            @Parameter(description = "Prioridade: URGENTE, MEDIANO ou NO_PRAZO")
            @RequestParam Prioridade prioridade) {

        List<TarefaResponseDTO> tarefas = tarefaService.filtrarPorPrioridade(prioridade);
        return ResponseEntity.ok(RespostaDTO.sucesso(200, "Tarefas filtradas por prioridade", tarefas));
    }

    /**
     * GET /tarefas/dia-semana?diaSemana=SEGUNDA
     * Filtra tarefas por dia da semana.
     */
    @GetMapping("/dia-semana")
    @Operation(summary = "Filtrar por dia da semana", description = "Retorna tarefas filtradas por dia da semana")
    public ResponseEntity<RespostaDTO<List<TarefaResponseDTO>>> filtrarPorDiaSemana(
            @Parameter(description = "Dia da semana: SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO ou DOMINGO")
            @RequestParam DiaSemana diaSemana) {

        List<TarefaResponseDTO> tarefas = tarefaService.filtrarPorDiaSemana(diaSemana);
        return ResponseEntity.ok(RespostaDTO.sucesso(200, "Tarefas filtradas por dia da semana", tarefas));
    }
}
