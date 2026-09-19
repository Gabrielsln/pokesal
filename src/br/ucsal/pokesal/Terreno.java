package br.ucsal.pokesal;

/**
 * Classe que mostra os efeitos de luta no estacionamento da UCSal.
 */
public enum Terreno {
    ASFALTO_QUENTE,
    POCA_CHUVA,
    CANTEIRO_CENTRAL;

    public static final double BONUS_DANO_FOGO = 1.15;
    public static final double BONUS_DANO_AGUA = 1.10;
    public static final double FATOR_CURA_PLANTA = 0.05;
}