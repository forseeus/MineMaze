TOOLS:  ECLIPSE IDE 2024-12 (4.34.0)

DEPENDENCIES:  
--  NAME:     javafx-sdk-21.0.7
    LICENSE:  GNU General Public License, version 2, with the Classpath Exception (GPLv2+CE)
    URL:      https://gluonhq.com/products/javafx/
    FILES:    javafx.controls.jar
              javafx.graphics.jar
              javafx.base.jar
--  NAME:     gson 2.13.1
    LICENSE:  Apache 2.0
    URL:      https://mvnrepository.com/artifact/com.google.code.gson/gson/2.13.1
    FILES:    gson-2.13.1.jar
    NOTE:     Must rename jar file to gson-2.13.1.jar to deal with Eclipse bug.
--  NAME:     JustJ version of Open JDK 23.0.1.v20241024-1700
    LICENSE:  GNU General Public License, version 2
              GNU General Public License, version 2, with the Classpath Exception (GPLv2+CE)

SUMMARY:
MineMaze is a java program that lets you graphically draw the walls of a building on a grid.
You can then generate /fill commands that can be entered into Minecraft that will generate
the structure you drew.
The structure includes...
1)  A ceiling plane.
2)  Walls
3)  Fill (what's between the walls, usually air).
4)  Floor cover (one block tall just above the floor plane)
5)  A floor plane.

Any of the five structure elements can be made of any Minecraft block type.  For example,
dirt, cobblestone, gold blocks, air, wither skulls.

If you don't need the source code and just want to run the program there is a an installer
for Windows that will install a standalone version of the program that runs as an EXE file.

The installer file is MineMaze-1.0.exe in the releases folder.

The exe file contains all the javafx and java runtime, so its quite large (41MB).
If you want something smaller, the eclipse project was exported into MineMaze.jar
(also in the releases folder).  This jar file is only 318kB.  You can run the jar
file as usual using java.exe and specifying the proper module paths to gson and
javafx.

The installer was created using jlink and jpackage (which come with JDK).  If you want to
build your own installer you can modify /releases/install.bat.  At a minimum you need to
modify the environment varialbes PathToFx, PathToAppJar to match your situation.

Please note that MineMaze is not affiliated with Microsoft or Mojang in any way.

 
