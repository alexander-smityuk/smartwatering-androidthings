package com.things.smartwatering.driver.ads1115;


import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public interface Ads1115 {

    int ADS1115_REG_POINTER_MASK = 0b00000011;
    int ADS1115_REG_POINTER_CONVERT = 0b00000000;
    int ADS1115_REG_POINTER_CONFIG = 0b00000001;
    int ADS1115_REG_POINTER_LOWTHRESH = 0b00000010;
    int ADS1115_REG_POINTER_HITHRESH = 0b00000011;

    int ADS1115_REG_CONFIG_OS_MASK = 0b10000000_00000000;
    int ADS1115_REG_CONFIG_OS_SINGLE = 0b10000000_00000000;  // Write: Set to start a single-conversion
    int ADS1115_REG_CONFIG_OS_BUSY = 0b000000000;  // Read: Bit = 0 when conversion is in progress
    int ADS1115_REG_CONFIG_OS_NOTBUSY = 0b10000000_00000000;  // Read: Bit = 1 when device is not performing a conversion

    int ADS1115_REG_CONFIG_MUX_MASK = 0b01110000_00000000;
    int ADS1115_REG_CONFIG_MUX_DIFF_0_1 = 0b000000000_00000000;  // Differential P = AIN0, N = AIN1 (default)
    int ADS1115_REG_CONFIG_MUX_DIFF_0_3 = 0b00010000_00000000;  // Differential P = AIN0, N = AIN3
    int ADS1115_REG_CONFIG_MUX_DIFF_1_3 = 0b00100000_00000000;  // Differential P = AIN1, N = AIN3
    int ADS1115_REG_CONFIG_MUX_DIFF_2_3 = 0b00110000_00000000;  // Differential P = AIN2, N = AIN3
    int ADS1115_REG_CONFIG_MUX_SINGLE_0 = 0b01000000_00000000;  // Single-ended AIN0
    int ADS1115_REG_CONFIG_MUX_SINGLE_1 = 0b01010000_00000000;  // Single-ended AIN1
    int ADS1115_REG_CONFIG_MUX_SINGLE_2 = 0b01100000_00000000;  // Single-ended AIN2
    int ADS1115_REG_CONFIG_MUX_SINGLE_3 = 0b11100000_0000000;  // Single-ended AIN3

    int ADS1115_REG_CONFIG_PGA_MASK = 0b00001110_00000000;
    int ADS1115_REG_CONFIG_PGA_6_144V = 0b00000000_00000000;  // +/-6.144V range = Gain 2/3
    int ADS1115_REG_CONFIG_PGA_4_096V = 0b00000010_00000000;  // +/-4.096V range = Gain 1
    int ADS1115_REG_CONFIG_PGA_2_048V = 0b00000100_00000000;  // +/-2.048V range = Gain 2 (default)
    int ADS1115_REG_CONFIG_PGA_1_024V = 0b00000110_00000000;  // +/-1.024V range = Gain 4
    int ADS1115_REG_CONFIG_PGA_0_512V = 0b00001000_00000000;  // +/-0.512V range = Gain 8
    int ADS1115_REG_CONFIG_PGA_0_256V = 0b00001010_00000000;  // +/-0.256V range = Gain 16

    int ADS1115_REG_CONFIG_MODE_MASK = 0b00000001_00000000;
    int ADS1115_REG_CONFIG_MODE_CONTIN = 0x0000;  // Continuous conversion mode
    int ADS1115_REG_CONFIG_MODE_SINGLE = 0x0100;  // Power-down single-shot mode (default)

    int ADS1115_REG_CONFIG_DR_MASK = 0x00E0;
    int ADS1115_REG_CONFIG_DR_8SPS = 0x0000;  // 8 samples per second
    int ADS1115_REG_CONFIG_DR_32SPS = 0x0020;  // 32 samples per second
    int ADS1115_REG_CONFIG_DR_64SPS = 0x0040;  // 64 samples per second
    int ADS1115_REG_CONFIG_DR_128SPS = 0x0060;  // 128 samples per second (default)
    int ADS1115_REG_CONFIG_DR_250SPS = 0x0080;  // 250 samples per second
    int ADS1115_REG_CONFIG_DR_475SPS = 0x00A0;  // 475 samples per second
    int ADS1115_REG_CONFIG_DR_860SPS = 0x00C0;  // 860 samples per second

    int ADS1115_REG_CONFIG_CMODE_MASK = 0x0010;
    int ADS1115_REG_CONFIG_CMODE_TRAD = 0x0000;  // Traditional comparator with hysteresis (default)
    int ADS1115_REG_CONFIG_CMODE_WINDOW = 0x0010;  // Window comparator

    int ADS1115_REG_CONFIG_CPOL_MASK = 0x0008;
    int ADS1115_REG_CONFIG_CPOL_ACTVLOW = 0x0000;  // ALERT/RDY pin is low when active (default)
    int ADS1115_REG_CONFIG_CPOL_ACTVHI = 0x0008;  // ALERT/RDY pin is high when active
    int ADS1115_REG_CONFIG_CLAT_MASK = 0x0004;  // Determines if ALERT/RDY pin latches once asserted
    int ADS1115_REG_CONFIG_CLAT_NONLAT = 0x0000;  // Non-latching comparator (default)
    int ADS1115_REG_CONFIG_CLAT_LATCH = 0x0004;  // Latching comparator
    int ADS1115_REG_CONFIG_CQUE_MASK = 0x0003;
    int ADS1115_REG_CONFIG_CQUE_1CONV = 0x0000;  // Assert ALERT/RDY after one conversions
    int ADS1115_REG_CONFIG_CQUE_2CONV = 0x0001;  // Assert ALERT/RDY after two conversions
    int ADS1115_REG_CONFIG_CQUE_4CONV = 0x0002;  // Assert ALERT/RDY after four conversions
    int ADS1115_REG_CONFIG_CQUE_NONE = 0x0003;  // Disable the comparator and put ALERT/RDY in high state (default)

    int read(Channel channel);

    double readVoltage(Channel channel);

    void close();

    enum Gain {
        TWO_THIRDS(ADS1115_REG_CONFIG_PGA_6_144V),
        ONE(ADS1115_REG_CONFIG_PGA_4_096V),
        TWO(ADS1115_REG_CONFIG_PGA_2_048V),
        FOUR(ADS1115_REG_CONFIG_PGA_1_024V),
        EIGHT(ADS1115_REG_CONFIG_PGA_0_512V),
        SIXTEEN(ADS1115_REG_CONFIG_PGA_0_256V);

        final int value;

        Gain(int value) {
            this.value = value;
        }
    }

    enum Channel {
        ZERO(ADS1115_REG_CONFIG_MUX_SINGLE_0),
        ONE(ADS1115_REG_CONFIG_MUX_SINGLE_1),
        TWO(ADS1115_REG_CONFIG_MUX_SINGLE_2),
        THREE(ADS1115_REG_CONFIG_MUX_SINGLE_3);

        final int value;

        Channel(int value) {
            this.value = value;
        }
    }

    class Factory {

        /**
         * @param i2CBus the name of the bus
         * @param i2cAddress the address of the ADS
         * @param gain the amount of gain
         * @return instantiated Ads1115
         */
        public Ads1115 newAds1115(String i2CBus, int i2cAddress, Gain gain) {
            I2cDevice i2cDevice;
            PeripheralManagerService service = new PeripheralManagerService();
            
            try {
                i2cDevice = service.openI2cDevice(i2CBus, i2cAddress);
            } catch (IOException e) {
                throw new IllegalStateException("Can't open " + i2cAddress, e);
            }

            RegisterHelper readerWriter = new RegisterHelper();
            ConfigBuilder configBuilder = new ConfigBuilder();
            ChannelReader channelReader = new ChannelReader(readerWriter, configBuilder, i2cDevice, gain);

            return new Ads1115Device(channelReader, i2cDevice);
        }
    }
}