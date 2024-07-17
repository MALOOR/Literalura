package com.alurachallenge.literalura.repositorio;

import com.alurachallenge.literalura.modelos.Idiomas;
import com.alurachallenge.literalura.modelos.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepositorio  extends JpaRepository<Libro, Long> {

    @Query("SELECT l FROM Libro l JOIN FETCH l.lenguajes lenguajes JOIN FETCH l.autores WHERE lenguajes = :idioma")
    List<Libro> findByIdioma(@Param("idioma")Idiomas idioma);

    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.autores LEFT JOIN FETCH l.lenguajes WHERE l.id = :id")
    Optional<Libro> findByIdWithAutoresAndLenguajes(@Param("id") Long id);

    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.autores LEFT JOIN FETCH l.lenguajes")
    List<Libro> findAllWithAutoresAndLenguajes();
}
