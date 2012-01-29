call set MAVEN_OPTS=%MAVEN_OPTS% -Xmx512m
call mvn test tomcat:run
call pause