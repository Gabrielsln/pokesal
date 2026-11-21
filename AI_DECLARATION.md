# Declaração de Uso de Inteligência Artificial

Em estrito cumprimento das diretrizes de integridade acadêmica e do teto de 10% de uso de ferramentas de IA:
* A concepção conceitual e a implementação dos 3 requisitos autorais foram elaboradas exclusivamente pelo desenvolvedor.
* As regras de negócio de combate e as classes do projeto foram codificadas pelo aluno.
* O uso de ferramentas de IA limitou-se à formatação da documentação estática, revisão ortográfica e checagem de padrões formais de Checkstyle.

Prompts:

"o documento especifica se as entradas tem que ser variáveis ou podemos executar turnos com dados fixos que nem essa da main?"
Uso: Interpretação e análise dos requisitos do enunciado.

"Cannot resolve symbol 'Item' / Cannot resolve method 'getValorCura()'"
Uso: Correção de dependência sintática entre classes apontada pelo compilador.   

"eu crio dentro ou fora da package o md"
Uso: Organização estrutural de arquivos no repositório Git

"por exemplo, se eu digitar this == FOGO e this.tipoAlvo == FOGO é a mesma coisa?"
Uso: Resolução de erro conceitual/sintático sobre comparação de instâncias de Enum.

"O documento pede pra criar um arquivo chamado AI_DECLARATION.md falando o uso de ferramentas de IA (máximo 10%) e detalhando que os requisitos autorais e regras foram feitos pelo aluno. Como posso desenvolver o texto desse documento de forma formal?"
Uso: Formatação e estruturação textual do documento AI_DECLARATION.md em conformidade com as regras do edital.


Fase 03:

Prompts:

"como posso executar o sonarqube na pasta raiz desse projeto?"
Uso: Orientação sobre o comando e a configuração de execução do SonarScanner na raiz do projeto Maven.

"Como posso resolver o erro a seguir?: Error during SonarScanner execution
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'org.sonarsource.scanner.api.internal.IsolatedClassloader@5123a213-org.sonar.scanner.report.ReportPublisher':"
Uso: Identificação e diagnóstico de erro de ambiente/configuração durante a execução do SonarScanner.

"INFO: ------------------------------------------------------------------------
INFO: EXECUTION FAILURE
INFO: ------------------------------------------------------------------------
INFO: Total time: 1.115s
INFO: Final Memory: 7M/48M
INFO: ------------------------------------------------------------------------
ERROR: Error during SonarScanner execution - me ajude a identificar o erro na execução do sonar"
Uso: Diagnóstico de falha na execução do SonarScanner para geração do relatório estático.

"Atualizar o Diagrama de Classes, destacando as novas interfaces e suas implementações.
Produzir pelo menos 2 Diagramas de Sequência: um turno completo de batalha e o fluxo de registro do resultado no torneio.
Entregar um relatório curto explicando quais classes foram refatoradas e por quê, citando acoplamento e o Princípio da Inversão de Dependência (SOLID).
tem como eu fazer isso de forma simples, sem fazer um diagrama extenso?"
Uso: Apoio na estruturação e na organização do relatório de refatoração.

""GeradorAleatorio gerador = new GeradorAleatorioPadrao(new Random())" se eu usar isso na main, usar o new random não seria proibido pelo edital?"
Uso: Interpretação das regras do edital quanto à injeção de dependência e ao papel da classe Main como ponto de composição.