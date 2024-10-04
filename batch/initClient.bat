@echo off

set ip=localhost
set port=6000

echo Stating up Client on %ip%:%port%

java -cp "../out" Client %ip% %port%