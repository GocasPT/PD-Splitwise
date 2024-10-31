@echo off

set database_path=../BackupServer/src/main/java/databasefiles/database.db

echo Starting up Backup Server with database at '%database_path%'...

java -jar ../BackupServer/target/BackupServer-1.0.jar %database_path%

pause