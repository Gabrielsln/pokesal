package br.ucsal.pokesal;

import br.ucsal.pokesal.registros.RegistroBatalha;
import br.ucsal.pokesal.repositorios.RepositorioResultados;
import br.ucsal.pokesal.servicos.GeradorAleatorio;
import br.ucsal.pokesal.servicos.ServicoClima;
import br.ucsal.pokesal.servicos.ServicoNotificacao;
import br.ucsal.pokesal.servicos.ServicoRanking;
import br.ucsal.pokesal.servicos.ServicoTerreno;
import br.ucsal.pokesal.servicos.ServicoTerrenoEstacionamento;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes com Mockito para a classe Batalha, ServicoTerrenoEstacionamento
 * e os dois novos requisitos autorais da Fase 03.
 */
@ExtendWith(MockitoExtension.class)
public class BatalhaMockitoTest {

    @Mock
    private GeradorAleatorio geradorAleatorio;

    @Mock
    private ServicoTerreno servicoTerreno;

    @Mock
    private RepositorioResultados repositorioResultados;

    @Mock
    private RegistroBatalha registroBatalha;

    @Mock
    private ServicoNotificacao servicoNotificacao;

    @Mock
    private ServicoRanking servicoRanking;

    @Mock
    private ServicoClima servicoClima;

    @InjectMocks
    private Batalha batalha;

    @Test
    public void testAcertoCriticoComGeradorMockado() {
        Pokesal atacante = new Pokesal("CharSal", TipoElemental.FOGO, 100, 30, 10, 20);
        Pokesal defensor = new Pokesal("CyndaSal", TipoElemental.FOGO, 100, 20, 10, 15);
        Golpe golpe = new Golpe("Brasa", TipoElemental.FOGO, 100);

        when(servicoTerreno.obterTerrenoAtual()).thenReturn(Terreno.NORMAL);
        when(geradorAleatorio.gerarPercentual()).thenReturn(20.0, 3.0);

        int dano = batalha.realizarAtaque(atacante, defensor, golpe, null);

        assertEquals(30, dano, "Dano base 20 multiplicado por 1.5 no critico deve ser 30.");
        assertEquals(70, defensor.getHpAtual());
        verify(geradorAleatorio, times(2)).gerarPercentual();
        verify(registroBatalha, times(1)).registrarDano(eq(defensor), eq(30), eq(true));
    }

    @Test
    public void testGolpeErraQuandoPrecisaoFalha() {
        Pokesal atacante = new Pokesal("CharSal", TipoElemental.FOGO, 100, 30, 10, 20);
        Pokesal defensor = new Pokesal("BulbaSal", TipoElemental.PLANTA, 100, 20, 10, 15);
        Golpe golpeImpreciso = new Golpe("Chama Instavel", TipoElemental.FOGO, 70);

        when(servicoTerreno.obterTerrenoAtual()).thenReturn(Terreno.NORMAL);
        when(geradorAleatorio.gerarPercentual()).thenReturn(85.0);

        int dano = batalha.realizarAtaque(atacante, defensor, golpeImpreciso, null);

        assertEquals(0, dano, "Quando a precisao falha, o dano deve ser zero.");
        assertEquals(100, defensor.getHpAtual(), "HP do alvo deve permanecer inalterado.");
        verify(geradorAleatorio, times(1)).gerarPercentual();
        verify(registroBatalha, times(1)).registrarDano(eq(defensor), eq(0), eq(false));
    }

