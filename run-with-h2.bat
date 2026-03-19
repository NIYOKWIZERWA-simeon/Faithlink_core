@echo off
echo Starting FaithLink Core API with H2 Database...
echo.

echo Checking if MySQL is available...
net start | findstr /i mysql >nul
if %errorlevel% == 0 (
    echo MySQL service is running, using MySQL database...
    java -jar build\libs\core-0.0.1-SNAPSHOT.jar
) else (
    echo MySQL not found, trying to run with embedded configuration...
    echo Note: The current JAR was built before H2 was added.
    echo You may need to rebuild the project for H2 support.
    echo.
    echo Attempting to run anyway...
    java -jar build\libs\core-0.0.1-SNAPSHOT.jar --spring.datasource.url=jdbc:h2:mem:faithlink_db --spring.datasource.driver-class-name=org.h2.Driver --spring.jpa.hibernate.ddl-auto=create-drop --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
)

pause
