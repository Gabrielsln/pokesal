package br.ucsal.pokesal;

/**
 * Classe do treinador.
 */
public class Treinador {

    public static final int LIMITE_ITENS = 2;

    private final String nome;
    private final Pokesal pokesal;
    private int itensUsados;

    /**
     * Construtor do treinador e seu Pokesal inicial.
     *
     * @param nome Nome do treinador.
     * @param pokesal Pokesal inicial selecionado.
     */
    public Treinador(String nome, Pokesal pokesal) {
        this.nome = nome;
        this.pokesal = pokesal;
        this.itensUsados = 0;
    }

    /**
     * Usa um item de combate respeitando o limite de 2 itens.
     *
     * @param item Item a ser usado.
     */
    public void usarItem(Item item) {
        if (this.itensUsados >= LIMITE_ITENS) {
            throw new LimiteItensExcedidoException("Limite maximo de 2 itens atingido.");
        }

        if (item == Item.CURA) {
            this.pokesal.setEfeito(Efeito.NENHUM);
        } else {
            this.pokesal.curar(item.getValorCura());
        }
        this.itensUsados++;
    }

    public String getNome() {
        return nome;
    }

    public Pokesal getPokesal() {
        return pokesal;
    }

    public int getItensUsados() {
        return itensUsados;
    }
}