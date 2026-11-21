package br.ucsal.pokesal;

/**
 * Representa um golpe utilizado por um Pokesal durante a batalha.
 */
public class Golpe {

    public static final int PRECISAO_MINIMA = 0;
    public static final int PRECISAO_MAXIMA = 100;

    private final String nome;
    private final TipoElemental tipo;
    private final int precisao;

    /**
     * Cria um golpe com nome, tipo elemental e precisao entre 0 e 100.
     *
     * @param nome Nome do golpe.
     * @param tipo Tipo elemental do golpe.
     * @param precisao Chance de acerto de 0 a 100.
     */
    public Golpe(String nome, TipoElemental tipo, int precisao) {
        if (precisao < PRECISAO_MINIMA || precisao > PRECISAO_MAXIMA) {
            throw new IllegalArgumentException(
                    "Precisao do golpe deve estar entre 0 e 100.");
        }
        this.nome = nome;
        this.tipo = tipo;
        this.precisao = precisao;
    }

    /**
     * Retorna o nome do golpe.
     *
     * @return Nome do golpe.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o tipo elemental do golpe.
     *
     * @return Tipo elemental.
     */
    public TipoElemental getTipo() {
        return tipo;
    }

    /**
     * Retorna a precisao do golpe (0 a 100).
     *
     * @return Valor de precisao.
     */
    public int getPrecisao() {
        return precisao;
    }
}
