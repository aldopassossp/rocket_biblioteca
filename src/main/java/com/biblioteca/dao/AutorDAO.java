package com.biblioteca.dao;

import com.biblioteca.model.Autor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {
    private Connection connection;

    public AutorDAO(Connection connection) {
        this.connection = connection;
    }

    // Metodo para salvar um novo autor
    public void salvar(Autor autor) throws SQLException {
        String sql = "INSERT INTO autor (nome, data_nascimento) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, autor.getNome());
            stmt.setDate(2, Date.valueOf(autor.getDataNascimento()));
            stmt.executeUpdate();

            // Obter o ID gerado automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    autor.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Metodo para buscar autor por ID
    public Autor buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM autor WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Autor autor = new Autor();
                    autor.setId(rs.getInt("id"));
                    autor.setNome(rs.getString("nome"));
                    autor.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                    return autor;
                }
            }
        }
        return null;
    }

    // Metodo para listar todos os autores
    public List<Autor> listarTodos() throws SQLException {
        String sql = "SELECT * FROM autor";
        List<Autor> autores = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Autor autor = new Autor();
                autor.setId(rs.getInt("id"));
                autor.setNome(rs.getString("nome"));
                autor.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                autores.add(autor);
            }
        }
        return autores;
    }

    // Metodo para atualizar um autor
    public void atualizar(Autor autor) throws SQLException {
        String sql = "UPDATE autor SET nome = ?, data_nascimento = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, autor.getNome());
            stmt.setDate(2, Date.valueOf(autor.getDataNascimento()));
            stmt.setInt(3, autor.getId());
            stmt.executeUpdate();
        }
    }

    // Metodo para deletar um autor
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM autor WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
