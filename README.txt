TOOLS:  ECLIPSE IDE 2024-12 (4.34.0) <BR>
 <BR>
DEPENDENCIES: <BR>
<ul>
  <li> javafx-sdk-21.0.7 <BR>
    LICENSE:  GNU General Public License, version 2, with the Classpath Exception (GPLv2+CE) <BR>
    URL:      https://gluonhq.com/products/javafx/ <BR>
    FILES:    javafx.controls.jar <BR>
              javafx.graphics.jar <BR>
              javafx.base.jar <BR>
   </li>
<BR>
  <li> gson 2.13.1 <BR>
    LICENSE:  Apache 2.0 <BR>
    URL:      https://mvnrepository.com/artifact/com.google.code.gson/gson/2.13.1 <BR>
    FILES:    gson-2.13.1.jar <BR>
    NOTE:     Must rename jar file to gson-2.13.1.jar to deal with Eclipse bug. <BR>
   </li>
<BR>
  <li> JustJ version of Open JDK 23.0.1.v20241024-1700 <BR>
    LICENSE:  Eclipse Public License - v 2.0 <BR>
              GNU General Public License, version 2, with the Classpath Exception (GPLv2+CE)<BR>
   </li>
 </ul>
<BR>
For licenses related to third party libraries see...<BR>
 <ul>
  <li>https://www.eclipse.org/legal/epl-2.0/</li>
  <li>https://openjdk.org/legal/gplv2+ce.html</li>
  <li>https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html</li>
 </ul>
 <BR>
MineMaze itself uses the CC0 1.0 Universal license. <BR>
  https://creativecommons.org/publicdomain/zero/1.0/legalcode.txt <BR>
 <BR>
SUMMARY: <BR>
MineMaze is a java program that lets you graphically draw the walls of a building on a grid. <BR>
You can then generate /fill commands that can be entered into Minecraft that will generate <BR>
the structure you drew. <BR>
The structure includes... <BR>
1)  A ceiling plane. <BR>
2)  Walls <BR>
3)  Fill (what's between the walls, usually air). <BR>
4)  Floor cover (one block tall just above the floor plane) <BR>
5)  A floor plane. <BR>
 <BR>
Any of the five structure elements can be made of any Minecraft block type.  For example, <BR>
dirt, cobblestone, gold blocks, air, wither skulls. <BR>
 <BR>
If you don't need the source code and just want to run the program there is a an installer <BR>
for Windows that will install a standalone version of the program that runs as an EXE file. <BR>
 <BR>
The installer file is MineMaze-1.0.exe in the releases folder. <BR>
 <BR>
The exe file contains all the javafx and java runtime, so its quite large (41MB). <BR>
If you want something smaller, the eclipse project was exported into MineMaze.jar <BR>
(also in the releases folder).  This jar file is only 318kB.  You can run the jar <BR>
file as usual using java.exe and specifying the proper module paths to gson and <BR>
javafx. <BR>
 <BR>
The installer was created using jlink and jpackage (which come with JDK).  If you want to <BR>
build your own installer you can modify /releases/install.bat.  At a minimum you need to <BR>
modify the environment varialbes PathToFx, PathToAppJar to match your situation. <BR>
 <BR>
Please note that MineMaze is not affiliated with Microsoft or Mojang in any way. <BR>

 
