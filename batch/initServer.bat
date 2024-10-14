@echo off

set port=6000
set database_path=../database/database.db

echo Stating up Server on %port% with database at %database_path%...

java -cp "../out/" Server.Server %port% %database_path%