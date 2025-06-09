FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

# ⬇️ gradlew 실행 권한 부여
RUN chmod +x gradlew

# ⬇️ build (test 제외)
RUN ./gradlew clean build -x test

WORKDIR /app/build/libs

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "Jooksop-0.0.1-SNAPSHOT.jar"]
