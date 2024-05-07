# Используйте официальный образ OpenJDK для базового слоя
FROM openjdk:17-jdk-slim as build

# Установите рабочую директорию в контейнере
WORKDIR /workspace/app

# Скопируйте Maven файлы проекта в контейнер
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Скопируйте исходные коды проекта
COPY src src

# Соберите приложение с помощью Maven и упакуйте его в JAR файл
RUN ./mvnw install -DskipTests

# Вторая стадия сборки для уменьшения размера образа
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
