package br.ucsal.pokesal.servicos;

import br.ucsal.pokesal.Treinador;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementacao em memoria do servico de ranking de vitorias consecutivas.
 */
public class ServicoRankingMemoria implements ServicoRanking {

    private final Map<String, Integer> vitoriasPorTreinador = new HashMap<>();

    @Override
    public int consultarVitoriasConsecutivas(Treinador treinador) {
        return vitoriasPorTreinador.getOrDefault(treinador.getNome(), 0);
    }

    @Override
    public void registrarVitoria(Treinador treinador) {
        int atual = consultarVitoriasConsecutivas(treinador);
        vitoriasPorTreinador.put(treinador.getNome(), atual + 1);
    }
}
