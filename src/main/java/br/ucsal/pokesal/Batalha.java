package br.ucsal.pokesal;

import br.ucsal.pokesal.registros.RegistroBatalha;
import br.ucsal.pokesal.repositorios.RepositorioResultados;
import br.ucsal.pokesal.servicos.ExecutorBatalha;
import br.ucsal.pokesal.servicos.GeradorAleatorio;
import br.ucsal.pokesal.servicos.ServicoNotificacao;
import br.ucsal.pokesal.servicos.ServicoRanking;
import br.ucsal.pokesal.servicos.ServicoTerreno;

import java.util.Arrays;
import java.util.Objects;

/**
 * Classe que gerencia acoes, calculo de dano e fluxo de combate.
 */
public class Batalha implements ExecutorBatalha {

    public static final int DANO_ZERO = 0;
    public static final int DANO_MINIMO = 1;
    public static final int DANO_QUEIMADURA = 6;
    public static final int DANO_ENVENENAMENTO = 8;
    public static final double CHANCE_CRITICO = 6.25;
    public static final double MULTIPLICADOR_CRITICO = 1.5;
    public static final double MULTIPLICADOR_NEUTRO = 1.0;
    public static final int LIMIAR_INVICTO = 3;
    public static final double BONUS_INVICTO = 1.10;
    public static final int MAXIMO_TURNOS = 100;

    private final GeradorAleatorio geradorAleatorio;
    private final ServicoTerreno servicoTerreno;
    private final RepositorioResultados repositorioResultados;
    private final RegistroBatalha registroBatalha;
    private final ServicoNotificacao servicoNotificacao;
    private final ServicoRanking servicoRanking;

    /**
     * Construtor com injecao de todos os colaboradores necessarios para a batalha.
     *
     * @param geradorAleatorio Gerador de aleatoriedade para precisao e critico.
     * @param servicoTerreno Servico que informa o terreno atual da arena.
     * @param repositorioResultados Repositorio para salvar resultados finais.
     * @param registroBatalha Registro dos eventos de cada turno.
     * @param servicoNotificacao Servico de notificacao do torneio.
     * @param servicoRanking Servico de ranking de vitorias consecutivas.
     */
    public Batalha(GeradorAleatorio geradorAleatorio,
                   ServicoTerreno servicoTerreno,
                   RepositorioResultados repositorioResultados,
                   RegistroBatalha registroBatalha,
                   ServicoNotificacao servicoNotificacao,
                   ServicoRanking servicoRanking) {
        this.geradorAleatorio = Objects.requireNonNull(geradorAleatorio,
                "GeradorAleatorio nao pode ser nulo.");
        this.servicoTerreno = Objects.requireNonNull(servicoTerreno,
                "ServicoTerreno nao pode ser nulo.");
        this.repositorioResultados = Objects.requireNonNull(repositorioResultados,
                "RepositorioResultados nao pode ser nulo.");
        this.registroBatalha = Objects.requireNonNull(registroBatalha,
                "RegistroBatalha nao pode ser nulo.");
        this.servicoNotificacao = Objects.requireNonNull(servicoNotificacao,
                "ServicoNotificacao nao pode ser nulo.");
        this.servicoRanking = Objects.requireNonNull(servicoRanking,
                "ServicoRanking nao pode ser nulo.");
    }

    /**
     * Calcula o dano base causado pelo atacante no defensor.
     *
     * @param atacante Pokesal que ataca.
     * @param defensor Pokesal que recebe o golpe.
     * @param terreno Lugar onde a batalha esta acontecendo.
     * @return Valor numerico inteiro do dano base.
     */
    public int calcularDano(Pokesal atacante, Pokesal defensor, Terreno terreno) {
        double base = calcularDanoBruto(atacante, defensor, atacante.getTipo(), terreno, null);
        return Math.max((int) base, DANO_MINIMO);
    }

    /**
     * Calcula o dano de um golpe considerando terreno atual, precisao,
     * bonus de ranking e chance de acerto critico.
     *
     * @param atacante Pokesal que ataca.
     * @param defensor Pokesal defensor.
     * @param golpe Golpe executado.
     * @param treinadorAtacante Treinador do atacante (opcional para consulta de ranking).
     * @return Dano final calculado (0 se o golpe errar).
     */
    public int calcularDanoComGolpe(Pokesal atacante, Pokesal defensor,
                                    Golpe golpe, Treinador treinadorAtacante) {
        if (!acertouGolpe(golpe)) {
            return DANO_ZERO;
        }
        Terreno terreno = servicoTerreno.obterTerrenoAtual();
        double danoBruto = calcularDanoBruto(atacante, defensor, golpe.getTipo(),
                terreno, treinadorAtacante);
        if (isAcertoCritico()) {
            danoBruto *= MULTIPLICADOR_CRITICO;
        }
        return Math.max((int) danoBruto, DANO_MINIMO);
    }

