package br.ucsal.pokesal;

import br.ucsal.pokesal.registros.RegistroBatalha;
import br.ucsal.pokesal.registros.RegistroBatalhaMemoria;
import br.ucsal.pokesal.repositorios.RepositorioResultados;
import br.ucsal.pokesal.repositorios.RepositorioResultadosMemoria;
import br.ucsal.pokesal.servicos.GeradorAleatorio;
import br.ucsal.pokesal.servicos.GeradorAleatorioPadrao;
import br.ucsal.pokesal.servicos.ServicoNotificacao;
import br.ucsal.pokesal.servicos.ServicoRanking;
import br.ucsal.pokesal.servicos.ServicoRankingMemoria;
import br.ucsal.pokesal.servicos.ServicoTerreno;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitarios das regras de negocio das Fases 01 e 02,
 * atualizados para a a Fase 03.
 */
@ExtendWith(MockitoExtension.class)
public class PokesalTest {

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

    @InjectMocks
    private Batalha batalha;

    @Test
    public void testVantagemElemental() {
        Pokesal charSal = new Pokesal("CharSal", TipoElemental.FOGO, 100, 20, 10, 20);
        Pokesal bulbaSal = new Pokesal("BulbaSal", TipoElemental.PLANTA, 100, 20, 10, 20);

        int danoCausado = batalha.calcularDano(charSal, bulbaSal, Terreno.CANTEIRO_CENTRAL);

        assertEquals(20, danoCausado,
                "O dano de Fogo contra Planta deveria ser dobrado!");
    }

    @Test
    public void testEfeitoTerrenoEstacionamentoUCSal() {
        Pokesal charSal = new Pokesal("CharSal", TipoElemental.FOGO, 100, 20, 10, 20);
        Pokesal outroFogo = new Pokesal("OutroFogo", TipoElemental.FOGO, 100, 20, 10, 20);

        int danoCausado = batalha.calcularDano(charSal, outroFogo, Terreno.ASFALTO_QUENTE);

        assertEquals(11, danoCausado,
                "O dano deveria ter 15% de bonus por causa do Asfalto Quente!");
    }

    @Test
    public void testOrdemDeAtaquePorVelocidade() {
        Pokesal rapido = new Pokesal("Rapido", TipoElemental.FOGO, 100, 20, 10, 50);
        Pokesal lento = new Pokesal("Lento", TipoElemental.AGUA, 100, 20, 10, 10);

        boolean rapidoAtacaPrimeiro =
                rapido.getVelocidadeEfetiva() > lento.getVelocidadeEfetiva();

        assertTrue(rapidoAtacaPrimeiro,
                "O Pokesal com maior Velocidade (SPD) deve ter a iniciativa");
    }

    @Test
    public void testUsoLimiteDeItensExcedido() {
        Pokesal pokesal = new Pokesal("Teste", TipoElemental.FOGO, 100, 20, 10, 20);
        Treinador treinador = new Treinador("Pedro", pokesal);

        treinador.usarItem(Item.POCAO);
        treinador.usarItem(Item.SUPER_POCAO);

        assertThrows(LimiteItensExcedidoException.class, () -> {
            treinador.usarItem(Item.POCAO);
        }, "Deveria bloquear e lancar erro ao usar o 3o item!");
    }

    @Test
    public void testCalculoDanoBoundaryValues() {
        Pokesal fraco = new Pokesal("Fraco", TipoElemental.FOGO, 100, 5, 10, 10);
        Pokesal tanque = new Pokesal("Tanque", TipoElemental.AGUA, 100, 10, 50, 10);

        int danoCausado = batalha.calcularDano(fraco, tanque, Terreno.CANTEIRO_CENTRAL);

        assertEquals(1, danoCausado,
                "O dano calculado nao pode ser negativo, deve respeitar o limite minimo de 1.");
    }

    @Test
    public void testRequisitoAutoralGolpeCarregado() {
        Pokesal atacante = new Pokesal("Char", TipoElemental.FOGO, 100, 20, 10, 10);
        Pokesal defensor = new Pokesal("Toto", TipoElemental.AGUA, 100, 10, 10, 10);

        atacante.executarGolpeCarregado(defensor);

        assertEquals(90, atacante.getHpAtual(),
                "O Pokesal atacante deveria sofrer o autodano (recuo) de 10% do HP.");
    }

    @Test
    public void testRequisitoAutoralDesespero() {
        Pokesal desesperado = new Pokesal("Desesperado", TipoElemental.PLANTA, 100, 20, 10, 10);

        desesperado.receberDano(80);

        assertEquals(1.3, desesperado.verificarBonusDesespero(),
                "Com HP em 25% ou menos, o multiplicador de dano deve ser de 1.3");
    }

    @Test
    public void testRequisitoAutoralQuenteDemais() {
        Pokesal totoSal = new Pokesal("TotoSal", TipoElemental.AGUA, 100, 20, 10, 15);
        totoSal.aplicarEfeitoTerrenoQuente(Terreno.ASFALTO_QUENTE);
        assertEquals(10, totoSal.getVelocidade());

        Pokesal charSal = new Pokesal("CharSal", TipoElemental.FOGO, 100, 20, 10, 15);
        charSal.aplicarEfeitoTerrenoQuente(Terreno.ASFALTO_QUENTE);
        assertEquals(15, charSal.getVelocidade());
    }

