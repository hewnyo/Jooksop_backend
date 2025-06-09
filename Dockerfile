FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test

WORKDIR /app/build/libs

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "Jooksop-0.0.1-SNAPSHOT.jar"]
