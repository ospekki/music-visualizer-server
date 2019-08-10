# music-visualizer-server

This is a Java server application for Raspberry Pi. 
The server controls WS281X led strip based on the data that
is received from the client application running on the PC.

NetBeans IDE is required for this project.


## Creating and adding JAR files

The Java server requires the following jar files to work:

- log4j-api.2.10.0.jar
- log4j-core.2.10.0.jar
- rpi-ws281x-java-2.0.0-SNAPSHOT-javadoc.jar
- rpi-ws281x-java-2.0.0-SNAPSHOT-sources.jar
- rpi-ws281x-java-2.0.0-SNAPSHOT.jar

log4j jar files are in the “music-visualizer-server\lib” folder. 
You must create rpi-ws281x jar files that are compatible with your Raspberry Pi by following the instructions below.

You must have the following tools installed on your Raspberry Pi:

**swig
  sudo apt-get install swig
ar, ranlib
  sudo apt-get install binutils
gcc
  sudo apt-get install gcc
git
  sudo apt-get install git**
