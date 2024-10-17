@echo off

set port=6000
set database_path=../src/databasefiles/database.db

echo Stating up Server on %port% with database at %database_path%

java -cp "../out/;../drivers/sqlite-jdbc-3.46.1.3.jar" Server.Server %port% %database_path%

pause