package br.ucsal.pokesal;

import br.ucsal.pokesal.registros.RegistroBatalha;
import br.ucsal.pokesal.repositorios.RepositorioResultados;
import br.ucsal.pokesal.servicos.ExecutorBatalha;
import br.ucsal.pokesal.servicos.ServicoNotificacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Gerencia o torneio eliminatorio do Estacionamento da UCSal com 4 ou 8 treinadores.
 */
public class Torneio {

    public static final int QUANTIDADE_QUATRO = 4;
    public static final int QUANTIDADE_OITO = 8;

    private final ExecutorBatalha executorBatalha;
    private final RepositorioResultados repositorioResultados;
    private final RegistroBatalha registroBatalha;
    private final ServicoNotificacao servicoNotificacao;
    private final List<ResultadoBatalha> historicoResultados = new ArrayList<>();

    /**
     * Construtor com injecao das dependencias do torneio.
     *
     * @param executorBatalha Executor responsavel por rodar cada confronto.
     * @param repositorioResultados Repositorio para persistir resultados das chaves.
     * @param registroBatalha Registro para logar erros operacionais.
     * @param servicoNotificacao Servico para anunciar o campeao do torneio.
     */
    public Torneio(ExecutorBatalha executorBatalha,
                   RepositorioResultados repositorioResultados,
                   RegistroBatalha registroBatalha,
                   ServicoNotificacao servicoNotificacao) {
        this.executorBatalha = Objects.requireNonNull(executorBatalha,
                "ExecutorBatalha nao pode ser nulo.");
        this.repositorioResultados = Objects.requireNonNull(repositorioResultados,
                "RepositorioResultados nao pode ser nulo.");
        this.registroBatalha = Objects.requireNonNull(registroBatalha,
                "RegistroBatalha nao pode ser nulo.");
        this.servicoNotificacao = Objects.requireNonNull(servicoNotificacao,
                "ServicoNotificacao nao pode ser nulo.");
    }

    /**
     * Executa o chaveamento eliminatorio completo ate definir o campeao.
     *
     * @param participantes Lista com exatamente 4 ou 8 treinadores.
     * @param golpePadrao Golpe utilizado nas batalhas da chave.
     * @return Treinador campeao do torneio.
     */
    public Treinador executarTorneio(List<Treinador> participantes, Golpe golpePadrao) {
        validarParticipantes(participantes);
        historicoResultados.clear();

        List<Treinador> faseAtual = new ArrayList<>(participantes);

        while (faseAtual.size() > 1) {
            List<Treinador> proximaFase = new ArrayList<>();
            for (int i = 0; i < faseAtual.size(); i += 2) {
                Treinador t1 = faseAtual.get(i);
                Treinador t2 = faseAtual.get(i + 1);

                ResultadoBatalha resultado = executorBatalha.executarBatalha(
                        t1, t2, golpePadrao, golpePadrao);
                historicoResultados.add(resultado);
                salvarResultadoComSeguranca(resultado);

                Treinador vencedor = resultado.getVencedor();
                vencedor.getPokesal().restaurarParaProximaFase();
                proximaFase.add(vencedor);
            }
            faseAtual = proximaFase;
        }

        Treinador campeao = faseAtual.get(0);
        notificarCampeaoComSeguranca(campeao);
        return campeao;
    }

    /**
     * Retorna os resultados de todas as batalhas disputadas no torneio.
     *
     * @return Lista imutavel de ResultadoBatalha.
     */
    public List<ResultadoBatalha> getHistoricoResultados() {
        return Collections.unmodifiableList(historicoResultados);
    }

    private void validarParticipantes(List<Treinador> participantes) {
        if (participantes == null) {
            throw new IllegalArgumentException("Lista de participantes nao pode ser nula.");
        }
        int total = participantes.size();
        if (total != QUANTIDADE_QUATRO && total != QUANTIDADE_OITO) {
            throw new IllegalArgumentException(
                    "O torneio eliminatorio deve ter exatamente 4 ou 8 treinadores.");
        }
    }

    private void salvarResultadoComSeguranca(ResultadoBatalha resultado) {
        try {
            repositorioResultados.salvar(resultado);
        } catch (RuntimeException e) {
            registroBatalha.registrarErro(
                    "Erro no repositorio durante o torneio: " + e.getMessage());
        }
    }

    private void notificarCampeaoComSeguranca(Treinador campeao) {
        try {
            servicoNotificacao.notificar(
                    campeao.getNome(),
                    "Parabens! Voce venceu o Torneio PokeSal da UCSal!");
        } catch (RuntimeException e) {
            registroBatalha.registrarErro(
                    "Erro ao notificar campeao: " + e.getMessage());
        }
    }
}
