package com.biblioteca.model;

import java.time.LocalDate;

public class Usuario {
    private int id;
    private String nome;
    private LocalDate dataNascimento;
    private String email;

    // Construtores
    public Usuario() {}

    public Usuario(String nome, LocalDate dataNascimento, String email) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nome=" + nome + ", data de nascimento=" + dataNascimento + ", email=" + email + "]";
    }
}