    @Test
    public void testAsfaltoQuenteApenasDuranteODia() {
        ZoneId zona = ZoneId.of("America/Bahia");
        Clock relogioDia = Clock.fixed(Instant.parse("2026-11-20T15:00:00Z"), zona);
        Clock relogioNoite = Clock.fixed(Instant.parse("2026-11-21T01:00:00Z"), zona);

        when(servicoClima.obterClimaAtual()).thenReturn(Clima.ENSOLARADO);

        ServicoTerrenoEstacionamento servicoDia =
                new ServicoTerrenoEstacionamento(servicoClima, relogioDia);
        ServicoTerrenoEstacionamento servicoNoite =
                new ServicoTerrenoEstacionamento(servicoClima, relogioNoite);

        Terreno terrenoDia = servicoDia.obterTerrenoAtual();
        Terreno terrenoNoite = servicoNoite.obterTerrenoAtual();

        assertEquals(Terreno.ASFALTO_QUENTE, terrenoDia,
                "Durante o dia ensolarado o terreno deve ser ASFALTO_QUENTE.");
        assertEquals(Terreno.NORMAL, terrenoNoite,
                "Durante a noite o efeito Asfalto Quente nao deve ser ativado.");

        when(servicoClima.obterClimaAtual()).thenReturn(Clima.CHUVOSO, Clima.NUBLADO);
        assertEquals(Terreno.POCA_CHUVA, servicoDia.obterTerrenoAtual());
        assertEquals(Terreno.CANTEIRO_CENTRAL, servicoDia.obterTerrenoAtual());
    }

    @Test
    public void testSequenciaDeEventosNoTurno() {
        Pokesal atacante = new Pokesal("BulbaSal", TipoElemental.PLANTA, 100, 25, 10, 20);
        Pokesal defensor = new Pokesal("CharSal", TipoElemental.FOGO, 100, 20, 10, 15);
        defensor.setEfeito(Efeito.QUEIMADO);
        atacante.receberDano(20);

        Treinador treinadorAtacante = new Treinador("Gabriel", atacante);
        Treinador treinadorDefensor = new Treinador("Lucas", defensor);
        Golpe golpe = new Golpe("Chicote de Vinha", TipoElemental.PLANTA, 100);

        when(servicoTerreno.obterTerrenoAtual()).thenReturn(Terreno.CANTEIRO_CENTRAL);
        when(servicoRanking.consultarVitoriasConsecutivas(eq(treinadorAtacante))).thenReturn(0);
        when(geradorAleatorio.gerarPercentual()).thenReturn(10.0, 50.0);

        batalha.executarTurno(treinadorAtacante, treinadorDefensor, golpe, Item.POCAO);

        InOrder ordem = inOrder(registroBatalha);
        ordem.verify(registroBatalha).registrarAtaque(eq(atacante), eq(defensor), eq(golpe));
        ordem.verify(registroBatalha).registrarDano(eq(defensor), anyInt(), anyBoolean());
        ordem.verify(registroBatalha).registrarEfeitoStatus(eq(defensor), eq(Efeito.QUEIMADO));
        ordem.verify(registroBatalha).registrarEfeitoTerreno(
                eq(atacante), eq(Terreno.CANTEIRO_CENTRAL));
        ordem.verify(registroBatalha).registrarUsoItem(eq(treinadorAtacante), eq(Item.POCAO));
    }

    @Test
    public void testResultadoSalvoAoFimDaBatalha() {
        Pokesal vencedorPokesal = new Pokesal("CharSal", TipoElemental.FOGO, 100, 40, 10, 25);
        Pokesal perdedorPokesal = new Pokesal("BulbaSal", TipoElemental.PLANTA, 20, 15, 10, 15);
        Treinador t1 = new Treinador("Gabriel", vencedorPokesal);
        Treinador t2 = new Treinador("Marina", perdedorPokesal);
        Golpe golpeFogo = new Golpe("Lanca Chamas", TipoElemental.FOGO, 100);
        Golpe golpePlanta = new Golpe("Folha Navalha", TipoElemental.PLANTA, 100);

        when(servicoTerreno.obterTerrenoAtual()).thenReturn(Terreno.NORMAL);
        when(servicoRanking.consultarVitoriasConsecutivas(any(Treinador.class))).thenReturn(0);
        when(geradorAleatorio.gerarPercentual()).thenReturn(10.0, 50.0);

        ResultadoBatalha retorno = batalha.executarBatalha(t1, t2, golpeFogo, golpePlanta);

        ArgumentCaptor<ResultadoBatalha> captor =
                ArgumentCaptor.forClass(ResultadoBatalha.class);
        verify(repositorioResultados, times(1)).salvar(captor.capture());

        ResultadoBatalha salvo = captor.getValue();
        assertNotNull(retorno);
        assertEquals(t1, salvo.getVencedor());
        assertEquals(t2, salvo.getPerdedor());
        assertEquals(1, salvo.getNumeroTurnos());
        assertEquals(2, salvo.getPokesalsUtilizados().size());
    }

