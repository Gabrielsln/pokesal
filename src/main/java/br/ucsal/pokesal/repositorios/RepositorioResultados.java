package br.ucsal.pokesal.repositorios;

import br.ucsal.pokesal.ResultadoBatalha;

/**
 * interface de persistencia dos resultados de batalhas do torneio.
 */
public interface RepositorioResultados {

    /**
     * Salva o resultado de uma batalha finalizada.
     *
     * @param resultado Resultado da batalha.
     */
    void salvar(ResultadoBatalha resultado);
}
