@echo off

set database_path=../db/BackupServer/

set unique_folder=%database_path%\%RANDOM%_%DATE:~10,4%%DATE:~4,2%%DATE:~7,2%_%TIME:~0,2%%TIME:~3,2%%TIME:~6,2%
mkdir %unique_folder%

echo Starting up Backup Server with database at '%unique_folder%'...

java -jar ../BackupServer/target/BackupServer-1.0.jar %unique_folder%

rmdir /s /q %unique_folder%

pause