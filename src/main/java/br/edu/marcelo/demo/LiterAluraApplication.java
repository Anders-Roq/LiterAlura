package br.edu.marcelo.demo;

import br.edu.marcelo.demo.Principal.Principal;
import br.edu.marcelo.demo.Repositorio.RepositorioAutores;
import br.edu.marcelo.demo.Repositorio.RepositorioLivros;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

    @Autowired
    private RepositorioAutores repositorioAutores;
    
    @Autowired
    private RepositorioLivros repositorioLivros;

    public static void main(String[] args) {
        SpringApplication.run(LiterAluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(repositorioAutores, repositorioLivros);
        principal.exibirMenu(); 
    }
}