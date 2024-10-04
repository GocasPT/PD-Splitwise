@echo off

set port=6000

echo Stating up Server on %port%

java -cp "../out" Server %port%