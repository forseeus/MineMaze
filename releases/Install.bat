REM Download WiX 3.0 or later from https://wixtoolset.org and add it to the PATH.

ECHO OFF
ECHO STARTING...
cls
set RuntimeFolder=myapp-runtime
set PathToFx=C:/Program Files/Java/javafx-jmods-21.0.7
set PathToAppJar=.
set ModulePath="%PathToFx%;%PathToAppJar%"

cd %PathToAppJar%
rmdir "%RuntimeFolder%" /s /q

ECHO LINKING...
jlink --module-path %ModulePath% --add-modules mineMaze,javafx.controls,javafx.fxml --output %RuntimeFolder% --strip-debug --compress=2 --no-header-files --no-man-pages

ECHO PACKAGING...
jpackage --name MineMaze --app-version 1.0 --runtime-image myapp-runtime --input %PathToAppJar% --main-jar MineMaze.jar --main-class mineMaze.MineMazeApp --type exe

REM ECHO RUNNING INSTALLER...
REM Make sure MineMaze is not already installed or the installer will fail.
REM MineMaze-1.0.exe



ECHO DONE...
pause
EXIT