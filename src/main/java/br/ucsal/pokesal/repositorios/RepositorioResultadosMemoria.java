package br.ucsal.pokesal.repositorios;

import br.ucsal.pokesal.ResultadoBatalha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementacao em memoria do repositorio de resultados de batalhas.
 */
public class RepositorioResultadosMemoria implements RepositorioResultados {

    private final List<ResultadoBatalha> resultados = new ArrayList<>();

    @Override
    public void salvar(ResultadoBatalha resultado) {
        resultados.add(resultado);
    }

    /**
     * Retorna os resultados armazenados.
     *
     * @return Lista de resultados.
     */
    public List<ResultadoBatalha> listarTodos() {
        return Collections.unmodifiableList(resultados);
    }
}
