# Ata de Reunião Nº 02 (Fase 03)

## 1. Informações Gerais

| Campo | Informação |
| :--- | :--- |
| **Nome da Equipe / Dupla:** | Gabriel Silva Nunes (Equipe Individual – PokeSal) |
| **Data e Horário:** | 20/11/2026 das 18:00 às 20:20 |
| **Local / Plataforma:** | Remoto / Estudo Individual (Salvador – BA) |
| **Fase do Projeto:** | Fase 03 |

---

## 2. Registro de Presença e Assinaturas

| Membro | Status | Assinatura Digital / De acordo |
| :--- | :--- | :--- |
| Gabriel Silva Nunes | Presente | Confirmado – Gabriel Silva Nunes |

---

## 3. Pautas da Reunião

- Validação da suíte de testes unitários com JUnit 5 e Mockito (`PokesalTest`, `BatalhaMockitoTest` e `TorneioMockitoTest`).
- Conferência da cobertura de testes medida pelo JaCoCo (metas de 80% de linhas e 70% de branches) e inclusão do relatório no repositório.
- Execução do SonarQube local e análise comparativa das métricas entre a Fase 02 e a Fase 03.
- Revisão do pipeline de Integração Contínua (`.github/workflows/ci.yml`), diagramas UML, documento de testes, registro de bugs e relatório individual.

---

## 4. Tópicos Discutidos e Deliberações

- **Suíte de Testes com Mockito:** todos os 28 testes executaram com sucesso (`mvn clean verify`), cobrindo os 8 testes obrigatórios da Fase 03, os 4 testes dos 2 novos requisitos autorais e os 7 testes preservados das Fases 01/02, aplicando `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`, `when/thenReturn` com retornos em sequência, `verify` (`times`, `never`, `atLeastOnce`), `InOrder`, `ArgumentCaptor`, `doThrow` e argument matchers (`any()`, `eq()`).
- **Cobertura JaCoCo:** atingidos **97,4% de linhas** e **88,9% de branches** nas classes de regra de negócio (superando as metas de 80% e 70%), com validação automática configurada no `pom.xml` (`jacoco:check`) e relatório salvo em `documentacao/jacoco/`.
- **Análise SonarQube (Fase 02 → Fase 03):**
  - `Code Smells` reduziu de **19** para **9** (Nota A);
  - `Coverage %` subiu de **0,0%** para **95,2%** com a integração do `jacoco.xml`;
  - `Vulnerabilities` manteve **0** (Nota A) e `Duplications` manteve **0,0%**;
  - Identificado 1 alerta de `Reliability` (`java:S2184` — subtração de `int` atribuída a `double` em `Batalha.calcularDanoBruto`) e 1 `Security Hotspot` referente ao `new Random()` na `Main` (justificado por tratar-se apenas de aleatoriedade de simulação de jogo, sem uso criptográfico).
- **Documentação e Entrega:** revisados os entregáveis finais (Diagrama de Classes atualizado, 2 Diagramas de Sequência, Relatório de Refatoração, Documento de Testes com Matriz de Rastreabilidade, Justificativa dos Mocks e Registro de Bugs, Comparativo SonarQube, Relatório Individual e Atas de Reunião em `docs/atas/`).

---

## 5. Distribuição de Atividades (Acompanhamento)

| Atividade / Tarefa | Responsável | Prazo de Entrega | Status |
| :--- | :--- | :--- | :--- |
| Implementação e execução dos testes com Mockito | Gabriel Silva Nunes | 20/11/2026 | Concluído |
| Geração do relatório JaCoCo e inclusão em `documentacao/jacoco/` | Gabriel Silva Nunes | 20/11/2026 | Concluído |
| Execução do SonarQube local e elaboração do relatório comparativo Fase 02 → Fase 03 | Gabriel Silva Nunes | 20/11/2026 | Concluído |
| Finalização do Documento de Testes, Matriz de Rastreabilidade, Justificativa dos Mocks e Registro de Bugs | Gabriel Silva Nunes | 20/11/2026 | Concluído |
| Revisão final dos arquivos, atualização dos hashes de commit no registro de bugs e push no GitHub | Gabriel Silva Nunes | 20/11/2026 | Concluído |

---

## 6. Encerramento

Sem mais assuntos a tratar, a reunião foi encerrada às 20:20. Por tratar-se da reunião de fechamento da Fase 03, não houve agendamento de nova reunião, ficando pendente apenas o depósito final no repositório Git e a preparação para a arguição individual. O membro leu, concordou e assinou este documento.
