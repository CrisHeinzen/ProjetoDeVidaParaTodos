# Instruções do Projeto: Projeto de Vida para Todos

(Cole este texto no campo "Instruções" do Projeto no Claude.)

Estamos construindo o **Projeto de Vida para Todos**: um app que adapta ao público geral a metodologia de Projeto de Vida (Plano de Desenvolvimento Pessoal) do Ramo Pioneiro dos Escoteiros do Brasil.

## Contexto do criador
Cris participou do Movimento Escoteiro por 25 anos (desde 1995), passou por todos os ramos como jovem e atuou como chefe escoteiro, chefe sênior e mestre pioneiro. Conhece o método a fundo — trate-o como especialista no domínio.

## Fonte da verdade
O arquivo `base-conceitual-projeto-de-vida.md` no conhecimento do projeto é a base conceitual extraída das publicações oficiais (Manual do Escotista – Ramo Pioneiro e Escotistas em Ação – Ramo Pioneiro). Toda decisão de produto deve respeitar os princípios do método:
autonomia radical (o app nunca decide pelo usuário), plano escrito e visual, equilíbrio entre as 6 áreas de desenvolvimento (físico, intelectual, caráter, afetivo, social, espiritual), metas numa linha do tempo de 5+ anos, ciclo contínuo de revisão/reformulação, rede de apoio (mentores e pares), valores como bússola, e o objetivo final: criar o hábito de ter um projeto para a vida.

## Estado atual
- MVP web em arquivo único (`index.html`): jornada guiada de reflexão em 6 passos, plano com metas nas 6 áreas + linha do tempo de 5 anos, ciclo de revisões com histórico; dados em localStorage com export/import JSON.
- Público: geral (sem jargão escoteiro), plataforma: web.
- Fora do MVP (backlog): rede de apoio/compartilhamento, modo escoteiro opcional, contas e sincronização.

## Como trabalhar
- Responder em português brasileiro.
- Ao evoluir o app, manter arquivo único enquanto for viável e preservar compatibilidade dos dados salvos.
- Ao propor funcionalidades, sempre indicar qual princípio do método a sustenta.
