package com.things.smartwatering.driver.ads1115;

import com.google.android.things.pio.I2cDevice;

import java.io.IOException;

public class RegisterHelper {

    /**
     * @param device The device to write to
     * @param reg    The register to write to
     * @param value  A 16 bit value held in a 32 bit int
     */
    void writeRegister(I2cDevice device, int reg, int value) {
        try {
            byte lsb = (byte) (value & 0xFF);
            byte msb = (byte) (value >> 8);
            byte[] b = new byte[]{msb, lsb};
            device.writeRegBuffer(reg, b, b.length);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write " + reg + " with value " + value, e);
        }
    }

    /**
     * @param device The device to read from
     * @param reg The register to read from
     * @return int value
     */
    int readRegister(I2cDevice device, int reg) {
        try {
            byte[] b = new byte[2];
            device.readRegBuffer(reg, b, b.length);
            return ((b[0] & 0xff) << 8) | (b[1] & 0xff);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read " + reg, e);
        }
    }
}
