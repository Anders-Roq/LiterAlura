package br.edu.marcelo.demo.Principal;

import br.edu.marcelo.demo.Modelo.*;
import br.edu.marcelo.demo.Repositorio.RepositorioAutores;
import br.edu.marcelo.demo.Repositorio.RepositorioLivros;

import java.util.*;

public class Principal {
    private final Scanner scan = new Scanner(System.in);
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();
    private final static String URL_BASE = "https://gutendex.com/books/";
    
    private RepositorioAutores repositorioAutores;
    private RepositorioLivros repositorioLivros;
    
  
    public Principal(RepositorioAutores repositorioAutores, RepositorioLivros repositorioLivros) {
        this.repositorioAutores = repositorioAutores;
        this.repositorioLivros = repositorioLivros;
    }
    
    public void exibirMenu() {
        var opcao = -1;
        
        while (opcao != 0) {
            System.out.println("""
                \n=== BIBLIOTECA DIGITAL GUTENBERG ===
                1. Buscar livros por título
                2. Buscar livros por autor
                3. Buscar livros por idioma
                4. Listar top 10 livros mais baixados
                5. Listar todos os autores salvos
                6. Listar todos os livros salvos
                7. Salvar dados da busca no banco
                0. Sair
                """);
            
            System.out.print("Escolha uma opção: ");
            opcao = scan.nextInt();
            scan.nextLine(); 
            
            switch (opcao) {
                case 1:
                    buscarLivrosPorTitulo();
                    break;
                case 2:
                    buscarLivrosPorAutor();
                    break;
                case 3:
                    buscarLivrosPorIdioma();
                    break;
                case 4:
                    listarTop10Downloads();
                    break;
                case 5:
                    listarAutoresSalvos();
                    break;
                case 6:
                    listarLivrosSalvos();
                    break;
                case 7:
                    salvarDadosNoBanco();
                    break;
                case 0:
                    System.out.println("Encerrando sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    
    private void buscarLivrosPorTitulo() {
        System.out.print("Digite o título do livro: ");
        var titulo = scan.nextLine();
        
        var url = URL_BASE + "?search=" + titulo.replace(" ", "%20");
        var json = consumoApi.obterDados(url);
        var dados = conversor.obterDados(json, Dados.class);
        
        if (dados.resultados().isEmpty()) {
            System.out.println("\nNenhum livro encontrado!");
            return;
        }
        
        System.out.println("\n=== LIVROS ENCONTRADOS ===");
        dados.resultados().forEach(livro -> {
            System.out.println("\nTítulo: " + livro.titulo());
            System.out.print("Autor(es): ");
            if (livro.autor() != null && !livro.autor().isEmpty()) {
                livro.autor().forEach(autor -> 
                    System.out.println("  • " + autor.nomeAutor() + 
                                     " (" + autor.anoNascimiento() + 
                                     " - " + autor.anoFalecimento() + ")"));
            } else {
                System.out.println("Desconhecido");
            }
            System.out.println("Idioma(s): " + livro.idiomas());
            System.out.printf("Downloads: %.0f\n", livro.numeroDownloads());
        });
    }
    
    private void buscarLivrosPorAutor() {
        System.out.print("Digite o nome do autor: ");
        var autor = scan.nextLine();
        
        var url = URL_BASE + "?search=" + autor.replace(" ", "%20");
        var json = consumoApi.obterDados(url);
        var dados = conversor.obterDados(json, Dados.class);
        
        if (dados.resultados().isEmpty()) {
            System.out.println("\nNenhum livro encontrado para este autor!");
            return;
        }
        
        System.out.println("\n=== LIVROS DO AUTOR ===");
        dados.resultados().forEach(livro -> {
            System.out.println("\n• " + livro.titulo());
            System.out.printf("  Downloads: %.0f\n", livro.numeroDownloads());
        });
    }
    
    private void buscarLivrosPorIdioma() {
        System.out.println("""
            Idiomas disponíveis:
            en (Inglês), es (Espanhol), fr (Francês), pt (Português),
            de (Alemão), it (Italiano), ru (Russo)
            """);
        System.out.print("Digite o código do idioma (ex: 'pt'): ");
        var idioma = scan.nextLine();
        
        var url = URL_BASE + "?languages=" + idioma;
        var json = consumoApi.obterDados(url);
        var dados = conversor.obterDados(json, Dados.class);
        
        if (dados.resultados().isEmpty()) {
            System.out.println("\nNenhum livro encontrado neste idioma!");
            return;
        }
        
        System.out.println("\n=== LIVROS NO IDIOMA " + idioma.toUpperCase() + " ===");
        dados.resultados().stream()
            .sorted((l1, l2) -> Double.compare(l2.numeroDownloads(), l1.numeroDownloads()))
            .limit(10)
            .forEach(livro -> {
                System.out.printf("\n• %s (%.0f downloads)", 
                    livro.titulo(), livro.numeroDownloads());
            });
    }
    
    private void listarTop10Downloads() {
        var url = URL_BASE + "?sort=popular";
        var json = consumoApi.obterDados(url);
        var dados = conversor.obterDados(json, Dados.class);
        
        System.out.println("\n=== TOP 10 LIVROS MAIS BAIXADOS ===");
        dados.resultados().stream()
            .sorted((l1, l2) -> Double.compare(l2.numeroDownloads(), l1.numeroDownloads()))
            .limit(10)
            .forEach(livro -> {
                System.out.printf("\n• %s - ", livro.titulo());
                if (livro.autor() != null && !livro.autor().isEmpty()) {
                    System.out.print(livro.autor().get(0).nomeAutor());
                }
                System.out.printf(" (%.0f downloads)", livro.numeroDownloads());
            });
    }
    
    private void listarAutoresSalvos() {
        var autores = repositorioAutores.findAll();
        
        if (autores.isEmpty()) {
            System.out.println("\nNenhum autor salvo no banco de dados!");
            return;
        }
        
        System.out.println("\n=== AUTORES SALVOS NO BANCO ===");
        autores.forEach(autor -> {
            System.out.printf("\n• %s (%d - %d)", 
                autor.getNome(), 
                autor.getAnoNascimento(), 
                autor.getAnoFalecimento());
            System.out.printf(" - Livros: %d", autor.getLivros().size());
        });
    }
    
    private void listarLivrosSalvos() {
        var livros = repositorioLivros.findAll();
        
        if (livros.isEmpty()) {
            System.out.println("\nNenhum livro salvo no banco de dados!");
            return;
        }
        
        System.out.println("\n=== LIVROS SALVOS NO BANCO ===");
        livros.forEach(livro -> {
            System.out.printf("\n• %s", livro.getTitulo());
            System.out.printf(" - Autor: %s", livro.getNomeAutor());
            System.out.printf(" - Idioma: %s", livro.getIdioma());
            System.out.printf(" - Downloads: %.0f", livro.getNumeroDownloads());
        });
    }
    
    private void salvarDadosNoBanco() {
        System.out.print("Digite o título para buscar e salvar: ");
        var titulo = scan.nextLine();
        
        var url = URL_BASE + "?search=" + titulo.replace(" ", "%20");
        var json = consumoApi.obterDados(url);
        var dados = conversor.obterDados(json, Dados.class);
        
        if (dados.resultados().isEmpty()) {
            System.out.println("\nNenhum livro encontrado para salvar!");
            return;
        }
        
        int salvos = 0;
        for (DadosLivros dadosLivro : dados.resultados()) {
            if (dadosLivro.autor() != null && !dadosLivro.autor().isEmpty()) {
                for (DadosAutores dadosAutor : dadosLivro.autor()) {
                    var autorExistente = repositorioAutores.findByNome(dadosAutor.nomeAutor());
                    Autores autor;
                    
                    if (autorExistente == null) {
                        autor = new Autores(
                            dadosAutor.nomeAutor(),
                            dadosAutor.anoNascimiento(),
                            dadosAutor.anoFalecimento()
                        );
                        repositorioAutores.save(autor);
                    } else {
                        autor = autorExistente;
                    }
                    
                    var livrosDoAutor = repositorioLivros.findByAutorId(autor.getId());
                    boolean livroJaExiste = livrosDoAutor.stream()
                        .anyMatch(l -> l.getTitulo().equalsIgnoreCase(dadosLivro.titulo()));
                    
                    if (!livroJaExiste) {
                        Livros livro = new Livros();
                        livro.setTitulo(dadosLivro.titulo());
                        livro.setAutor(autor);
                        livro.setNomeAutor(autor.getNome());
                        
                        if (dadosLivro.idiomas() != null && !dadosLivro.idiomas().isEmpty()) {
                            livro.setIdioma(dadosLivro.idiomas().get(0));
                        }
                        
                        livro.setNumeroDownloads(dadosLivro.numeroDownloads());
                        repositorioLivros.save(livro);
                        
           
                        autor.getLivros().add(livro);
                        repositorioAutores.save(autor);
                        
                        salvos++;
                        System.out.printf("✓ Salvo: %s - %s\n", 
                            dadosLivro.titulo(), autor.getNome());
                    }
                }
            }
        }
        
        System.out.printf("\nTotal de %d livros salvos com sucesso!\n", salvos);
    }
}