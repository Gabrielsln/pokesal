package br.ucsal.pokesal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Armazena o resumo final de uma batalha concluida.
 */
public class ResultadoBatalha {

    private final Treinador vencedor;
    private final Treinador perdedor;
    private final int numeroTurnos;
    private final List<Pokesal> pokesalsUtilizados;

    /**
     * Construtor com os dados consolidados da batalha.
     *
     * @param vencedor Treinador vencedor.
     * @param perdedor Treinador perdedor.
     * @param numeroTurnos Quantidade total de turnos disputados.
     * @param pokesalsUtilizados Lista de Pokesals que participaram do combate.
     */
    public ResultadoBatalha(Treinador vencedor, Treinador perdedor,
                            int numeroTurnos, List<Pokesal> pokesalsUtilizados) {
        this.vencedor = vencedor;
        this.perdedor = perdedor;
        this.numeroTurnos = numeroTurnos;
        this.pokesalsUtilizados = new ArrayList<>(pokesalsUtilizados);
    }

    /**
     * Retorna o treinador vencedor da batalha.
     *
     * @return Treinador vencedor.
     */
    public Treinador getVencedor() {
        return vencedor;
    }

    /**
     * Retorna o treinador perdedor da batalha.
     *
     * @return Treinador perdedor.
     */
    public Treinador getPerdedor() {
        return perdedor;
    }

    /**
     * Retorna o total de turnos da batalha.
     *
     * @return Numero de turnos.
     */
    public int getNumeroTurnos() {
        return numeroTurnos;
    }

    /**
     * Retorna lista imutavel dos Pokesals utilizados na batalha.
     *
     * @return Lista de Pokesals.
     */
    public List<Pokesal> getPokesalsUtilizados() {
        return Collections.unmodifiableList(pokesalsUtilizados);
    }
}
