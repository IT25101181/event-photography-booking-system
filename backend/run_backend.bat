@echo off
echo Starting PhotoBooking Backend...
echo -------------------------------
echo Checking Maven Wrapper...
if not exist "mvnw.cmd" (
    echo Error: mvnw.cmd not found in this directory.
    pause
    exit /b
)

echo Compiling and Running Spring Boot...
call .\mvnw.cmd spring-boot:run
pause
