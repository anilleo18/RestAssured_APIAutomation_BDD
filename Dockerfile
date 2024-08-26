# Base image with OpenJDK 8 runtime
FROM openjdk:8-jre-slim

# Set the working directory inside the container
WORKDIR /usr/share/myapp

# Copy the necessary JAR files from the host to the container
COPY target/rest-assured-apis.jar                 /usr/share/myapp/rest-assured-apis.jar
COPY target/rest-assured-apis-tests.jar           /usr/share/myapp/rest-assured-apis-tests.jar
COPY target/libs                                  /usr/share/myapp/libs

# Copy the XML suite files
COPY xml-suites/                                  /usr/share/myapp/

# Include additional resources like properties and data files
COPY src/test/resources/properties/config.properties /usr/share/myapp/config.properties
COPY src/test/resources/jsonfiles/librarybook.json   /usr/share/myapp/librarybook.json
COPY src/test/resources/cucumber.properties          /usr/share/myapp/cucumber.properties

# Define the entry point to execute TestNG with the specified suite
ENTRYPOINT ["java", "-cp", "rest-assured-apis.jar:rest-assured-apis-tests.jar:libs/*", "org.testng.TestNG", "$TESTSUITE"]
