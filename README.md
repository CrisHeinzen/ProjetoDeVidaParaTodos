# Projeto de Vida para Todos

App que leva a qualquer pessoa a metodologia de **Projeto de Vida** do Ramo Pioneiro dos Escoteiros do Brasil — o Plano de Desenvolvimento Pessoal (PDP), usado há décadas para ajudar jovens de 18 a 21 anos a construir, por escrito, um projeto para a própria vida.

## Por que "para todos"

O método escoteiro reserva essa ferramenta aos pioneiros, mas seus princípios são universais: qualquer pessoa, em qualquer idade, se beneficia do hábito de ter um projeto para a vida — escrito, equilibrado entre as áreas do desenvolvimento humano, revisado continuamente e apoiado por uma rede de confiança.

## Fundamentos (do método escoteiro para o app)

| Princípio do Ramo Pioneiro | Tradução no app |
|---|---|
| "Toma tua própria canoa e rema" | Autonomia: o app nunca decide pelo usuário; provoca reflexão |
| Plano escrito e visual | Editor do projeto de vida com expressão gráfica (linha do tempo, mapa) |
| 6 áreas de desenvolvimento (físico, intelectual, caráter, afetivo, social, espiritual) | Estrutura do plano e visão de equilíbrio entre áreas |
| Linha do tempo de 5+ anos com metas por período | Metas com horizonte de tempo e marcos |
| Esboço → Vigília → Apresentação → Revisão | Jornada guiada com momentos de reflexão profunda e revisões periódicas |
| Padrinho/madrinha e Clã | Rede de apoio: mentores e pares que conhecem o projeto e ajudam a "manter o rumo" |
| Valores como bússola (Lei e Promessa) | Etapa de definição de valores pessoais que guiam as escolhas |
| Flexibilidade e reformulação contínua | O plano nunca "termina"; o objetivo é o hábito |

## Estrutura do repositório

- `index.html` — MVP web em arquivo único: jornada guiada de reflexão em 6 passos, plano com metas nas 6 áreas + linha do tempo de 5 anos, ciclo de revisões com histórico; dados em localStorage com export/import JSON
- `docs/base-conceitual-projeto-de-vida.md` — extração comentada do método (fonte primária do produto)
- `docs/instrucoes-do-projeto-claude.md` — instruções de contexto para trabalhar no projeto com o Claude

## Como usar

Abra o `index.html` em qualquer navegador. Não requer servidor nem instalação; os dados ficam no localStorage do navegador.

## Status

MVP web funcional. Backlog: rede de apoio/compartilhamento, modo escoteiro opcional, contas e sincronização.
