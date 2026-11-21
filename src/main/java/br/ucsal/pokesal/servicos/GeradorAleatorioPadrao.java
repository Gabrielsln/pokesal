package br.ucsal.pokesal.servicos;

import java.util.Random;

/**
 * Implementacao padrao do gerador aleatorio utilizando java.util.Random.
 */
public class GeradorAleatorioPadrao implements GeradorAleatorio {

    private static final double ESCALA_PERCENTUAL = 100.0;

    private final Random random;

    /**
     * Construtor que recebe a instancia de Random.
     *
     * @param random Instancia de Random a ser utilizada.
     */
    public GeradorAleatorioPadrao(Random random) {
        this.random = random;
    }

    @Override
    public double gerarPercentual() {
        return random.nextDouble() * ESCALA_PERCENTUAL;
    }
}
