package br.ucsal.pokesal;

/**
 * Classe que gerencia acoes e logica de combate.
 */
public class Batalha {

    public static final int DANO_MINIMO = 1;
    public static final int DANO_QUEIMADURA = 6;
    public static final int DANO_ENVENENAMENTO = 8;

    /**
     * Calcula o dano causado pelo atacante no defensor.
     *
     * @param atacante Pokesal que ataca.
     * @param defensor Pokesal que recebe o golpe.
     * @param terreno Lugar onde a batalha esta acontecendo.
     * @return Valor numerico inteiro do dano final.
     */
    public int calcularDano(Pokesal atacante, Pokesal defensor, Terreno terreno) {
        double base = atacante.getAtaque() - defensor.getDefesa();
        if (base < DANO_MINIMO) {
            base = DANO_MINIMO;
        }

        double multTipo = atacante.getTipo().getMultiplicador(defensor.getTipo());
        double multTerreno = 1.0;

        if (terreno == Terreno.ASFALTO_QUENTE && atacante.getTipo() == TipoElemental.FOGO) {
            multTerreno = Terreno.BONUS_DANO_FOGO;
        } else if (terreno == Terreno.POCA_CHUVA && atacante.getTipo() == TipoElemental.AGUA) {
            multTerreno = Terreno.BONUS_DANO_AGUA;
        }

        double bonusDesespero = atacante.verificarBonusDesespero();
        int danoFinal = (int) (base * multTipo * multTerreno * bonusDesespero);

        return Math.max(danoFinal, DANO_MINIMO);
    }

    /**
     * Faz uma acao de ataque entre os combatentes.
     *
     * @param atacante Pokesal atacante.
     * @param defensor Pokesal que vai ser atingido.
     * @param terreno Lugar do confronto.
     */
    public void realizarAtaque(Pokesal atacante, Pokesal defensor, Terreno terreno) {
        int dano = calcularDano(atacante, defensor, terreno);
        defensor.receberDano(dano);
    }

    /**
     * Usa recuperacao do Canteiro Central e de danos de status.
     *
     * @param pokesal Pokesal a ser processado no final do turno.
     * @param terreno Terreno da arena.
     */
    public void processarFinalTurno(Pokesal pokesal, Terreno terreno) {
        if (!pokesal.isVivo()) {
            return;
        }

        if (terreno == Terreno.CANTEIRO_CENTRAL && pokesal.getTipo() == TipoElemental.PLANTA) {
            int cura = (int) (pokesal.getHpMax() * Terreno.FATOR_CURA_PLANTA);
            pokesal.curar(cura);
        }

        if (pokesal.getEfeito() == Efeito.QUEIMADO) {
            pokesal.receberDano(DANO_QUEIMADURA);
        } else if (pokesal.getEfeito() == Efeito.ENVENENADO) {
            pokesal.receberDano(DANO_ENVENENAMENTO);
        }
    }
}