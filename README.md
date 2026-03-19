# Todo List Portlet — Desafio SEA

Portlet de gerenciamento de tarefas desenvolvido para o Desafio SEA, construído sobre a plataforma **Liferay DXP 7.4.3** com arquitetura MVC Portlet e persistência gerada pelo Service Builder.

---

## Índice

- [Visão Geral](#visão-geral)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Configuração](#instalação-e-configuração)
- [Compilação e Deploy](#compilação-e-deploy)
- [Como Usar](#como-usar)
- [Testes](#testes)
- [Bibliotecas e Decisões Técnicas](#bibliotecas-e-decisões-técnicas)

---

## Visão Geral

Aplicação de todo list completa rodando como portlet no Liferay. Cada usuário possui seu próprio espaço isolado de tarefas, com suporte a categorias, subtarefas, comentários, filtros por status/categoria e histórico de itens excluídos.

**Stack principal:** Java 17 · Liferay DXP 7.4.3 · Gradle · MySQL 8 · JSP · Bootstrap

---

## Funcionalidades

### Tarefas

- Criar, editar e excluir tarefas (exclusão lógica — histórico preservado)
- Marcar como concluída / reabrir
- Definir data de vencimento
- Anexar imagem à tarefa

### Organização

- Criar e gerenciar **categorias** por usuário
- Filtro lateral por categoria com contador de tarefas em cada uma
- Abas de status: **Pendentes · Atrasadas · Concluídas**

### Subtarefas e Comentários

- Adicionar subtarefas (checklist) vinculadas a uma tarefa
- Marcar subtarefas como concluídas individualmente
- Adicionar, editar e remover comentários por tarefa

### Segurança

- Isolamento total por usuário: cada usuário vê e manipula apenas seus próprios dados
- Verificação de propriedade em todas as operações de escrita
- Proteção CSRF via `portlet:actionURL`
- Escape de HTML via `HtmlUtil.escape` (prevenção de XSS)
- Extração segura de parâmetros com `ParamUtil`

---

## Arquitetura

O projeto segue a estrutura **Liferay Workspace** com três módulos OSGi independentes:

```text
DesafioSEA/
├── modules/
│   ├── todo-list-api/          # Interfaces, modelos e utilitários (gerado pelo Service Builder)
│   ├── todo-list-service/      # Implementações de serviço, persistência e testes
│   └── todo-list-web/          # Portlet MVC, JSPs e ActionCommands
├── docker-compose.yml          # Liferay + MySQL via Docker
├── .github/workflows/ci.yml    # Pipeline CI (build + testes no GitHub Actions)
├── build.gradle                # Configuração raiz do workspace
└── settings.gradle             # Inclusão dos módulos
```

### Fluxo de dados

```text
JSP (View)
  → MVCActionCommand (valida e despacha)
    → LocalServiceImpl (regras de negócio)
      → Persistence / Service Builder (SQL gerado)
        → MySQL
```

### Entidades (tabelas com prefixo `SEA_`)

| Entidade   | Descrição                                |
|------------|------------------------------------------|
| `Task`     | Tarefa principal com soft delete         |
| `Category` | Categoria pertencente a um usuário       |
| `Subtask`  | Item de checklist vinculado a uma tarefa |
| `Comment`  | Comentário vinculado a uma tarefa        |

---

## Pré-requisitos

| Ferramenta     | Versão   | Observação                                  |
|----------------|----------|---------------------------------------------|
| JDK            | 17       | Necessário para compilar e rodar o Gradle   |
| Docker Desktop | 4.x      | Para subir os containers de Liferay e MySQL |
| Git            | qualquer | Para clonar o repositório                   |

> O Gradle Wrapper (`gradlew`) já está incluso — não é necessário instalar o Gradle separadamente.

---

## Instalação e Configuração

### 1. Clonar o repositório

```bash
git clone <url-do-repositório>
cd DesafioSEA
```

### 2. Configurar variáveis de ambiente

Copie o arquivo de exemplo e ajuste as credenciais do banco se necessário:

```bash
cp .env.example .env
```

Os valores padrão já funcionam sem alteração para desenvolvimento local.

### 3. Subir os containers

```bash
docker-compose up -d
```

Isso inicia dois serviços:

- **liferay** → `http://localhost:8080` (aguarde ~2 minutos na primeira execução)
- **mysql** → porta `3306`

### 4. Acompanhar a inicialização (opcional)

```bash
docker-compose logs -f liferay
```

Aguarde a mensagem `Server startup in X ms` antes de prosseguir.

---

## Compilação e Deploy

Com o Liferay rodando, execute a partir da raiz do projeto:

```bash
# Build e deploy completo dos três módulos
./gradlew deploy
```

Os arquivos `.jar` são copiados automaticamente para `deploy/`, pasta mapeada no container. O Liferay realiza o hot-deploy em segundos.

### Comandos adicionais

```bash
# Build sem deploy
./gradlew build

# Build de um módulo específico
./gradlew :modules:todo-list-service:build
./gradlew :modules:todo-list-web:build

# Executar testes unitários
./gradlew :modules:todo-list-service:test

# Hot reload do módulo web (sem rebuild completo)
./gradlew :modules:todo-list-web:dockerCopyDeploy
```

### Adicionando o portlet ao Liferay

1. Acesse `http://localhost:8080`
2. **Crie uma conta** pelo botão "Criar conta" na tela inicial
3. Após o login, crie ou edite uma página
4. No menu de widgets, busque por **"Todo List"**
5. Arraste o portlet para a página

> As tabelas do banco (`SEA_Task`, `SEA_Category`, `SEA_Subtask`, `SEA_Comment`) são criadas automaticamente pelo Service Builder no primeiro deploy.

---

## Como Usar

### Tela principal

- O portlet exibe três abas: **Pendentes**, **Atrasadas** e **Concluídas**
- Tarefas com data de vencimento passada aparecem automaticamente em Atrasadas
- A barra lateral lista as categorias com o contador de tarefas de cada uma

### Gerenciando tarefas

- **Nova tarefa:** botão "Nova Tarefa" no cabeçalho — preencha título, descrição, data de vencimento, categoria e imagem (todos opcionais exceto o título)
- **Editar:** ícone de edição na linha da tarefa
- **Concluir / Reabrir:** ícone de check — alterna o status da tarefa
- **Excluir:** remove logicamente; a tarefa some da lista mas permanece no banco (histórico)
- **Detalhe:** clique no título da tarefa para abrir a página com subtarefas e comentários

### Categorias

- Acessadas pelo botão "Categorias" no cabeçalho
- Cada categoria exibe a contagem de tarefas vinculadas
- Ao criar ou editar uma tarefa, selecione a categoria no formulário

### Subtarefas e comentários

- Disponíveis na página de detalhe de cada tarefa
- Subtarefas possuem checkbox individual de conclusão
- Comentários podem ser editados e removidos apenas pelo autor

---

## Testes

Os testes unitários estão em `modules/todo-list-service/src/test/` e utilizam **JUnit 4** e **Mockito** (incluindo `mockito-inline` para mockar métodos estáticos do Liferay).

```bash
./gradlew :modules:todo-list-service:test
```

O pipeline de CI no GitHub Actions executa build e testes automaticamente a cada push nas branches `main` e `dev`.

**Cobertura atual:**

- `TaskLocalServiceImplTest` — criação, atualização, soft delete e toggle de status
- `SubtaskLocalServiceImplTest` — criação, toggle e remoção de subtarefas

---

## Bibliotecas e Decisões Técnicas

### Liferay Service Builder

Gera toda a camada de persistência: interfaces, implementações, utilitários e finders customizados. Elimina boilerplate de acesso a banco e garante consistência com os padrões Liferay.

**Finders customizados (`service.xml`):**

- `UserActiveTasks` — tarefas ativas do usuário (`isDeleted = false`)
- `UserHistoryTasks` — todas as tarefas, incluindo excluídas
- `UserCategories` — categorias por `userId/groupId`
- `TaskSubtasks` / `TaskComments` — relacionamentos por chave estrangeira

### Soft Delete

Tarefas não são removidas fisicamente. O campo `isDeleted` marca a exclusão lógica, preservando o histórico sem tabelas separadas de auditoria.

### Docker Compose

Ambiente de desenvolvimento containerizado com Liferay 7.4.3.112-ga112 e MySQL 8.0. A configuração JDBC é injetada via variáveis de ambiente, sem necessidade de editar arquivos internos do portal.

### Mockito Inline

Utilizado para mockar métodos estáticos do Liferay (ex.: `ServiceContext`, `UserLocalServiceUtil`), viabilizando testes unitários sem a necessidade de um container em execução.

### Segurança por design

Toda operação de escrita verifica `task.getUserId() == themeDisplay.getUserId()` antes de prosseguir, garantindo que um usuário nunca modifique dados de outro, independente de manipulação de parâmetros na requisição.
