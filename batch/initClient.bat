@echo off

set ip=localhost
set port=6000

echo Stating up Client on %ip%:%port%

java -jar ../target/Client.jar %ip% %port%

pause