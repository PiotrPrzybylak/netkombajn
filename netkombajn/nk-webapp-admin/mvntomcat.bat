set MAVEN_OPTS=%MAVEN_OPTS% -Xmx512m
mvn clean test tomcat:run
call pause