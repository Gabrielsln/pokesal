package br.ucsal.pokesal;

/**
 * Enum explicando os tipos dos pokésals e cada benefício de cada um.
 */
public enum TipoElemental {
    FOGO,
    AGUA,
    PLANTA;

    public static final double DANO_SUPER_EFETIVO = 2.0;
    public static final double DANO_POUCO_EFETIVO = 0.5;
    public static final double DANO_NEUTRO = 1.0;

    /**
     * Diz o multiplicador de dano com base nas fraquezas de cada.
     *
     * @param tipoAlvo Tipo de elemento de quem defende.
     * @return Multiplicador de dano com base no golpe.
     */
    public double getMultiplicador(TipoElemental tipoAlvo) {
        if (this == FOGO) {
            if (tipoAlvo == PLANTA) {
                return DANO_SUPER_EFETIVO;
            }
            if (tipoAlvo == AGUA) {
                return DANO_POUCO_EFETIVO;
            }
        } else if (this == AGUA) {
            if (tipoAlvo == FOGO) {
                return DANO_SUPER_EFETIVO;
            }
            if (tipoAlvo == PLANTA) {
                return DANO_POUCO_EFETIVO;
            }
        } else if (this == PLANTA) {
            if (tipoAlvo == AGUA) {
                return DANO_SUPER_EFETIVO;
            }
            if (tipoAlvo == FOGO) {
                return DANO_POUCO_EFETIVO;
            }
        }
        return DANO_NEUTRO;
    }
}