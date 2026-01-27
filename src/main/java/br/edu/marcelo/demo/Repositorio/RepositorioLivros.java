package br.edu.marcelo.demo.Repositorio;

import br.edu.marcelo.demo.Modelo.Livros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioLivros extends JpaRepository<Livros, Long> {
    List<Livros> findByTituloContainingIgnoreCase(String titulo);
    List<Livros> findByAutorId(Long autorId);
}