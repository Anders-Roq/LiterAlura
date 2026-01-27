package br.edu.marcelo.demo.Repositorio;

import br.edu.marcelo.demo.Modelo.Autores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioAutores extends JpaRepository<Autores, Long> {
    Autores findByNome(String nome);
}