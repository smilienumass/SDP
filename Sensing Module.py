import RPi.GPIO as GPIO
import smbus
import time
import numpy
import json

GPIO.setmode(GPIO.BCM)
GPIO.setup(17,GPIO.IN)


class mpu_6050:
    # global variables
    g = 9.806; # gravity constant
    address = None
    bus = None
   
    #registers
    tempReg = 0x41
    gyroXreg = 0x43
    gyroYreg = 0x45
    gyroZreg = 0x47
    accelXreg = 0x3B
    accelYreg = 0x3D
    accelZreg = 0x3F
    powerMgmt= 0x6B
    powerMgmt2= 0x6C
    accelConfig = 0x1C
    gyroConfig = 0x1B
   
    # Pre-defined ranges
    accelRange2G = 0x00
    accelRange4G = 0x08
    accelRange8G = 0x10
    accelRange16G = 0x18
    gyroRange250DEG = 0x00
    gyroRange500DEG = 0x08
    gyroRange1000DEG = 0x10
    gyroRange2000DEG = 0x18
   
   
    def __init__(self, address, bus=1):
        self.address = address
        self.bus = smbus.SMBus(bus)
        self.bus.write_byte_data(self.address,self.powerMgmt, 0x00)
   
    def read_i2c_word(self,register):
        high = self.bus.read_byte_data(self.address, register)
        low = self.bus.read_byte_data(self.address, register + 1)
       
        value = (high << 8) + low
       
        if (value >= 0x8000):
            return -((65535 - value) + 1)
        else:
            return value
       
    def getTemp(self):
        rawTemp = self.read_i2c_word(self.tempReg)
        realTemp = (rawTemp / 340.0) + 36.53
       
        return realTemp
   
    def setGyroRange(self, gyroRange):
 
        # First change it to 0x00 to make sure we write the correct value later
        self.bus.write_byte_data(self.address, self.gyroConfig, 0x00)

        # Write the new range to the ACCEL_CONFIG register
        self.bus.write_byte_data(self.address, self.gyroConfig, gyroRange)
       
    def readGyroRange(self, raw = False):
        rawData = self.bus.read_byte_data(self.address, self.gyroConfig)
        if raw is True:
            return rawData
        elif raw is False:
            if rawData == self.gyroRange250DEG:
                return 250
            elif rawData == self.gyroRange500DEG:
                return 500
            elif rawData == self.gyroRange1000DEG:
                return 1000
            elif rawData == self.gyroRange2000DEG:
                return 2000
            else:
                return -1

    def getGyro(self):
        gX = self.read_i2c_word(self.gyroXreg)
        gY = self.read_i2c_word(self.gyroYreg)
        gZ = self.read_i2c_word(self.gyroZreg)
       
        gyroRange = self.readGyroRange(True)
        gyroScaleMod = None
       
        if gyroRange == self.gyroRange250DEG:
            gyroScaleMod = 131.0
        elif gyroRange == self.gyroRange500DEG:
            gyroScaleMod = 65.5
        elif gyroRange == self.gyroRange1000DEG:
            gyroScaleMod = 32.8
        elif gyroRange == self.gyroRange2000DEG:
            gyroScaleMod = 16.4
        else:
            print("Unkown range - gyro_scale_modifier set to self.GYRO_SCALE_MODIFIER_250DEG")
            gyroScaleMod = 131.0

        x = gX / gyroScaleMod
        y = gY / gyroScaleMod
        z = gZ / gyroScaleMod
       
        return {'x': x, 'y': y, 'z': z}
   
    def setAccelRange(self, accelRange):
   
        # First change it to 0x00 to make sure we write the correct value later
        self.bus.write_byte_data(self.address, self.accelConfig, 0x00)
        # Write the new range to the ACCEL_CONFIG register
        self.bus.write_byte_data(self.address, self.accelConfig, accelRange)

    def readAccelRange(self, raw = False):
 
        rawData = self.bus.read_byte_data(self.address, self.accelConfig)

        if raw is True:
            return rawData
        elif raw is False:
            if rawData == self.accelRange2G:
                return 2
            elif rawData == self.accelRange4G:
                return 4
            elif rawData == self.accelRange8G:
                return 8
            elif rawData == self.accelRange16G:
                return 16
            else:
                return -1

    def getAccel(self, g = False):
 
        aX = self.read_i2c_word(self.accelXreg)
        aY = self.read_i2c_word(self.accelYreg)
        aZ = self.read_i2c_word(self.accelZreg)

        accelScaleMod = None
        accelRange = self.readAccelRange(True)

        if accelRange == self.accelRange2G:
            accelScaleMod = 16384.0
        elif accelRange == self.accelRange4G:
            accelScaleMod = 8192.0
        elif accelRange == self.accelRange8G:
            accelScaleMod = 4096.0
        elif accelRange == self.accelRange16G:
            accelScaleMod = 2048.0
        else:
            print("Unkown range - accel_scale_modifier set to self.ACCEL_SCALE_MODIFIER_2G")
            accelScaleMod = 16384.0

        x = aX / accelScaleMod
        y = aY / accelScaleMod
        z = aZ / accelScaleMod

        if g is True:
            return {'x': x, 'y': y, 'z': z}
        elif g is False:
            x = x * self.g
            y = y * self.g
            z = z * self.g
            return {'x': x, 'y': y, 'z': z}
       
    def writeJSON(self, path, fileName, data):
        filePathWExt = './' + path + '/' + fileName + '.json'
        with open(filePathWExt, 'w') as f:
            json.dump(data, f, indent=1)
            f.close()
   
