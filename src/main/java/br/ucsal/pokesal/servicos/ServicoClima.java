package br.ucsal.pokesal.servicos;

import br.ucsal.pokesal.Clima;

/**
 * Servico responsavel que informa o clima atual do estacionamento.
 */
public interface ServicoClima {

    /**
     * Consulta o clima atual.
     *
     * @return Clima atual.
     */
    Clima obterClimaAtual();
}
