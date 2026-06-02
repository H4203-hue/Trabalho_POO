package com.tarefas.exception;

/**
 * Exceção lançada quando uma tarefa não é encontrada no banco de dados.
 * Estende RuntimeException para ser uma exceção não verificada.
 */
public class TarefaNotFoundException extends RuntimeException {

    public TarefaNotFoundException(Long id) {
        super("Tarefa com ID " + id + " não encontrada");
    }
}
