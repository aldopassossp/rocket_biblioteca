package com.biblioteca.model;

public class Livro {
    private int id;
    private String titulo;
    private int autorId;
    private boolean disponivel;

    // Atributo adicional para exibir o nome do autor
    private String nomeAutor;

    // Construtores
    public Livro() {}

    public Livro(String titulo, int autorId) {
        this.titulo = titulo;
        this.autorId = autorId;
        this.disponivel = true; // Por padrão, o livro está disponível
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAutorId() {
        return autorId;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    @Override
    public String toString() {
        return "Livro [id=" + id + ", titulo=" + titulo + ", autorId=" + autorId + ", disponivel=" + disponivel + "]";
    }
}
