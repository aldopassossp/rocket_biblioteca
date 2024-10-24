package com.biblioteca.dao;

import com.biblioteca.model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {
    private Connection connection;

    public LivroDAO(Connection connection) {
        this.connection = connection;
    }

    // Metodo para salvar um novo livro
    public void salvar(Livro livro) throws SQLException {
        String sql = "INSERT INTO livro (titulo, autor_id, disponivel) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setInt(2, livro.getAutorId());
            stmt.setBoolean(3, livro.isDisponivel());
            stmt.executeUpdate();

            // Obter o ID gerado automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    livro.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Metodo para buscar livro por ID
    public Livro buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM livro WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Livro livro = new Livro();
                    livro.setId(rs.getInt("id"));
                    livro.setTitulo(rs.getString("titulo"));
                    livro.setAutorId(rs.getInt("autor_id"));
                    livro.setDisponivel(rs.getBoolean("disponivel"));
                    return livro;
                }
            }
        }
        return null;
    }

    // Metodo para listar todos os livros
    public List<Livro> listarTodos() throws SQLException {
        String sql = "SELECT l.*, a.nome AS nomeAutor FROM livro l LEFT JOIN autor a ON l.autor_id = a.id";
        List<Livro> livros = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutorId(rs.getInt("autor_id"));
                livro.setNomeAutor(rs.getString("nomeAutor"));
                livro.setDisponivel(rs.getBoolean("disponivel"));
                livros.add(livro);
            }
        }
        return livros;
    }

    // Metodo para atualizar a disponibilidade do livro
    public void atualizarDisponibilidade(int livroId, boolean disponivel) throws SQLException {
        String sql = "UPDATE livro SET disponivel = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, disponivel);
            stmt.setInt(2, livroId);
            stmt.executeUpdate();
        }
    }

    // Metodo para atualizar um livro
    public void atualizar(Livro livro) throws SQLException {
        String sql = "UPDATE livro SET titulo = ?, autor_id = ?, disponivel = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setInt(2, livro.getAutorId());
            stmt.setBoolean(3, livro.isDisponivel());
            stmt.setInt(4, livro.getId());
            stmt.executeUpdate();
        }
    }

    // Metodo para deletar um livro
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM livro WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
