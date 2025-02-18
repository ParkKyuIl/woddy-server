# Debian 기반 OpenJDK 이미지 사용
FROM openjdk:17-jdk-slim


# JAR 파일과 리소스 복사
COPY build/libs/demo-0.0.1-SNAPSHOT.jar /app/woddy-server.jar
COPY src/main/resources/workouts.csv /app/resources/workouts.csv

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/woddy-server.jar"]
