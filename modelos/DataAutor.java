package com.alurachallenge.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataAutor(@JsonAlias("name") String nombre,
                        @JsonAlias("birth_year") Integer fechaNacimiento,
                        @JsonAlias("death_year") Integer fechaFallecimiento) {
}
