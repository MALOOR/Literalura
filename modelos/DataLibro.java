package com.alurachallenge.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record DataLibro(@JsonAlias("id") Long id,
                        @JsonAlias("title") String titulo,
                        @JsonAlias("authors") List<DataAutor> autores,
                        @JsonAlias("download_count") Double numeroDeDescargas,
                        @JsonAlias("languages") List<Idiomas> idiomas) {
}
