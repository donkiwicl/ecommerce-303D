FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /cart

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /cart

RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring

COPY --from=build /cart/target/*.jar cart.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "cart.jar"]