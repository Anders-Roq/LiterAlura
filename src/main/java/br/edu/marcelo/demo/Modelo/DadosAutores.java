package br.edu.marcelo.demo.Modelo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosAutores(@JsonAlias("name") String nomeAutor,
                           @JsonAlias("birth_year") int anoNascimiento,
                           @JsonAlias("death_year") int anoFalecimento) {
    
}
