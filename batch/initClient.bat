@echo off

set ip=localhost
set port=6000

echo Stating up Client on %ip%:%port%

java -jar ../Client/target/Client-1.0.jar %ip% %port%

pause