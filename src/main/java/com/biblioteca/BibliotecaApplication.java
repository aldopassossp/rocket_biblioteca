package com.biblioteca;

import com.biblioteca.conexao.Database;
import com.biblioteca.dao.AutorDAO;
import com.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.dao.LivroDAO;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.Autor;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class BibliotecaApplication {

	public static void main(String[] args) {

		try (Connection connection = Database.getConnection()) {
			LivroDAO livroDAO = new LivroDAO(connection);
			AutorDAO autorDAO = new AutorDAO(connection);
			UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
			EmprestimoDAO emprestimoDAO = new EmprestimoDAO(connection);

			Scanner scanner = new Scanner(System.in);
			while (true) {
				exibirMenu();
				int opcao = scanner.nextInt();
				scanner.nextLine(); // Consome a nova linha

				switch (opcao) {
					case 1:
						listarLivros(livroDAO);
						break;
					case 2:
						realizarEmprestimo(emprestimoDAO, livroDAO, usuarioDAO, scanner);
						break;
					case 3:
						devolverLivro(emprestimoDAO, livroDAO, scanner);
						break;
					case 4:
						listarUsuarios(usuarioDAO);
						break;
					case 5:
						listarEmprestimos(emprestimoDAO);
						break;
					case 6:
						buscarLivroPorTituloOuAutor(livroDAO, scanner);
						break;
					case 7:
						cadastrarLivro(livroDAO, autorDAO, scanner);
						break;
					case 8:
						cadastrarUsuario(usuarioDAO, scanner);
						break;
					case 0:
						System.out.println("Saindo do sistema...");
						return;
					default:
						System.out.println("Opção inválida. Tente novamente.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void exibirMenu() {
		System.out.println("---- Menu Principal ----");
		System.out.println("1. Listar livros disponíveis");
		System.out.println("2. Realizar empréstimo");
		System.out.println("3. Devolver livro");
		System.out.println("4. Listar usuários");
		System.out.println("5. Listar empréstimos");
		System.out.println("6. Buscar livro por título ou autor");
		System.out.println("7. Cadastrar livro");
		System.out.println("8. Cadastrar usuário");
		System.out.println("0. Sair");
		System.out.print("Escolha uma opção: ");
	}

	private static void listarLivros(LivroDAO livroDAO) throws SQLException {
		List<Livro> livros = livroDAO.listarTodos();
		System.out.println("---- Livros Disponíveis ----");
		for (Livro livro : livros) {
			if (livro.isDisponivel()) {
				System.out.printf("ID: %d, Título: %s, Autor: %s%n", livro.getId(), livro.getTitulo(), livro.getNomeAutor());
			}
		}
	}

	private static void realizarEmprestimo(EmprestimoDAO emprestimoDAO, LivroDAO livroDAO, UsuarioDAO usuarioDAO, Scanner scanner) throws SQLException {
		System.out.println("---- Realizar Empréstimo ----");
		System.out.print("Informe seu nome: ");
		String nomeUsuario = scanner.nextLine();
		Usuario usuario = usuarioDAO.buscarPorNome(nomeUsuario);
		if (usuario == null) {
			System.out.println("Usuário não encontrado.");
			return;
		}

		System.out.print("Informe o ID do livro: ");
		int livroId = scanner.nextInt();
		Livro livro = livroDAO.buscarPorId(livroId);
		if (livro == null || !livro.isDisponivel()) {
			System.out.println("Livro não disponível para empréstimo.");
			return;
		}

		Emprestimo emprestimo = new Emprestimo();
		emprestimo.setUsuarioId(usuario.getId());
		emprestimo.setLivroId(livro.getId());
		emprestimo.setDataEmprestimo(LocalDate.now());

		emprestimoDAO.salvar(emprestimo);
		livroDAO.atualizarDisponibilidade(livroId, false);
		System.out.println("Empréstimo realizado com sucesso!");
	}

	private static void devolverLivro(EmprestimoDAO emprestimoDAO, LivroDAO livroDAO, Scanner scanner) throws SQLException {
		System.out.println("---- Devolver Livro ----");
		System.out.print("Informe o ID do empréstimo: ");
		int emprestimoId = scanner.nextInt();
		Emprestimo emprestimo = emprestimoDAO.buscarPorId(emprestimoId);
		if (emprestimo == null || emprestimo.getDataDevolucao() != null) {
			System.out.println("Empréstimo não encontrado ou livro já devolvido.");
			return;
		}

		emprestimoDAO.registrarDevolucao(emprestimoId, LocalDate.now());
		livroDAO.atualizarDisponibilidade(emprestimo.getLivroId(), true);
		System.out.println("Devolução realizada com sucesso!");
	}

	private static void listarUsuarios(UsuarioDAO usuarioDAO) throws SQLException {
		List<Usuario> usuarios = usuarioDAO.listarTodos();
		System.out.println("---- Usuários Cadastrados ----");
		for (Usuario usuario : usuarios) {
			System.out.printf("ID: %d, Nome: %s, Data de Nascimento: %s, Email: %s%n",
					usuario.getId(), usuario.getNome(), usuario.getDataNascimento(), usuario.getEmail());
		}
	}

	private static void listarEmprestimos(EmprestimoDAO emprestimoDAO) throws SQLException {
		List<Emprestimo> emprestimos = emprestimoDAO.listarTodos();
		System.out.println("---- Empréstimos ----");
		for (Emprestimo emprestimo : emprestimos) {
			String devolvido = (emprestimo.getDataDevolucao() != null) ? "Devolvido" : "Não Devolvido";
			System.out.printf("ID: %d, Livro: %s, Usuário: %s, Data de Empréstimo: %s, Status: %s%n",
					emprestimo.getId(), emprestimo.getTituloLivro(), emprestimo.getNomeUsuario(),
					emprestimo.getDataEmprestimo(), devolvido);
		}
	}

	private static void buscarLivroPorTituloOuAutor(LivroDAO livroDAO, Scanner scanner) throws SQLException {
		System.out.println("---- Buscar Livro ----");
		System.out.print("Informe o título ou nome do autor: ");
		String busca = scanner.nextLine();
		List<Livro> livros = livroDAO.listarTodos(); // Poderia ser otimizado para busca direta no banco
		System.out.println("Resultados da busca:");
		for (Livro livro : livros) {
			if (livro.getTitulo().contains(busca) || livro.getNomeAutor().contains(busca)) {
				System.out.printf("ID: %d, Título: %s, Autor: %s%n", livro.getId(), livro.getTitulo(), livro.getNomeAutor());
			}
		}
	}

	private static void cadastrarLivro(LivroDAO livroDAO, AutorDAO autorDAO, Scanner scanner) throws SQLException {
		System.out.println("---- Cadastrar Livro ----");
		System.out.print("Título do livro: ");
		String titulo = scanner.nextLine();

		System.out.println("Autores disponíveis:");
		List<Autor> autores = autorDAO.listarTodos();
		for (Autor autor : autores) {
			System.out.printf("ID: %d, Nome: %s%n", autor.getId(), autor.getNome());
		}

		System.out.print("Informe o ID do autor: ");
		int autorId = scanner.nextInt();
		scanner.nextLine(); // Consome a nova linha

		Livro livro = new Livro();
		livro.setTitulo(titulo);
		livro.setAutorId(autorId);
		livro.setDisponivel(true);

		livroDAO.salvar(livro);
		System.out.println("Livro cadastrado com sucesso!");
	}

	private static void cadastrarUsuario(UsuarioDAO usuarioDAO, Scanner scanner) throws SQLException {
		System.out.println("---- Cadastrar Usuário ----");
		System.out.print("Nome: ");
		String nome = scanner.nextLine();
		System.out.print("Data de Nascimento (YYYY-MM-DD): ");
		LocalDate dataNascimento = LocalDate.parse(scanner.nextLine());
		System.out.print("Email: ");
		String email = scanner.nextLine();

		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setDataNascimento(dataNascimento);
		usuario.setEmail(email);

		usuarioDAO.salvar(usuario);
		System.out.println("Usuário cadastrado com sucesso!");
	}
}