@echo off

rem Check for a valid filename
IF "%1"=="" (
	ECHO You have to provide a valid source file.
	EXIT /b
)

IF "%2"=="" (
	ECHO You have to provide a valid destination path.
	EXIT /b
)

SET PUTTYSCP_BIN="D:\Program Files (x86)\PuTTY\pscp.exe"
SET PUTTY_BIN="D:\Program Files (x86)\PuTTY\putty.exe"
SET RASPBERRYPI_ADDR=192.168.0.100
SET USERNAME=pi
SET PASSWORD=raspberry
SET CMD_FILENAME=commands.sh

rem Upload the file to raspberry pi
%PUTTYSCP_BIN% -pw %PASSWORD% "%1" %USERNAME%@%RASPBERRYPI_ADDR%:"%2"

rem Build a list of actions to do on the pi (chmod, execute GDB server)
if exist %~dp0%CMD_FILENAME% del %~dp0%CMD_FILENAME%
rem echo rm "%2" >> %~dp0%CMD_FILENAME%
echo chmod +x "%2" >> %~dp0%CMD_FILENAME%
echo gdbserver :3785 "%2" >> %~dp0%CMD_FILENAME%

rem Execute the action list on the raspberry pi
%PUTTY_BIN% -pw %PASSWORD% -m %~dp0%CMD_FILENAME% %USERNAME%@%RASPBERRYPI_ADDR%

exit /b %ERRORLEVEL%