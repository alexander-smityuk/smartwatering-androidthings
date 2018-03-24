package com.things.smartwatering.driver.ads1115;


import com.google.android.things.pio.I2cDevice;

import java.io.IOException;

public class Ads1115Device implements AutoCloseable, Ads1115 {

    private final ChannelReader channelReader;
    private final I2cDevice device;

    Ads1115Device(ChannelReader channelReader, I2cDevice device) {
        this.channelReader = channelReader;
        this.device = device;
    }

    @Override
    public int read(Channel channel) {
        return channelReader.read(channel);
    }

    @Override
    public double readVoltage(Channel channel) {
        return (channelReader.read(channel) * 0.1875)/1000;
    }

    @Override
    public void close() {
        try {
            device.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
