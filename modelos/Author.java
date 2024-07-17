package com.alurachallenge.literalura.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "autores")

public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombreAutor;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_id")
    private Libro libro;

    public Author () {}

    public Author(DataAutor dataAutor) {
        this.nombreAutor = dataAutor.nombre();
        this.fechaDeNacimiento = dataAutor.fechaNacimiento();
        this.fechaDeFallecimiento = dataAutor.fechaFallecimiento();
    }

    public Author(String nombreAutor, Integer fechaDeNacimiento, Integer fechaDeFallecimiento) {
        this.nombreAutor = nombreAutor;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    @Override
    public String toString() {
        return "*********** Autor ***********\n" +
                "Nombre: " + nombreAutor + "\n" +
                "Año de nacimiento: " + fechaDeNacimiento + "\n" +
                "Año de fallecimiento: " + fechaDeFallecimiento + "\n" +
                "Libro: " + libro + "\n";
    }

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}
