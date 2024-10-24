package com.biblioteca.model;

import java.time.LocalDate;

public class Autor {
    private int id;
    private String nome;
    private LocalDate dataNascimento;

    // Construtores
    public Autor() {}

    public Autor(String nome, LocalDate dataNascimento) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String toString() {
        return "Autor [id=" + id + ", nome=" + nome + ", data de nascimento=" + dataNascimento + "]";
    }
}
