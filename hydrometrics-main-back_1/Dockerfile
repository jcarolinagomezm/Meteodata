# Etapa de construcción
FROM maven:3.8.4-openjdk-17-slim AS builder

WORKDIR /app

# Copiar los archivos de configuración y de proyecto
COPY pom.xml .
COPY src ./src

# Instalar dependencias y construir el proyecto
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:17-jdk-slim

WORKDIR /app

# Establecer variables de entorno necesarias
ENV DB_HOST=
ENV DB_PORT=3306
ENV DB_NAME=hydrometrics
ENV DB_USER=root
ENV DB_PASSWORD=

# Copiar el jar construido desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto de la aplicación
EXPOSE 8080

# Comando de entrada para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
