package com.alurachallenge.literalura.modelos;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Arrays;

public class DeserializerIdiomas extends JsonDeserializer<Idiomas> {


    @Override
    public Idiomas deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String clave =  jsonParser.getText().toUpperCase().trim();

        return Arrays.stream(Idiomas.values()).filter(idiomas -> idiomas.getIdiomas().equalsIgnoreCase(clave)).findFirst().orElseThrow(() ->new IllegalArgumentException("No enum constant com.alurachallenge.literalura.modelos.Idiomas." + clave));
    }
}
