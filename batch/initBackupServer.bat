@echo off

set database_path=../database/database.db

echo Starting up Backup Server with database at %database_path%...

java -cp "../out/" BackupServer.BackupServer %database_path%