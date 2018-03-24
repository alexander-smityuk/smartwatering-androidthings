package com.things.smartwatering.driver.ads1115;


import com.google.android.things.pio.I2cDevice;

import static com.things.smartwatering.driver.ads1115.Ads1115.ADS1115_REG_POINTER_CONFIG;
import static com.things.smartwatering.driver.ads1115.Ads1115.ADS1115_REG_POINTER_CONVERT;

class ChannelReader {

    private final RegisterHelper registerReaderWriter;
    private final ConfigBuilder configBuilder;
    private final I2cDevice i2cBus;
    private final Ads1115.Gain gain;

    ChannelReader(RegisterHelper registerReaderWriter,
                  ConfigBuilder configBuilder,
                  I2cDevice i2cBus,
                  Ads1115.Gain gain) {

        this.registerReaderWriter = registerReaderWriter;
        this.configBuilder = configBuilder;
        this.i2cBus = i2cBus;
        this.gain = gain; /* +/- 6.144V range (limited to VDD +0.3V max!) */
    }

    int read(Ads1115.Channel channel) {
        writeRegister(ADS1115_REG_POINTER_CONFIG, configBuilder.reader(gain, channel));
        return readRegister(ADS1115_REG_POINTER_CONVERT);
    }

    private void writeRegister(int reg, int value) {
        registerReaderWriter.writeRegister(i2cBus, reg, value);
    }

    private int readRegister(int reg) {
        return registerReaderWriter.readRegister(i2cBus, reg);
    }
}