    @Test
    public void testFalhaNoRepositorioNaoInvalidaBatalha() {
        Pokesal vencedorPokesal = new Pokesal("CharSal", TipoElemental.FOGO, 100, 30, 10, 20);
        Pokesal perdedorPokesal = new Pokesal("TotoSal", TipoElemental.AGUA, 100, 20, 10, 15);
        perdedorPokesal.receberDano(100);

        Treinador t1 = new Treinador("Gabriel", vencedorPokesal);
        Treinador t2 = new Treinador("Lucas", perdedorPokesal);

        doThrow(new RuntimeException("Erro de conexao com o banco"))
                .when(repositorioResultados).salvar(any(ResultadoBatalha.class));

        ResultadoBatalha resultado = batalha.finalizarBatalha(t1, t2, 4);

        assertNotNull(resultado);
        assertEquals(t1, resultado.getVencedor(),
                "Mesmo com falha no repositorio, o vencedor deve permanecer definido.");
        assertEquals(t2, resultado.getPerdedor());
        verify(registroBatalha, atLeastOnce()).registrarErro(anyString());
    }

    @Test
    public void testRepositorioNaoChamadoSeBatalhaNaoTerminou() {
        Pokesal p1 = new Pokesal("CharSal", TipoElemental.FOGO, 100, 20, 10, 20);
        Pokesal p2 = new Pokesal("TotoSal", TipoElemental.AGUA, 100, 20, 10, 15);
        Treinador t1 = new Treinador("Gabriel", p1);
        Treinador t2 = new Treinador("Lucas", p2);

        ResultadoBatalha resultado = batalha.finalizarBatalha(t1, t2, 1);

        assertNull(resultado, "Enquanto ambos estiverem vivos, a batalha nao terminou.");
        verify(repositorioResultados, never()).salvar(any(ResultadoBatalha.class));
        verify(servicoNotificacao, never()).notificar(anyString(), anyString());
    }

    @Test
    public void testNotificacaoEnviadaAoVencedorNoFimDaBatalha() {
        Pokesal p1 = new Pokesal("CharSal", TipoElemental.FOGO, 100, 30, 10, 20);
        Pokesal p2 = new Pokesal("BulbaSal", TipoElemental.PLANTA, 100, 20, 10, 15);
        p2.receberDano(100);

        Treinador vencedor = new Treinador("Gabriel", p1);
        Treinador perdedor = new Treinador("Marina", p2);

        batalha.finalizarBatalha(vencedor, perdedor, 3);

        verify(servicoNotificacao, times(1)).notificar(eq("Gabriel"), anyString());
    }

    @Test
    public void testFalhaNaNotificacaoNaoInterrompeBatalha() {
        Pokesal p1 = new Pokesal("CharSal", TipoElemental.FOGO, 100, 30, 10, 20);
        Pokesal p2 = new Pokesal("BulbaSal", TipoElemental.PLANTA, 100, 20, 10, 15);
        p2.receberDano(100);

        Treinador vencedor = new Treinador("Gabriel", p1);
        Treinador perdedor = new Treinador("Marina", p2);

        doThrow(new RuntimeException("Servidor de notificacao fora do ar"))
                .when(servicoNotificacao).notificar(anyString(), anyString());

        ResultadoBatalha resultado = batalha.finalizarBatalha(vencedor, perdedor, 2);

        assertNotNull(resultado);
        assertEquals(vencedor, resultado.getVencedor());
        verify(registroBatalha, atLeastOnce()).registrarErro(anyString());
    }

