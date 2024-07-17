package com.alurachallenge.literalura.repositorio;

import com.alurachallenge.literalura.modelos.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepositorio extends JpaRepository<Author, Long> {


    Optional<Author> findByNombreAutor(String nombreAutor);

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libro")
    List<Author> findAllWithLibros();

    @Query("SELECT a FROM Autor a WHERE LOWER(a.nombreAutor) LIKE LOWER(CONCAT('%', :nombreAutor, '%'))")
    List<Author> buscarAutorPorNombre(@Param("nombreAutor") String nombreAutor);

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libro WHERE a.fechaDeNacimiento <= :anio AND (a.fechaDeFallecimiento >= :anio OR a.fechaDeFallecimiento IS NULL)")
    List<Author> findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(@Param("anio") int anio);

}
