# ============= DEPENDENCY RESOLVER =============
# Download the dependencies on container
# ===============================================

FROM maven:3.9.8-eclipse-temurin-21 AS dependency_resolver

# Download all library dependencies
COPY ./pom.xml /app/pom.xml

WORKDIR /app

RUN mvn dependency:copy-dependencies $MAVEN_CLI_OPTS

# ============= TESTING =================
# Run tests on container
# =======================================

FROM dependency_resolver AS tester

WORKDIR /app

CMD mvn clean test $MAVEN_CLI_OPTS

# ============= BUILDER =================
# Build the artifact on container
# =======================================

FROM dependency_resolver as builder

# Build application
COPY . /app

RUN mvn package $MAVEN_CLI_OPTS -Dmaven.test.skip=true

# ============= DOCKER IMAGE ================
# Prepare container image with application artifacts
# ===========================================

FROM eclipse-temurin:21-jre

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]