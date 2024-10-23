package com.biblioteca.dao;

import com.biblioteca.conexao.Database;
import com.biblioteca.model.Livro;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    public boolean realizarEmprestimo(int livroId) {
        String updateLivroSql = "UPDATE livros SET disponivel = false WHERE id = ?";
        String insertEmprestimoSql = "INSERT INTO emprestimos (livro_id, data_emprestimo) VALUES (?, ?)";

        try (Connection conn = Database.getConnection()) {

            // Atualiza a disponibilidade do livro
            try (PreparedStatement updateStmt = conn.prepareStatement(updateLivroSql)) {
                updateStmt.setInt(1, livroId);
                updateStmt.executeUpdate();
            }

            // Insere um novo registro de empréstimo
            try (PreparedStatement insertStmt = conn.prepareStatement(insertEmprestimoSql)) {
                insertStmt.setInt(1, livroId);
                insertStmt.setDate(2, Date.valueOf(LocalDate.now()));
                insertStmt.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Novo método para devolver um livro
    public boolean devolverLivro(int livroId) {
        String updateLivroSql = "UPDATE livros SET disponivel = true WHERE id = ?";
        String updateEmprestimoSql = "UPDATE emprestimos SET data_devolucao = ? WHERE livro_id = ? AND data_devolucao IS NULL";

        try (Connection conn = Database.getConnection()) {

            // Atualiza a disponibilidade do livro
            try (PreparedStatement updateStmt = conn.prepareStatement(updateLivroSql)) {
                updateStmt.setInt(1, livroId);
                updateStmt.executeUpdate();
            }

            // Atualiza a data de devolução no registro de empréstimo
            try (PreparedStatement updateEmprestimoStmt = conn.prepareStatement(updateEmprestimoSql)) {
                updateEmprestimoStmt.setDate(1, Date.valueOf(LocalDate.now()));
                updateEmprestimoStmt.setInt(2, livroId);
                int rowsAffected = updateEmprestimoStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Livro devolvido com sucesso!");
                    return true;
                } else {
                    System.out.println("Nenhum empréstimo encontrado para este livro.");
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Livro> listarLivrosEmprestados() {
        String sql = "SELECT l.id, l.titulo, l.autor FROM livros l " +
                "INNER JOIN emprestimos e ON l.id = e.livro_id " +
                "WHERE e.data_devolucao IS NULL";
        List<Livro> livrosEmprestados = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livrosEmprestados.add(livro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livrosEmprestados;
    }
}