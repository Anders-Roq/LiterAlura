package br.edu.marcelo.demo.Modelo;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "livros")
public class Livros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autores autor;

    @Column(name = "nome_autor")
    private String nomeAutor;

    @Column(name = "idioma")
    private String idioma;

    private double numeroDownloads;

    public Livros() {
    }

    public Livros(DadosLivros dadoslivros, Autores autor) {
        this.titulo = dadoslivros.titulo();
        this.autor = autor;
        this.nomeAutor = autor.getNome();
        setIdiomas(dadoslivros.idiomas());
        this.numeroDownloads = dadoslivros.numeroDownloads();
    }

    public void setIdiomas(List<String> idiomas) {
        if (idiomas != null && !idiomas.isEmpty()) {
            this.idioma = idiomas.get(0);
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autores getAutor() {
        return autor;
    }

    public void setAutor(Autores autor) {
        this.autor = autor;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public double getNumeroDownloads() {
        return numeroDownloads;
    }

    public void setNumeroDownloads(double numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }

}
