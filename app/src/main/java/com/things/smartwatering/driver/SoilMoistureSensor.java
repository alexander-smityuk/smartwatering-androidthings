package com.things.smartwatering.driver;


import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class SoilMoistureSensor implements AutoCloseable {

    public static final int I2C_ADDRESS = 0x48;

    private I2cDevice mDevice;

    private final byte[] mBuffer = new byte[2];

    public SoilMoistureSensor(String bus) throws IOException {
        PeripheralManagerService pioService = new PeripheralManagerService();
        I2cDevice device = pioService.openI2cDevice(bus, I2C_ADDRESS);

        System.out.print(pioService.getI2cBusList());

        try {
            connect(device);
        } catch (IOException | RuntimeException e) {
            try {
                close();
            } catch (IOException | RuntimeException ignored) {
            }
            throw e;
        }
    }

    private void connect(I2cDevice device) throws IOException {
        mDevice = device;
    }

    @Override
    public void close() throws IOException {
        if (mDevice != null) {
            try {
                mDevice.close();
            } finally {
                mDevice = null;
            }
        }
    }

    public int readData() throws IOException, IllegalStateException, InterruptedException {
        if (mDevice == null) {
            throw new IllegalStateException("I2C device not open");
        }

        byte[] config = {(byte) 0xc0000};

        mDevice.writeRegBuffer(0x01, config, 1);

        Thread.sleep(500);

        mDevice.read(mBuffer, 2);

        return ((mBuffer[0] & 0xff) << 8) | (mBuffer[1] & 0xff);
    }
}
