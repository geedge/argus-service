@echo off
rem /**
rem  * Copyright &copy;  All rights reserved.
rem  *
rem  * Author: darnell
rem  */
echo.
echo [info] rebuilding your DB and clearing data......
echo.
pause
echo.
echo [INFO] This operation will clear your data and restore initial state
echo.
echo [WARN] Do you confirm the continuation ? Otherwise, please close the window
echo.
pause
echo.
echo [WARN] Do you confirm the continuation? Otherwise, please close the window
echo.
pause
echo.

cd %~dp0
cd ..

call mvn antrun:run -Pinit-db

cd db
pause