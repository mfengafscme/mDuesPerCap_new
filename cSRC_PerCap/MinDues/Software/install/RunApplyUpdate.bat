REM
REM Process Apply Update
REM

SET UTILJAR=.\eap_util.jar
SET ARGS=http://localhost:8080
SET CLASSPATH=%CLASSPATH%
SET JAVA_OPTS=-Xms512m -Xmx512m

java %JAVA_OPTS% -cp %UTILJAR% org.afscme.enterprise.update.ApplyUpdateRequest %ARGS%
