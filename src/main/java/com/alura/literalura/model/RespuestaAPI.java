package com.alura.literalura.model;

import com.alura.literalura.dto.LibroDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RespuestaAPI {
    @JsonAlias("results") List<LibroDTO> results;

    // Getters y Setters
    public List<LibroDTO> getResults() {
        return results;
    }

    public void setResults(List<LibroDTO> results) {
        this.results = results;
    }
}
