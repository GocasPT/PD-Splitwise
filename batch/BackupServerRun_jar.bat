@echo off

set DB_FILE=./db/BackupServer

echo Running JAR BackupServer with DB_FILE: %DB_FILE%

cd ..
call  ./gradlew :BackupServer:run --args="%DB_FILE%" -Dlog.level=DEBUG

pause