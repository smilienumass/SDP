# SDP
Senior Design Project
# RFID Branch

# Set Up
The folder titled "RFID" contains the mercury-api and code required to run the RFID read code ("SDP_RFID.java") on a Raspbian device with Java installed. So it is necessary to install Java and confirm it is functioning before you can execute these files.


# Operation
Terminal Compile Command

Note: Use this command to compile the java file "SDP_RFID.java" after any changes.

	javac -cp .:ltkjava-1.0.0.6.jar:mercuryapi.jar SDP_RFID.java

Terminal Execution Command

Note: Use this command to execute the java file

	java -cp .:ltkjava-1.0.0.6.jar:mercuryapi.jar SDP_RFID

- It is normal to see "ERROR [main] (SerialReader.java:1273) - " when running sucessfully... 
  this seems to be an error in an API file, but does not affect execution

- If you see "Reader Exception: No such file or directory" then it is likely you have the wrong
  comport, or you do not have a reader connected. Make sure to verify the comport by using 
  terminal commands or checking the file dir /dev/... on my machine, the top left (ethernet side)
  USB port is ttyACM0 so this is what I have in the code though it is possible to change this
  line to fit your needs both on raspi and on windows.

- Note that it is also VERY important to choose the correct antenna. They are labeled on the dev
  board. However, incorrect antenna selection could lead to destruction of the M6E chip so
  be careful.