    @Test
    public void testBonusInvictoAplicadoComTresVitoriasConsecutivas() {
        Pokesal atacante = new Pokesal("CharSal", TipoElemental.FOGO, 100, 30, 10, 20);
        Pokesal defensor = new Pokesal("CyndaSal", TipoElemental.FOGO, 100, 20, 10, 15);
        Treinador treinadorAtacante = new Treinador("Gabriel", atacante);
        Golpe golpe = new Golpe("Brasa", TipoElemental.FOGO, 100);

        when(servicoTerreno.obterTerrenoAtual()).thenReturn(Terreno.NORMAL);
        when(servicoRanking.consultarVitoriasConsecutivas(eq(treinadorAtacante))).thenReturn(3);
        when(geradorAleatorio.gerarPercentual()).thenReturn(15.0, 50.0);

        int dano = batalha.calcularDanoComGolpe(atacante, defensor, golpe, treinadorAtacante);

        assertEquals(22, dano,
                "Com 3 vitorias seguidas, o dano base 20 recebe +10% e passa para 22.");
        verify(servicoRanking, times(1)).consultarVitoriasConsecutivas(eq(treinadorAtacante));
    }

    @Test
    public void testBonusInvictoNaoAplicadoAbaixoDoLimiar() {
        Pokesal atacante = new Pokesal("CharSal", TipoElemental.FOGO, 100, 30, 10, 20);
        Pokesal defensor = new Pokesal("CyndaSal", TipoElemental.FOGO, 100, 20, 10, 15);
        Treinador treinadorAtacante = new Treinador("Lucas", atacante);
        Golpe golpe = new Golpe("Brasa", TipoElemental.FOGO, 100);

        when(servicoTerreno.obterTerrenoAtual()).thenReturn(Terreno.NORMAL);
        when(servicoRanking.consultarVitoriasConsecutivas(eq(treinadorAtacante))).thenReturn(2);
        when(geradorAleatorio.gerarPercentual()).thenReturn(15.0, 50.0);

        int dano = batalha.calcularDanoComGolpe(atacante, defensor, golpe, treinadorAtacante);

        assertEquals(20, dano,
                "Abaixo de 3 vitorias consecutivas, o bonus Invicto nao deve ser aplicado.");
        verify(servicoRanking, times(1)).consultarVitoriasConsecutivas(eq(treinadorAtacante));
    }

    @Test
    public void testExecutarBatalhaQuandoSegundoTreinadorMaisRapidoEStatusEnvenenado() {
        Pokesal lento = new Pokesal("TotoSal", TipoElemental.AGUA, 25, 18, 10, 10);
        Pokesal rapido = new Pokesal("BulbaSal", TipoElemental.PLANTA, 100, 25, 10, 30);
        lento.setEfeito(Efeito.ENVENENADO);

        Treinador t1 = new Treinador("Lucas", lento);
        Treinador t2 = new Treinador("Marina", rapido);
        Golpe golpeAgua = new Golpe("Jato", TipoElemental.AGUA, 100);
        Golpe golpePlanta = new Golpe("Chicote", TipoElemental.PLANTA, 100);

        when(servicoTerreno.obterTerrenoAtual()).thenReturn(Terreno.NORMAL);
        when(servicoRanking.consultarVitoriasConsecutivas(any(Treinador.class))).thenReturn(0);
        when(geradorAleatorio.gerarPercentual()).thenReturn(10.0, 50.0);

        ResultadoBatalha resultado = batalha.executarBatalha(t1, t2, golpeAgua, golpePlanta);

        assertEquals(t2, resultado.getVencedor());
        assertEquals(t1, resultado.getPerdedor());
    }
}
