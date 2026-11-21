package br.ucsal.pokesal.servicos;

import br.ucsal.pokesal.Clima;
import br.ucsal.pokesal.Terreno;

import java.time.Clock;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Implementacao do servico do terreno no estacionamento,
 * combina o clima e horario fornecido por um Clock.
 */
public class ServicoTerrenoEstacionamento implements ServicoTerreno {

    public static final int HORA_INICIO_DIA = 6;
    public static final int HORA_FIM_DIA = 18;

    private final ServicoClima servicoClima;
    private final Clock clock;

    /**
     * Construtor com injecao de ServicoClima e Clock.
     *
     * @param servicoClima Servico que informa o clima atual.
     * @param clock Relogio usado para consultar o horario atual.
     */
    public ServicoTerrenoEstacionamento(ServicoClima servicoClima, Clock clock) {
        this.servicoClima = Objects.requireNonNull(servicoClima,
                "ServicoClima nao pode ser nulo.");
        this.clock = Objects.requireNonNull(clock, "Clock nao pode ser nulo.");
    }

    @Override
    public Terreno obterTerrenoAtual() {
        Clima clima = servicoClima.obterClimaAtual();
        if (clima == Clima.CHUVOSO) {
            return Terreno.POCA_CHUVA;
        }
        if (clima == Clima.NUBLADO) {
            return Terreno.CANTEIRO_CENTRAL;
        }
        if (isHorarioDiurno()) {
            return Terreno.ASFALTO_QUENTE;
        }
        return Terreno.NORMAL;
    }

    private boolean isHorarioDiurno() {
        int hora = LocalTime.now(clock).getHour();
        return hora >= HORA_INICIO_DIA && hora < HORA_FIM_DIA;
    }
}
