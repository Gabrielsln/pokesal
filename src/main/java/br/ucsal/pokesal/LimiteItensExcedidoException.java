package br.ucsal.pokesal;

/**
 * Excecao quando o treinador tenta usar mais de 2 itens na batalha.
 */
public class LimiteItensExcedidoException extends RuntimeException {

    /**
     * Construtor da mensagem de erro.
     *
     * @param mensagem Texto do erro.
     */
    public LimiteItensExcedidoException(String mensagem) {
        super(mensagem);
    }
}