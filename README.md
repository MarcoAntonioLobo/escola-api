# 📚 Escola Metrics Project

## 🌟 Descrição do Projeto

O **Escola Metrics Project** é uma aplicação completa para gerenciamento de métricas escolares, permitindo acompanhar clientes (escolas), alunos registrados, receitas, despesas e indicadores de performance de forma prática e visual. Ideal para gestores que querem insights rápidos e decisões baseadas em dados.

A aplicação conta com um **backend robusto em Java/Spring Boot**, **frontend moderno em React** e **banco de dados MySQL**, tudo containerizado via **Docker**, facilitando o setup e deployment.

## 🛠 Tecnologias Utilizadas

- **Backend:** Java, Spring Boot, JPA, Lombok, Maven
- **Frontend:** React, Tailwind CSS, Lucide Icons
- **Banco de Dados:** MySQL
- **Ferramentas:** Docker, Docker Compose
- **Controle de versão:** Git

## 🚀 Funcionalidades

- Cadastro e gestão de clientes/escolas
- Registro mensal de métricas: receita, despesas, número de alunos registrados
- Dashboard com filtros por cliente e mês/ano
- Ordenação, paginação, exportação CSV e impressão de métricas
- Interface intuitiva e responsiva
- Backend estruturado com DTOs, serviços e repositórios

## 📦 Pré-requisitos

- Docker >= 24
- Docker Compose >= 2.20
- Git
- Navegador moderno (Chrome, Firefox, Edge)

## 🐳 Rodando a aplicação via Docker

1. Clone o repositório:

```
git clone https://github.com/MarcoAntonioLobo/escola-api.git
cd escola-api

```

2. Build e start dos containers:

```
docker-compose up --build
```

3. Acesse a aplicação:

```
- Frontend: http://localhost:8080
- API Backend: http://localhost:8080/api/clients
```
4. Parar a aplicação:

```
docker-compose down
```
## 💻 Rodando Localmente (Opcional)

### Backend
1. Configure seu banco de dados MySQL local (porta 3306) e atualize o `application.properties` ou `application.yml` do backend com as credenciais corretas:

```
properties
url: jdbc:mysql://db:3306/escola?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: escola_user
    password: escola_pass
```

2. Navegue até a pasta root e execute:

```
mvn clean install
mvn spring-boot:run
O backend estará disponível em: http://localhost:8080/api/clients
```

### Frontend

1. Navegue até a pasta do frontend:

```
cd frontend
npm install
npm start
O frontend será iniciado em: http://localhost:3000
```

⚠️ Observação: Ao rodar localmente, certifique-se de que o MySQL está ativo e as credenciais estão corretas no backend.

## 🧪 Testes

### Backend
- Testes unitários com JUnit e Mockito:

```
mvn test
```

## ✨ Autor

- **Marco Lobo** – Desenvolvedor Fullstack
- LinkedIn: [Clique Aqui](https://www.linkedin.com/in/marco-antonio-lobo-35568628b/)
- GitHub: [Clique Aqui](https://github.com/MarcoAntonioLobo)

---
