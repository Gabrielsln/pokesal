package br.ucsal.pokesal.servicos;

import br.ucsal.pokesal.Golpe;
import br.ucsal.pokesal.ResultadoBatalha;
import br.ucsal.pokesal.Treinador;

/**
 * interface de execucao de uma batalha completa entre dois treinadores.
 */
public interface ExecutorBatalha {

    /**
     * Executa uma batalha completa ate a definicao de um vencedor.
     *
     * @param treinador1 Primeiro treinador.
     * @param treinador2 Segundo treinador.
     * @param golpe1 Golpe utilizado pelo primeiro treinador.
     * @param golpe2 Golpe utilizado pelo segundo treinador.
     * @return Resultado consolidado da batalha.
     */
    ResultadoBatalha executarBatalha(Treinador treinador1, Treinador treinador2,
                                     Golpe golpe1, Golpe golpe2);
}
