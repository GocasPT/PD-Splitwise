@echo off

echo Compiling java files...

javac -d ../out/ ^
    ../src/*.java ^
    ../src/Message/Request/*.java ^
    ../src/Message/Response/*.java

echo Compilation finished