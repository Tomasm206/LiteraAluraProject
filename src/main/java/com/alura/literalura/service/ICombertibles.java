package com.alura.literalura.service;

public interface ICombertibles {
    <T> T obtenerDatos(String json, Class<T> clase);
}
