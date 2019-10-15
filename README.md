#  WS281X Music Visualizer Server

This is a Java server application for Raspberry Pi. 
The server controls WS281X led strip based on the data that
is received from the client application running on the PC.

NetBeans IDE is required for this project.

The client application:

https://github.com/ospekki/music-visualizer-client


## Creating and adding JAR files

The Java server requires the following jar files to work:

- log4j-api.2.10.0.jar
- log4j-core.2.10.0.jar
- rpi-ws281x-java-2.0.0-SNAPSHOT-javadoc.jar
- rpi-ws281x-java-2.0.0-SNAPSHOT-sources.jar
- rpi-ws281x-java-2.0.0-SNAPSHOT.jar

log4j jar files are in the “lib” folder. 
You must create rpi-ws281x jar files that are compatible with your Raspberry Pi by following the instructions below.

You must have the following tools installed on your Raspberry Pi:

- **swig**

	sudo apt-get install swig
- **ar, ranlib**

	sudo apt-get install binutils
- **gcc**

	sudo apt-get install gcc
- **git**

	sudo apt-get install git
     

Open the terminal and clone the following repo to your Raspberry Pi:

	git clone https://github.com/rpi-ws281x/rpi-ws281x-java.git
     
![alt text](https://drive.google.com/uc?export=download&id=1e8JB_qJu9P3f6j6e0YO65wK9I5IIFv7A)

Change the directory:

	cd rpi-ws281x-java/

![alt text](https://drive.google.com/uc?export=download&id=16K6HZT6XULu_281fBeLx_Z1KK4egvTBG)

Next, run the following command:

     sudo bash src/scripts/createNativeLib.sh

![alt text](https://drive.google.com/uc?export=download&id=1JYJ7prt60syfgMaW0U46EuU-5DjK_GM_)

If you have all the required tools installed (swig, gcc, ar, ranlib, git) and it still says “cannot continue!”, open the createNativeLib.sh file with text editor:

     sudo nano src/scripts/createNativeLib.sh

Comment all lines starting with “programInstalled” by adding “#” then save.

![alt text](https://drive.google.com/uc?export=download&id=1Ux-ZvKMZc7QZKb5TrEv4hIP6nEBCSsxN)

Try running the following command again:

     sudo nano src/scripts/createNativeLib.sh
     
![alt text](https://drive.google.com/uc?export=download&id=1o7jjDjAXNz9k19OWOSOL-pIeI4R_ZGuV)

Then run this command to compile the java code:

     sudo bash ./gradlew assemble -x signArchives
     
![alt text](https://drive.google.com/uc?export=download&id=1cEvJVKexmYzlDOVcNYB0SAmityar8kDo)

Three jar files are now created in the /build/libs folder. Move these files to your PC.

![alt text](https://drive.google.com/uc?export=download&id=1ZNbN2L2P18qQKz8NCpjgdLcMXvO-jkBm)

![alt text](https://drive.google.com/uc?export=download&id=1U7CVNRaeO-MQeL-iYcKKMetDwNtwIXM7)

Now start NetBeans on your PC and click “File”, “Open Project…” and open the “music-visualizer-server” project.

![alt text](https://drive.google.com/uc?export=download&id=1OHGsb0r99cZqjoj0lA1anPmZRvnPDtiF)

![alt text](https://drive.google.com/uc?export=download&id=12baq2DfPD9cOsRYfRUlqpeGJZM_P2zqD)

Right-click on the “Libraries” folder and select “Add JAR/Folder…”.

![alt text](https://drive.google.com/uc?export=download&id=1yBSs1G7-j8Ohx69Tqfbavlfgt6-6ATTz)

Select all three rpi-ws281x jar files you created on your Raspberry Pi and select the log4j jar files that are in the “music-visualizer-server\lib” folder.

![alt text](https://drive.google.com/uc?export=download&id=1ht0IJjm5hI8q1kLiFTGWCcjtzKSu1yV1)

You now have all the required jar files for the server



## Building and running the server

Right-click on the "MusicVisualizerServer" and select “Properties”.

![alt text](https://drive.google.com/uc?export=download&id=1smqB9KCs4QAle0_k_E6ooc8cpBdgDaIC)

Click “Packaging” and check “Compress JAR file” and “Build JAR after compiling”. Click “OK” to accept changes.

![alt text](https://drive.google.com/uc?export=download&id=1giMRot35Z7-9ZffdeaFFNjt9aiIuE-xp)

Right-click on the MusicVisualizerServer and select “Build”.

![alt text](https://drive.google.com/uc?export=download&id=1mRlFuQCty6ibD9Pu4AcHyI869p2XH-Ps)

Build files are created in the “music-visualizer-server\dist” folder. Move the jar file and the “lib” folder to your Raspberry Pi.

![alt text](https://drive.google.com/uc?export=download&id=1XX83bhNYrEe5nB1zKXnQ-2zxM2MSGFL8)

Open the terminal and run the following command to start the server:

         sudo java -jar MusicVisualizerServer.jar


## Wiring

- Connect the data wire from the strip to the pin 12 (GPIO 18) on the Raspberry Pi.
- Connect the positive wire from the strip to the external power supply.
- Connect the negative wire from the strip to the pin 6 (GND) on the Raspberry Pi and to the external power supply.

![alt text](https://drive.google.com/uc?export=download&id=1793l4Eo0S6mbcjvK6K3G7LOf6rSVdCns)
