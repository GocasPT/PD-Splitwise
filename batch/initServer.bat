@echo off

set port=6000
set database_path=../db/Server/database.db

echo Stating up Server on %port% with database at '%database_path%'

java -DLOG_LEVEL=DEBUG -jar ../Server/target/Server-1.0.jar %port% %database_path%

pause