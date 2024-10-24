package com.biblioteca.dao;

import com.biblioteca.model.Emprestimo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {
    private Connection connection;

    public EmprestimoDAO(Connection connection) {
        this.connection = connection;
    }

    // Metodo para registrar um novo empréstimo
    public void salvar(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimo (livro_id, usuario_id, data_emprestimo) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, emprestimo.getLivroId());
            stmt.setInt(2, emprestimo.getUsuarioId());
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
            stmt.executeUpdate();

            // Obter o ID gerado automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    emprestimo.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Metodo para registrar a devolução de um livro
    public void registrarDevolucao(int emprestimoId, LocalDate dataDevolucao) throws SQLException {
        String sql = "UPDATE emprestimo SET data_devolucao = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(dataDevolucao));
            stmt.setInt(2, emprestimoId);
            stmt.executeUpdate();
        }
    }

    // Metodo para listar todos os empréstimos
    public List<Emprestimo> listarTodos() throws SQLException {
        String sql = "SELECT e.*, u.nome AS nomeUsuario, l.titulo AS tituloLivro " +
                "FROM emprestimo e " +
                "JOIN usuario u ON e.usuario_id = u.id " +
                "JOIN livro l ON e.livro_id = l.id";
        List<Emprestimo> emprestimos = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setId(rs.getInt("id"));
                emprestimo.setLivroId(rs.getInt("livro_id"));
                emprestimo.setUsuarioId(rs.getInt("usuario_id"));
                emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
                emprestimo.setDataDevolucao(rs.getDate("data_devolucao") != null ? rs.getDate("data_devolucao").toLocalDate() : null);
                emprestimo.setNomeUsuario(rs.getString("nomeUsuario"));
                emprestimo.setTituloLivro(rs.getString("tituloLivro"));
                emprestimos.add(emprestimo);
            }
        }
        return emprestimos;
    }

    // Metodo para buscar um empréstimo por ID
    public Emprestimo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM emprestimo WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Emprestimo emprestimo = new Emprestimo();
                    emprestimo.setId(rs.getInt("id"));
                    emprestimo.setLivroId(rs.getInt("livro_id"));
                    emprestimo.setUsuarioId(rs.getInt("usuario_id"));
                    emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
                    emprestimo.setDataDevolucao(rs.getDate("data_devolucao") != null ? rs.getDate("data_devolucao").toLocalDate() : null);
                    return emprestimo;
                }
            }
        }
        return null;
    }
}
