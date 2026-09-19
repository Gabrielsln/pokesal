package br.ucsal.pokesal;

/**
 * Classe main que simula uma rodada do Pokesal.
 */
public class Main {

    /**
     * Metodo principal.
     *
     * @param args Argumentos da main.
     */
    public static void main(String[] args) {
        System.out.println("=== BATALHA POKESAL: UCSAL PITUACU ===");

        Pokesal charSal = new Pokesal("CharSal", TipoElemental.FOGO, 100, 26, 14, 20);
        Pokesal totoSal = new Pokesal("TotoSal", TipoElemental.AGUA, 110, 22, 18, 16);

        Treinador t1 = new Treinador("Treinador 1", charSal);
        Treinador t2 = new Treinador("Treinador 2", totoSal);

        Terreno terrenoAtual = Terreno.ASFALTO_QUENTE;


        charSal.aplicarEfeitoTerrenoQuente(terrenoAtual);
        totoSal.aplicarEfeitoTerrenoQuente(terrenoAtual);

        Batalha batalha = new Batalha();

        System.out.println("Terreno sorteado: " + terrenoAtual);
        System.out.println(t1.getNome() + " com " + charSal.getNome() + " | SPD: " + charSal.getVelocidade());
        System.out.println(t2.getNome() + " com " + totoSal.getNome() + " | SPD: " + totoSal.getVelocidade());
        System.out.println();

        System.out.println("TURNO 1");
        if (charSal.getVelocidade() >= totoSal.getVelocidade()) {
            System.out.println(charSal.getNome() + " e mais veloz e ataca primeiro!");
            batalha.realizarAtaque(charSal, totoSal, terrenoAtual);
            System.out.println(totoSal.getNome() + " ficou com " + totoSal.getHpAtual() + " de HP.");

            if (totoSal.isVivo()) {
                System.out.println(totoSal.getNome() + " revida com Golpe Carregado!");
                totoSal.executarGolpeCarregado(charSal);
                System.out.println(charSal.getNome() + " ficou com " + charSal.getHpAtual() + " de HP.");
            }
        } else {
            System.out.println(totoSal.getNome() + " e mais veloz e ataca primeiro!");
            batalha.realizarAtaque(totoSal, charSal, terrenoAtual);
            System.out.println(charSal.getNome() + " ficou com " + charSal.getHpAtual() + " de HP.");

            if (charSal.isVivo()) {
                batalha.realizarAtaque(charSal, totoSal, terrenoAtual);
                System.out.println(totoSal.getNome() + " ficou com " + totoSal.getHpAtual() + " de HP.");
            }
        }


        System.out.println("\n" + t2.getNome() + " utiliza POCAO em " + totoSal.getNome() + "!");
        t2.usarItem(Item.POCAO);
        System.out.println("HP de " + totoSal.getNome() + ": " + totoSal.getHpAtual()
                + " | Itens usados: " + t2.getItensUsados() + "/" + Treinador.LIMITE_ITENS);


        batalha.processarFinalTurno(charSal, terrenoAtual);
        batalha.processarFinalTurno(totoSal, terrenoAtual);

        System.out.println("Fim do turno finalizado com sucesso!");
    }
}