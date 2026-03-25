# Todo List Portlet - Desafio SEA

Portlet de gerenciamento de tarefas desenvolvido para o Desafio SEA, construido sobre **Liferay 7.4.3.112-ga112** com arquitetura MVC Portlet e persistencia gerada pelo Service Builder.

---

## Indice

- [Visao geral](#visao-geral)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Pre-requisitos](#pre-requisitos)
- [Instalacao e execucao](#instalacao-e-execucao)
- [Build e deploy dos modulos](#build-e-deploy-dos-modulos)
- [Como adicionar o portlet](#como-adicionar-o-portlet)
- [Testes](#testes)
- [Observacoes importantes](#observacoes-importantes)

---

## Visao geral

Aplicacao de todo list rodando como portlet no Liferay. Cada usuario possui seu proprio espaco isolado de tarefas, com suporte a categorias, subtarefas, comentarios, filtros e historico de exclusao logica.

**Stack principal:** Java 17, Liferay 7.4.3.112, Gradle, MySQL 8, JSP, Bootstrap

---

## Funcionalidades

- Criacao, edicao e exclusao logica de tarefas
- Marcacao de tarefas como concluidas
- Categorias por usuario
- Subtarefas vinculadas a cada tarefa
- Comentarios por tarefa
- Isolamento de dados por usuario
- Validacoes de seguranca nas operacoes de escrita

---

## Arquitetura

O projeto segue o formato de **Liferay Workspace** com tres modulos OSGi:

```text
DesafioSEA/
|-- modules/
|   |-- todo-list-api/      # Interfaces, modelos e utilitarios
|   |-- todo-list-service/  # Regras de negocio e persistencia
|   `-- todo-list-web/      # Portlet MVC, JSPs e commands
|-- docker-compose.yml
|-- build.gradle
|-- settings.gradle
`-- README.md
```

Fluxo principal:

```text
JSP
 -> MVCActionCommand
   -> LocalServiceImpl
     -> Persistence / Service Builder
       -> MySQL
```

---

## Pre-requisitos

Para rodar o projeto localmente, um novo usuario precisa de:

- Docker Desktop
- JDK 17 instalado
- Git
- Internet na primeira execucao do Gradle Wrapper

O projeto usa o `gradlew` incluido no repositorio, entao nao e necessario instalar Gradle manualmente.

---

## Instalacao e execucao

### 1. Clonar o repositorio

```bash
git clone <url-do-repositorio>
cd DesafioSEA
```

### 2. Configurar as variaveis de ambiente

Copie o arquivo de exemplo:

```bash
cp .env.example .env
```

No Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

Os valores padrao do arquivo de exemplo ja sao suficientes para desenvolvimento local.

### 3. Subir os containers

```bash
docker compose up -d
```

Isso sobe:

- `todolist-liferay` em `http://localhost:8080`
- `mysql-todolist` na porta `3306`

### 4. Esperar o Liferay terminar de subir

Opcionalmente acompanhe os logs:

```bash
docker compose logs -f liferay
```

Espere a inicializacao terminar antes de fazer o deploy dos modulos.

---

## Build e deploy dos modulos

**Importante:** apenas subir o Docker **nao** faz o portlet aparecer.

O container do Liferay nao compila os modulos Java automaticamente. Depois que os containers estiverem no ar, e obrigatorio publicar os bundles OSGi do projeto.

### Comando recomendado

Da raiz do projeto, execute:

```bash
./gradlew dockerDeployAll
```

No Windows PowerShell:

```powershell
.\gradlew.bat dockerDeployAll
```

Esse comando envia os tres modulos necessarios para o container:

- `todo-list-api`
- `todo-list-service`
- `todo-list-web`

Sem isso, o Liferay pode exibir mensagens como:

```text
This portlet could not be found. Please redeploy it or remove it from the page.
```

### Outros comandos uteis

```bash
# Build completo
./gradlew build

# Testes do modulo service
./gradlew :modules:todo-list-service:test

# Deploy apenas do modulo web
./gradlew :modules:todo-list-web:dockerCopyDeploy
```

No fluxo normal, prefira sempre `dockerDeployAll`.

---

## Como adicionar o portlet

Depois do deploy:

1. Acesse `http://localhost:8080`
2. Crie uma conta ou faca login
3. Entre em uma pagina editavel
4. Abra o painel de widgets
5. Busque por **Todo List**
6. Arraste o portlet para a pagina

Se a pagina tiver uma instancia antiga quebrada, remova o bloco com erro e adicione o portlet novamente apos o deploy.

---

## Testes

Os testes unitarios estao no modulo `todo-list-service` e usam **JUnit 4** e **Mockito**.

```bash
./gradlew :modules:todo-list-service:test
```

No Windows PowerShell:

```powershell
.\gradlew.bat :modules:todo-list-service:test
```

---

## Observacoes importantes

- O primeiro boot do Liferay pode demorar varios minutos.
- O compose deste projeto desabilita sample data e aumenta a janela do healthcheck para evitar `unhealthy` durante a carga inicial.
- O portlet depende dos bundles `api`, `service` e `web`; deploy parcial pode fazer o widget sumir do menu do Liferay.
- Se voce alterar codigo de modulo, rode novamente `dockerDeployAll`.
- As tabelas do banco sao criadas automaticamente no primeiro deploy dos modulos.

### Resumo rapido

Para um usuario novo, o fluxo correto e:

```powershell
Copy-Item .env.example .env
docker compose up -d
.\gradlew.bat dockerDeployAll
```
