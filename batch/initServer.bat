@echo off

set port=6000
set database_path=../db/Server/database.db

echo Stating up Server on %port% with database at '%database_path%'

java -DLOG_LEVEL=DEBUG -jar ../target/Server.jar %port% %database_path%

pause