package com.tarefas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação.
 * O @SpringBootApplication habilita a configuração automática do Spring Boot.
 */
@SpringBootApplication
public class GerenciadorTarefasApplication {

    public static void main(String[] args) {
        SpringApplication.run(GerenciadorTarefasApplication.class, args);
    }
}
