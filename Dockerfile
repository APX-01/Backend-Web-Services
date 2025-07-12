# ------------------------------------------------------------
# 1️⃣ Imagen base oficial con JDK 17 (ajusta si usas otro)
FROM eclipse-temurin:17-jdk-alpine

# ------------------------------------------------------------
# 2️⃣ Directorio de trabajo dentro del contenedor
WORKDIR /app

# ------------------------------------------------------------
# 3️⃣ Copiar el JAR compilado desde tu máquina local
# Asegúrate de haber ejecutado: ./mvnw clean package (o ./gradlew bootJar)
# Esto generará el archivo JAR en la carpeta target/
COPY target/eduhive-backend.jar app.jar

# ------------------------------------------------------------
# 4️⃣ Expone el puerto 8080 (el de Spring Boot por defecto)
EXPOSE 8080

# ------------------------------------------------------------
# 5️⃣ Configura el entrypoint para ejecutar el JAR
ENTRYPOINT ["java","-jar","/app/app.jar"]