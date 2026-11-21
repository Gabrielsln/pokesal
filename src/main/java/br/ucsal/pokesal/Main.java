package br.ucsal.pokesal;

import br.ucsal.pokesal.registros.RegistroBatalhaMemoria;
import br.ucsal.pokesal.repositorios.RepositorioResultadosMemoria;
import br.ucsal.pokesal.servicos.GeradorAleatorio;
import br.ucsal.pokesal.servicos.GeradorAleatorioPadrao;
import br.ucsal.pokesal.servicos.ServicoClima;
import br.ucsal.pokesal.servicos.ServicoNotificacao;
import br.ucsal.pokesal.servicos.ServicoRanking;
import br.ucsal.pokesal.servicos.ServicoRankingMemoria;
import br.ucsal.pokesal.servicos.ServicoTerreno;
import br.ucsal.pokesal.servicos.ServicoTerrenoEstacionamento;

import java.time.Clock;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Classe principal que configura as dependencias e executa um torneio PokeSal.
 */
public class Main {

    /**
     * Metodo principal de execucao do simulador.
     *
     * @param args Argumentos de linha de comando.
     */
    public static void main(String[] args) {
        System.out.println("=== TORNEIO POKESAL: ESTACIONAMENTO UCSAL PITUACU ===");

        GeradorAleatorio gerador = new GeradorAleatorioPadrao(new Random());
        ServicoClima servicoClima = () -> Clima.ENSOLARADO;
        ServicoTerreno servicoTerreno = new ServicoTerrenoEstacionamento(
                servicoClima, Clock.systemDefaultZone());
        RepositorioResultadosMemoria repositorio = new RepositorioResultadosMemoria();
        RegistroBatalhaMemoria registro = new RegistroBatalhaMemoria();
        ServicoNotificacao notificacao = (destinatario, mensagem) ->
                System.out.println("[NOTIFICACAO -> " + destinatario + "] " + mensagem);
        ServicoRanking servicoRanking = new ServicoRankingMemoria();

        Batalha batalha = new Batalha(
                gerador,
                servicoTerreno,
                repositorio,
                registro,
                notificacao,
                servicoRanking
        );

        Torneio torneio = new Torneio(batalha, repositorio, registro, notificacao);

        List<Treinador> treinadores = Arrays.asList(
                new Treinador("Gabriel", new Pokesal("CharSal", TipoElemental.FOGO, 100, 26, 14, 20)),
                new Treinador("Lucas", new Pokesal("TotoSal", TipoElemental.AGUA, 110, 22, 18, 16)),
                new Treinador("Marina", new Pokesal("BulbaSal", TipoElemental.PLANTA, 105, 24, 15, 18)),
                new Treinador("Rafael", new Pokesal("CyndaSal", TipoElemental.FOGO, 95, 27, 13, 22))
        );

        Golpe golpePadrao = new Golpe("Investida Elemental", TipoElemental.FOGO, 90);
        Treinador campeao = torneio.executarTorneio(treinadores, golpePadrao);

        System.out.println("Terreno atual: " + servicoTerreno.obterTerrenoAtual());
        System.out.println("Campeao do torneio: " + campeao.getNome()
                + " com " + campeao.getPokesal().getNome());
        System.out.println("Total de eventos registrados: " + registro.getEventos().size());

        int partida = 1;
        for (ResultadoBatalha r : torneio.getHistoricoResultados()) {
            System.out.println("Partida " + partida++ + ": "
                    + r.getVencedor().getNome() + " venceu "
                    + r.getPerdedor().getNome() + " em "
                    + r.getNumeroTurnos() + " turnos");
        }

        System.out.println();
        System.out.println("=== EVENTOS DE BATALHA REGISTRADOS ===");
        for (String evento : registro.getEventos()) {
                System.out.println(evento);

        }
    }
}
