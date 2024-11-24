@echo off

set DB_FOLDER=.\db\BackupServer

:: TODO: create custom folder for backup db files

echo Running BackupServer with DB_FOLDER: %DB_FOLDER%

cd ..
call ./gradlew :BackupServer:run --console=plain --args="%DB_FOLDER%" -Dlog.level=DEBUG

pause