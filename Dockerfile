# -----------------------------
# Etapa 1: Build do frontend (Vite)
# -----------------------------
FROM node:22 AS frontend-build

WORKDIR /app/frontend

# Copia apenas o que é necessário para instalar deps
COPY frontend/package*.json ./
RUN npm ci

# Copia todo o projeto frontend
COPY frontend/ ./

# Build
RUN npm run build


# -----------------------------
# Etapa 2: Build do backend (Spring Boot)
# -----------------------------
FROM maven:3.9.5-eclipse-temurin-17 AS backend-build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código do backend
COPY src ./src

# Copia dist do frontend para static
COPY --from=frontend-build /app/frontend/dist ./src/main/resources/static

# Compila
RUN mvn clean package -DskipTests


# -----------------------------
# Etapa 3: Runtime (produção)
# -----------------------------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia JAR gerado
COPY --from=backend-build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
