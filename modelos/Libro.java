package com.alurachallenge.literalura.modelos;
import jakarta.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true)
        private String titulo;

        @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Set<Author> autores;

        private Double numeroDeDescargas;

        @ElementCollection(targetClass = Idiomas.class, fetch = FetchType.LAZY)
        @CollectionTable(name = "lenguajes", joinColumns = @JoinColumn(name = "libro_id"))
        @Enumerated(EnumType.ORDINAL)
        @Column(name = "lenguaje")
        private Set<Idiomas> idiomas;

        // Constructor por defecto
        public Libro() {}

        public Libro(String titulo, Set<Author> autores, Double numeroDeDescargas, Set<Idiomas> idiomas) {
            this.titulo = titulo;
            this.autores = autores;
            this.numeroDeDescargas = numeroDeDescargas;
            this.idiomas = idiomas;
        }

        @Override
        public String toString() {
            return "Libro \n" +
                    "Titulo: " + titulo + "\n" +
                    "Autor: " + autores.stream().map(Author::getNombreAutor).collect(Collectors.joining(", ")) + "\n" +
                    "Lenguaje: " + idiomas + "\n" +
                    "Número de descargas: " + numeroDeDescargas + "\n";
        }

        // getters and setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public Set<Author> getAutores() {
            return autores;
        }

        public void setAutores(Set<Author> autores) {
            this.autores = autores;
        }

        public Double getNumeroDeDescargas() {
            return numeroDeDescargas;
        }

        public void setNumeroDeDescargas(Double numeroDeDescargas) {
            this.numeroDeDescargas = numeroDeDescargas;
        }

        public Set<Idiomas> getIdiomas() {
            return idiomas;
        }

        public void setIdiomas(Set<Idiomas> idiomas) {
            this.idiomas = idiomas;
        }
}
