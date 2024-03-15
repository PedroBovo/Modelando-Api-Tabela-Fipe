package br.com.pedro.fipe.sevice;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IConverteDados {
    <T> T converteDados (String json, Class<T> classe) throws JsonProcessingException;

    <T> List<T> obterLista(String json, Class<T> classe);
}
