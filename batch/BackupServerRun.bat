@echo off

set DB_FILE=./db/BackupServer

echo Running BackupServer with DB_FILE: %DB_FILE%

cd ..
call ./gradlew :BackupServer:run --console=plain --args="%DB_FILE%" -Dlog.level=DEBUG

pause