    @Test
    public void testEfeitosDeStatusQueimadoParalisadoEItemCura() {
        Pokesal pokesal = new Pokesal("BulbaSal", TipoElemental.PLANTA, 100, 20, 10, 20);
        pokesal.setEfeito(Efeito.QUEIMADO);
        assertEquals(16, pokesal.getAtaqueEfetivo());

        pokesal.setEfeito(Efeito.PARALISADO);
        assertEquals(10, pokesal.getVelocidadeEfetiva());

        Treinador treinador = new Treinador("Gabriel", pokesal);
        treinador.usarItem(Item.CURA);
        assertEquals(Efeito.NENHUM, pokesal.getEfeito());
        assertEquals(1, treinador.getItensUsados());
    }

    @Test
    public void testMatrizCompletaDeTiposElementais() {
        assertEquals(2.0, TipoElemental.FOGO.getMultiplicador(TipoElemental.PLANTA));
        assertEquals(0.5, TipoElemental.FOGO.getMultiplicador(TipoElemental.AGUA));
        assertEquals(1.0, TipoElemental.FOGO.getMultiplicador(TipoElemental.FOGO));

        assertEquals(2.0, TipoElemental.AGUA.getMultiplicador(TipoElemental.FOGO));
        assertEquals(0.5, TipoElemental.AGUA.getMultiplicador(TipoElemental.PLANTA));
        assertEquals(1.0, TipoElemental.AGUA.getMultiplicador(TipoElemental.AGUA));

        assertEquals(2.0, TipoElemental.PLANTA.getMultiplicador(TipoElemental.AGUA));
        assertEquals(0.5, TipoElemental.PLANTA.getMultiplicador(TipoElemental.FOGO));
        assertEquals(1.0, TipoElemental.PLANTA.getMultiplicador(TipoElemental.PLANTA));
    }

    @Test
    public void testTerrenoPocaChuvaECanteiroCentral() {
        Pokesal agua = new Pokesal("SquirtSal", TipoElemental.AGUA, 100, 20, 10, 15);
        Pokesal outroAgua = new Pokesal("TotoSal", TipoElemental.AGUA, 100, 20, 10, 15);

        int danoPoca = batalha.calcularDano(agua, outroAgua, Terreno.POCA_CHUVA);
        assertEquals(11, danoPoca);

        Pokesal planta = new Pokesal("BulbaSal", TipoElemental.PLANTA, 100, 20, 10, 15);
        planta.receberDano(20);
        batalha.processarFinalTurno(planta, Terreno.CANTEIRO_CENTRAL);
        assertEquals(85, planta.getHpAtual());
    }

    @Test
    public void testValidacaoPrecisaoGolpeEImplementacoesEmMemoria() {
        assertThrows(IllegalArgumentException.class,
                () -> new Golpe("Invalido", TipoElemental.FOGO, -1));
        assertThrows(IllegalArgumentException.class,
                () -> new Golpe("Invalido", TipoElemental.FOGO, 101));

        Pokesal p1 = new Pokesal("CharSal", TipoElemental.FOGO, 50, 20, 10, 15);
        p1.receberDano(60);
        assertFalse(p1.isVivo());
        assertEquals(0, p1.getHpAtual());
        p1.curar(100);
        assertEquals(50, p1.getHpAtual());

        GeradorAleatorioPadrao geradorPadrao = new GeradorAleatorioPadrao(new Random(42L));
        double valor = geradorPadrao.gerarPercentual();
        assertTrue(valor >= 0.0 && valor < 100.0);

        RegistroBatalhaMemoria registroMemoria = new RegistroBatalhaMemoria();
        Golpe golpe = new Golpe("Brasa", TipoElemental.FOGO, 90);
        Treinador t1 = new Treinador("Gabriel", p1);
        registroMemoria.registrarAtaque(p1, p1, golpe);
        registroMemoria.registrarDano(p1, 15, true);
        registroMemoria.registrarDano(p1, 10, false);
        registroMemoria.registrarEfeitoStatus(p1, Efeito.QUEIMADO);
        registroMemoria.registrarEfeitoTerreno(p1, Terreno.ASFALTO_QUENTE);
        registroMemoria.registrarUsoItem(t1, Item.POCAO);
        registroMemoria.registrarErro("Erro simulado");
        assertEquals(7, registroMemoria.getEventos().size());

        RepositorioResultadosMemoria repoMemoria = new RepositorioResultadosMemoria();
        ResultadoBatalha res = new ResultadoBatalha(t1, t1, 2, java.util.Collections.singletonList(p1));
        repoMemoria.salvar(res);
        assertEquals(1, repoMemoria.listarTodos().size());

        ServicoRankingMemoria rankingMemoria = new ServicoRankingMemoria();
        assertEquals(0, rankingMemoria.consultarVitoriasConsecutivas(t1));
        rankingMemoria.registrarVitoria(t1);
        assertEquals(1, rankingMemoria.consultarVitoriasConsecutivas(t1));
    }
}
