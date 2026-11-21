package br.ucsal.pokesal;

/**
 * Classe que detalha um Pokesal completo.
 */
public class Pokesal {

    public static final int DANO_MINIMO = 1;
    public static final int ATRIBUTO_MINIMO = 1;
    public static final double FATOR_DESESPERO_DANO = 1.30;
    public static final double FATOR_LIMIAR_DESESPERO = 0.25;
    public static final int PENALIDADE_SPD_ASFALTO = 5;
    public static final double FATOR_AUTODANO = 0.10;
    public static final double MULTIPLICADOR_GOLPE_CARREGADO = 1.4;
    public static final double FATOR_ATAQUE_QUEIMADO = 0.80;
    public static final double FATOR_VELOCIDADE_PARALISADO = 0.50;

    private final String nome;
    private final TipoElemental tipo;
    private final int hpMax;
    private int hpAtual;
    private final int ataque;
    private final int defesa;
    private int velocidade;
    private Efeito efeito;

    /**
     * Construtor com os atributos do Pokesal.
     *
     * @param nome Nome do Pokesal.
     * @param tipo Tipo do elemento.
     * @param hp Pontos de vida.
     * @param ataque Poder de ataque.
     * @param defesa Poder de defesa.
     * @param velocidade Velocidade de iniciativa.
     */
    public Pokesal(String nome, TipoElemental tipo, int hp,
                   int ataque, int defesa, int velocidade) {
        this.nome = nome;
        this.tipo = tipo;
        this.hpMax = hp;
        this.hpAtual = hp;
        this.ataque = ataque;
        this.defesa = defesa;
        this.velocidade = velocidade;
        this.efeito = Efeito.NENHUM;
    }

    /**
     * Indica se o pokesal ainda tem pontos de vida.
     *
     * @return true se o HP for maior que zero.
     */
    public boolean isVivo() {
        return this.hpAtual > 0;
    }

    /**
     * Aplica dano, sem deixar o HP negativo.
     *
     * @param dano Quantidade de pontos de vida a diminuir.
     */
    public void receberDano(int dano) {
        this.hpAtual -= dano;
        if (this.hpAtual < 0) {
            this.hpAtual = 0;
        }
    }

    /**
     * Recupera vida respeitando o limite maximo de HP.
     *
     * @param valorCura Quantidade de pontos curados.
     */
    public void curar(int valorCura) {
        this.hpAtual += valorCura;
        if (this.hpAtual > this.hpMax) {
            this.hpAtual = this.hpMax;
        }
    }

    /**
     * Restaura o HP para o valor maximo e limpa efeitos de status.
     */
    public void restaurarParaProximaFase() {
        this.hpAtual = this.hpMax;
        this.efeito = Efeito.NENHUM;
    }

    /**
     * Regra 1: Golpe Carregado.
     * Causa dano aumentado com base no ataque, mas consome 10% do proprio HP.
     *
     * @param alvo Pokesal que recebera o golpe.
     */
    public void executarGolpeCarregado(Pokesal alvo) {
        int danoBase = (int) (getAtaqueEfetivo() * MULTIPLICADOR_GOLPE_CARREGADO)
                - alvo.getDefesa();
        if (danoBase < DANO_MINIMO) {
            danoBase = DANO_MINIMO;
        }
        alvo.receberDano(danoBase);
        int danoProprio = (int) (this.hpMax * FATOR_AUTODANO);
        this.receberDano(danoProprio);
    }

    /**
     * Regra 2: Desespero.
     * Da 30% a mais de dano se a vida estiver em 25% ou menos.
     *
     * @return Multiplicador de bonus de desespero.
     */
    public double verificarBonusDesespero() {
        if (this.hpAtual <= (this.hpMax * FATOR_LIMIAR_DESESPERO)) {
            return FATOR_DESESPERO_DANO;
        }
        return 1.0;
    }

    /**
     * Regra 3: Quente Demais!
     * Pokesal de Agua perde 5 pontos de velocidade no asfalto quente.
     *
     * @param terreno Terreno atual da arena.
     */
    public void aplicarEfeitoTerrenoQuente(Terreno terreno) {
        if (this.tipo == TipoElemental.AGUA && terreno == Terreno.ASFALTO_QUENTE) {
            this.velocidade = Math.max(ATRIBUTO_MINIMO,
                    this.velocidade - PENALIDADE_SPD_ASFALTO);
        }
    }

    /**
     * Calcula o ataque efetivo considerando o efeito Queimado.
     *
     * @return Ataque efetivo no turno.
     */
    public int getAtaqueEfetivo() {
        if (this.efeito == Efeito.QUEIMADO) {
            return Math.max(ATRIBUTO_MINIMO, (int) (this.ataque * FATOR_ATAQUE_QUEIMADO));
        }
        return this.ataque;
    }

    /**
     * Calcula a velocidade efetiva considerando o efeito Paralisado.
     *
     * @return Velocidade efetiva no turno.
     */
    public int getVelocidadeEfetiva() {
        if (this.efeito == Efeito.PARALISADO) {
            return Math.max(ATRIBUTO_MINIMO,
                    (int) (this.velocidade * FATOR_VELOCIDADE_PARALISADO));
        }
        return this.velocidade;
    }

    public String getNome() {
        return nome;
    }

    public TipoElemental getTipo() {
        return tipo;
    }

    public int getHpAtual() {
        return hpAtual;
    }

    public int getHpMax() {
        return hpMax;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public Efeito getEfeito() {
        return efeito;
    }

    public void setEfeito(Efeito efeito) {
        this.efeito = efeito;
    }
}
