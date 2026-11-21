package br.ucsal.pokesal.registros;

import br.ucsal.pokesal.Efeito;
import br.ucsal.pokesal.Golpe;
import br.ucsal.pokesal.Item;
import br.ucsal.pokesal.Pokesal;
import br.ucsal.pokesal.Terreno;
import br.ucsal.pokesal.Treinador;

/**
 * Interface que registra cada evento ocorrido durante os turnos.
 */
public interface RegistroBatalha {

    /**
     * Registra o inicio de um ataque.
     *
     * @param atacante Pokesal atacante.
     * @param defensor Pokesal defensor.
     * @param golpe Golpe utilizado.
     */
    void registrarAtaque(Pokesal atacante, Pokesal defensor, Golpe golpe);

    /**
     * Registra o dano aplicado no defensor.
     *
     * @param defensor Pokesal atingido.
     * @param dano Valor de dano aplicado.
     * @param critico Indica se houve acerto critico.
     */
    void registrarDano(Pokesal defensor, int dano, boolean critico);

    /**
     * Registra o processamento de efeito de status no turno.
     *
     * @param pokesal Pokesal afetado.
     * @param efeito Efeito de status atual.
     */
    void registrarEfeitoStatus(Pokesal pokesal, Efeito efeito);

    /**
     * Registra o efeito de terreno processado no turno.
     *
     * @param pokesal Pokesal na arena.
     * @param terreno Terreno ativo.
     */
    void registrarEfeitoTerreno(Pokesal pokesal, Terreno terreno);

    /**
     * Registra o uso de um item por um treinador.
     *
     * @param treinador Treinador que usou o item.
     * @param item Item utilizado.
     */
    void registrarUsoItem(Treinador treinador, Item item);

    /**
     * Registra uma falha sem interromper o combate.
     *
     * @param mensagem Descricao do erro ocorrido.
     */
    void registrarErro(String mensagem);
}
