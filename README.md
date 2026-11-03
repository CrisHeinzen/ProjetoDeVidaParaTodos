# Projeto de Vida para Todos

🌐 **Acesse o app:** https://crisheinzen.github.io/ProjetoDeVidaParaTodos/

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

O app foi refatorado para **Java 21 + Spring Boot 3.5** (páginas geradas no servidor com Thymeleaf, dados em banco via JPA).

```
pom.xml                                   # projeto Maven
src/main/java/br/com/projetodevida/
  dominio/      # entidades JPA e enums (Area, Horizonte, PassoJornada, Meta, Habito, Revisao...)
  repositorio/  # interfaces Spring Data JPA
  servico/      # regras de negócio (jornada, metas, semana, constância, revisões)
  backup/       # exportar/importar JSON (compatível com o backup da versão web antiga)
  web/          # controllers Spring MVC, um por aba
src/main/resources/
  templates/    # páginas Thymeleaf (descobrir, planejar, semana, constancia, revisar)
  static/css/   # estilo.css
  application.properties           # H2 em arquivo (padrão)
  application-postgres.properties  # perfil opcional com PostgreSQL
src/test/java/  # testes (JUnit 5 + Spring Boot Test + MockMvc)
index.html      # versão web antiga (JavaScript + localStorage), mantida para o GitHub Pages
docs/           # base conceitual e estrutura do app
```

## Como rodar (versão Java)

Pré-requisitos: **JDK 21** e **Maven 3.9+**.

```bash
mvn spring-boot:run
```

Abra http://localhost:8080. Os dados ficam no banco H2 gravado na pasta `dados/` (criada automaticamente e ignorada pelo git).

Para usar **PostgreSQL** em vez do H2:

```bash
# crie o banco antes: CREATE DATABASE projeto_de_vida;
DB_URL=jdbc:postgresql://localhost:5432/projeto_de_vida DB_USER=postgres DB_PASSWORD=sua_senha \
  mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

As tabelas são criadas automaticamente (`spring.jpa.hibernate.ddl-auto=update`).

Testes: `mvn test`. Gerar o `.jar`: `mvn package` → `java -jar target/projeto-de-vida-para-todos-3.0.0.jar`.

### Migrar dados da versão web antiga

Na versão web (`index.html`), use **Exportar (JSON)**. Na versão Java, aba **Revisar → Seus dados → Importar**, escolha esse arquivo. O formato é o mesmo, então backups antigos e novos são aceitos.

### Importante: GitHub Pages

O GitHub Pages só hospeda arquivos estáticos, **não roda Java**. O link publicado continua servindo o `index.html` antigo. Para publicar a versão Java na internet é preciso um servidor que rode Java (ex.: uma VM, Render, Railway, Fly.io etc.).

## Status

Versão Java funcional, com as mesmas 5 abas do MVP web. Backlog: contas e sincronização, compartilhamento com a rede de apoio, modo escoteiro opcional.
