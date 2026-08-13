# Estrutura do App — Projeto de Vida para Todos (v2)

Síntese dos três "manuais" que fundamentam o produto:

1. **Projeto de Vida escoteiro (PDP — Ramo Pioneiro)** — a espinha dorsal: reflexão → plano escrito → equilíbrio nas 6 áreas → linha do tempo → revisão contínua → rede de apoio → autonomia radical.
2. **Método Cronos (Wendell Carvalho)** — o motor de execução: cascata visão → metas 12 meses → indicadores → semana → dia; disciplina como músculo; abrir espaço livre.
3. **Jornada das 5 Liberdades (Wendell Carvalho)** — o alicerce interno: narrativa pessoal e crenças, CHA (conhecimento-habilidade-atitude), maestria ("amar o platô"), KPIs pessoais.

## Princípio unificador

> O app nunca decide pelo usuário. Ele provoca reflexão, organiza a execução e celebra a constância — até que ter um projeto de vida vire **hábito**.

## A cascata do projeto (modelo mental central)

```
VISÃO (5–20 anos)  ──  "futuro desejado" (PDP) + V20 (Cronos)
   └─ METAS (6 áreas, 6 meses–5 anos)  ──  SMART + CHA + primeiro passo
       └─ SEMANA  ──  top 3 da semana + hábitos/rotina (PLADIM simplificado)
           └─ DIA  ──  checklist de hábitos → placar de constância
```

Revisões fecham o ciclo em duas cadências: **leve (semanal)** e **profunda (trimestral, a "vigília")**.

## Módulos do app (5 abas)

### 1. Descobrir — jornada de reflexão guiada
Passos (cada um salvo como texto livre):
1. **Minha história** — quem sou, forças, limitações (PDP) + "mude sua história" (5L: a narrativa pode ser reescrita).
2. **Meus valores** — a bússola (Lei/Promessa → valores pessoais).
3. **Roda da Vida** — diagnóstico 0–10 nas 6 áreas do desenvolvimento (físico, intelectual, caráter, afetivo, social, espiritual). Gráfico radar; entrada do equilíbrio (PDP) medida à moda Cronos.
4. **Meu mundo ao redor** — cenário, oportunidades, obstáculos (PDP).
5. **Futuro desejado** — visão de 5 a 20 anos, escrita no presente (PDP + V20).
6. **As escolhas** — o essencial vs. o que fica para depois (PDP: "não dá para atingir tudo").
7. **Rede de apoio** — padrinho/madrinha + pares (PDP); pessoas nomeadas, com lembrete de compartilhar o projeto.

### 2. Planejar — metas nas 6 áreas
- Meta = título + área + horizonte (6m/1a/2a/3a/5a) + **por que importa** (liga ao valor/visão) + **primeiro passo concreto** + **CHA** opcional (o que preciso conhecer/praticar/mudar de atitude).
- Barra de equilíbrio entre áreas (alerta suave se alguma área está vazia — nunca bloqueia).
- Linha do tempo de 5 anos gerada das metas.

### 3. Semana — execução (PLADIM simplificado)
- **Top 3 da semana**: até 3 compromissos escolhidos das metas (Pareto: foco é força).
- **Hábitos/rotina**: hábitos recorrentes com checklist diário.
- **Abrir espaço**: registro rápido de coisas a **parar / delegar / começar**.
- Planejamento semanal como ritual: botão "planejar minha semana" guia a escolha do top 3 revendo metas.

### 4. Constância — placar da disciplina
- **Placar**: sequência (streak) de dias com hábitos cumpridos e semanas com top 3 concluído.
- Indicadores mensais simples (KPIs pessoais, 5L): % hábitos, metas avançadas, revisões feitas.
- Filosofia do platô (5L): mensagens que celebram constância, não só resultado.

### 5. Revisar — o ciclo que nunca termina
- **Revisão semanal leve** (fecha a semana): o que avançou? o que travou? top 3 da próxima.
- **Vigília trimestral** (profunda): reler jornada, atualizar roda da vida, reformular metas. Agendável com lembrete visual.
- Histórico completo de revisões.

## Dados

- `localStorage` (chave `pvpt2`), arquivo único `index.html`, sem servidor.
- Export/import JSON (backup e portabilidade).
- Redesign v2: sem migração automática do formato v1 (decisão de 12/08/2026); usuários v1 podem exportar o JSON antigo.

## Mapa de rastreabilidade (funcionalidade → princípio)

| Funcionalidade | Fonte |
|---|---|
| Jornada de reflexão em passos | PDP: esboço é produto da reflexão |
| Roda da Vida com radar | PDP: 6 áreas / Cronos: roda da vida |
| Visão 5–20 anos escrita no presente | PDP: futuro desejado / Cronos: V20 |
| Metas SMART + primeiro passo | PDP: metas na linha do tempo / Cronos: metas SMART-E |
| CHA nas metas | 5 Liberdades: conhecimento-habilidade-atitude |
| Top 3 da semana | Cronos: PLADIM + Pareto / Guardiões: "regra do top 3" |
| Parar/Delegar/Começar | Cronos: Abrindo Espaço Livre |
| Placar de constância (streaks) | Cronos: Placar da Disciplina |
| Mensagens de "amar o platô" | 5 Liberdades: maestria |
| Revisão semanal + vigília trimestral | PDP: revisão contínua + Vigília / Cronos: PLADIM |
| Rede de apoio nomeada | PDP: padrinho/madrinha e Clã |
| Nenhuma decisão automática | PDP: autonomia radical |

## Fora desta versão (backlog)

Contas e sincronização; compartilhamento real com a rede de apoio; modo escoteiro (jargão do Ramo Pioneiro); notificações push; educação financeira aprofundada (Lei dos Potes) como módulo opcional.
