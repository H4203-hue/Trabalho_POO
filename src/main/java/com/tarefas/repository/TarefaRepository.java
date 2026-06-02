package com.tarefas.repository;

import com.tarefas.entity.Tarefa;
import com.tarefas.enums.DiaSemana;
import com.tarefas.enums.Prioridade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório responsável pelo acesso ao banco de dados da entidade Tarefa.
 * O Spring Data JPA gera automaticamente as queries a partir dos nomes dos métodos.
 */
@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    /**
     * Busca todas as tarefas com a prioridade informada.
     *
     * @param prioridade o nível de prioridade a filtrar
     * @return lista de tarefas com essa prioridade
     */
    List<Tarefa> findByPrioridade(Prioridade prioridade);

    /**
     * Busca todas as tarefas do dia da semana informado.
     *
     * @param diaSemana o dia da semana a filtrar
     * @return lista de tarefas desse dia
     */
    List<Tarefa> findByDiaSemana(DiaSemana diaSemana);
}
