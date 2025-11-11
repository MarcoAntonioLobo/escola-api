# Imagem com JDK 17 + Maven
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copia pom.xml e baixa dependências
COPY pom.xml .

RUN mvn dependency:go-offline -B

# Copia o restante do código
COPY src ./src

# Build da aplicação
RUN mvn clean package -DskipTests

# Segunda fase: imagem mínima apenas com JRE
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]