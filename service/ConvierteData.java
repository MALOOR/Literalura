package com.alurachallenge.literalura.service;

import com.fasterxml.jackson.databind.ObjectMapper;
public class ConvierteData implements InterfaceData{

    private ObjectMapper objectMapper;

    @Override
    public <T> T getData(String json, Class<T> clase) {
        return null;
    }
}