    /**
     * Executa uma acao de ataque entre os combatentes, aplicando precisao,
     * acerto critico e registrando os eventos de ataque e dano.
     *
     * @param atacante Pokesal atacante.
     * @param defensor Pokesal que vai ser atingido.
     * @param golpe Golpe utilizado no ataque.
     * @param treinadorAtacante Treinador do atacante (pode ser null).
     * @return Valor de dano efetivamente aplicado ao defensor.
     */
    public int realizarAtaque(Pokesal atacante, Pokesal defensor,
                              Golpe golpe, Treinador treinadorAtacante) {
        Terreno terreno = servicoTerreno.obterTerrenoAtual();
        registroBatalha.registrarAtaque(atacante, defensor, golpe);

        if (!acertouGolpe(golpe)) {
            registroBatalha.registrarDano(defensor, DANO_ZERO, false);
            return DANO_ZERO;
        }

        double danoBruto = calcularDanoBruto(atacante, defensor, golpe.getTipo(),
                terreno, treinadorAtacante);
        boolean critico = isAcertoCritico();
        if (critico) {
            danoBruto *= MULTIPLICADOR_CRITICO;
        }

        int danoFinal = Math.max((int) danoBruto, DANO_MINIMO);
        defensor.receberDano(danoFinal);
        registroBatalha.registrarDano(defensor, danoFinal, critico);
        return danoFinal;
    }

    /**
     * Processa efeitos de status e recuperacao do Canteiro Central no final do turno.
     *
     * @param pokesal Pokesal a ser processado no final do turno.
     * @param terreno Terreno da arena.
     */
    public void processarFinalTurno(Pokesal pokesal, Terreno terreno) {
        if (!pokesal.isVivo()) {
            return;
        }
        processarEfeitoStatus(pokesal);
        if (pokesal.isVivo()) {
            processarEfeitoTerreno(pokesal, terreno);
        }
    }

    /**
     * Executa um turno completo enviando os eventos ao RegistroBatalha na ordem:
     * ataque, dano, efeito de status, efeito de terreno e uso de item.
     *
     * @param treinadorAtacante Treinador que realiza o turno.
     * @param treinadorDefensor Treinador adversario.
     * @param golpe Golpe executado.
     * @param itemUsado Item utilizado no turno .
     */
    public void executarTurno(Treinador treinadorAtacante, Treinador treinadorDefensor,
                              Golpe golpe, Item itemUsado) {
        Pokesal atacante = treinadorAtacante.getPokesal();
        Pokesal defensor = treinadorDefensor.getPokesal();
        Terreno terreno = servicoTerreno.obterTerrenoAtual();

        realizarAtaque(atacante, defensor, golpe, treinadorAtacante);
        processarEfeitoStatus(defensor);
        processarEfeitoTerreno(atacante, terreno);

        if (itemUsado != null) {
            treinadorAtacante.usarItem(itemUsado);
            registroBatalha.registrarUsoItem(treinadorAtacante, itemUsado);
        }
    }

    /**
     * Finaliza a batalha caso um dos combatentes tenha sido derrotado,
     * salva o resultado no repositorio, atualiza o ranking e notifica o vencedor.
     * Se ambos ainda estiverem vivos, retorna null sem acionar o repositorio.
     *
     * @param treinador1 Primeiro treinador.
     * @param treinador2 Segundo treinador.
     * @param numeroTurnos Quantidade de turnos disputados.
     * @return ResultadoBatalha se concluida, ou null se a batalha ainda nao terminou.
     */
    public ResultadoBatalha finalizarBatalha(Treinador treinador1,
                                             Treinador treinador2,
                                             int numeroTurnos) {
        Pokesal p1 = treinador1.getPokesal();
        Pokesal p2 = treinador2.getPokesal();

        if (p1.isVivo() && p2.isVivo()) {
            return null;
        }

        Treinador vencedor = p1.isVivo() ? treinador1 : treinador2;
        Treinador perdedor = p1.isVivo() ? treinador2 : treinador1;

        ResultadoBatalha resultado = new ResultadoBatalha(
                vencedor,
                perdedor,
                numeroTurnos,
                Arrays.asList(p1, p2)
        );

        salvarResultadoComSeguranca(resultado);
        registrarVitoriaComSeguranca(vencedor);
        notificarVencedorComSeguranca(vencedor, perdedor, numeroTurnos);

        return resultado;
    }

    @Override
    public ResultadoBatalha executarBatalha(Treinador treinador1, Treinador treinador2,
                                            Golpe golpe1, Golpe golpe2) {
        Terreno terrenoInicial = servicoTerreno.obterTerrenoAtual();
        treinador1.getPokesal().aplicarEfeitoTerrenoQuente(terrenoInicial);
        treinador2.getPokesal().aplicarEfeitoTerrenoQuente(terrenoInicial);

        int turnos = 0;
        while (treinador1.getPokesal().isVivo()
                && treinador2.getPokesal().isVivo()
                && turnos < MAXIMO_TURNOS) {
            turnos++;
            executarRodadaPorIniciativa(treinador1, treinador2, golpe1, golpe2);
        }

        return finalizarBatalha(treinador1, treinador2, turnos);
    }

