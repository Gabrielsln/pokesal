Documento de Especificação de Requisitos Autorais

Este documento detalha as 3 regras autorais criadas para o projeto PokéSal. O objetivo é explicar a utilização e a motivação para o uso das mesmas nas batalhas e terrenos.

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