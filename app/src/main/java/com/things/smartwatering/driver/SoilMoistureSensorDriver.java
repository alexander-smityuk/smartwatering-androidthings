package com.things.smartwatering.driver;


import android.hardware.Sensor;

import com.google.android.things.userdriver.UserDriverManager;
import com.google.android.things.userdriver.UserSensor;
import com.google.android.things.userdriver.UserSensorDriver;
import com.google.android.things.userdriver.UserSensorReading;
import com.things.smartwatering.driver.ads1115.Ads1115;
import com.things.smartwatering.utils.AppConstant;

import java.io.IOException;
import java.util.UUID;

public class SoilMoistureSensorDriver implements AutoCloseable {

    private static final String DRIVER_NAME = "Soil Moisture";
    private static final String SENSOR_STRING_TYPE = "com.smartwatering.soilmoisture";

    private static final float SOIL_MAGIC_CONST = 4.2f;

    private static final int I2C_ADDRESS = 0x48;

    private Ads1115 mDevice;

    private SoilMoistureUserDriver mSoilMoistureUserDriver;

    public SoilMoistureSensorDriver(String bus) {
        mDevice = new Ads1115.Factory().newAds1115(bus, I2C_ADDRESS, Ads1115.Gain.TWO_THIRDS);
    }

    @Override
    public void close() throws IOException {
        unregisterSoilMoistureSensor();
        if (mDevice != null) {
            try {
                mDevice.close();
            } finally {
                mDevice = null;
            }
        }
    }

    public void registerSoilMoistureSensor() {
        if (mDevice == null) {
            throw new IllegalStateException("cannot register closed driver");
        }

        if (mSoilMoistureUserDriver == null) {
            mSoilMoistureUserDriver = new SoilMoistureSensorDriver.SoilMoistureUserDriver();
            UserDriverManager.getManager().registerSensor(mSoilMoistureUserDriver.getUserSensor());
        }
    }

    public void unregisterSoilMoistureSensor() {
        if (mSoilMoistureUserDriver != null) {
            UserDriverManager.getManager().unregisterSensor(mSoilMoistureUserDriver.getUserSensor());
            mSoilMoistureUserDriver = null;
        }
    }

    private class SoilMoistureUserDriver extends UserSensorDriver {

        private static final int DRIVER_VERSION = 1;
        private boolean mEnabled;
        private UserSensor mUserSensor;

        private UserSensor getUserSensor() {
            if (mUserSensor == null) {
                mUserSensor = new UserSensor.Builder()
                        .setCustomType(
                                AppConstant.TYPE_SOIL_MOISTURE,
                                SENSOR_STRING_TYPE,
                                Sensor.REPORTING_MODE_ON_CHANGE)
                        .setName(DRIVER_NAME)
                        .setVersion(DRIVER_VERSION)
                        .setUuid(UUID.randomUUID())
                        .setDriver(this)
                        .build();
            }
            return mUserSensor;
        }

        /**
         * max voltage = 4.2V -> 0% moisture
         *
         * @return soil moisture in percents
         * @throws IOException
         */
        @Override
        public UserSensorReading read() throws IOException {
            return new UserSensorReading(new float[]{100 - ((float) mDevice.readVoltage(Ads1115.Channel.ZERO) / SOIL_MAGIC_CONST * 100)});
        }

        @Override
        public void setEnabled(boolean enabled) throws IOException {
            mEnabled = enabled;
        }

        private boolean isEnabled() {
            return mEnabled;
        }
    }
}
