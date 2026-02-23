# Usa una imagen oficial de Maven con Java 21 para construir la aplicación
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia el POM y descarga las dependencias primero (mejora el cache de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el resto del código y compila el JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Usa una imagen ligera de Java 21 para ejecutar la aplicación
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia el JAR generado en la etapa de construcción
COPY --from=build /app/target/logisticaMovil-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto que usará Render (Render inyecta la variable $PORT automáticamente)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
