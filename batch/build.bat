@echo off

echo Compiling java files...

javac -d ../out/ ^
    ../src/Data/*.java ^
    ../src/Client/*.java ^
    ../src/Client/ui/TUI/*.java ^
    ../src/Server/*.java ^
    ../src/BackupServer/*.java ^
    ../src/Message/*.java ^
    ../src/Message/Request/*.java ^
    ../src/Message/Request/User/*.java ^
    ../src/Message/Request/Group/*.java ^
    ../src/Message/Request/Payment/*.java ^
    ../src/Message/Request/Expense/*.java ^
    ../src/Message/Response/*.java

echo Compilation finished

pause