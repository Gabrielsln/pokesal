package br.ucsal.pokesal.registros;

import br.ucsal.pokesal.Efeito;
import br.ucsal.pokesal.Golpe;
import br.ucsal.pokesal.Item;
import br.ucsal.pokesal.Pokesal;
import br.ucsal.pokesal.Terreno;
import br.ucsal.pokesal.Treinador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementacao em memoria do registro de eventos de batalha.
 */
public class RegistroBatalhaMemoria implements RegistroBatalha {

    private final List<String> eventos = new ArrayList<>();

    @Override
    public void registrarAtaque(Pokesal atacante, Pokesal defensor, Golpe golpe) {
        eventos.add("ATAQUE: " + atacante.getNome() + " usou " + golpe.getNome()
                + " em " + defensor.getNome());
    }

    @Override
    public void registrarDano(Pokesal defensor, int dano, boolean critico) {
        String sufixo = critico ? " (CRITICO)" : "";
        eventos.add("DANO: " + defensor.getNome() + " recebeu " + dano + sufixo);
    }

    @Override
    public void registrarEfeitoStatus(Pokesal pokesal, Efeito efeito) {
        eventos.add("STATUS: " + pokesal.getNome() + " com efeito " + efeito);
    }

    @Override
    public void registrarEfeitoTerreno(Pokesal pokesal, Terreno terreno) {
        eventos.add("TERRENO: " + pokesal.getNome() + " no terreno " + terreno);
    }

    @Override
    public void registrarUsoItem(Treinador treinador, Item item) {
        eventos.add("ITEM: " + treinador.getNome() + " utilizou " + item);
    }

    @Override
    public void registrarErro(String mensagem) {
        eventos.add("ERRO: " + mensagem);
    }

    /**
     * Retorna a lista de eventos que foram registrados.
     *
     * @return Lista de mensagens registradas.
     */
    public List<String> getEventos() {
        return Collections.unmodifiableList(eventos);
    }
}
