#  WS281X Music Visualizer Server

This is a Java server application for Raspberry Pi. 
The server controls WS281X led strip based on the data that
is received from the client application running on the PC.

NetBeans IDE is required for this project.

The client application:

https://github.com/ospekki/music-visualizer-client

Uses rpi281x wrapper for Java by mbelling
https://github.com/rpi-ws281x/rpi-ws281x-java


[![alt text](https://www.dropbox.com/s/f71dd5qi3kmyc3o/Raspberry_Pi_WS2812B_LED_strip_music_visualizer.gif?raw=1)](https://www.youtube.com/watch?v=hVwcQ8_BEE8)

## Adding JAR files to the project

The Java server requires the following jar files to work:

- log4j-api.2.10.0.jar
- log4j-core.2.10.0.jar
- rpi-ws281x-java-2.0.0-SNAPSHOT-javadoc.jar
- rpi-ws281x-java-2.0.0-SNAPSHOT-sources.jar
- rpi-ws281x-java-2.0.0-SNAPSHOT.jar

log4j jar files are in the “lib” folder. 
You must create rpi-ws281x jar files that are compatible with your Raspberry Pi by following the instructions below.

Install the following tools on your Raspberry Pi:

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
     
![alt text](https://www.dropbox.com/s/1cvw0s0ezvv9r37/image002.png?raw=1)

Change the directory:

	cd rpi-ws281x-java/

![alt text](https://www.dropbox.com/s/yh4r8x71q5kg0yo/image003.png?raw=1)

Run command:

     sudo bash src/scripts/createNativeLib.sh

![alt text](https://www.dropbox.com/s/sy0z6e35p7sf579/image004.png?raw=1)

If you have all the required tools installed (swig, gcc, ar, ranlib, git) and it still says “cannot continue!”, open the createNativeLib.sh file with text editor:

     sudo nano src/scripts/createNativeLib.sh

Comment all starting with “programInstalled” by adding “#” at the beginning of the lines then save.

![alt text](https://www.dropbox.com/s/41wnx4rgessk9fj/image005.png?raw=1)

Try running the following command again:

     sudo bash src/scripts/createNativeLib.sh
     
![alt text](https://www.dropbox.com/s/u3eiw15y3kzuq4q/image006.png?raw=1)

Then run this command to compile the java code:

     sudo bash ./gradlew assemble -x signArchives
     
![alt text](https://www.dropbox.com/s/iyrda4q0y3itpeh/image007.png?raw=1)

Three jar files are now created in the /build/libs folder. Move these files to your PC.

![alt text](https://www.dropbox.com/s/cmwdhdt1wwr0uwt/image008.png?raw=1)

![alt text](https://www.dropbox.com/s/8g20e5b26g86whk/image009.png?raw=1)

Now start NetBeans on your PC and click “File”, “Open Project…” and open the “music-visualizer-server” project.

![alt text](https://www.dropbox.com/s/9qr8r3cji7sbi8z/image010.png?raw=1)

![alt text](https://www.dropbox.com/s/fbabm3fw27cwrg1/image011.png?raw=1)

Right-click on the “Libraries” folder and select “Add JAR/Folder…”.

![alt text](https://www.dropbox.com/s/xslhzz5p1jprmk8/image012.png?raw=1)

Select all three rpi-ws281x jar files you created on your Raspberry Pi and select the log4j jar files that are in the “music-visualizer-server\lib” folder.

![alt text](https://www.dropbox.com/s/2eug3i51qd9go5i/image013.png?raw=1)

You now have all the required jar files for the server



## Building and running the server

Right-click on the "MusicVisualizerServer" and select “Properties”.

![alt text](https://www.dropbox.com/s/hmmh11qrff8gf7q/image015.png?raw=1)

Click “Packaging” and check “Compress JAR file” and “Build JAR after compiling”. Click “OK” to accept changes.

![alt text](https://www.dropbox.com/s/gu382abc8f3giks/image016.png?raw=1)

Right-click on the MusicVisualizerServer and select “Build”.

![alt text](https://www.dropbox.com/s/3u3aeyv415tgijf/image017.png?raw=1)

Build files are created in the “music-visualizer-server\dist” folder. Move the jar file and the “lib” folder to your Raspberry Pi.

![alt text](https://www.dropbox.com/s/l8f8kq3ebehgypg/image018.png?raw=1)

Open the terminal and run the following command to start the server:

         sudo java -jar MusicVisualizerServer.jar


## Disable Raspberry Pi audio output

It is not possible to simultaneously play sounds and control the ledstrip, so the audio output must be disabled.

Open the configuration file with text editor:

         sudo nano /boot/config.txt
	 
Comment "dtparam=audio=on" by adding "#" at the beginning of the line then save.

Reboot Raspberry Pi:

         sudo reboot


## Wiring

- Connect the data wire from the strip to the pin 12 (GPIO 18) on the Raspberry Pi.
- Connect the positive wire from the strip to the external power supply.
- Connect the negative wire from the strip to the pin 6 (GND) on the Raspberry Pi and to the external power supply.

![alt text](https://www.dropbox.com/s/akmz3f14w1robb5/image020.png?raw=1)
