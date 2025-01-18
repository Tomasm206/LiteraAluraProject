package com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libro")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String titulo;
    private String idioma;
    private String numDescargas;

    @ManyToOne
    private Autor autor;

    public Libro(String titulo, String idioma, String numDescargas) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.numDescargas = numDescargas;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getNumDescargas() {
        return numDescargas;
    }

    public void setNumDescargas(String numDescargas) {
        this.numDescargas = numDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "---------- Libro --------------\\n" +
                ", titulo='" + titulo + '\'' +
                ", idioma='" + idioma + '\'' +
                ", numDescargas='" + numDescargas + '\'' +
                ", autor=" + autor +
                "-----------------------------";
    }
}
