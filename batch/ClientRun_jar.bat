@echo off

set IP=localhost
set PORT=6000

echo Running JAR Client with IP: %IP% and PORT: %PORT%

cd ..
call  ./gradlew :Client:run --args="%IP% %PORT%"

pause