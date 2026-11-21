package br.ucsal.pokesal.servicos;

import br.ucsal.pokesal.Treinador;

/**
 * Servico de consulta e atualizacao do ranking de vitorias (Requisito Autoral 5).
 */
public interface ServicoRanking {

    /**
     * Consulta quantas vitorias consecutivas o treinador possui.
     *
     * @param treinador Treinador consultado.
     * @return Quantidade de vitorias seguidas.
     */
    int consultarVitoriasConsecutivas(Treinador treinador);

    /**
     * Registra uma nova vitoria para o treinador no ranking.
     *
     * @param treinador Treinador vencedor.
     */
    void registrarVitoria(Treinador treinador);
}
