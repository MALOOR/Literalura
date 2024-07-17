package com.alurachallenge.literalura.principal;

import com.alurachallenge.literalura.modelos.*;
import com.alurachallenge.literalura.repositorio.AutorRepositorio;
import com.alurachallenge.literalura.repositorio.LibroRepositorio;
import com.alurachallenge.literalura.service.ConsumirApi;
import com.alurachallenge.literalura.service.ConvierteData;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

public class MenuPrincipal {


    private Scanner lectura = new Scanner(System.in);
    private ConsumirApi consumoAPI = new ConsumirApi();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteData conversor = new ConvierteData();
    private Set<Libro> librosEncontrados = new HashSet<>();
    private LibroRepositorio libroRepository;
    private AutorRepositorio autorRepository;
    private Set<Libro> libros;

    @Autowired
    public MenuPrincipal(LibroRepositorio libroRepository, AutorRepositorio autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ******************* Menu *******************
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = lectura.nextInt();
            lectura.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    autorAnoConcreto();
                    break;
                case 5:
                    librosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private DataLibro getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = lectura.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        Data datos = conversor.getData(json, Data.class);

        if (datos.resultados().isEmpty()) {
            throw new RuntimeException("No se encontraron libros para el título dado.");
        }

        DataLibro datosLibro = datos.resultados().get(0); // obtener el primer libro de los resultados
        return new DataLibro(
                datosLibro.id(),
                datosLibro.titulo(),
                datosLibro.autores(),
                datosLibro.numeroDeDescargas(),
                datosLibro.idiomas()
        );
    }

    private void buscarLibroPorTitulo() {
        DataLibro datos = getDatosLibro();
        Set<Author> autores = datos.autores().stream()
                .map(datoAutor -> new Author(datoAutor.nombre(), datoAutor.fechaNacimiento(), datoAutor.fechaFallecimiento()))
                .collect(Collectors.toSet());
        Libro libro = new Libro(datos.titulo(), autores, datos.numeroDeDescargas(), new HashSet<>(datos.idiomas()));
        libroRepository.save(libro);
        librosEncontrados.add(libro);
        System.out.println(libro.toString());
    }

    @Transactional(readOnly = true)
    private void listarLibros() {
        libros = new HashSet<>(libroRepository.findAll());

        // Inicializa las colecciones de cada libro
        libros.forEach(libro -> {
            libroRepository.findByIdWithAutoresAndLenguajes(libro.getId()).ifPresent(libroConAutores -> {
                libro.setAutores(libroConAutores.getAutores());
                libro.setIdiomas(libroConAutores.getIdiomas());
            });
        });

        // Comparador para ordenar libros por el nombre del primer autor
        Comparator<Libro> comparador = Comparator.comparing(libro ->
                libro.getAutores().isEmpty() ? "" : libro.getAutores().iterator().next().getNombreAutor()
        );

        libros.stream()
                .sorted(comparador)
                .forEach(System.out::println);
    }


    @Transactional(readOnly = true)
    private void listarAutores() {
        List<Author> autores = autorRepository.findAllWithLibros();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados");
        } else {
            autores.forEach(autor -> {
                System.out.println("Nombre: " + autor.getNombreAutor());
                System.out.println("Fecha de Nacimiento: " + autor.getFechaDeNacimiento());
                System.out.println("Fecha de Fallecimiento: " + (autor.getFechaDeFallecimiento() != null ? autor.getFechaDeFallecimiento() : "N/A"));
                if (autor.getLibro() != null) {
                    System.out.println("Libro: " + autor.getLibro().getTitulo());
                } else {
                    System.out.println("Libro: N/A");
                }
                System.out.println();
            });
        }
    }

    @Transactional(readOnly = true)
    private void autorAnoConcreto() {
        System.out.println("Por favor escribe el año que desees buscar:");
        var anio = lectura.nextInt();
        lectura.nextLine();
        List<Author> autors = autorRepository.findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(anio);
        if (autors.isEmpty()) {
            System.out.println("No se pudo encontrar a un autor que haya vivido en el año:  " + anio);
        } else {
            autors.forEach(autor -> {
                System.out.println("Nombre: " + autor.getNombreAutor());
                System.out.println("Fecha de Nacimiento: " + autor.getFechaDeNacimiento());
                System.out.println("Fecha de Fallecimiento: " + (autor.getFechaDeFallecimiento() != null ? autor.getFechaDeFallecimiento() : "N/A"));
                System.out.println("Libro: " + (autor.getLibro() != null ? autor.getLibro().getTitulo() : "N/A"));
                System.out.println();
            });
        }
    }

    @Transactional(readOnly = true)
    private void librosPorIdioma() {
        System.out.println("Por favor ingrese el idioma en el cual desea buscar los libros (es, en, fr, pt, etc.):");
        var idiomaInput = lectura.nextLine().toLowerCase();

        try {
            Idiomas idiomas = Idiomas.fromString(idiomaInput);
            List<Libro> libros = libroRepository.findByIdioma(idiomas);

            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros en el idioma: " + idiomaInput);
            } else {
                libros.forEach(libro -> {
                    Hibernate.initialize(libro.getAutores());
                    Hibernate.initialize(libro.getIdiomas());

                    System.out.println(
                            "------ LIBRO ------" +
                                    "\nTitulo: " + libro.getTitulo() +
                                    "\nAutores: " + libro.getAutores().stream().map(Author::getNombreAutor).collect(Collectors.joining(", ")) +
                                    "\nIdiomas: " + libro.getIdiomas().stream().map(Idiomas::getDescripcion).collect(Collectors.joining(", ")) +
                                    "\nNúmero de descargas: " + libro.getNumeroDeDescargas() +
                                    "\n------------------"
                    );
                });
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Idioma no reconocido: " + idiomaInput);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MenuPrincipal.class, args);
    }
}
