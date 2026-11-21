Documento de Especificação de Requisitos Autorais

Este documento detalha as regras autorais criadas para o projeto PokéSal nas Fases 01 e 03. O objetivo é explicar a utilização e a motivação para o uso das mesmas nas batalhas, terrenos e torneio.

=== REQUISITOS AUTORAIS DA FASE 01 ===

1. Golpe Carregado

O Pokésal usa das suas energias para golpear mais forte do que o normal, mas o seu esforço usa da sua energia física, logo causando dano a si próprio.

O dano recebe um multiplicador de 1.4x (40%).

O atacante recebe 10% de dano no seu próprio HP.

Variáveis usadas:
MULTIPLICADOR_GOLPE_CARREGADO = 1.4
FATOR_AUTODANO = 0.10


2. Desespero

Quando um Pokésal está com pouca vida e se encontra em desvantagem, ele usa do seu instinto de sobrevivência, fazendo com que ele lute com mais garra e dê mais dano ao adversário.

O sistema vê o HP do Pokésal antes do ataque, e se estiver com 25% ou menos de vida o atacante ganha bônus em dano ao adversário em 1.30x (ou 30%).

Variáveis usadas:
FATOR_LIMIAR_DESESPERO = 0.25
FATOR_DESESPERO_DANO = 1.30


3. Quente Demais!

O terreno da luta afeta diretamente os competidores. Pokésals do tipo elementar de água cansam mais e ficam mais lentos quando são expostos a superfícies muito quentes, como o asfalto.

Este evento ocorre apenas quando o Terreno for Asfalto Quente e o tipo elementar do Pokésal for Água.

O mesmo causa uma redução de -5 pontos de velocidade ao mesmo.

É utilizado o método Math.max(1, velocidade - 5) para garantir que a velocidade do monstrinho nunca caia para valores negativos ou zero.

Variáveis usadas:
PENALIDADE_SPD_ASFALTO = 5

4. Notificação de Resultados do Torneio

Os treinadores vão ser avisados automaticamente toda vez que ganharem uma batalha e, claro, quando levarem o título do campeonato.

Colaboradores mockáveis: a interface ServicoNotificacao, com o método notificar(String destinatario, String mensagem), entra pelo construtor em Batalha e em Torneio.

Regras:

Quando uma batalha acaba e já tem vencedor, o sistema manda uma notificação pro treinador que ganhou, contando quem ele derrotou e quantos turnos a luta durou.
Quando o chaveamento eliminatório termina, o campeão do torneio recebe uma notificação avisando que levou a taça.
Se o ServicoNotificacao der erro (o serviço cair), isso não pode finalizar a batalha nem o torneio. O erro vai pro RegistroBatalha e o fluxo segue normalmente.

5. Invicto do Estacionamento (Bônus de Sequência de Vitórias no Ranking)

Se acumular uma sequência de 3 vitórias seguidas, o Pokésal dá um dano mais forte.

Colaboradores mockáveis: a interface ServicoRanking, com os métodos consultarVitoriasConsecutivas(Treinador) e registrarVitoria(Treinador), entra pelo construtor em Batalha.

Regras:

Antes de calcular o dano de cada golpe, a Batalha pergunta pro ServicoRanking quantas vitórias seguidas o treinador atacante tem.
Se ele tiver 3 ou mais vitórias seguidas (LIMIAR_INVICTO = 3), o dano ganha um bônus de 10% (BONUS_INVICTO = 1.10).
Com menos de 3 vitórias seguidas, sem bônus.
Quando o treinador ganha a batalha, o sistema anota a vitória nova dele no ServicoRanking.

Constantes usadas:

LIMIAR_INVICTO = 3
BONUS_INVICTO = 1.10
