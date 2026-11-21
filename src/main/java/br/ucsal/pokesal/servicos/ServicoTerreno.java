package br.ucsal.pokesal.servicos;

import br.ucsal.pokesal.Terreno;

/**
 * Servico que define o terreno ativo considerando clima e horario.
 */
public interface ServicoTerreno {

    /**
     * Obtem o terreno atual da arena.
     *
     * @return Terreno ativo no momento da batalha.
     */
    Terreno obterTerrenoAtual();
}
