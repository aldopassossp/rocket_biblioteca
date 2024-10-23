package com.biblioteca;

import com.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.dao.LivroDAO;
import com.biblioteca.model.Livro;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class BibliotecaApplication {

	public static void main(String[] args) {

		LivroDAO livroDAO = new LivroDAO();
		EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("\n=== Sistema de Biblioteca ===");
			System.out.println("1. Listar livros disponíveis");
			System.out.println("2. Cadastrar novo livro");
			System.out.println("3. Alterar livro");
			System.out.println("4. Remover livro");
			System.out.println("5. Realizar empréstimo");
			System.out.println("6. Devolver livro");
			System.out.println("7. Listar livros emprestados");
			System.out.println("0. Sair");
			System.out.print("Escolha uma opção: ");
			int opcao = scanner.nextInt();
			scanner.nextLine();  // Consumir a nova linha

			switch (opcao) {
				case 1:
					listarLivrosDisponiveis(livroDAO);
					break;
				case 2:
					cadastrarLivro(livroDAO, scanner);
					break;
				case 3:
					alterarLivro(livroDAO, scanner);
					break;
				case 4:
					removerLivro(livroDAO, scanner);
					break;
				case 5:
					realizarEmprestimo(emprestimoDAO, scanner);
					break;
				case 6:
					devolverLivro(emprestimoDAO, scanner);
					break;
				case 7:
					listarLivrosEmprestados(emprestimoDAO);
					break;
				case 0:
					System.out.println("Saindo...");
					scanner.close();
					System.exit(0);
					break;
				default:
					System.out.println("Opção inválida.");
			}
		}
	}

	private static void listarLivrosDisponiveis(LivroDAO livroDAO) {
		System.out.println("\nLista de livros disponíveis:");
		List<Livro> livros = livroDAO.listarLivrosDisponiveis();
		livros.forEach(System.out::println);
	}

	private static void cadastrarLivro(LivroDAO livroDAO, Scanner scanner) {
		System.out.print("Digite o título do livro: ");
		String titulo = scanner.nextLine();
		System.out.print("Digite o autor do livro: ");
		String autor = scanner.nextLine();

		Livro livro = new Livro();
		livro.setTitulo(titulo);
		livro.setAutor(autor);

		livroDAO.cadastrarLivro(livro);
	}

	private static void alterarLivro(LivroDAO livroDAO, Scanner scanner) {
		System.out.print("Digite o ID do livro a ser alterado: ");
		int livroId = scanner.nextInt();
		scanner.nextLine();  // Consumir a nova linha

		System.out.print("Digite o novo título do livro: ");
		String novoTitulo = scanner.nextLine();
		System.out.print("Digite o novo autor do livro: ");
		String novoAutor = scanner.nextLine();

		Livro livro = new Livro();
		livro.setId(livroId);
		livro.setTitulo(novoTitulo);
		livro.setAutor(novoAutor);

		livroDAO.alterarLivro(livro);
	}

	private static void removerLivro(LivroDAO livroDAO, Scanner scanner) {
		System.out.print("Digite o ID do livro a ser removido: ");
		int livroId = scanner.nextInt();

		livroDAO.removerLivro(livroId);
	}

	private static void realizarEmprestimo(EmprestimoDAO emprestimoDAO, Scanner scanner) {
		System.out.print("Digite o ID do livro para realizar o empréstimo: ");
		int livroId = scanner.nextInt();

		if (emprestimoDAO.realizarEmprestimo(livroId)) {
			System.out.println("Empréstimo realizado com sucesso!");
		} else {
			System.out.println("Erro ao realizar empréstimo.");
		}
	}

	private static void devolverLivro(EmprestimoDAO emprestimoDAO, Scanner scanner) {
		System.out.print("Digite o ID do livro a ser devolvido: ");
		int livroId = scanner.nextInt();

		if (emprestimoDAO.devolverLivro(livroId)) {
			System.out.println("Devolução realizada com sucesso!");
		} else {
			System.out.println("Erro ao realizar a devolução.");
		}
	}

	private static void listarLivrosEmprestados(EmprestimoDAO emprestimoDAO) {
		System.out.println("\nLista de livros emprestados:");
		List<Livro> livrosEmprestados = emprestimoDAO.listarLivrosEmprestados();
		if (livrosEmprestados.isEmpty()) {
			System.out.println("Nenhum livro emprestado no momento.");
		} else {
			livrosEmprestados.forEach(System.out::println);
		}
	}
}