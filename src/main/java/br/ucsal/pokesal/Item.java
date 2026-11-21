package br.ucsal.pokesal;

/**
 * Itens que podem ser utilizados pelos treinadores durante a batalha.
 */
public enum Item {
    POCAO(20),
    SUPER_POCAO(50),
    CURA(0);

    private final int valorCura;

    Item(int valorCura) {
        this.valorCura = valorCura;
    }

    public int getValorCura() {
        return valorCura;
    }
}