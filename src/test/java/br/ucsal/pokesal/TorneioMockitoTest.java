package br.ucsal.pokesal;

import br.ucsal.pokesal.registros.RegistroBatalha;
import br.ucsal.pokesal.repositorios.RepositorioResultados;
import br.ucsal.pokesal.servicos.ExecutorBatalha;
import br.ucsal.pokesal.servicos.ServicoNotificacao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes feitos para o chaveamento do torneio.
 */
@ExtendWith(MockitoExtension.class)
public class TorneioMockitoTest {

    @Mock
    private ExecutorBatalha executorBatalha;

    @Mock
    private RepositorioResultados repositorioResultados;

    @Mock
    private RegistroBatalha registroBatalha;

    @Mock
    private ServicoNotificacao servicoNotificacao;

    @InjectMocks
    private Torneio torneio;

    @Test
    public void testChaveamentoTorneioAvancaVencedores() {
        Treinador t1 = criarTreinador("Gabriel", "CharSal", TipoElemental.FOGO);
        Treinador t2 = criarTreinador("Lucas", "TotoSal", TipoElemental.AGUA);
        Treinador t3 = criarTreinador("Marina", "BulbaSal", TipoElemental.PLANTA);
        Treinador t4 = criarTreinador("Rafael", "CyndaSal", TipoElemental.FOGO);
        Golpe golpe = new Golpe("Investida", TipoElemental.FOGO, 95);

        ResultadoBatalha semi1 = new ResultadoBatalha(
                t1, t2, 3, Arrays.asList(t1.getPokesal(), t2.getPokesal()));
        ResultadoBatalha semi2 = new ResultadoBatalha(
                t4, t3, 4, Arrays.asList(t3.getPokesal(), t4.getPokesal()));
        ResultadoBatalha finalTorneio = new ResultadoBatalha(
                t1, t4, 5, Arrays.asList(t1.getPokesal(), t4.getPokesal()));

        when(executorBatalha.executarBatalha(eq(t1), eq(t2), eq(golpe), eq(golpe)))
                .thenReturn(semi1);
        when(executorBatalha.executarBatalha(eq(t3), eq(t4), eq(golpe), eq(golpe)))
                .thenReturn(semi2);
        when(executorBatalha.executarBatalha(eq(t1), eq(t4), eq(golpe), eq(golpe)))
                .thenReturn(finalTorneio);

        Treinador campeao = torneio.executarTorneio(Arrays.asList(t1, t2, t3, t4), golpe);

        assertEquals(t1, campeao, "O vencedor das semifinais e da final deve ser t1.");
        assertEquals(3, torneio.getHistoricoResultados().size());
        verify(executorBatalha, times(1)).executarBatalha(eq(t1), eq(t4), eq(golpe), eq(golpe));
        verify(repositorioResultados, times(3)).salvar(any(ResultadoBatalha.class));
        verify(servicoNotificacao, times(1)).notificar(eq("Gabriel"), anyString());
    }

    @Test
    public void testChaveamentoTorneioComOitoTreinadores() {
        List<Treinador> participantes = Arrays.asList(
                criarTreinador("T1", "CharSal", TipoElemental.FOGO),
                criarTreinador("T2", "TotoSal", TipoElemental.AGUA),
                criarTreinador("T3", "BulbaSal", TipoElemental.PLANTA),
                criarTreinador("T4", "CyndaSal", TipoElemental.FOGO),
                criarTreinador("T5", "SquirtSal", TipoElemental.AGUA),
                criarTreinador("T6", "ChikoSal", TipoElemental.PLANTA),
                criarTreinador("T7", "CharSal2", TipoElemental.FOGO),
                criarTreinador("T8", "TotoSal2", TipoElemental.AGUA)
        );
        Golpe golpe = new Golpe("Investida", TipoElemental.FOGO, 100);

        when(executorBatalha.executarBatalha(
                any(Treinador.class), any(Treinador.class), eq(golpe), eq(golpe)))
                .thenAnswer(invocation -> {
                    Treinador primeiro = invocation.getArgument(0);
                    Treinador segundo = invocation.getArgument(1);
                    return new ResultadoBatalha(primeiro, segundo, 2,
                            Arrays.asList(primeiro.getPokesal(), segundo.getPokesal()));
                });

        Treinador campeao = torneio.executarTorneio(participantes, golpe);

        assertEquals(participantes.get(0), campeao);
        assertEquals(7, torneio.getHistoricoResultados().size());
        verify(repositorioResultados, times(7)).salvar(any(ResultadoBatalha.class));
    }

    @Test
    public void testTorneioComQuantidadeInvalidaLancaExcecao() {
        Golpe golpe = new Golpe("Investida", TipoElemental.FOGO, 100);
        List<Treinador> tresTreinadores = Arrays.asList(
                criarTreinador("T1", "CharSal", TipoElemental.FOGO),
                criarTreinador("T2", "TotoSal", TipoElemental.AGUA),
                criarTreinador("T3", "BulbaSal", TipoElemental.PLANTA)
        );

        assertThrows(IllegalArgumentException.class,
                () -> torneio.executarTorneio(tresTreinadores, golpe));
        assertThrows(IllegalArgumentException.class,
                () -> torneio.executarTorneio(null, golpe));
    }

    @Test
    public void testFalhaNoRepositorioDuranteTorneioNaoInterrompeChaveamento() {
        Treinador t1 = criarTreinador("Gabriel", "CharSal", TipoElemental.FOGO);
        Treinador t2 = criarTreinador("Lucas", "TotoSal", TipoElemental.AGUA);
        Treinador t3 = criarTreinador("Marina", "BulbaSal", TipoElemental.PLANTA);
        Treinador t4 = criarTreinador("Rafael", "CyndaSal", TipoElemental.FOGO);
        Golpe golpe = new Golpe("Investida", TipoElemental.FOGO, 100);

        when(executorBatalha.executarBatalha(
                any(Treinador.class), any(Treinador.class), eq(golpe), eq(golpe)))
                .thenReturn(
                        new ResultadoBatalha(t1, t2, 2, Collections.singletonList(t1.getPokesal())),
                        new ResultadoBatalha(t3, t4, 2, Collections.singletonList(t3.getPokesal())),
                        new ResultadoBatalha(t3, t1, 3, Collections.singletonList(t3.getPokesal()))
                );

        doThrow(new RuntimeException("Falha de disco"))
                .when(repositorioResultados).salvar(any(ResultadoBatalha.class));
        doThrow(new RuntimeException("Notificacao indisponivel"))
                .when(servicoNotificacao).notificar(anyString(), anyString());

        Treinador campeao = torneio.executarTorneio(Arrays.asList(t1, t2, t3, t4), golpe);

        assertEquals(t3, campeao, "O torneio deve continuar mesmo com falhas no repositorio.");
        verify(registroBatalha, atLeastOnce()).registrarErro(anyString());
    }

    private Treinador criarTreinador(String nome, String nomePokesal, TipoElemental tipo) {
        return new Treinador(nome, new Pokesal(nomePokesal, tipo, 100, 25, 12, 18));
    }
}
