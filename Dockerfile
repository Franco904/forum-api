FROM eclipse-temurin:17
EXPOSE 8080
ADD /build/libs/forum-api-0.0.1-SNAPSHOT.jar forum.jar
ENTRYPOINT ["java", "-jar", "forum.jar"]
