FROM eclipse-temurin:24-jdk AS build
WORKDIR /app
COPY . .


RUN chmod +x ./mvnw


RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:24-jdk
WORKDIR /app
COPY --from=build /app/target/eduhive-backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]