    private void executarRodadaPorIniciativa(Treinador treinador1, Treinador treinador2,
                                             Golpe golpe1, Golpe golpe2) {
        Pokesal p1 = treinador1.getPokesal();
        Pokesal p2 = treinador2.getPokesal();
        Terreno terreno = servicoTerreno.obterTerrenoAtual();

        if (p1.getVelocidadeEfetiva() >= p2.getVelocidadeEfetiva()) {
            realizarAtaque(p1, p2, golpe1, treinador1);
            if (p2.isVivo()) {
                realizarAtaque(p2, p1, golpe2, treinador2);
            }
        } else {
            realizarAtaque(p2, p1, golpe2, treinador2);
            if (p1.isVivo()) {
                realizarAtaque(p1, p2, golpe1, treinador1);
            }
        }

        processarFinalTurno(p1, terreno);
        processarFinalTurno(p2, terreno);
    }

    private double calcularDanoBruto(Pokesal atacante, Pokesal defensor,
                                     TipoElemental tipoGolpe, Terreno terreno,
                                     Treinador treinadorAtacante) {
        double base = (double) atacante.getAtaqueEfetivo() - defensor.getDefesa();
        if (base < DANO_MINIMO) {
            base = DANO_MINIMO;
        }

        double multTipo = tipoGolpe.getMultiplicador(defensor.getTipo());
        double multTerreno = obterMultiplicadorTerreno(tipoGolpe, terreno);
        double bonusDesespero = atacante.verificarBonusDesespero();
        double bonusInvicto = obterBonusInvicto(treinadorAtacante);

        return base * multTipo * multTerreno * bonusDesespero * bonusInvicto;
    }

    private double obterMultiplicadorTerreno(TipoElemental tipoGolpe, Terreno terreno) {
        if (terreno == Terreno.ASFALTO_QUENTE && tipoGolpe == TipoElemental.FOGO) {
            return Terreno.BONUS_DANO_FOGO;
        }
        if (terreno == Terreno.POCA_CHUVA && tipoGolpe == TipoElemental.AGUA) {
            return Terreno.BONUS_DANO_AGUA;
        }
        return MULTIPLICADOR_NEUTRO;
    }

    private double obterBonusInvicto(Treinador treinadorAtacante) {
        if (treinadorAtacante == null) {
            return MULTIPLICADOR_NEUTRO;
        }
        int vitorias = servicoRanking.consultarVitoriasConsecutivas(treinadorAtacante);
        if (vitorias >= LIMIAR_INVICTO) {
            return BONUS_INVICTO;
        }
        return MULTIPLICADOR_NEUTRO;
    }

    private boolean acertouGolpe(Golpe golpe) {
        return geradorAleatorio.gerarPercentual() < golpe.getPrecisao();
    }

    private boolean isAcertoCritico() {
        return geradorAleatorio.gerarPercentual() < CHANCE_CRITICO;
    }

    private void processarEfeitoStatus(Pokesal pokesal) {
        if (pokesal.getEfeito() == Efeito.QUEIMADO) {
            pokesal.receberDano(DANO_QUEIMADURA);
        } else if (pokesal.getEfeito() == Efeito.ENVENENADO) {
            pokesal.receberDano(DANO_ENVENENAMENTO);
        }
        registroBatalha.registrarEfeitoStatus(pokesal, pokesal.getEfeito());
    }

    private void processarEfeitoTerreno(Pokesal pokesal, Terreno terreno) {
        if (terreno == Terreno.CANTEIRO_CENTRAL && pokesal.getTipo() == TipoElemental.PLANTA) {
            int cura = (int) (pokesal.getHpMax() * Terreno.FATOR_CURA_PLANTA);
            pokesal.curar(cura);
        }
        registroBatalha.registrarEfeitoTerreno(pokesal, terreno);
    }

    private void salvarResultadoComSeguranca(ResultadoBatalha resultado) {
        try {
            repositorioResultados.salvar(resultado);
        } catch (RuntimeException e) {
            registroBatalha.registrarErro("Falha ao salvar resultado: " + e.getMessage());
        }
    }

    private void registrarVitoriaComSeguranca(Treinador vencedor) {
        try {
            servicoRanking.registrarVitoria(vencedor);
        } catch (RuntimeException e) {
            registroBatalha.registrarErro("Falha ao atualizar ranking: " + e.getMessage());
        }
    }

    private void notificarVencedorComSeguranca(Treinador vencedor,
                                               Treinador perdedor,
                                               int numeroTurnos) {
        try {
            String mensagem = "Vitoria sobre " + perdedor.getNome()
                    + " em " + numeroTurnos + " turnos.";
            servicoNotificacao.notificar(vencedor.getNome(), mensagem);
        } catch (RuntimeException e) {
            registroBatalha.registrarErro("Falha ao enviar notificacao: " + e.getMessage());
        }
    }
}
