package com.biblioteca.dao;

import com.biblioteca.conexao.Database;
import com.biblioteca.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public List<Livro> listarLivrosDisponiveis() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros WHERE disponivel = true";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livros.add(livro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livros;
    }

    public void cadastrarLivro(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, disponivel) VALUES (?, ?, true)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.executeUpdate();

            System.out.println("Livro cadastrado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void alterarLivro(Livro livro) {
        String sql = "UPDATE livros SET titulo = ?, autor = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Livro alterado com sucesso!");
            } else {
                System.out.println("Livro não encontrado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerLivro(int livroId) {
        String sql = "DELETE FROM livros WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livroId);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Livro removido com sucesso!");
            } else {
                System.out.println("Livro não encontrado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
