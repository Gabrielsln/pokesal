package br.ucsal.pokesal.servicos;

/**
 * Interface para encapsular a geracao de numeros aleatorios nas batalhas.
 */
public interface GeradorAleatorio {

    /**
     * Gera um valor percentual no intervalo [0.0, 100.0).
     *
     * @return Valor percentual gerado.
     */
    double gerarPercentual();
}
