package br.ucsal.pokesal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PokesalTest {


    @Test
    public void testVantagemElemental() {
        Pokesal charSal = new Pokesal("CharSal", TipoElemental.FOGO, 100, 20, 10, 20);
        Pokesal bulbaSal = new Pokesal("BulbaSal", TipoElemental.PLANTA, 100, 20, 10, 20);
        Batalha batalha = new Batalha();


        int danoCausado = batalha.calcularDano(charSal, bulbaSal, Terreno.CANTEIRO_CENTRAL);


        assertEquals(20, danoCausado, "O dano de Fogo contra Planta deveria ser dobrado!");
    }
    @Test
    public void testEfeitoTerrenoEstacionamentoUCSal() {
        Pokesal charSal = new Pokesal("CharSal", TipoElemental.FOGO, 100, 20, 10, 20);
        Pokesal outroFogo = new Pokesal("OutroFogo", TipoElemental.FOGO, 100, 20, 10, 20);
        Batalha batalha = new Batalha();

        int danoCausado = batalha.calcularDano(charSal, outroFogo, Terreno.ASFALTO_QUENTE);

        assertEquals(11, danoCausado, "O dano deveria ter 15% de bônus por causa do Asfalto Quente!");
    }

    @Test
    public void testOrdemDeAtaquePorVelocidade() {
        Pokesal rapido = new Pokesal("Rapido", TipoElemental.FOGO, 100, 20, 10, 50);
        Pokesal lento = new Pokesal("Lento", TipoElemental.AGUA, 100, 20, 10, 10);

        boolean rapidoAtacaPrimeiro = rapido.getVelocidade() > lento.getVelocidade();

        assertTrue(rapidoAtacaPrimeiro, "O Pokesal com maior Velocidade (SPD) deve ter a iniciativa");
    }

    @Test
    public void testUsoLimiteDeItensExcedido() {

        Pokesal pokesal = new Pokesal("Teste", TipoElemental.FOGO, 100, 20, 10, 20);
        Treinador treinador = new Treinador("Pedro", pokesal);

        treinador.usarItem(Item.POCAO);
        treinador.usarItem(Item.POCAO);

        assertThrows(LimiteItensExcedidoException.class, () -> {
            treinador.usarItem(Item.POCAO);
        }, "Deveria bloquear e lançar erro ao usar o 3º item!");
    }
    @Test
    public void testCalculoDanoBoundaryValues() {

        Pokesal fraco = new Pokesal("Fraco", TipoElemental.FOGO, 100, 5, 10, 10);
        Pokesal tanque = new Pokesal("Tanque", TipoElemental.AGUA, 100, 10, 50, 10);
        Batalha batalha = new Batalha();

        int danoCausado = batalha.calcularDano(fraco, tanque, Terreno.CANTEIRO_CENTRAL);

        assertEquals(1, danoCausado, "O dano calculado não pode ser negativo, deve respeitar o limite mínimo de 1.");
    }

    @Test
    public void testRequisitoAutoralGolpeCarregado() {

        Pokesal atacante = new Pokesal("Char", TipoElemental.FOGO, 100, 20, 10, 10);
        Pokesal defensor = new Pokesal("Toto", TipoElemental.AGUA, 100, 10, 10, 10);

        atacante.executarGolpeCarregado(defensor);

        assertEquals(90, atacante.getHpAtual(), "O Pokesal atacante deveria sofrer o autodano (recuo) de 10% do HP.");
    }

    @Test
    public void testRequisitoAutoralDesespero() {

        Pokesal desesperado = new Pokesal("Desesperado", TipoElemental.PLANTA, 100, 20, 10, 10);

        desesperado.receberDano(80);

        assertEquals(1.3, desesperado.verificarBonusDesespero(), "Com HP em 25% ou menos, o multiplicador de dano deve ser de 1.3");
    }
}