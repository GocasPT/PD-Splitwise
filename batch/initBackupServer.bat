@echo off

set database_path=../db/BackupServer

echo Starting up Backup Server with database at '%database_path%'...

java -DLOG_LEVEL=DEBUG -jar ../target/BackupServer.jar %database_path%

pause