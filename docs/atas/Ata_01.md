# Ata de Reunião Nº 01 (Fase 03)

## 1. Informações Gerais

| Campo | Informação |
| :--- | :--- |
| **Nome da Equipe / Dupla:** | Gabriel Silva Nunes (Equipe Individual – PokeSal) |
| **Data e Horário:** | 18/11/2026 das 19:00 às 20:30 |
| **Local / Plataforma:** | Remoto / Estudo Individual (Salvador – BA) |
| **Fase do Projeto:** | Fase 03 |

---

## 2. Registro de Presença e Assinaturas

| Membro | Status | Assinatura Digital / De acordo |
| :--- | :--- | :--- |
| Gabriel Silva Nunes | Presente | Confirmado – Gabriel Silva Nunes |

---

## 3. Pautas da Reunião

- Leitura completa do edital da Fase 03 e levantamento de todas as novas regras de negócio e entregáveis.
- Revisão das pendências e bugs identificados na Fase 02 (`QUEIMADO`, `PARALISADO` e duplicação na `Main`).
- Definição da estrutura de pacotes e migração do projeto para Maven (`pom.xml`) com JUnit 5, Mockito e JaCoCo.
- Planejamento da refatoração com Injeção de Dependências (DIP – SOLID) e das novas interfaces do sistema.
- Definição dos 2 novos requisitos autorais da Fase 03 dependentes de colaboradores externos mockáveis.

---

## 4. Tópicos Discutidos e Deliberações

- **Estrutura Maven e Pacotes:** deliberado migrar o projeto para o layout padrão Maven (`src/main/java` e `src/test/java`) e organizar as novas interfaces e implementações nos subpacotes `registros`, `repositorios` e `servicos` dentro de `br.ucsal.pokesal`.
- **Inversão de Dependência (DIP):** definido que nenhuma classe de regra de negócio (`Batalha`, `Torneio`, `Pokesal`, `Treinador`, `Golpe`) poderá usar `Math.random()`, `new Random()` ou instanciar colaboradores internamente. Todos os colaboradores (`GeradorAleatorio`, `ServicoTerreno`, `ServicoClima`, `Clock`, `RepositorioResultados`, `RegistroBatalha`, `ExecutorBatalha`, `ServicoNotificacao` e `ServicoRanking`) serão injetados via construtor, ficando a montagem concreta centralizada na `Main`.
- **Regras da Fase 03:**
  - `Golpe` receberá o atributo `precisao` (0 a 100); `Batalha` aplicará falha de precisão (dano zero) e acerto crítico (6,25% de chance, dano ×1.5) via `GeradorAleatorio`.
  - `ServicoTerrenoEstacionamento` determinará o terreno combinando `ServicoClima` e `java.time.Clock`, ativando `ASFALTO_QUENTE` apenas durante o dia (06:00 às 17:59) e retornando `NORMAL` à noite.
  - `Torneio` organizará o chaveamento eliminatório para 4 ou 8 treinadores, salvando resultados em `RepositorioResultados` sem invalidar a batalha ou o torneio em caso de falha do repositório.
  - `RegistroBatalha` receberá os eventos do turno na ordem fixa: ataque, dano, efeito de status, efeito de terreno e uso de item.
- **2 Novos Requisitos Autorais:**
  - **Requisito Autoral 4 – Alerta Oficial do Torneio:** notificação de vitória e campeão via interface `ServicoNotificacao`, com tolerância a falhas.
  - **Requisito Autoral 5 – Invicto do Estacionamento:** bônus de +10% de dano para treinadores com 3 ou mais vitórias consecutivas consultadas via interface `ServicoRanking`.

---

## 5. Distribuição de Atividades (Acompanhamento)

| Atividade / Tarefa | Responsável | Prazo de Entrega | Status |
| :--- | :--- | :--- | :--- |
| Configuração do `pom.xml` (JUnit 5, Mockito, JaCoCo) e organização de pacotes (`registros`, `repositorios`, `servicos`) | Gabriel Silva Nunes | 19/11/2026 | Concluído |
| Criação das interfaces, modelos (`Golpe`, `Clima`, `ResultadoBatalha`, `Torneio`) e refatoração de `Batalha`, `Pokesal`, `Terreno` e `Main` | Gabriel Silva Nunes | 19/11/2026 | Concluído |
| Correção dos bugs da Fase 02 (`QUEIMADO` reduzindo ATK e `PARALISADO` reduzindo SPD) | Gabriel Silva Nunes | 19/11/2026 | Concluído |
| Implementação dos 2 novos requisitos autorais (`ServicoNotificacao` e `ServicoRanking`) | Gabriel Silva Nunes | 19/11/2026 | Concluído |
| Atualização do Diagrama de Classes e elaboração dos 2 Diagramas de Sequência | Gabriel Silva Nunes | 20/11/2026 | Em andamento |
| Implementação da suíte de testes com Mockito, JaCoCo, SonarQube e documentação final | Gabriel Silva Nunes | 20/11/2026 | Em andamento |

---

## 6. Encerramento

Sem mais assuntos a tratar, a reunião foi encerrada às 20:30. A próxima reunião ficou agendada para 20/11/2026 às 18:00. O membro leu, concordou e assinou este documento.
