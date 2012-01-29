set MAVEN_OPTS=%MAVEN_OPTS% -Xmx512m
call cd ../sklep3-core
call mvn clean install
call cd ../sklep3-common-web
call mvn clean install
call cd ../sklep3-rich-admin
mvn clean tomcat:run
call pause