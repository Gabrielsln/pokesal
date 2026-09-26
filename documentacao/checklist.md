Checklist de Teste Estático:

Questões exigidas em relação a verificação estática do código:

1- Todas as variáveis estão inicializadas antes do uso?

Sim, estão inicializadas tanto nas classes de modelo pelos construtores ou pela declaração, e também nas lógicas de cálculo recebem valores, logo iniciam-se.

2 - Há variáveis declaradas e nunca usadas?

Sim. Há ponto de melhoria nessa questão em 2 valores declarados, mas não usados no código.

1. Na classe "Efeito", foi declarado o enum 'PARALISADO', mas não foi utilizado.
2. Na classe "Item", se declarou também o enum 'SUPER_POCAO', mas nunca instanciado.

3 - Existe código inacessível?

Não, todos os métodos funcionam dentro do fluxo do jogo.

4 -  Há código duplicado?

Na classe Main a lógica de batalha foi repetida duas vezes, isso em um sistema maior executado em vários turnos resultará em problemas futuros.

5 - Existem erros de sintaxe?

Não há.

6 - Há erros de lógica que quebram regras do negócio?

No ponto 4 das regras de negócio do sistema, 2 lógicas acabaram não sendo aplicadas corretamente. O efeito "Queimado" deveria reduzir HP e ATK, mas na execução acabou sendo reduzido apenas o HP. Além disso, o efeito "Paralisado" não foi utilizado como apontado no ponto 2 desse checklist.

7 - Há erros de tipagem?

Não, todos os tipos primitivos foram usados corretamente.


8 - O fluxo de controle é válido (sem loops infinitos, condições impossíveis)?

Sim, é valido, as condições possuem abertura e fechamento.


9 - Outros tipos de erros identificados:

Não identificados.

