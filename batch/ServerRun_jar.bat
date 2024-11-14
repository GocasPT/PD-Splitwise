@echo off

set PORT=6000
set DB_FILE=./db/Server/database.db

echo Running JAR Server with PORT: %PORT%

cd ..
call  ./gradlew :Server:run --args="%IP% %DB_FILE%" -Dlog.level=DEBUG

pause