###############################################

mpu6050 = mpu_6050(0x68) #creating an MPU6050


g = 9.806; # gravity constant

### Magnitude Calculations ###
magVector = numpy.array([mpu6050.getAccel()['x'], mpu6050.getAccel()['y'], mpu6050.getAccel()['z']])
magnitude = numpy.linalg.norm(magVector, ord=1) # calculates magnitude using numpy linalg function

magVector = numpy.array([mpu6050.getAccel()['x'], mpu6050.getAccel()['y'], mpu6050.getAccel()['z']])
magnitude = numpy.linalg.norm(magVector, ord=1) # calculates magnitude using numpy linalg function

maxMag = 0
tempMax = 0
minMag = magnitude
tempMin = magnitude

### State Mode ###
on = 1 #If Rpi is active (state)
off = 0#If Rpi is not active (state)        

### Counters ###
dropCounter = 0

while on:
   
    ### Time Variables ###
    localTime = time.localtime() # retrieves local time
    currentTime = time.strftime("%H:%M:%S", localTime) #retwrites time
   
    ### Magnitude Calculations ###
    magVector = numpy.array([mpu6050.getAccel()['x'], mpu6050.getAccel()['y'], mpu6050.getAccel()['z']])
    magnitude = numpy.linalg.norm(magVector, ord=1) # calculates magnitude using numpy linalg function
   
    ### Boolean flags ###
    drop = 0 #drop flag
    switch = 0 #switch flag
   
    ### Drop detection readings ###
    tempMax = magnitude
    if (tempMax > maxMag):
        maxMag = tempMax
       
    tempMin = magnitude
    if (tempMin < minMag):
        minMag = tempMin
       
    #print("Max: " + str(maxMag))
    #print("Min: " + str(minMag))
   
   
    ### Interaction Logs flag ###
    if (magnitude > 2 * g): #Drop detection threshold
        drop = 1

    #print(GPIO.input(17))    
   
    if(GPIO.input(17)): #Switch input detection
      switch = 1
       
    ### JSON File Output ###
    filePath = 'Desktop'
    fileName = 'Interaction_Logs'

    if (drop):
        time.sleep(.5)
        dropMaxMag = tempMax
        dropLog = {
            'detection': "drop",
            'time': str(currentTime),
            'magnitude': str(magnitude) + " (" + str(dropMaxMag/g) + "g)",
            'Gyroscope': str(mpu6050.getGyro()),
            'Accelerometer': str(mpu6050.getAccel()),
            'Temperature': str(mpu6050.getTemp()) + " Celcius"
    }
        mpu6050.writeJSON(filePath,fileName + currentTime,dropLog)
        dropMaxMag = 0
        print("Drop")

    if (switch):
        switchLog = {
            'detection': "open/close",
            'time': str(currentTime),
            'magnitude': str(magnitude) + " (" + str(magnitude/g) + "g)",
            'Gyroscope': str(mpu6050.getGyro()),
            'Accelerometer': str(mpu6050.getAccel()),
            'Temperature': str(mpu6050.getTemp()) + " Celcius"
    }
        mpu6050.writeJSON(filePath,fileName + currentTime,switchLog)
        print("Switch")
        time.sleep(1)
