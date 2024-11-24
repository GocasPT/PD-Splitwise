@echo off

set DB_FOLDER=.\db\BackupServer

echo Running BackupServer with DB_FOLDER: %DB_FOLDER%

cd ..
call ./gradlew :BackupServer:run --console=plain --args="%DB_FOLDER%" -Dlog.level=DEBUG

pause